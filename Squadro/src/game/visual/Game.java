package game.visual;

import game.visual.Player.Player;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.util.Scanner;

public class Game {


    boolean finished;
    Scanner keyboardUser = new Scanner(System.in);
    Board board;

    public Game(){
        board = new Board();
        finished = false;
    }


    public Piece askPlayer(Player player){
        System.out.println();
        if (player.isItPlayer1())
            System.out.println("Player 1 :");
        else
            System.out.println("Player 2 : ");


        int pieceNb;

        do {
            do {
                System.out.print("Choose your piece(1,2,3,4 or 5) : ");
                pieceNb = keyboardUser.nextInt();
            } while ((pieceNb != 1) && (pieceNb != 2) && (pieceNb != 3) && (pieceNb != 4) && (pieceNb != 5));
            // if i want i can implement a msg to say the piece reached the end
        } while (player.pieces[pieceNb-1].isItAtTheEnd);

        return player.pieces[pieceNb-1];


        }

    public void play(boolean player1whoStarts, boolean training){
        int indexPlayer = player1whoStarts? 0 : 1;

        if (training){
            //TODO
        }
        else {
            while (!finished){
                board.update();
                System.out.println();
                System.out.println();
                System.out.println();
                board.printBoard();
                board.move(  askPlayer(board.players[indexPlayer])  );
                finished = board.isItFinished();
                indexPlayer = indexPlayer == 1? 0 : 1;
            }


        }
    }

}
