package game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardV3Test {

    @Test
    void printBoard() {
        long board = 0;
        String[][] boardString0 = new String[][]{
                {"   ", "   ", " 3 ", " 1 ", " 2 ", " 1 ", " 3 ", "   ", "   "},
                {"   ", "   ", " . ", " . ", " . ", " . ", " . ", "   ", "   "},
                {" 1 ", " > ", " . ", " . ", " . ", " . ", " . ", " . ", " 1 "},
                {" 3 ", " > ", " . ", " . ", " . ", " . ", " . ", " . ", " 3 "},
                {" 2 ", " > ", " . ", " . ", " . ", " . ", " . ", " . ", " 2 "},
                {" 3 ", " > ", " . ", " . ", " . ", " . ", " . ", " . ", " 3 "},
                {" 1 ", " > ", " . ", " . ", " . ", " . ", " . ", " . ", " 1 "},
                {"   ", "   ", " Ʌ ", " Ʌ ", " Ʌ ", " Ʌ ", " Ʌ ", "   ", "   "},
                {"   ", "   ", " 3 ", " 1 ", " 2 ", " 1 ", " 3 ", "   ", "   "}
        };
        String[][] str = BoardV3.toTabString(board);
        for(int i = 0; i < 9 ; i++) {
            System.out.println();
            for (int j = 0; j < 9; j++)
                System.out.print(boardString0[i][j]);
        }
        for(int i = 0; i < 9 ; i++) {
            System.out.println();
            for (int j = 0; j < 9; j++)
                System.out.print(str[i][j]);
        }

    }
    @Test
    void printBoardAdvanced() {
        long board = 0b1100_0000_0010_0000_1000_0111_1100_0000_0001_0100L;
        String[][] boardString0 = new String[][]{
                {"   ", "   ", " 3 ", " 1 ", " 2 ", " 1 ", " 3 ", "   ", "   "},
                {"   ", "   ", " . ", " . ", " . ", " . ", " . ", "   ", "   "},
                {" 1 ", " . ", " . ", " . ", " . ", " . ", " < ", " . ", " 1 "},
                {" 3 ", " < ", " V ", " . ", " . ", " . ", " . ", " . ", " 3 "},
                {" 2 ", " > ", " . ", " . ", " . ", " . ", " . ", " . ", " 2 "},
                {" 3 ", " . ", " > ", " . ", " Ʌ ", " . ", " . ", " . ", " 3 "},
                {" 1 ", " . ", " . ", " . ", " . ", " > ", " . ", " . ", " 1 "},
                {"   ", "   ", " . ", " Ʌ ", " . ", " Ʌ ", " V ", "   ", "   "},
                {"   ", "   ", " 3 ", " 1 ", " 2 ", " 1 ", " 3 ", "   ", "   "}
        };
        String[][] str = BoardV3.toTabString(board);
        for(int i = 0; i < 9 ; i++) {
            System.out.println();
            for (int j = 0; j < 9; j++)
                System.out.print(boardString0[i][j]);
        }
        for(int i = 0; i < 9 ; i++) {
            System.out.println();
            for (int j = 0; j < 9; j++)
                System.out.print(str[i][j]);
        }

    }

    @Test
    void isGameFinished() {
        long board = 0b0000_0000_0010_0010_0000_0111_0000_1100_0000_0001;
        assertFalse(BoardV3.isGameFinished(board));
        long board_2 = 0b1100_1100_1100_0010_1100_0111_1100_1100_1100_0001L;
        assertTrue(BoardV3.isGameFinished(board_2));

    }

    @Test
    void isPieceHere() {
        long board = 0b0000_0000_0010_0000_0000_0111_0000_0000_0000_0000;
        assertTrue(BoardV3.isPieceHere(board,1,3));
        assertFalse(BoardV3.isPieceHere(board,1,2));
        assertTrue(BoardV3.isPieceHere(board,9,5));
        assertFalse(BoardV3.isPieceHere(board,0,11));

    }

    @Test
    void whereIsThisPlace_returnIndexPieceEnemy() {
        long board = 0b0000_0000_0010_0000_0000_0111_0000_0000_0000_0000;
        int expected_Piece4 = 9;
        int expected_Piece7 = 1;
        assertEquals(expected_Piece4,BoardV3.whereIsThisPlace_returnIndexPieceEnemy(board,4,0));
        assertEquals(expected_Piece7,BoardV3.whereIsThisPlace_returnIndexPieceEnemy(board,7,0));
        long board2 = 0b1101_1100_0010_0000_0000_0111_1100_0000_0000_0000L;
        int expected_Piece3 = -1;
        int expected_Piece8 = -1;
        assertEquals(expected_Piece3,BoardV3.whereIsThisPlace_returnIndexPieceEnemy(board2,3,0));
        assertEquals(expected_Piece8,BoardV3.whereIsThisPlace_returnIndexPieceEnemy(board2,8,0));
        assertThrows(IllegalArgumentException.class, () -> {
            System.out.println(BoardV3.whereIsThisPlace_returnIndexPieceEnemy(board2,9,0));});
        long board3 = 0b0000_0000_0000_0000_1000_0000_0000_0000_0000_1001;
        int expected_Piece5 = 3;
        int expected_Piece0 = 7;
        assertEquals(expected_Piece5,BoardV3.whereIsThisPlace_returnIndexPieceEnemy(board3,5,0));
        assertEquals(expected_Piece0, BoardV3.whereIsThisPlace_returnIndexPieceEnemy(board3,0,0));
    }

    @Test
    void whereIsThisPiece() {
        long board = 0b0011_0100;
        long expected = 0b0100;
        long expected_2 = 0b0011;
        assertEquals(expected,BoardV3.whereIsThisPiece(board,0));
        assertEquals(expected_2,BoardV3.whereIsThisPiece(board,1));
    }

    @Test
    void withPieceMoved() {
        long board = 0b0011_0100;
        long expected = 0b0100_0100;
        long expected_2 = 0b0011_0111;
        assertEquals(expected,BoardV3.withPieceMoved(board,1,1));
        assertEquals(expected_2, BoardV3.withPieceMoved(board, 0,3));
    }

    @Test
    void testWithPieceMoved() {
    }

    @Test
    void getXthBit() {
        long board = 0b101111;
        assertTrue(BoardV3.getXthBit(board, 5));
        assertFalse(BoardV3.getXthBit(board, 4));
    }

    @Test
    void changeXthBit() {
        long board = 0b1111;
        long expected = 0b1011;
        assertEquals(expected, BoardV3.changeXthBit(board,2,false));
        assertEquals(board, BoardV3.changeXthBit(board,3,true));
    }

    @Test
    void checkIndexBit() {
        assertDoesNotThrow(() -> {BoardV3.checkIndexBit(32);});
        assertThrows(IllegalArgumentException.class, () -> {
            BoardV3.checkIndexBit(-1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            BoardV3.checkIndexBit(40);
        });
    }

    @Test
    void checkIndexPiece() {
        assertDoesNotThrow(() -> {BoardV3.checkIndexPiece(6);});
        assertThrows(IllegalArgumentException.class, () -> {
            BoardV3.checkIndexPiece(-1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            BoardV3.checkIndexPiece(10);
        });
    }

    @Test
    void checkArgument() {
        assertThrows(IllegalArgumentException.class, () -> {
            BoardV3.checkArgument(false);
        });
        assertDoesNotThrow(() -> {BoardV3.checkArgument(true);});

    }
}