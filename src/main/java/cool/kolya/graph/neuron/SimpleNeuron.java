package cool.kolya.graph.neuron;

import cool.kolya.graph.layer.Layer;
import cool.kolya.util.MathUtil;

public class SimpleNeuron implements Neuron {


    private final Layer layer;
    private final int index;
    private final double[] connections;
    private double bias;
//    private double activation = 0;

    private boolean isActivated = false;

    public SimpleNeuron(Layer layer, int index, int connectionsAmount) {
        this.layer = layer;
        this.index = index;
        this.bias = MathUtil.random();

        connections = new double[connectionsAmount];
        for (int i = 0; i < connectionsAmount; i++) {
            connections[i] = MathUtil.random();
        }

        layer.neurons()[index] = this;
    }

    @Override
    public void fireActivationInto(double activation) {
//        this.activation += activation
        layer.activations()[index] += activation;
    }

    @Override
    public void clearActivation() {
//        activation = 0;
        layer.activations()[index] = 0;
        isActivated = false;
    }

    @Override
    public double activation() {
//        return activation;
        return layer.activations()[index];
    }

    @Override
    public void activate() {
        if (isActivated) {
            throw new IllegalStateException("Activation func has already been applied");
        }
//        activation = MathUtil.SIGMOID_FUNC.apply(activation);
        layer.activations()[index] = MathUtil.SIGMOID_FUNC.apply(layer.activations()[index]);
        isActivated = true;
    }

    @Override
    public double bias() {
        return bias;
    }

    @Override
    public double connectionWeight(int connectionIndex) {
        return connections[connectionIndex];
    }

    @Override
    public void correctBias(double gradient) {
        bias += gradient;
    }

    @Override
    public void correctWeight(int connectionIndex, double delta) {
        connections[connectionIndex] += delta;
    }
}
