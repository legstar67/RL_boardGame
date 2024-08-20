package game;


/**
 * the third version codes the board in 40bits
 */
public class BoardV3 {

    private long board; //type long bcs it's easier to manipulate
    private static final int sizeBitsBoard = 40;
    private static final int sizeBitsPiece = 4;
    public static final int nbPiecePerPlayer = 5;
    public static final int nbOfPiece = 2 * nbPiecePerPlayer;
    private static final int maxDistancePiece = nbOfPiece + 2;
    private static final int nbPieceFinishedToWin = 4;
    //number of case the piece walks, order : Player1 bottom to top then Player2 left to right
    private static final int[] pathPieces =
            {1,3,2,3,1, //Player 1
            3,1,2,1,3}; //Player 2
    private static final int[] pathPiecesReturned =
            {3,1,2,1,3,
            1,3,2,3,1};
    private static final String[][] boardStringsModel = new String[][]{
        {"       ", "   ", " 1 ", " 3 ", " 2 ", " 3 ", " 1 ", "   ", "       "},
        {"       ", "   ", " . ", " . ", " . ", " . ", " . ", "   ", "       "},
        {" 4 (1) ", " . ", " . ", " . ", " . ", " . ", " . ", " . ", " (3) 4 "},
        {" 3 (3) ", " . ", " . ", " . ", " . ", " . ", " . ", " . ", " (1) 3 "},
        {" 2 (2) ", " . ", " . ", " . ", " . ", " . ", " . ", " . ", " (2) 2 "},
        {" 1 (3) ", " . ", " . ", " . ", " . ", " . ", " . ", " . ", " (1) 1 "},
        {" 0 (1) ", " . ", " . ", " . ", " . ", " . ", " . ", " . ", " (3) 0 "},
        {"       ", "   ", " . ", " . ", " . ", " . ", " . ", "   ", "       "},
        {"       ", "   ", " 3 ", " 1 ", " 2 ", " 1 ", " 3 ", "   ", "       "}
    };
    public static long INIT_BOARD = 0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000L;


    /**
     * move the piece following the 3 rules of the game:
     *      - stop at the end of the colon/line
     *      - at the end of colon/line, change the path of the piece
     *      - if there is an enemy in its way, it makes it return to the beginning of the line (extremity behind it)
     *      - if it meets an enemy, it finishes its course juste after the enemy
     * @param board
     * @param indexPiece
     * @return
     */
    public static long withPiecePlayed(long board, int indexPiece){
        if (isPieceFinished(board,indexPiece))
            return Long.MAX_VALUE;
        checkArgument(!(isPieceFinished(board,indexPiece)));
        long newBoard = board;
        boolean meetEnemy = false;
        int whereIsThisPiece = whereIsThisPiece(board,indexPiece);
        int i = 0;
        do{
            i++;
            if (isPieceHere(newBoard,indexPiece,i)){
                meetEnemy = true;
                int indexPieceEnemy = whereIsThisPlace_returnIndexPieceEnemy(newBoard,indexPiece,i);
                newBoard = withReturnedPiece(newBoard,indexPieceEnemy);
            }
        }while ( (
                (meetEnemy && isPieceHere(board,indexPiece,i))
                    || (i < (whereIsThisPiece < maxDistancePiece/2 ? pathPieces[indexPiece] : pathPiecesReturned[indexPiece]) && !meetEnemy)
                )
                && i < (whereIsThisPiece < maxDistancePiece/2 ? maxDistancePiece/2 - whereIsThisPiece : maxDistancePiece - whereIsThisPiece));

        newBoard = withPieceMoved(newBoard,indexPiece,i);
        return newBoard;
    }

    private static long withReturnedPiece(long board, int indexPiece){
            int whereIsThisPiece = whereIsThisPiece(board,indexPiece);
            int negativePathToReturn = whereIsThisPiece < maxDistancePiece/2 ? -whereIsThisPiece : maxDistancePiece/2 - whereIsThisPiece;
            return withPieceMoved(board,indexPiece,negativePathToReturn);
    }

