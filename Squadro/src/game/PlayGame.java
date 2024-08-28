package game;

import game.RL.DQLAgent;
import game.RL.TrainAgent;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.Scanner;

import static game.RL.DQLAgent.STATE_SIZE;
import static game.RL.TrainAgent.*;

public class PlayGame {
//TODO mettre en place un message pour quand l'ia fait un move non autorisé

    /**
     * Train the agentAI
     * @param agent the agent to train
     * @param num_episode number of episode (game) to play
     * @param max_step_per_episode maximum number of round per game
     * @param positiveReward reward for winning
     * @param negativeRewardForLose reward for losing
     * @param negativeRewardForCrashing reward for crashing (make a move not allowed)
     * @param modelPath path to save the model
     */
    public static void trainPlayerAI(
            DQLAgent agent,
            int num_episode,
            int max_step_per_episode,
            double positiveReward,
            double negativeRewardForLose,
            double negativeRewardForCrashing,
            String modelPath){

        for (int episode = 0; episode < num_episode; episode++) {
            long state = initialState();
            int step = 0;
            boolean done = false;

            while (!done && step < max_step_per_episode) {
                int action = agent.chooseAction(state);
                long nextState = performAction(state, action);
                double reward = getReward(nextState,positiveReward,negativeRewardForLose,negativeRewardForCrashing);
                done = isDone(nextState);

                agent.train(state, action, reward, nextState, done);

                state = nextState;
                step++;
            }
            if (episode % ( num_episode >= 1000 ? num_episode/1000 : 1) == 0)
                System.out.println("Episode " + episode + " finished after " + step + " steps.");
        }

        saveModel(agent,modelPath);

    }

    /**
     * Play a game between two players
     * @param player1 kind of player 1
     * @param player2 kind of player 2
     * @param nbGame number of game to play
     * @param maxStepPerGame maximum number of round per game
     * @param agent1 agent of player 1 (if player 1 is AI)
     * @param agent2 agent of player 2 (if player 2 is AI)
     * @param printProbaAI print the probability of the output of the model //TODO to implement
     * @param printBoard print the board at each round
     * @param trainingModel1 train the model of player 1
     * @param trainingModel2 train the model of player 2
     */
    public static void playPlayer1vsPlayer2(pPlayer player1,
                                            pPlayer player2,
                                            int nbGame,
                                            int maxStepPerGame,
                                            DQLAgent agent1,
                                            DQLAgent agent2,
                                            boolean printProbaAI,
                                            boolean printBoard,
                                            boolean trainingModel1,
                                            boolean trainingModel2){
        //Evaluation
        int nbOfGames = nbGame;
        int nbRoundMaxPerGame = maxStepPerGame;
        long board = BoardV3.INIT_BOARD;
        //int gamePlayed = 0;
        int gameFailed = 0;
        int gameOvertime = 0;
        int winPlayer1 = 0;
        int winPlayer2 = 0;
        //boolean printBoard = false;
        //boolean random = true;
        INDArray stateVector = Nd4j.create(1, STATE_SIZE);
        for (int i = 0; i < nbOfGames; i++) {
            board = BoardV3.INIT_BOARD;
            int player = 1;
            int countRoundPlayed = 0;
            do {
                int playerChoice = -1;
                if (player == 1) {
                    playerChoice = playOneActionDependingOfKindPlayer(1,player1,board,stateVector,agent1,printProbaAI);
                } else {
                    playerChoice = playOneActionDependingOfKindPlayer(2,player2,board,stateVector,agent2,printProbaAI);
                }
                board = BoardV3.withPiecePlayed(board, playerChoice);
                if(printBoard){
                    String[][] tab = BoardV3.toTabString(board);
                    for(int k = 0; k < 9 ; k++) {
                        System.out.println();
                        for (int j = 0; j < 9; j++)
                            System.out.print(tab[k][j]);
                    }
                    System.out.println("");
                    System.out.println("");
                    System.out.println("");
                }
                player = player == 1 ? 2 : 1;
            } while (!isDone(board) && countRoundPlayed++ < nbRoundMaxPerGame);
            if((board != Long.MAX_VALUE) && countRoundPlayed < nbRoundMaxPerGame) {
                int bin = player == 1 ? winPlayer2++ : winPlayer1++;
            }else if(board == Long.MAX_VALUE){
                System.out.println("Game n°"+ i+" : Move Not Allowed from Player "+(player == 1 ? 2 : 1)+" !   --> rate of fail :" + ( (gameFailed/(i + 1)) * 100) + "%" );
                System.out.println("gameFailed = "+gameFailed);
                gameFailed++;
            }
            else {
                System.out.println("Game n°"+ i+" : Overtime !");
                gameOvertime++;
            }
        }


        System.out.println("----------     Game finished     ----------");
        System.out.println();

        System.out.println("--  result of the evaluation  --");
        System.out.println("nbOfGames: " + nbOfGames);
        System.out.println("nbRoundMaxPerGame: " + nbRoundMaxPerGame);
        System.out.println("Player 1  ("+toStringKindPlayer(player1)+") won " + winPlayer1 + " times");
        System.out.println("Player 2 ("+toStringKindPlayer(player2)+") won " + winPlayer2 + " times");
        System.out.println("Game failed " + gameFailed + " times");
        System.out.println("Game overtime " + gameOvertime + " times");
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();





        if(player1 == pPlayer.AI)
            printAgentParameters(agent1,1);
        if(player2 == pPlayer.AI)
            printAgentParameters(agent2,2);

        System.out.println();

        if(player1 == pPlayer.AI && trainingModel1)
            printTrainingAgentParameters(1, nbOfGames, nbRoundMaxPerGame, 1, -1, -1);
        if (player2 == pPlayer.AI && trainingModel2)
            printTrainingAgentParameters(2, nbOfGames, nbRoundMaxPerGame, 1, -1, -1);







    }
    private static int playOneActionDependingOfKindPlayer(int player, pPlayer playerType, long board, INDArray stateVector, DQLAgent agent, boolean printProbaAI){
        int playerChoice = -1;

        switch (playerType){
            case HUMAN -> {//TODO --------------
                Scanner scanner = new java.util.Scanner(System.in);
                boolean[] piecesPlayable = BoardV3.getPiecesPlayable(board);
                do {
                    System.out.print("Player " + player + " : Choose a piece to play (");
                    StringBuilder pieces = new StringBuilder();

                    for (int i = 0; i < BoardV3.nbOfPiece; i++) {
                        if (piecesPlayable[i] &&
                                ((player == 1 && i < BoardV3.nbPiecePerPlayer)
                                        || (player == 2 && i >= BoardV3.nbPiecePerPlayer)
                                )
                        )

                            pieces.append(i).append(", ");
                    }
                    pieces.deleteCharAt(pieces.length() - 1);
                    pieces.deleteCharAt(pieces.length() - 1);
                    System.out.print(pieces.toString() + ") : ");
                    playerChoice = scanner.nextInt();
                } while (!piecesPlayable[playerChoice] && ( (player == 2 && playerChoice >= BoardV3.nbPiecePerPlayer) || (player == 1 && playerChoice < BoardV3.nbPiecePerPlayer) ));
                return playerChoice;
            }


            case AI -> {
                long[] boardArray = new long[STATE_SIZE];
                for (int j = 0; j < STATE_SIZE; j++) {
                    boardArray[j] = (board >> j) & 1;
                }
                stateVector.assign(Nd4j.createFromArray(boardArray).reshape(1, STATE_SIZE));
                INDArray qValues = agent.getModel().output(stateVector);
                if (printProbaAI)
                    System.out.println("proba of Player " + player + " : " + qValues.toString());
                playerChoice = Nd4j.argMax(qValues, 1).getInt(0);
                return player==1? playerChoice : playerChoice + 5;
            }


            case RANDOM -> {
                return TrainAgent.chosePieceRandom(player, BoardV3.getPiecesPlayable(board));
            }
            default -> {
                return playerChoice;
            }


        }

    }

