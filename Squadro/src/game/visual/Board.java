package game.visual;

import game.visual.Player.Player;
import game.visual.Player.Player_1;
import game.visual.Player.Player_2;

public class Board {

    protected boolean[][][] boardModel; /// each case of the board is a tab of 3 boolean (piece ? , player_1 ? , directionInitial ?)
    protected boolean[][][] board;
    private int nbPiecePerPlayer = 5;
    private int sizeBoard = nbPiecePerPlayer+2;
    private String[][] boardStringsModel;
    private String[][] boardStrings;
    Player[] players;




    public Board(){
        boardModel = new boolean[sizeBoard][sizeBoard][3];
        for (int i = 0 ; i < sizeBoard ; i++){
            for(int j = 0; j < sizeBoard ; j++){
                //don't initialize the four corners
                if (( (i==0 && j==0) || (i==sizeBoard-1 && j==0) || (i==0 && j== sizeBoard-1) || (i==sizeBoard-1 && j == sizeBoard-1)   )){
                    //board[i][j] = new boolean[]{false, false, false};
                    boardModel[i][j] = null;

                }

            }
        }

        board = new boolean[sizeBoard][sizeBoard][3];


        players = new Player[]{new Player_1(),new Player_2()};



        boardStringsModel = new String[][]{
                {"   ", "   ", " 3 ", " 1 ", " 2 ", " 1 ", " 3 ", "   ", "   "},
                {"   ", "   ", " . ", " . ", " . ", " . ", " . ", "   ", "   "},
                {" 1 ", " . ", " . ", " . ", " . ", " . ", " . ", " . ", " 1 "},
                {" 3 ", " . ", " . ", " . ", " . ", " . ", " . ", " . ", " 3 "},
                {" 2 ", " . ", " . ", " . ", " . ", " . ", " . ", " . ", " 2 "},
                {" 3 ", " . ", " . ", " . ", " . ", " . ", " . ", " . ", " 3 "},
                {" 1 ", " . ", " . ", " . ", " . ", " . ", " . ", " . ", " 1 "},
                {"   ", "   ", " . ", " . ", " . ", " . ", " . ", "   ", "   "},
                {"   ", "   ", " 3 ", " 1 ", " 2 ", " 1 ", " 3 ", "   ", "   "}
        };
        boardStrings = new String[boardStringsModel.length][boardStringsModel.length];
    }


    /**
     * Fonction qui update le board
     * elle recrée tout le tableau puis replace un à un chaque piont
     * fonction largement optimisable
     */
    public void update(){
        board = new boolean[sizeBoard][sizeBoard][3];
        for (int i = 0 ; i < sizeBoard ; i++){
            for(int j = 0; j < sizeBoard ; j++){
                //don't initialize the four corners
                if (( (i==0 && j==0) || (i==sizeBoard-1 && j==0) || (i==0 && j== sizeBoard-1) || (i==sizeBoard-1 && j == sizeBoard-1)   )){
                    //board[i][j] = new boolean[]{false, false, false};
                    board[i][j] = null;

                }

            }
        }
        for(Player player : players){
            for(Piece piece:player.pieces){
                setPieceOnBoard(piece);
            }

        }


    }

    public void setPieceOnBoard(Piece piece){
        board[sizeBoard-1-piece.y][piece.x][0] = true;
        board[sizeBoard-1-piece.y][piece.x][1] = piece.isItPlayer1;
        board[sizeBoard-1-piece.y][piece.x][2] = piece.directionInitial;


    }


