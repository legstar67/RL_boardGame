package game.visual;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IA {
    ArrayList<ArrayList<Object>> qValues = new ArrayList<>();
    int sizeQValues = 0;
    int[] actions = new int[]{1,2,3,4,5};

    // pour éviter de faire un tableau en return et ainsi optimiser l'algo
    // je crée une variable endehors des fonctions
    int tempIndex = 0;
    int oldStateIndex;
    protected double epsilon;

    public IA(){
        qValues = new ArrayList<>();
        ArrayList<Object> sublist= new ArrayList<>();
        sublist.add(new BigInteger("0"));
        sublist.add(0.0);
        sublist.add(0.0);
        sublist.add(0.0);
        sublist.add(0.0);
        sublist.add(0.0);
        qValues.add(sublist);
        sizeQValues+=1;

        epsilon = 0.99999;
        // Chargement des Q-values par désérialisation

/*        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("qvalues.ser"))) {
            qValues = (ArrayList<ArrayList<Object>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }*/

    }



    public void save(){
        // Sauvegarde des Q-values par sérialisation
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("qvalues.ser"))) {
            oos.writeObject(qValues);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getNextAction(BigInteger integerBoard,int[] movesAvailable ,double epsilon){
        int pieceNb;
        if (Math.random() < epsilon){
            pieceNb = followPolicy(integerBoard, movesAvailable);
        }
        else{
            pieceNb = randomAction(movesAvailable);
        }

        oldStateIndex = search(integerBoard);
        return pieceNb;
    }//TODO

    public int randomAction(int[] movesAvailable){
        Random random = new Random();
        int pieceIndex;
        int max = movesAvailable.length;

        pieceIndex = random.nextInt(max)+ 1;
        return movesAvailable[pieceIndex-1];
    }


    public int search(BigInteger integer,int min,int max){ //TODO utilsier binary search
        max -= 1;
        int mid = (max - min) / 2 + min;
        if (max>0&&min <= max) {
            if (integer.equals((BigInteger) qValues.get(mid).get(0))) {
                return mid; // the index of the integer in the qvalues array
            } else if (integer.compareTo((BigInteger) qValues.get(mid).get(0)) == -1) { // integer is less than the number on the array
                return search(integer, min, mid - 1);
            } else {
                return search(integer, mid + 1, max);

            }
        }
        else { //integer not found
            return integer.compareTo((BigInteger) qValues.get(mid).get(0)) == -1? -min:-(min+1) ; //TODO look if "mid-1" works instead of "mid"
        }
    }
    public int search(BigInteger integer){ return search(integer,0,sizeQValues);}

    /**
     * function qui va renvoyer l'action en fonction de Qvalues
     * si l'état n'est pas encore implémenter dans Qvalues alors il le crée
     * en utilisant la variable tempIndex transversale entre les fonctions //TODO bien réfléchir à use tempIndex (< , > ou = )
     * @param integerBoard
     * @return best action by following the policy
     */
    public int followPolicy(BigInteger integerBoard, int[] movesAvailable){
        int nbPiece = 0;
        int index = search(integerBoard);
        if (index<0){//element is not yet in the list, so we will create
            ArrayList<Object> line = new ArrayList<>();
            line.add(integerBoard);

            for(int i = 0 ; i<5;i++){line.add(0.0);}
            qValues.add(-index,line);
            sizeQValues += 1;
            nbPiece = randomAction(movesAvailable);
        }
        else {
            int maxAction = movesAvailable[0]; //Question : maybe it's better if i save the line in a new array to not "get()" in qValues so many time
            for (int i:movesAvailable){ //Question : IDK if it's good but if many actions are equals it takes always the lowest indexAction (ex at the beg
                if((double)(qValues.get(index).get(maxAction)) < (double)(qValues.get(index).get(i))){
                    maxAction = i;
                }
            }

        }

        return nbPiece;
    }


    public void updateQvalues( BigInteger previousState, int action, double reward, BigInteger nextState ){ //TODO IMPLEMENT THIS FUNCTION(copied from internet)
        double learningRate = 0.1;
        double discountFactor = 0.1;
        // next state's action estimations
        int previousStateIndex = search(previousState);
        int nextStateIndex = search(nextState);
        if (nextStateIndex < 0){
            ArrayList<Object> line = new ArrayList<>();
            line.add(nextState);

            for(int i = 0 ; i<5;i++){line.add(0.0);}
            qValues.add(-nextStateIndex,line);
            sizeQValues += 1;
            nextStateIndex *= -1;
        }
        if (previousStateIndex < 0){
            ArrayList<Object> line = new ArrayList<>();
            line.add(previousState);

            for(int i = 0 ; i<5;i++){line.add(0.0);}
            qValues.add(-previousStateIndex,line);
            sizeQValues += 1;
            previousStateIndex *= -1;
        }
        System.out.printf(""); //TODO a supp
        ArrayList<Object> nextActionEstimations = qValues.get(nextStateIndex);
        // find maximum expected summary reward from the next state
        double maxNextExpectedReward = (double)nextActionEstimations.get(1);

        for ( int i = 2; i < 5; i++ ){
            if ( (double)nextActionEstimations.get(i) > maxNextExpectedReward )
                maxNextExpectedReward = (double)nextActionEstimations.get(i);
        }

        // previous state's action estimations
        ArrayList<Object> previousActionEstimations = qValues.get(previousStateIndex);
        // update expexted summary reward of the previous state
        double previousEstimateAction = (double)previousActionEstimations.get(action);
        previousActionEstimations.set(action,previousEstimateAction + (learningRate * (reward + discountFactor * maxNextExpectedReward - previousEstimateAction)));
    }

}

