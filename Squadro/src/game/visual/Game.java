package game.visual;
import java.math.BigInteger;
import java.util.Random;

import game.visual.Player.Player;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.util.Scanner;

public class Game {


    boolean finished;
    Scanner keyboardUser = new Scanner(System.in);
    Board board;
    int nbRound;
    boolean versionOptimized;
    boolean trainingPlayer2;
    boolean trainingPlayer1;

    public Game(int nbRound_,boolean versionOptimized_,boolean trainingPlayer1_, boolean trainingPlayer2_){
        versionOptimized = versionOptimized_;
        board = new Board(versionOptimized,trainingPlayer1_,trainingPlayer2_);
        finished = false;
        nbRound = nbRound_;
        trainingPlayer2 = trainingPlayer2_;
        trainingPlayer1 = trainingPlayer1_;


    }


    public int askPlayer(Player player){
        int pieceNb;
        if (trainingPlayer2 && !player.isItPlayer1()){
            int[] pieceAvailable = new int[5];
            int max = 0;
            for (int i = 0; i < 5 ; i++){
                if (!player.pieces[i].isItAtTheEnd) {
                    pieceAvailable[max] = i + 1;
                    max += 1;
                }
            }
            pieceNb = player.getNextAction(board.getBoard(),pieceAvailable,player.epsilon);
            player.epsilon -= 0.0000001;
        }

        else if (!player.playRandom) {
            System.out.println();
            if (player.isItPlayer1())
                System.out.println("Player 1 :");
            else
                System.out.println("Player 2 : ");



            do {
                do {
                    System.out.print("Choose your piece(1,2,3,4 or 5) : ");
                    pieceNb = keyboardUser.nextInt();
                } while ((pieceNb != 1) && (pieceNb != 2) && (pieceNb != 3) && (pieceNb != 4) && (pieceNb != 5));
                // if i want i can implement a msg to say the piece reached the end
            } while (player.pieces[pieceNb - 1].isItAtTheEnd);
        }
        else{
            Random random = new Random();
            int pieceIndex;
            int[] pieceAvailable = new int[5];
            int max = 0;
            int min =1;

            for (int i = 0; i < 5 ; i++){
                if (!player.pieces[i].isItAtTheEnd) {
                    pieceAvailable[max] = i + 1;
                    max += 1;
                }
            }

            pieceIndex = random.nextInt(max)+ min;
            pieceNb = pieceAvailable[pieceIndex-1];

        }


        return pieceNb;


    }

    public void play(boolean player1whoStarts, boolean training){
        int indexPlayer = player1whoStarts? 1 : 2;

        int roundPlayed = 0;
        int gameWonByPlayer1 = 0;
        int gameWonByPlayer2 = 0;
        if (training) {
            while (roundPlayed < nbRound) {
                board = new Board(versionOptimized,trainingPlayer1,trainingPlayer2);
                finished = false;
                board.update();
                BigInteger newStatePlayer2 = board.getBoard();
                BigInteger oldStatePlayer2 = new BigInteger("0"); //NOT USEFUL, it's just to put sth into
                int pieceChoosen;
                int actionPlayer2 = 0;//NOT USEFUL, it's just to put sth into
                boolean isItFirstMove = true;
                while (!finished) {
                  System.out.println();
                    System.out.println();
                    System.out.println();
                    board.printBoard();

                    pieceChoosen = askPlayer(board.players[indexPlayer - 1]);
                    board.move(board.players[indexPlayer - 1].pieces[pieceChoosen-1]);
                    finished = board.isItFinished();
                    indexPlayer = indexPlayer == 2 ? 1 : 2;
                    //TODO à supp :
                    System.out.println("__________________________________________________________________");
                    System.out.println("le nombre de partie effectue est : ");
                    System.out.println((gameWonByPlayer1+gameWonByPlayer2));
                    System.out.println();


                    if (indexPlayer == 2) {
                        oldStatePlayer2 = newStatePlayer2;
                        actionPlayer2 = pieceChoosen;
                        isItFirstMove = false;
                    }
                    board.update();
                    if (indexPlayer == 1 && !isItFirstMove) {
                        newStatePlayer2 = board.getBoard();
                        board.players[indexPlayer - 1].updateQvalues(oldStatePlayer2,actionPlayer2,0,newStatePlayer2 );
                    }
                }
                if (board.whoWon()) {
                    gameWonByPlayer1 += 1;
                    board.players[indexPlayer - 1].updateQvalues(oldStatePlayer2,actionPlayer2,-1000,newStatePlayer2 );
                }
                else {
                    gameWonByPlayer2 += 1;
                    board.players[indexPlayer - 1].updateQvalues(oldStatePlayer2, actionPlayer2, 1000, newStatePlayer2);
                }
                roundPlayed += 1;
            }
        }

        else {
            while(roundPlayed < nbRound) {
                board = new Board(versionOptimized, trainingPlayer1, trainingPlayer2);
                finished = false;
                while (!finished) {
                    board.update();



                    //TODO supp les Decomment here if need of the board :
                    //System.out.println();
                    //System.out.println();
                    //System.out.println();
                    //board.printBoard();


                    board.move(board.players[indexPlayer - 1].pieces[askPlayer(board.players[indexPlayer - 1]) - 1]);
                    ;
                    finished = board.isItFinished();
                    indexPlayer = indexPlayer == 2 ? 1 : 2;
                }

                if (board.whoWon())
                    gameWonByPlayer1 += 1;
                else
                    gameWonByPlayer2 += 1;
                roundPlayed += 1;
            }

            }
            System.out.println("----------END OF THE GAME LOOK THE RESULT------------");
            System.out.println("Player 1 won " + gameWonByPlayer1 + " rounds.");
            System.out.println("Player 2 won " + gameWonByPlayer2 + " rounds.");


    }
}