    private static void printAgentParameters(DQLAgent agent, int player){
        System.out.println();
        System.out.println("--  parameter of the model of Player "+player+"  --");
        System.out.println("learning rate: " + agent.getLearningRate());
        System.out.println("discount factor: " + agent.getDiscountFactor());
        System.out.println("exploration rate: " + agent.getExplorationRate());
        System.out.println("exploration decay: " + agent.getExplorationDecay());
        System.out.println("min exploration rate: " + agent.getMinExplorationRate());
        System.out.println("Nunber of layers: " + agent.getNbLayer());
        for (int i = 0; i < agent.getNbLayer(); i++) {
            System.out.println("nbNeuronsLayer"+(i+1)+": " + agent.getNbNeuronsPerLayer()[i]);
        }

    }

    private static void printTrainingAgentParameters(int player, int num_episode, int max_step_per_episode, double positiveReward, double negativeRewardForLose, double negativeRewardForCrashing){
        System.out.println();
        System.out.println("--  Parameters of the training of the " + "Player " + player + "  --");
        System.out.println("nbEpisodes: " + num_episode);
        System.out.println("maxStepsPerEpisode: " + max_step_per_episode);
        System.out.println("positiveReward: " + positiveReward);
        System.out.println("negativeRewardForLose: " + negativeRewardForLose);
        System.out.println("negativeRewardForCrashing: " + negativeRewardForCrashing);
    }
    private static String toStringKindPlayer(pPlayer playerType){
        return (playerType == pPlayer.AI ? "AI": playerType == pPlayer.RANDOM ? "RANDOM":"HUMAN");
    }

    public enum pPlayer{
        HUMAN,
        RANDOM,
        AI
    }



}
