import game.visual.Board;
import game.visual.Game;

import javax.management.ObjectName;
import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws InterruptedException {

/*        Board bd = new Board();
        int a = 2;
        bd.update();
        bd.printBoard();*/

        Game game = new Game(1,true,false,false);
        game.play(true,false);

/*        int[] tab = new int[]{
                1,2,3,4,5
        };

        int[] tab2 = new int[]{
                1,2,3,4,5
        };

        System.out.println("is it equals ? : " + Arrays.equals(tab, tab2));
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("test.ser"))) {
            oos.writeObject(tab2);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

/*        int[] tab2 = new int[1];
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("test.ser"))) {
            tab2 = (int[]) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        for (int i : tab2)
            System.out.print(i + " ");*/

/*        int b = 0b01111111_11111111_11111111_11111111;
        System.out.println(b);
        BigInteger a = new BigInteger(String.valueOf(b));
        System.out.println((a.bitCount()));*/


        //BigInteger test = new BigInteger("12");
/*        String test_list = "111000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
        BigInteger test = new BigInteger(test_list,2);
        System.out.println(test.toString(10));
        test = changeBit(test,3,false);
        System.out.println(test.toString(2));*/
/*        System.out.println(test);
        test = test.setBit(0);
        test = test.setBit(1);
        test = test.setBit(1);
        test = test.setBit(8);
        System.out.println(test);
        for (int i = 0; i<10;i++)
            System.out.print(" " +getXthBit(test,i));
        test = changeBit(test,0,false);
        System.out.println("");
        for (int i = 0; i<10;i++)
            System.out.print(" " +getXthBit(test,i));*/

/*
        ArrayList<ArrayList<Object>> list = new ArrayList<>();
        list.add(new ArrayList<>());
        ArrayList<Object> sublist = list.get(0);
        sublist.add(1);
        sublist.add(1);
        sublist.add(1);
        sublist.add(1, 2);

        System.out.println("list = ");
        for (int i = 0; i < 4; i++) {
            System.out.print(list.get(0).get(i));

        }*/
    }

/*    public static boolean getXthBit(BigInteger integer, int index){

        BigInteger mask = new BigInteger(String.valueOf(1<<index));
        BigInteger resultOperation = integer.and(mask);

        return (resultOperation.equals(mask));
    }*/
public static boolean getXthBit(BigInteger integer, int index){ //VERIFIED

    BigInteger mask = new BigInteger(String.valueOf(1<<index));
    BigInteger resultOperation = integer.and(mask);
    boolean returnVar = resultOperation.equals(mask);
    return returnVar;
}
    public static BigInteger changeBit(BigInteger integer,int index,boolean value){ //VERIFIED
        integer = value ? integer.setBit(index) : integer.clearBit(index);
        return integer;
    }

}


