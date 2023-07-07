package cool.kolya.graph.neuron;

public interface Neuron {

    void fireActivationInto(double activation);

    void clearActivation();

    double activation();

    void activate();

    double bias();

    double connectionWeight(int connectionIndex);

    void correctBias(double gradient);

    void correctWeight(int connectionIndex , double delta);
}
