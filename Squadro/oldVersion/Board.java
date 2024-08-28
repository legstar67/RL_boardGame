package oldVersion;

import oldVersion.Player.Player;
import oldVersion.Player.Player_1;
import oldVersion.Player.Player_2;

import java.math.BigInteger;

public class Board {

    protected boolean[][][] boardModel; /// each case of the board is a tab of 3 boolean (piece ? , player_1 ? , directionInitial ?)
    protected boolean[][][] board;
    private int nbPiecePerPlayer = 5;
    private int sizeBoard = nbPiecePerPlayer+2;
    private String[][] boardStringsModel;
    private String[][] boardStrings;
    Player[] players;
    boolean V2;
    BigInteger boardV2; //on démarre de en haut à gauche (donc les 3 premiers bits sont la case interdite)
    boolean player1IsAI;
    boolean player2IsAI;

    //                    Ʌ
    //                    |
    //                    |
    //Repère du plateau : --->




    public Board(boolean V2_, boolean player1IsAI_, boolean player2IsAI_){
        player1IsAI = player1IsAI_;
        player2IsAI = player2IsAI_;
        boardModel = new boolean[sizeBoard][sizeBoard][3];
        V2 = V2_;
        boardV2 = new BigInteger("0");
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


        players = new Player[]{new Player_1(player1IsAI_),new Player_2(player2IsAI)};



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

    //for copy a board (V2 only)
    private Board(BigInteger boardV2, int nbPiecePerPlayer, int sizeBoard, boolean player1IsAI, boolean player2IsAI){
/*        protected boolean[][][] boardModel; /// each case of the board is a tab of 3 boolean (piece ? , player_1 ? , directionInitial ?)
        protected boolean[][][] board;
        protected BigInteger boardOpti;
        private int nbPiecePerPlayer = 5;
        private int sizeBoard = nbPiecePerPlayer+2;
        private String[][] boardStringsModel;
        private String[][] boardStrings;
        Player[] players;
        boolean V2;
        BigInteger boardV2; //on démarre de en haut à gauche (donc les 3 premiers bits sont la case interdite)
        boolean player1IsAI;
        boolean player2IsAI;*/


        this.boardV2 = boardV2;
        this.nbPiecePerPlayer = nbPiecePerPlayer;
        this.sizeBoard = sizeBoard;



    }


    /**
     * Fonction qui update le board
     * elle recrée tout le tableau puis replace un à un chaque piont
     * fonction largement optimisable
     */
    public void update(){
        if (V2)
            boardV2 = new BigInteger("0");
        else {
            board = new boolean[sizeBoard][sizeBoard][3];
            for (int i = 0; i < sizeBoard; i++) {
                for (int j = 0; j < sizeBoard; j++) {
                    //don't initialize the four corners
                    if (((i == 0 && j == 0) || (i == sizeBoard - 1 && j == 0) || (i == 0 && j == sizeBoard - 1) || (i == sizeBoard - 1 && j == sizeBoard - 1))) {
                        //board[i][j] = new boolean[]{false, false, false};
                        board[i][j] = null;

                    }

                }
            }
        }

        for(Player player : players){
            for(Piece piece:player.pieces){
                //TODO supp les comments of debugging here :
/*                System.out.println();
                System.out.println("-----------------------------------------------------");
                System.out.println("-----------------------------------------------------");
                System.out.println("BEGIN-----------------------------------------------------");
                System.out.println();
                printBoard();
                System.out.println();
                printBoardValue();*/

                setPieceOnBoard(piece);

/*                System.out.println();
                printBoard();
                System.out.println();
                printBoardValue();
                System.out.println("END-----------------------------------------------------");
                System.out.println("-----------------------------------------------------");
                System.out.println("-----------------------------------------------------");
                System.out.println();*/

            }

        }


    }

    public void setPieceOnBoard(Piece piece){ // FAIRLY VERIFIED (not sure at 100%)
        if (V2){
            //TODO supp les comments of debugging here :
/*            System.out.println("BEGIN SET PIECE ON BOARD##################################");
            System.out.println(boardV2.toString(2));
            System.out.print("piece.y = ");
            System.out.println(piece.y);
            System.out.print("piece.x = ");
            System.out.println(piece.x);*/
            boardV2= changeBit(boardV2,(sizeBoard-1-piece.y)*sizeBoard*3+piece.x*3+0,true);;
            boardV2= changeBit(boardV2,(sizeBoard-1-piece.y)*sizeBoard*3+piece.x*3+1,piece.isItPlayer1);
            boardV2= changeBit(boardV2,(sizeBoard-1-piece.y)*sizeBoard*3+piece.x*3+2,piece.directionInitial);
/*            System.out.
ln(boardV2.toString(2));
            System.out.println("END SET PIECE ON BOARD##################################");*/
        }
        else {
            board[sizeBoard - 1 - piece.y][piece.x][0] = true;
            board[sizeBoard - 1 - piece.y][piece.x][1] = piece.isItPlayer1;
            board[sizeBoard - 1 - piece.y][piece.x][2] = piece.directionInitial;

        }

    }


    //function to move a piece
    public void move(Piece piece){
        int xStart = piece.x;
        int yStart = piece.y;
        if (piece.isItPlayer1){

            if (piece.directionInitial) {
                for (int x = piece.x + 1; x < xStart + piece.pace; x++) { // on ne traite pas dans la boucle for de la case d'arrivée
                    if (piece.x != 6) { //TODO demander à frabrice les règles en gros
                        // là qd il arrive ou bout du premier trajet il se stop même si il lui reste des pas
                        if (isTherePiece(x, yStart)) {
                            whichPieceHere(x, yStart).kill();
                        }
                        if (piece.moveByStep())
                            this.update();
                    }
                }
            }
            else {
                for (int x = piece.x - 1; x > xStart - piece.pace; x--) { // on ne traite pas dans la boucle for de la case d'arrivée
                    if (piece.x != 0) {
                        if (isTherePiece(x, yStart)) {
                            whichPieceHere(x, yStart).kill();
                        }
                        if (piece.moveByStep())
                            this.update();
                    }
                }
            }

            if (!(piece.directionInitial == false && piece.x == 0)) {
                boolean isBusy;
                do {
                    isBusy = isTherePiece(piece.x + (piece.directionInitial ? 1 : -1), piece.y);
                    if (isBusy)
                        whichPieceHere(piece.x + (piece.directionInitial ? 1 : -1), piece.y).kill();
                    if (piece.moveByStep())
                        this.update();

                } while (isBusy);
            }


        }
        else{ // player 2

            if (piece.directionInitial){
                for (int y = piece.y - 1; y > yStart - piece.pace; y--) { // on ne traite pas dans la boucle for de la case d'arrivée
                    if (piece.y != 0) { //TODO demander à frabrice les règles en gros
                        // là qd il arrive ou bout du premier trajet il se stop même si il lui reste des pas
                        if (isTherePiece(xStart, y)) {
                            whichPieceHere(xStart, y).kill();
                        }
                        if (piece.moveByStep())
                            this.update();
                    }
                }

            }
            else {
                for (int y = piece.y + 1; y < yStart + piece.pace; y++) { // on ne traite pas dans la boucle for de la case d'arrivée
                    if (piece.y != 6) {
                        if (isTherePiece(xStart, y)) {
                            whichPieceHere(xStart, y).kill();
                        }
                        if (piece.moveByStep())
                            this.update();
                    }
                }
            }

            if (!(piece.directionInitial == false && piece.y == 6)) {
                boolean isBusy;
                int nbite = 0;//TODO TO REmove for debugging
                do {
                    isBusy = isTherePiece(piece.x, piece.y + (piece.directionInitial ? -1 : 1));
                    if (isBusy)
                        whichPieceHere(piece.x, piece.y + (piece.directionInitial ? -1 : 1)).kill();
                    if (piece.moveByStep())
                        this.update();
                    if (nbite > 10){ //TODO to remove
                        assert false;
                    }
                    nbite += 1;

                } while (isBusy);
            }


        }

    }


    public boolean isTherePiece(int x, int y){
        if (V2)
            return getXthBit(boardV2,(sizeBoard-1-y)*sizeBoard*3+x*3+0);

        else
            return (board[sizeBoard-1-y][x][0]);
    }

    public Piece whichPieceHere(int x, int y) {

/*        System.out.println("-whichPieceHere---");
        System.out.println("x = " + x);
        System.out.println("y = "+ y);*/
        assert isTherePiece(x, y);
        boolean isItPlayer1 = V2? getXthBit(boardV2,(sizeBoard-1-y)*sizeBoard*3+x*3+1) :board[sizeBoard-1-y][x][1];
        if (isItPlayer1){
            assert y != 0;
            assert players[0].pieces[y - 1].x == x; //TODO a supp qd c'est réglé
            return players[0].pieces[y - 1]; // -1 bcs we search on list with index which starts to 0
        }
        else {
            assert x != 0;
            assert players[1].pieces[x - 1].y == y; //TODO a supp qd c'est réglé
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
    public boolean whoWon(){
        for (Player player:players){
            if(player.amIwin())
                return player.isItPlayer1();

        }
        return false;
    }

    public BigInteger getBoard(){return boardV2;}
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
        int boardLength = V2? sizeBoard : board.length;
        for (int y = 0 ; y < boardLength; y++){
            for (int x = 0 ; x < boardLength; x++){
                if (!( (y==0 && x==0) || (y==sizeBoard-1 && x==0) || (y==0 && x== sizeBoard-1) || (y==sizeBoard-1 && x == sizeBoard-1)   )){
                    if ((V2 && getXthBit(boardV2,(sizeBoard-1-y)*sizeBoard*3+x*3+0)) || (!V2 && board[y][x][0])){

                        if ((V2 && getXthBit(boardV2,(sizeBoard-1-y)*sizeBoard*3+x*3+1)) || (!V2 && board[y][x][1])){ // if it's player1
                            if(((V2 && getXthBit(boardV2,(sizeBoard-1-y)*sizeBoard*3+x*3+2)) || (!V2 && board[y][x][2]))) // it's initial direction
                                boardStrings[sizeBoard-y][x+decalage] = " > ";
                            else
                                boardStrings[sizeBoard-y][x+decalage] = " < ";
                        }
                        else{
                            if(((V2 && getXthBit(boardV2,(sizeBoard-1-y)*sizeBoard*3+x*3+2)) || (!V2 && board[y][x][2]))) // it's initial direction
                                boardStrings[sizeBoard-y][x+decalage] = " V ";
                            else
                                boardStrings[sizeBoard-y][x+decalage] = " Ʌ ";
                        }


                    }
                }

            }
        }


        return boardStrings;
    }

    public BigInteger changeBit(BigInteger integer,int index,boolean value){ //VERIFIED
        integer = value ? integer.setBit(index) : integer.clearBit(index);
        return integer;
    }
    public boolean getXthBit(BigInteger integer, int index){ //VERIFIED

        BigInteger mask = new BigInteger("2").pow(index)/*.divide(new BigInteger("2"))*/;
        BigInteger resultOperation = integer.and(mask);
        boolean returnVar = !resultOperation.equals(BigInteger.ZERO);
        return returnVar;
    }



    //Debugging function --------------------------------
    public void printBoardValue() {

        if (V2) {
            for(int i = 0;i<7;i++){
                for(int j = 0;j<7;j++){
                    for(int k = 0;k<3;k++){
                        board[i][j][k] = getXthBit(boardV2,i*7*3+j*3+k);
                    }
                }

            }
            System.out.println(boardV2.toString(2));
            System.out.println("[");
            for (boolean[][] i : board) {
                System.out.print("[ ");

                for (boolean[] j : i) {
                    System.out.print("[");

                    if (j != null) {
                        for (boolean k : j) {
                            if (k)
                                System.out.print("True ");
                            else
                                System.out.print("-----");
                            System.out.print(",");


                        }
                    } else {
                        System.out.print("                  ");
                    }
                    System.out.print("] ");


                }
                System.out.println("]");

            }

            System.out.println("]");
        }
        else {
            System.out.println("[");
            for (boolean[][] i : board) {
                System.out.print("[ ");

                for (boolean[] j : i) {
                    System.out.print("[");

                    if (j != null) {
                        for (boolean k : j) {
                            if (k)
                                System.out.print("True ");
                            else
                                System.out.print("-----");
                            System.out.print(",");


                        }
                    } else {
                        System.out.print("                  ");
                    }
                    System.out.print("] ");


                }
                System.out.println("]");

            }

            System.out.println("]");
        }
    }


    //Function to be used by DL4J


/*    public Board withNewMovePiece(Piece piece){
    }*/
}
