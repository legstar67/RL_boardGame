package game.visual;
import java.util.Random;

import game.visual.Player.Player;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.util.Scanner;

public class Game {


    boolean finished;
    Scanner keyboardUser = new Scanner(System.in);
    Board board;
    int nbRound;

    public Game(int nbRound_){
        board = new Board();
        finished = false;
        nbRound = nbRound_;
    }


    public Piece askPlayer(Player player){
        int pieceNb;
        if (!player.playRandom) {
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


        return player.pieces[pieceNb-1];


        }

    public void play(boolean player1whoStarts, boolean training){
        int indexPlayer = player1whoStarts? 0 : 1;

        if (training){
            //TODO
        }
        else {
            int roundPlayed = 0;
            int gameWonByPlayer1 = 0;
            int gameWonByPlayer2 = 0;
            while(roundPlayed < nbRound) {
                board = new Board();
                finished = false;
                while (!finished) {
                    board.update();
/*                    System.out.println();
                    System.out.println();
                    System.out.println();
                    board.printBoard();*/
                    board.move(askPlayer(board.players[indexPlayer]));
                    finished = board.isItFinished();
                    indexPlayer = indexPlayer == 1 ? 0 : 1;
                }
                if (board.whoWon())
                    gameWonByPlayer1 += 1;
                else
                    gameWonByPlayer2 += 1;
                roundPlayed += 1;

            }
            System.out.println("----------END OF THE GAME LOOK THE RESULT------------");
            System.out.println("Player 1 won " + gameWonByPlayer1 + " rounds.");
            System.out.println("Player 2 won " + gameWonByPlayer2 + " rounds.");


        }
    }

}