    public static boolean[] getPiecesPlayable(long board){ //TODO on peut opti en remplaçant "boolean[]" par "short"
        boolean[] indexesPiecePlayable = new boolean[nbOfPiece];
        for(int i = 0; i < nbOfPiece; i++){
            if(isPieceFinished(board,i))
                indexesPiecePlayable[i] = false;
            else
                indexesPiecePlayable[i] = true;
        }
        return indexesPiecePlayable;
    }
    public static String[][] toTabString(long board){
        String[][] str = copyStringArray(boardStringsModel);
        String pieceUp = " Ʌ ";
        String pieceDown = " V ";
        String pieceRight = " > ";
        String pieceLeft = " < ";
        for(int indexPiece = 0; indexPiece < nbOfPiece; indexPiece++){
            int whereIsThisPiece = whereIsThisPlace_returnIndexPieceEnemy(board,indexPiece,0);
            if(indexPiece < nbPiecePerPlayer){ //Player 1
                str[2 + (-indexPiece)+nbPiecePerPlayer-1][whereIsThisPiece == -1 ? 1:2+whereIsThisPiece-nbPiecePerPlayer] =
                        whereIsThisPiece(board,indexPiece) <= nbPiecePerPlayer ? pieceRight : pieceLeft;
            }
            else { //Player 2
                str[1+ (-(whereIsThisPiece != nbOfPiece ? whereIsThisPiece : nbPiecePerPlayer)) + nbPiecePerPlayer][2 + indexPiece-nbPiecePerPlayer] =
                whereIsThisPiece(board,indexPiece) <= nbPiecePerPlayer ? pieceUp : pieceDown;
            }
        }
        return str;
    }

    public static boolean isGameFinished(long board){
        boolean finished = false;
        int count = 0;
        for(int i = 0; i < nbPiecePerPlayer; i++ ){
            if (isPieceFinished(board,i)){
                count++;
                if (count >= nbPieceFinishedToWin)
                    finished = true;
            }
        }
        count = 0;
        for(int i = nbPiecePerPlayer; i < nbOfPiece; i++ ){
            if (isPieceFinished(board,i)){
                count++;
                if (count >= nbPieceFinishedToWin)
                    finished = true;
            }
        }
        return finished;
    }
    public static int whoWin(long board){
        checkArgument(isGameFinished(board));
        int count = 0;
        int player = 0;
        for(int i = 0; i < nbPiecePerPlayer; i++ ){
            if (isPieceFinished(board,i)){
                count++;
                if (count >= nbPieceFinishedToWin)
                    player = 1;
            }
        }
        count = 0;
        for(int i = nbPiecePerPlayer; i < nbOfPiece; i++ ){
            if (isPieceFinished(board,i)){
                count++;
                if (count >= nbPieceFinishedToWin)
                    player = 2;
            }
        }
        return player;
    }

    public static boolean isPieceFinished(long board, int indexPiece){
        return maxDistancePiece == whereIsThisPiece(board,indexPiece);
    }

     static boolean isPieceHere(long board, int indexPiece, int XthCaseAfter){
        checkIndexPiece(indexPiece);
        int indexPieceEnemy = whereIsThisPlace_returnIndexPieceEnemy(board,indexPiece,XthCaseAfter);
        if (indexPieceEnemy < 0 || indexPieceEnemy == nbOfPiece)
            return false; //bcs at the extremity , there is always any enemy
        else
            return indexPiece == whereIsThisPlace_returnIndexPieceEnemy(board, indexPieceEnemy,0);

    }

