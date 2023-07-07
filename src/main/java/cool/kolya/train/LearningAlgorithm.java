package cool.kolya.train;

import cool.kolya.NeuralNetwork;

public interface LearningAlgorithm<I, O> {

    void learn(NeuralNetwork<I, O> nn, int rightNeuronIndex);
}
