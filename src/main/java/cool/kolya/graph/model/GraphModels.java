package cool.kolya.graph.model;

public class GraphModels {

    public static GraphModel perceptronLayers(int ... layersSize) {
        return new PerceptronLayers(layersSize);
    }
}
