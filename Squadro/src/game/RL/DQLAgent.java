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
    private static final double LEARNING_RATE = 0.01;//0.01
    private static final double DISCOUNT_FACTOR = 0.99;//0.99
    private static double EXPLORATION_RATE = 1.0;//1.0
    private static final double EXPLORATION_DECAY = 0.995;//0.995
    private static final double MIN_EXPLORATION_RATE = 0.01;//0.01

    // Environment variables
    static final int STATE_SIZE = 40; // Number of bits in state representation
    private static final int ACTION_SIZE = 5; // Number of possible actions

    // Random number generator
    private static final Random random = new Random();

    // Q-network
    private MultiLayerNetwork model;

    public DQLAgent() {
        model = createModel();
    }
    public DQLAgent(String modelPath) throws IOException {
        model = ModelSerializer.restoreMultiLayerNetwork(modelPath);
    }

    private MultiLayerNetwork createModel() {
        MultiLayerConfiguration config = new NeuralNetConfiguration.Builder()
                .seed(123)
                .weightInit(WeightInit.XAVIER)
                .updater(new Nesterovs(LEARNING_RATE, 0.9))
                .list()
                .layer(new DenseLayer.Builder().nIn(STATE_SIZE).nOut(24)
                        .activation(Activation.RELU)
                        .build())
                .layer(new DenseLayer.Builder().nIn(24).nOut(24)
                        .activation(Activation.RELU)
                        .build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                        .activation(Activation.IDENTITY)
                        .nIn(24).nOut(ACTION_SIZE).build())
                .build();

        MultiLayerNetwork model = new MultiLayerNetwork(config);
        model.init();
        return model;
    }

    public int chooseAction(long state) {
        if (random.nextDouble() < EXPLORATION_RATE) {
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
        double updatedQ = reward + (done ? 0 : DISCOUNT_FACTOR * maxNextQ);

        target.putScalar(action, updatedQ);

        model.fit(new DataSet(stateVector, target));

        if (EXPLORATION_RATE > MIN_EXPLORATION_RATE) {
            EXPLORATION_RATE *= EXPLORATION_DECAY;
        }
    }
    public MultiLayerNetwork getModel() {
        return model;
    }
}