    /**
     * give the position relatively to the index of piece enemy
     * @param board
     * @param indexPiece
     * @param XthCaseAfter
     * @return
     *      -1 if the piece didn't move yet or if it has finished
     *      nbPiece if the piece is at the other extremity
     *      otherwise indexOfPieceEnemy of the line/colonn it is
     *
     */
     static int whereIsThisPlace_returnIndexPieceEnemy(long board, int indexPiece, int XthCaseAfter){
        checkIndexPiece(indexPiece);
        int maxDistanceInOneWay = maxDistancePiece/2;
        int whereIsThisPiece = whereIsThisPiece(board,indexPiece) + XthCaseAfter;

        checkArgument(whereIsThisPiece >= 0 && whereIsThisPiece <= maxDistancePiece);

        if (indexPiece >= nbPiecePerPlayer){ //Player 2
            if(whereIsThisPiece == 0 || whereIsThisPiece == maxDistancePiece){
                return -1; //T
            }else if(whereIsThisPiece < maxDistanceInOneWay){
                return whereIsThisPiece - 1;
            } else if(whereIsThisPiece == maxDistanceInOneWay){
                return nbOfPiece;
            }else{
                int temp = (-whereIsThisPiece) % (nbPiecePerPlayer+1);
                return (temp<0?temp+nbPiecePerPlayer+1:temp) - 1;
            }
        } else { //Player 1
            int adjusting = nbPiecePerPlayer - 1;
            if(whereIsThisPiece == 0 ||whereIsThisPiece == maxDistancePiece){
                return -1;
            }else if(whereIsThisPiece < maxDistanceInOneWay){
                return adjusting + whereIsThisPiece;
            }else if (whereIsThisPiece == maxDistanceInOneWay) {
                return nbOfPiece;
            }else {
                return adjusting + ((-whereIsThisPiece)%(nbPiecePerPlayer+1) + nbPiecePerPlayer+1);/*bcs -a%b with a,b postive give negative */
            }
        }
    }

     static int whereIsThisPiece(long board, int indexPiece){
        checkIndexPiece(indexPiece);
        int gap = indexPiece * sizeBitsPiece;
        return (int) ((board & (0b1111L << gap))>>gap); //TODO rendre le 1111L dépendant d'une constante du jeu
    }

    /**
     *  just move the piece (without considering any rules) with the path specified
     * @param board
     * @param indexPiece //index 0-9, order : Player1 bottom to top then Player2 left to right
     * @param path
     * @return
     */
     static long withPieceMoved(long board, int indexPiece, int path){
        checkIndexPiece(indexPiece);
        checkArgument(!(isPieceFinished(board,indexPiece)));
        int gap = indexPiece * sizeBitsPiece;
        long piece = whereIsThisPiece(board, indexPiece);
        piece += path;
        checkArgument(0 <= piece && piece <= maxDistancePiece);
        for(int i = 0; i < sizeBitsPiece ; i++){
            board = changeXthBit(board, gap + i,false);
        }
        return board | (piece << gap);
    }
     static long withPieceMoved(long board, int indexPiece){
        return withPieceMoved(board,indexPiece,pathPieces[indexPiece]);
    }

    public static boolean getXthBit(long board, int index){
        checkIndexBit(index);
        return (board & (1L << index)) == 1L << index;
    }
    public static long changeXthBit(long board, int index,boolean value){
        checkIndexBit(index);
        return value? board | 1L <<index : board & ~(1L <<index);
    }

     static void checkIndexBit(int indexBit){
        checkArgument(0<= indexBit && indexBit < sizeBitsBoard);
    }
     static void checkIndexPiece(int indexPiece){
        checkArgument(0<= indexPiece && indexPiece < nbOfPiece);
    }
     static void checkArgument(boolean arg){
        if (!arg){
            throw new IllegalArgumentException();
        }
    }
    // Méthode pour copier un tableau bidimensionnel de chaînes de caractères
    public static String[][] copyStringArray(String[][] source) {
        if (source == null) {
            return null;
        }

        // Création d'un nouveau tableau avec les mêmes dimensions que le tableau source
        String[][] result = new String[source.length][];

        for (int i = 0; i < source.length; i++) {
            // Copie de chaque sous-tableau
            result[i] = new String[source[i].length];
            for (int j = 0; j < source[i].length; j++) {
                result[i][j] = source[i][j];
            }
        }
        return result;
    }
}
