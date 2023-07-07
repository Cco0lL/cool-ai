package cool.kolya.graph;

import cool.kolya.graph.layer.Layer;

public interface Graph {

    Layer[] layers();

    double[] feedForward(double... inputs);

    double[] outputActivations();

    int selectedNeuronForResponse();
}
