package game.RL;

import game.BoardV3;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import org.deeplearning4j.util.ModelSerializer;
import java.io.File;
import java.io.IOException;


import static game.RL.DQLAgent.STATE_SIZE;

public class TrainAgent {

    private static final int NUM_EPISODES = 45_000; //1000
    private static final int MAX_STEPS_PER_EPISODE = 100; //100
    private static final double negativeRewardForLose = -1;//-1
    private static final double negativeRewardForCrashing = -1  ;//-1
    private static final String MODEL_PATH = "list_models/modelV8.zip";

    public static void main(String[] args) {
        DQLAgent agent = new DQLAgent();

        for (int episode = 0; episode < NUM_EPISODES; episode++) {
            long state = initialState();
            int step = 0;
            boolean done = false;

            while (!done && step < MAX_STEPS_PER_EPISODE) {
                int action = agent.chooseAction(state);
                long nextState = performAction(state, action);
                double reward = getReward(nextState);
                done = isDone(nextState);

                agent.train(state, action, reward, nextState, done);

                state = nextState;
                step++;
            }
            if (episode % 1000 == 0)
                System.out.println("Episode " + episode + " finished after " + step + " steps.");
        }

        saveModel(agent);

        //Evaluation
        int nbOfGames = 100_000;
        int nbRoundMaxPerGame = 500;
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
                    //TODO optimize this code :
/*                    INDArray stateVector = Nd4j.create(1, STATE_SIZE);
                    for (int j = 0; j < STATE_SIZE; j++) {
                        stateVector.putScalar(j, (board >> j) & 1);
                    }
                    INDArray qValues = agent.getModel().output(stateVector);
                    playerChoice = Nd4j.argMax(qValues, 1).getInt(0);*/

                    //new code optimized :
                    long[] boardArray = new long[STATE_SIZE];
                    for (int j = 0; j < STATE_SIZE; j++) {
                        boardArray[j] = (board >> j) & 1;
                    }
                    stateVector.assign(Nd4j.createFromArray(boardArray).reshape(1, STATE_SIZE));
                    INDArray qValues = agent.getModel().output(stateVector);
                    playerChoice = Nd4j.argMax(qValues, 1).getInt(0);
                } else {
                    playerChoice = chosePieceRandom(2, BoardV3.getPiecesPlayable(board));
                }
                board = BoardV3.withPiecePlayed(board, playerChoice);
                player = player == 1 ? 2 : 1;
            } while (!isDone(board) && countRoundPlayed++ < nbRoundMaxPerGame);
            if((board != Long.MAX_VALUE) && countRoundPlayed < nbRoundMaxPerGame) {
                int bin = player == 1 ? winPlayer2++ : winPlayer1++;
            }else if(board == Long.MAX_VALUE){
                System.out.println("the game n°"+ i+" : Move Not Allowed !");
                gameFailed++;
            }
            else {
                System.out.println("the game n°"+ i+" : Overtime !");
                gameOvertime++;
            }
        }
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("------     VersionX     ------");
        System.out.println("Game finished");
        System.out.println("");

        System.out.println("Parameters of the training : ");
        System.out.println("nbEpisodes: " + NUM_EPISODES);
        System.out.println("maxStepsPerEpisode: " + MAX_STEPS_PER_EPISODE);
        System.out.println("negativeRewardForLose: " + negativeRewardForLose);
        System.out.println("negativeRewardForCrashing: " + negativeRewardForCrashing);
        System.out.println("");

        System.out.println("parameter of the model : ");
        System.out.println("learning rate: " + DQLAgent.LEARNING_RATE);
        System.out.println("discount factor: " + DQLAgent.DISCOUNT_FACTOR);
        System.out.println("exploration rate: " + DQLAgent.EXPLORATION_RATE);
        System.out.println("exploration decay: " + DQLAgent.EXPLORATION_DECAY);
        System.out.println("min exploration rate: " + DQLAgent.MIN_EXPLORATION_RATE);
        System.out.println("Nunber of layers: " + 2);
        System.out.println("nbNeuronsLayer1: " + DQLAgent.nbNeuronsLayer1);
        System.out.println("nbNeuronsLayer2: " + DQLAgent.nbNeuronsLayer2);
        System.out.println("");

        System.out.println("result of the evaluation : ");
        System.out.println("nbOfGames: " + nbOfGames);
        System.out.println("nbRoundMaxPerGame: " + nbRoundMaxPerGame);
        System.out.println("Player 1  (AI) won " + winPlayer1 + " times");
        System.out.println("Player 2 (Rdm) won " + winPlayer2 + " times");
        System.out.println("Game failed " + gameFailed + " times");
        System.out.println("Game overtime " + gameOvertime + " times");
    }



    private static long initialState() {
        // Implementation to initialize the game state
        return BoardV3.INIT_BOARD;
    }

    private static long performAction(long state, int action) {
        // Implementation to perform the given action on the state and return the new
        long newState = state;
        newState = BoardV3.withPiecePlayed(newState, action); //play the action of the agent
        if(isDone(newState)) return newState;
        newState = BoardV3.withPiecePlayed(newState, chosePieceRandom(2, BoardV3.getPiecesPlayable(newState))); //play the action of the opponent (random)
        return newState;
    }

    private static double getReward(long state) {
        // Implementation to calculate the reward for the given state
        if (BoardV3.isGameFinished(state) && BoardV3.whoWin(state) == 1 ) {
            return 1;
        }
        else if (state == Long.MAX_VALUE) {
            return negativeRewardForCrashing;
        } else if (BoardV3.isGameFinished(state) && BoardV3.whoWin(state) == 2) {
            return negativeRewardForLose;

        } else {
            return 0;
        }
    }

    private static boolean isDone(long state) {
        // Implementation to check if the game is finished
        return state == Long.MAX_VALUE || BoardV3.isGameFinished(state);
    }





    private static int chosePieceRandom(int player, boolean[] piecesPlayable) {
        double a;
        int piece = -1;

        do {
            a = Math.random();
            if (a <= 0.2) {
                piece = 0;
            } else if (a <= 0.4) {
                piece = 1;
            } else if (a <= 0.6) {
                piece = 2;
            } else if (a <= 0.8) {
                piece = 3;
            } else {
                piece = 4;
            }
        }while (!piecesPlayable[player == 1 ? piece : piece + 5]);
        return player == 1 ? piece : piece + 5;
    }

    private static void saveModel(DQLAgent agent) {
        try {
            File modelFile = new File(MODEL_PATH);

/*            // Assure-toi que le dossier parent existe, sinon crée-le
            if (!modelFile.getParentFile().exists()) {
                modelFile.getParentFile().mkdirs();
            }*/

            // Crée le fichier s'il n'existe pas
            if (!modelFile.exists()) {
                modelFile.createNewFile();
                System.out.println("Model file did not exist. Created a new file.");
            } else {
                System.out.println("Model file already exists. Overwriting it.");
            }

            // Sauvegarde le modèle dans le fichier
            ModelSerializer.writeModel(agent.getModel(), modelFile, true);
            System.out.println("Model saved successfully at " + modelFile.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save the model.");
        }
    }

}
