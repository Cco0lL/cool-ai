package cool.kolya.graph.model;

import cool.kolya.graph.*;
import cool.kolya.graph.layer.Layer;
import cool.kolya.graph.layer.SimpleLayer;
import cool.kolya.graph.neuron.SimpleNeuron;

public class PerceptronLayers implements GraphModel {

    private final int[] lengths;

    public PerceptronLayers(int... lengths) {
        this.lengths = lengths;
    }

    @Override
    public Graph createGraph() {
        int layersLength = lengths.length;
        Layer[] layers = createLayers(layersLength);
        int lastIndex = layersLength - 1;
        layers[lastIndex] = createOutputLayer(lastIndex);
//        layers[layersLength - 1] = new SimpleLayer(createNeurons(lengths[layersLength - 1], 0));
        return new SimpleGraph(layers);
    }

    private Layer[] createLayers(int layersLength) {
        Layer[] layers = new Layer[layersLength];
        for (int i = 0; i < layersLength - 1; i++) {
            int layerLength = lengths[i];
            Layer layer = new SimpleLayer(layerLength);
            createNeurons(layer, layerLength, lengths[i + 1]);
            layers[i] = layer;
        }
        return layers;
    }

    private Layer createOutputLayer(int lastIndex) {
        Layer outputLayer = new SimpleLayer(lengths[lastIndex]);
        createNeurons(outputLayer, lengths[lastIndex], 0);
        return outputLayer;
    }

    private void createNeurons(Layer layer, int currentLength, int nextLength) {
//        Neuron[] neurons = new Neuron[currentLength];
        for (int j = 0; j < currentLength; j++) {
            //yes it's not a good solution, however neuron's constructor adds himself in the layer
            new SimpleNeuron(layer, j, nextLength);
//            Neuron neuron = new SimpleNeuron(nextLength);
//            neurons[j] = neuron;
        }
//        return neurons;
    }
}
