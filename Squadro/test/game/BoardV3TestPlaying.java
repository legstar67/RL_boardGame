package game;

import game.BoardV3.*;

import static game.BoardV3.*;


public class BoardV3TestPlaying {
    static boolean random = true;
    static int nbOfGames = 1_000_000_0;
    static long board = BoardV3.INIT_BOARD;
    static boolean printBoard = false;

    public static void main(String[] args) {
        java.util.Scanner scan = new java.util.Scanner(System.in);
        int gamePlayed = 0;
        int winPlayer1 = 0;
        int winPlayer2 = 0;


        do {


            board = BoardV3.INIT_BOARD;
            if (printBoard) {
                System.out.println("Initial board:");
                tabToString(BoardV3.toTabString(board));
            }


            int player = 1;
            do {
                if (printBoard) {
                    System.out.println("");
                    System.out.print("Player " + player + " choose a piece to play" + (player == 1 ? "(0,1,2,3,4)" : "(5,6,7,8,9)") + ": ");
                }
                int playerChoice;
                if (random){
                    playerChoice = chosePieceRandom(player,getPiecesPlayable(board));
                    if (printBoard) {
                        System.out.print(playerChoice);
                    }

                } else {
                    playerChoice = scan.nextInt();
                }
                if(printBoard) {
                    System.out.println("");
                    System.out.println("");
                    System.out.println("");
                    System.out.println("");
                }
                board = withPiecePlayed(board, playerChoice);
                if(printBoard) {
                    System.out.println("Board after playing:");
                    tabToString(BoardV3.toTabString(board));
                }
                player = player == 1 ? 2 : 1;

            } while (!isGameFinished(board));
            int i = player == 1 ? winPlayer2++ : winPlayer1++;
        }while (gamePlayed++ < nbOfGames);
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("Game finished");
        System.out.println("Player 1 won " + winPlayer1 + " times");
        System.out.println("Player 2 won " + winPlayer2 + " times");

    }

    private static void tabToString(String[][] tab) {
        for(int i = 0; i < 9 ; i++) {
            System.out.println();
            for (int j = 0; j < 9; j++)
                System.out.print(tab[i][j]);
        }
    }

    /**
     * Chose a piece randomly if player 1 : (0,1,2,3,4) if player 2 : (5,6,7,8,9)
     * @param player
     * @return
     */
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
        int temp = player == 1 ? piece : piece + 5;
        int gg = 1;
        return temp;
    }
}
