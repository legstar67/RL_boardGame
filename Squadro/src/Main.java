import game.PlayGame;
import game.RL.DQLAgent;
import game.RL.TrainAgent;

import java.io.IOException;

//-------------------       How the program works ?       -------------------
// you can modify the variable in the zone signalised
//
// It's board game is played with 2 Players
// --> you can choose the type of both player : HUMAN , RANDOM and AI (eg : PlayGame.pPlayer.RANDOM)
// if you choose AI, you can either (by changing value of LOAD_MODEL_PLAYER and TRAINING_MODEL)
//      -load an existing model and use it (true and false)
//      -load an existing model and try to improve it by training it more and then use it (true and true)
//      -create a model (it destroys the existing at the path ! ), train it and then use it (false and true)
//
// For


//TODO TEST GIT transfer from gitlab to github

//-------------------       RULES OF THE GAME       -------------------
//TODO




public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {

        //-------------------       Parameters of the program (beginning)       -------------------

        //Player1
        final PlayGame.pPlayer playerType1 = PlayGame.pPlayer.AI;
        //if player1 is AI, the following parameter is important
            final boolean LOAD_MODEL_PLAYER_1 = true;
            final String MODEL_PATH_PLAYER_1 = "list_models/Player1_modelV3.zip";

            // Hyperparameters (not saved in the file) //TODO only for training right ?
            final double learningRate1 = 0.01;
            final double discountFactor1 = 0.99;
            double explorationRate1 = 1.0;
            final double explorationDecay1 = 0.995;
            final double minExplorationRate1 = 0.01;
                //Only for the creation of the model:
                final int nbLayer1 = 2;
                final int[] nbNeuronsPerLayer1 = {24, 24};

            //Training parameters
            final boolean TRAINING_MODEL_1 = false;
            final int NUM_EPISODE_1 = 1000; //Total number of games played by the AI to train
            final int MAX_STEPS_PER_EPISODE_1 = 100; //Maximum number of rounds per game
            final double positiveReward_1 = 1;
            final double negativeRewardForLose_1 = -1;//-1
            final double negativeRewardForCrashing_1 = -1  ;//-1





        //Player2
        final PlayGame.pPlayer playerType2 = PlayGame.pPlayer.RANDOM;
        //if player1 is AI, the following parameter is important
            final boolean LOAD_MODEL_PLAYER_2 = true;
            final String MODEL_PATH_PLAYER_2 = "list_models/Player1_modelV1.zip";

            // Hyperparameters (not saved in the file)
            final double learningRate2 = 0.01;
            final double discountFactor2 = 0.99;
            double explorationRate2 = 1.0;
            final double explorationDecay2 = 0.995;
            final double minExplorationRate2 = 0.01;
                //Only for the creation of the model:
                final int nbLayer2 = 2;
                final int[] nbNeuronsPerLayer2 = {24, 24};
            //Training parameters
            final boolean TRAINING_MODEL_2 = false;
            final int NUM_EPISODE_2 = 1000; //Total number of games played by the AI to train
            final int MAX_STEPS_PER_EPISODE_2 = 100; //Maximum number of rounds per game
            final double positiveReward_2 = 1;
            final double negativeRewardForLose_2 = -1;//-1
            final double negativeRewardForCrashing_2 = -1;//-1





        //Game
        final int nbOfGames = 1_0000;
        final int nbRoundMaxPerGame = 500;
        final boolean printGame = false; //
        final boolean printProbaOfOutputModel = false; //show probability for each action used by the agent to choose the best



        //-------------------       Parameters of the program (end)       -------------------




        DQLAgent agent1 = null;
        DQLAgent agent2 = null;

        //initialize Player 1 (if AI)
        if (playerType1 == PlayGame.pPlayer.AI){
            if(LOAD_MODEL_PLAYER_1)
                agent1 = new DQLAgent(
                        MODEL_PATH_PLAYER_1,
                        learningRate1,
                        discountFactor1,
                        explorationRate1,
                        explorationDecay1,
                        minExplorationRate1,
                        nbLayer1,
                        nbNeuronsPerLayer1);
            else
                agent1 = new DQLAgent(
                        learningRate1,
                        discountFactor1,
                        explorationRate1,
                        explorationDecay1,
                        minExplorationRate1,
                        nbLayer1,
                        nbNeuronsPerLayer1
                );

            if(TRAINING_MODEL_1)
                PlayGame.trainPlayerAI(
                        agent1,
                        NUM_EPISODE_1,
                        MAX_STEPS_PER_EPISODE_1,
                        positiveReward_1,
                        negativeRewardForLose_1,
                        negativeRewardForCrashing_1,
                        MODEL_PATH_PLAYER_1);
        }

        //initialize Player 2 (if AI)
        if (playerType2 == PlayGame.pPlayer.AI){
            if(LOAD_MODEL_PLAYER_2)
                agent2 = new DQLAgent(
                        MODEL_PATH_PLAYER_2,
                        learningRate2,
                        discountFactor2,
                        explorationRate2,
                        explorationDecay2,
                        minExplorationRate2,
                        nbLayer2,
                        nbNeuronsPerLayer2);
            else
                agent2 = new DQLAgent(
                        learningRate2,
                        discountFactor2,
                        explorationRate2,
                        explorationDecay2,
                        minExplorationRate2,
                        nbLayer2,
                        nbNeuronsPerLayer2
                );

            if(TRAINING_MODEL_2)
                PlayGame.trainPlayerAI(
                        agent2,
                        NUM_EPISODE_2,
                        MAX_STEPS_PER_EPISODE_2,
                        positiveReward_2,
                        negativeRewardForLose_2,
                        negativeRewardForCrashing_2,
                        MODEL_PATH_PLAYER_1);
        }


        //Play the games Player 1 vs Player 2
        PlayGame.playPlayer1vsPlayer2(
                playerType1,
                playerType2,
                nbOfGames,
                nbRoundMaxPerGame,
                agent1,
                agent2,
                printGame,
                printProbaOfOutputModel,
                TRAINING_MODEL_1,
                TRAINING_MODEL_2

        );






    }


}


