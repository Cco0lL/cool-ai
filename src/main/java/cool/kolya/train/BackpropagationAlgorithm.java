package cool.kolya.train;

import cool.kolya.NeuralNetwork;
import cool.kolya.graph.Graph;
import cool.kolya.graph.layer.Layer;
import cool.kolya.graph.neuron.Neuron;
import cool.kolya.util.MathUtil;

public class BackpropagationAlgorithm<I, O> implements LearningAlgorithm<I, O> {

    private final double learningRate;

    public BackpropagationAlgorithm(double learningRate) {
        this.learningRate = learningRate;
    }

    @Override
    public void learn(NeuralNetwork<I, O> nn, int rightNeuronIndex) {
        Graph graph = nn.getGraph();
        Layer[] layers = graph.layers();
        double[] errors = calculateErrors(graph.outputActivations(), rightNeuronIndex);
        for (int i = layers.length - 2; i >= 0; i--) {
            Layer layer = layers[i];
            Layer nextLayer = layers[i + 1];

            double[] activations = layer.activations();
            double[] nextActivations = nextLayer.activations();

            double[] gradients = calculateGradients(errors, nextActivations);
            double[][] deltas = calculateDeltas(gradients, activations, nextActivations);

            correctWeights(layer, deltas);
            correctBiases(nextLayer, gradients);

            double[] errorsNext = new double[activations.length];
            for (int j = 0; j < errorsNext.length; j++) {
                errorsNext[j] = 0;
                for (int k = 0; k < nextActivations.length; k++) {
                    errorsNext[j] += layer.neurons()[j].connectionWeight(k) * errors[k];
                }
            }
            errors = errorsNext;
        }
    }

    private double[] calculateErrors(double[] outputActivations, int rightNeuronIndex) {
        double[] errors = new double[outputActivations.length];
        for (int i = 0; i < errors.length; i++) {
            int target = i == rightNeuronIndex ? 1 : 0;
            errors[i] = target - outputActivations[i];
        }
        return errors;
    }

    private double[] calculateGradients(double[] errors, double[] nextActivations) {
        double[] gradients = new double[nextActivations.length];
        for (int i = 0; i < gradients.length; i++) {
            gradients[i] = errors[i] * MathUtil.DSIGMOID_FUNC.apply(nextActivations[i]);
            gradients[i] *= learningRate;
        }
        return gradients;
    }

    private double[][] calculateDeltas(double[] gradients, double[] activations, double[] nextActivations) {
        double[][] deltas = new double[activations.length][nextActivations.length];
        for (int i = 0; i < activations.length; i++) {
            for (int j = 0; j < nextActivations.length; j++) {
                deltas[i][j] = gradients[j] * activations[i];
            }
        }
        return deltas;
    }

    private void correctWeights(Layer layer, double[][] deltas) {
        for (int i = 0; i < deltas.length; i++) {
            Neuron neuron = layer.neurons()[i];
            for (int j = 0; j < deltas[0].length; j++) {
                neuron.correctWeight(j, deltas[i][j]);
            }
        }
    }

    private void correctBiases(Layer nextLayer, double[] gradients) {
        for (int i = 0; i < gradients.length; i++) {
            Neuron neuron = nextLayer.neurons()[i];
            neuron.correctBias(gradients[i]);
        }
    }
}
