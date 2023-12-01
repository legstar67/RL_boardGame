package game.visual;

public class Piece {
    int x;
    int y;
    boolean directionInitial; //when the piece returns itself , it becomes false
    int pace;
    boolean isItPlayer1;
    public boolean isItAtTheEnd;



    public Piece(int newX,int newY,int newPace, boolean isItPlayer1_){
        x = newX;
        y = newY;
        pace = newPace;
        isItPlayer1 = isItPlayer1_;
        directionInitial = true;
        isItAtTheEnd = false;

    }

    public void move(){
        if (isItPlayer1){
            if (directionInitial)
                x += pace;
            else
                x-= 1;
        }
        else{
            if (directionInitial)
                y -= pace;
            else
                y += 1;
        }
    }

    public void moveByStep(){
        System.out.println("Piece moveByStep :");
        System.out.println("Player1 " + isItPlayer1);
        System.out.println("pace = "+ pace);
        if (isItPlayer1){
            if (directionInitial) {
                System.out.println("x = " + x + "-->" + (x + 1));
                x += 1;
            }
            else{
                System.out.println("x = " + x + "-->" + (x - 1));
                x -= 1;
            }
        }
        else{
            if (directionInitial) {
                System.out.println("y = " + y + "-->" + (y - 1));
                y -= 1;
            }
            else {
                System.out.println("y = " + y + "-->" + (y + 1));
                y += 1;
            }
        }
        updateDirection();
    }

    public void updateDirection(){
        if (x == 6 || (!directionInitial && x == 0 )){ // only player 1 can do that
            if (directionInitial)
                directionInitial = false;
            else
                isItAtTheEnd = true;
        }
        else if (y == 0 || (!directionInitial && y == 6 )){ // only player 1 can do that
            if (directionInitial)
                directionInitial = false;
            else
                isItAtTheEnd = true;
        }
    }

    public void kill(){
        System.out.println("---" +
                "kill : piece :");
        System.out.println("Player1 " + isItPlayer1);
        System.out.println("pace = "+ pace);
        System.out.println("x = " + x);
        System.out.println("y = " + y);

        System.out.println("---");
        if (isItPlayer1) {
            if (directionInitial)
                x = 0;
            else
                x = 6;
        }
        else {
            if (directionInitial)
                y = 6;
            else
                y = 0;

        }
    }






    //Get information


/*    public int getPace() {
        return pace;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }*/
}
