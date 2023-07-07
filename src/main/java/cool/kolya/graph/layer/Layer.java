package cool.kolya.graph.layer;

import cool.kolya.graph.neuron.Neuron;

public interface Layer {

    Neuron[] neurons();

    double[] activations();

    void setActivations(double[] charges);

    double weightedSumOfActivationsForConnection(int connectionIndex);

    void fireActivationsFromPreviousLayer(Layer layer);
}
