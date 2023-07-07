package cool.kolya.graph;

import cool.kolya.graph.layer.Layer;

public record SimpleGraph(Layer[] layers) implements Graph {

    @Override
    public double[] feedForward(double... inputs) {
        layers[0].setActivations(inputs);
        for (int i = 1; i < layers.length; i++) {
            layers[i].fireActivationsFromPreviousLayer(layers[i - 1]);
        }
        return outputActivations();
    }

    @Override
    public double[] outputActivations() {
        return layers[layers.length - 1].activations();
    }

    @Override
    public int selectedNeuronForResponse() {
        double[] outputActivations = outputActivations();
        int selectedNeuron = 0;
        for (int i = 0; i < outputActivations.length; i++) {
            if (outputActivations[i] > outputActivations[selectedNeuron]) {
                selectedNeuron = i;
            }
        }
        return selectedNeuron;
    }
}
