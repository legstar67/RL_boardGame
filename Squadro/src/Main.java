

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws InterruptedException {
        //-------------------       Parameters of the program       -------------------

        //Player1
        final pPlayer player1 = pPlayer.AI;
        //if player1 is AI, the following parameter is important
            final boolean LOAD_MODEL_PLAYER_1 = true;
            final String MODEL_PATH_PLAYER_1 = "modelPath";
            final int NUM_EPISODE_1 = 1000; //Total number of games played by the AI to train
            final int MAX_STEPS_PER_EPISODE_1 = 100; //Maximum number of rounds per game
            //if loadModel is false, the following parameters are used to create the AI model
                final double LEARNING_RATE_1 = 0.01;//0.01
                final double DISCOUNT_FACTOR_1 = 0.99;//0.99
                double EXPLORATION_RATE_1 = 1.0;//1.0
                final double EXPLORATION_DECAY_1 = 0.995;//0.995
                final double MIN_EXPLORATION_RATE_1 = 0.01;//0.01

        //Player2
        final pPlayer player2 = pPlayer.AI;
        //if player1 is AI, the following parameter is important
            final boolean LOAD_MODEL_PLAYER_2 = true;
            final String MODEL_PATH_PLAYER_2 = "modelPath";
            final int NUM_EPISODE = 1000; //Total number of games played by the AI to train
            final int MAX_STEPS_PER_EPISODE = 100; //Maximum number of rounds per game
            //if loadModel is false, the following parameters are used to create the AI model
                final double LEARNING_RATE = 0.01;//0.01
                final double DISCOUNT_FACTOR = 0.99;//0.99
                double EXPLORATION_RATE = 1.0;//1.0
                final double EXPLORATION_DECAY = 0.995;//0.995
                final double MIN_EXPLORATION_RATE = 0.01;//0.01


        //Game





    }

    public enum pPlayer{
        HUMAN,
        RANDOM,
        AI
    }

}


