import game.visual.Board;
import game.visual.Game;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws InterruptedException {

/*        Board bd = new Board();
        int a = 2;
        bd.update();
        bd.printBoard();*/

        Game game = new Game();
        game.play(true,false);


    }
}