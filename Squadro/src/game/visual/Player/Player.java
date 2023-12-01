package game.visual.Player;

import game.visual.Piece;

public abstract class Player {
    protected boolean isItPlayer1;
    public Piece[] pieces;
    public boolean playRandom;


    public boolean isItPlayer1(){ return this.isItPlayer1;}

    public boolean amIwin(){
        int nbPointsEnd = 0;
        for (Piece piece: pieces){
            if (piece.isItAtTheEnd)
                nbPointsEnd += 1;
        }
        return nbPointsEnd >= 4;
    }
}