    //function to move a piece
    public void move(Piece piece){
        int xStart = piece.x;
        int yStart = piece.y;
        if (piece.isItPlayer1){

            if (piece.directionInitial) {
                for (int x = piece.x + 1; x < xStart + piece.pace; x++) { // on ne traite pas dans la boucle for de la case d'arrivée
                    if (isTherePiece(x, yStart)) {
                        whichPieceHere(x, yStart).kill();
                    }
                    piece.moveByStep();
                }
            }
            else {
                for (int x = piece.x - 1; x > xStart - piece.pace; x--) { // on ne traite pas dans la boucle for de la case d'arrivée
                    if (isTherePiece(x, yStart)) {
                        whichPieceHere(x, yStart).kill();
                    }
                    piece.moveByStep();
                }
            }

            boolean isBusy;
            do {
                isBusy = isTherePiece(piece.x +( piece.directionInitial? 1:-1) ,piece.y );
                if(isBusy)
                    whichPieceHere(piece.x+( piece.directionInitial? 1:-1), piece.y).kill();
                piece.moveByStep();

            } while (isBusy);


        }
        else{ // player 2

            if (piece.directionInitial){
                for (int y = piece.y - 1; y > yStart - piece.pace; y--) { // on ne traite pas dans la boucle for de la case d'arrivée
                    if (isTherePiece(xStart, y)) {
                        whichPieceHere(xStart, y).kill();
                    }
                    piece.moveByStep();
                }

            }
            else {
                for (int y = piece.y + 1; y < yStart + piece.pace; y++) { // on ne traite pas dans la boucle for de la case d'arrivée
                    if (isTherePiece(xStart, y)) {
                        whichPieceHere(xStart, y).kill();
                    }
                    piece.moveByStep();
                }
            }
            boolean isBusy;
            do {
                isBusy = isTherePiece(piece.x ,piece.y +( piece.directionInitial? -1:1));
                if(isBusy)
                    whichPieceHere(piece.x, piece.y+( piece.directionInitial? -1:1)).kill();
                piece.moveByStep();

            } while (isBusy);


        }

    }


    public boolean isTherePiece(int x, int y){
        //printBoardValue(); //TODO to remove

        return (board[sizeBoard-1-y][x][0]);
    }

    public Piece whichPieceHere(int x, int y) {
        System.out.println("-whichPieceHere---");
        System.out.println("x = " + x);
        System.out.println("y = "+ y);
        assert isTherePiece(x, y);
        boolean isItPlayer1 = board[y][x][1];

        if (isItPlayer1){
            assert y != 0;
        return players[0].pieces[y - 1]; // -1 bcs we search on list with index which starts to 0
        }
        else {
            assert x != 0;
            return players[1].pieces[x - 1];
        }


    }

    public boolean isItFinished(){
        for (Player player:players){
            if(player.amIwin())
                return true;

        }
        return false;
    }


/*    private void update(){
        //copy from boardModel to board



        for(Player player:players){
            for(Piece piece: player.pieces){
                board


            }

        }
    }*/





    // print the board visible by the players
    // use of these caracters "<" ">" "v" "Ʌ"
    public void printBoard() { //throws InterruptedException
        board2Strings();

        for (String[] i:boardStrings){
            for(String j:i){
                System.out.print(j);
            }
            System.out.println();
        }
/*        for (int i = 0; i < 10; i++) {

            System.out.print("Frame " + i);

            // Attendre pendant un court instant pour simuler l'animation
            Thread.sleep(500);

            // Effacer la ligne précédente en utilisant des caractères de contrôle ANSI
            System.out.print("\r\033[K");

            // Revenir au début de la ligne
            System.out.print("\r");

        }*/
    }

    public String[][] board2Strings(){
        //copy the model to the right boardStrigs
        for(int i = 0 ; i < boardStringsModel.length ; i++){
            for(int j = 0 ; j < boardStringsModel.length; j++){
                boardStrings[i][j] = boardStringsModel[i][j];
            }
        }



        int decalage = 1;
        for (int y = 0 ; y < board.length; y++){
            for (int x = 0 ; x < board.length; x++){
                if (!( (y==0 && x==0) || (y==sizeBoard-1 && x==0) || (y==0 && x== sizeBoard-1) || (y==sizeBoard-1 && x == sizeBoard-1)   )){
                    if (board[y][x][0]){

                        if (board[y][x][1]){ // if it's player1
                            if(board[y][x][2]) // it's initial direction
                                boardStrings[y+decalage][x+decalage] = " > ";
                            else
                                boardStrings[y+decalage][x+decalage] = " < ";
                        }
                        else{
                            if(board[y][x][2]) // it's initial direction
                                boardStrings[y+decalage][x+decalage] = " V ";
                            else
                                boardStrings[y+decalage][x+decalage] = " Ʌ ";
                        }


                    }
                }

            }
        }


        return boardStrings;
    }



    //Debugging function --------------------------------
    public void printBoardValue(){
        System.out.println("[");
        for (boolean[][] i : board){
            System.out.print("[ ");

            for (boolean[] j : i){
                System.out.print("[");

                if(j != null){
                    for (boolean k : j){
                        if (k)
                            System.out.print("True ");
                        else
                            System.out.print("-----");
                        System.out.print(",");


                    }
                }
                else {
                    System.out.print("                  ");
                }
                System.out.print("] ");


            }
            System.out.println("]");

        }

        System.out.println("]");
    }

}
