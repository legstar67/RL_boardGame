package game.visual.Player;

import com.sun.source.tree.WhileLoopTree;
import game.visual.Piece;

public class Player_1 extends Player{

    public Player_1(){
        super.isItPlayer1 = true;
        super.playRandom = true;
        pieces = new Piece[]{
                new Piece(0,1,1, isItPlayer1),
                new Piece(0,2,3, isItPlayer1),
                new Piece(0,3,2, isItPlayer1),
                new Piece(0,4,3, isItPlayer1),
                new Piece(0,5,1, isItPlayer1)
        };

    }


}
