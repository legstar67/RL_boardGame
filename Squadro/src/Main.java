

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws InterruptedException {
        //-------------------       Parameters of the program       -------------------

        //Player1
        final pPlayer player1 = pPlayer.AI;
        //if player1 is not trained, the following parameter is useless
        final boolean LOAD_MODEL_PLAYER1 = true;
        final String MODEL_PATH_PLAYER_1 = "modelPath";
        final int NUM_EPISODE = 1000; //Total number of games played by the AI to train
        final int MAX_STEPS_PER_EPISODE = 100; //Maximum number of rounds per game
        //if loadModel is false, the following parameters are used to create the AI model
        final double learningRate = 0.1; //Learning rate of the AI






        //Player2
        pPlayer player2 = pPlayer.RANDOM;


    }

    public enum pPlayer{
        HUMAN,
        RANDOM,
        AI
    }

}


