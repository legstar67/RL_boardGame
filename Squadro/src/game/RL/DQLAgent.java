package game.RL;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.io.IOException;
import java.util.Random;

public class DQLAgent {


    // Hyperparameters
    final double learningRate;//0.01
    final double discountFactor ;//0.99
    double explorationRate;//1.0
    final double explorationDecay;//0.995
    final double minExplorationRate;//0.01
    final int nbLayer;//2
    final int[] nbNeuronsPerLayer;//{24, 24}

    // Environment variables
    public static final int STATE_SIZE = 40; // Number of bits in state representation
    private static final int ACTION_SIZE = 5; // Number of possible actions

    // Random number generator
    private static final Random random = new Random(123);

    // Q-network
    private MultiLayerNetwork model;

    public DQLAgent(
            double learningRate,
            double discountFactor,
            double initialExplorationRate,
            double explorationDecay,
            double minExplorationRate,
            int nbLayer,
            int[] nbNeuronsPerLayer) {
        this.learningRate = learningRate;
        this.explorationRate = initialExplorationRate;
        this.discountFactor = discountFactor;
        this.explorationDecay = explorationDecay;
        this.minExplorationRate = minExplorationRate;
        this.nbLayer = nbLayer;
        this.nbNeuronsPerLayer = nbNeuronsPerLayer;
        model = createModel();



    }
    public DQLAgent(
            String modelPath,
            double learningRate,
            double discountFactor,
            double initialExplorationRate,
            double explorationDecay,
            double minExplorationRate,
            int nbLayer,
            int[] nbNeuronsPerLayer
    ) throws IOException {
        model = ModelSerializer.restoreMultiLayerNetwork(modelPath);

        this.learningRate = learningRate;
        this.explorationRate = initialExplorationRate;
        this.discountFactor = discountFactor;
        this.explorationDecay = explorationDecay;
        this.minExplorationRate = minExplorationRate;
        this.nbLayer = nbLayer;
        this.nbNeuronsPerLayer = nbNeuronsPerLayer;
    }

    private MultiLayerNetwork createModel() {
        NeuralNetConfiguration. ListBuilder configBuilder = new NeuralNetConfiguration.Builder()
                .seed(123)
                .weightInit(WeightInit.XAVIER)
                .updater(new Nesterovs(learningRate, 0.9))
                .list()
                .layer(new DenseLayer.Builder().nIn(STATE_SIZE).nOut(nbNeuronsPerLayer[0])
                        .activation(Activation.RELU)
                        .build());
                for (int i = 0; i < nbLayer-1; i++) {
                        configBuilder = configBuilder.layer(new DenseLayer.Builder().nIn(nbNeuronsPerLayer[i]).nOut(nbNeuronsPerLayer[i+1])
                            .activation(Activation.RELU)
                            .build());
                }
                MultiLayerConfiguration config = configBuilder.layer(new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                        .activation(Activation.IDENTITY)
                        .nIn(nbNeuronsPerLayer[nbNeuronsPerLayer.length-1]).nOut(ACTION_SIZE).build())
                .build();

        MultiLayerNetwork model = new MultiLayerNetwork(config);
        model.init();
        return model;
    }

    public int chooseAction(long state) {
        if (random.nextDouble() < explorationRate) {
            return random.nextInt(ACTION_SIZE);
        } else {
            INDArray stateVector = Nd4j.create(1, STATE_SIZE);
            for (int i = 0; i < STATE_SIZE; i++) {
                stateVector.putScalar(i, (state >> i) & 1);
            }
            INDArray qValues = model.output(stateVector);
            return Nd4j.argMax(qValues, 1).getInt(0);
        }
    }

    public void train(long state, int action, double reward, long nextState, boolean done) {
        INDArray stateVector = Nd4j.create(1, STATE_SIZE);
        for (int i = 0; i < STATE_SIZE; i++) {
            stateVector.putScalar(i, (state >> i) & 1);
        }

        INDArray nextStateVector = Nd4j.create(1, STATE_SIZE);
        for (int i = 0; i < STATE_SIZE; i++) {
            nextStateVector.putScalar(i, (nextState >> i) & 1);
        }

        INDArray target = model.output(stateVector).dup();
        INDArray nextQValues = model.output(nextStateVector);

        double maxNextQ = Nd4j.max(nextQValues).getDouble(0);
        double updatedQ = reward + (done ? 0 : discountFactor * maxNextQ);

        target.putScalar(action, updatedQ);

        model.fit(new DataSet(stateVector, target));

        if (explorationRate > minExplorationRate) {
            explorationRate *= explorationDecay;
        }
    }
    public MultiLayerNetwork getModel() {
        return model;
    }
    public double getLearningRate() {
        return learningRate;
    }

    public double getDiscountFactor() {
        return discountFactor;
    }

    public double getExplorationRate() {
        return explorationRate;
    }

    public double getExplorationDecay() {
        return explorationDecay;
    }

    public double getMinExplorationRate() {
        return minExplorationRate;
    }

    public int getNbLayer() {
        return nbLayer;
    }

    public int[] getNbNeuronsPerLayer() {
        return nbNeuronsPerLayer;
    }

}
