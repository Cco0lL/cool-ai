package cool.kolya.graph.layer;

import cool.kolya.graph.neuron.Neuron;

import java.util.Arrays;

public class SimpleLayer implements Layer {

    private final Neuron[] neurons;
    private final double[] activations;

    public SimpleLayer(int length) {
//        this.neurons = neurons;
        neurons = new Neuron[length];
        activations = new double[length];
    }

    @Override
    public Neuron[] neurons() {
        return neurons;
    }

    @Override
    public double[] activations() {
//        return Arrays.stream(neurons).mapToDouble(Neuron::activation).toArray();
        return activations;
    }

    //TODO System.arrayCopy(values, 0, activations, 0, length)
    @Override
    public void setActivations(double[] values) {
        if (values.length != neurons.length) {
            throw new IllegalArgumentException("length of charges should be equal length of neurons");
        }
        for (int i = 0; i < values.length; i++) {
            Neuron neuron = neurons[i];
            neuron.clearActivation();
            neurons[i].fireActivationInto(values[i]);
        }
    }

    @Override
    public double weightedSumOfActivationsForConnection(int connectionIndex) {
        return Arrays.stream(neurons)
                .mapToDouble(it -> it.activation() * it.connectionWeight(connectionIndex))
                .sum();
    }

    @Override
    public void fireActivationsFromPreviousLayer(Layer prevLayer) {
        for (int i = 0; i < neurons.length; i++) {
            Neuron neuron = neurons[i];
            neuron.clearActivation();
            neuron.fireActivationInto(prevLayer.weightedSumOfActivationsForConnection(i) + neuron.bias());
            neuron.activate();
        }
    }
}
