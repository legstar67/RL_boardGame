package game.visual.Player;

import game.visual.Piece;

public class Player_2 extends Player{
    public Player_2(){
        super.isItPlayer1 = false;
        pieces = new Piece[]{
                new Piece(1,6,3, isItPlayer1),
                new Piece(2,6,1, isItPlayer1),
                new Piece(3,6,2, isItPlayer1),
                new Piece(4,6,1, isItPlayer1),
                new Piece(5,6,3, isItPlayer1)
        };

    }
}
