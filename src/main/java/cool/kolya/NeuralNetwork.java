package cool.kolya;

import cool.kolya.convert.InputConverter;
import cool.kolya.convert.OutputConverter;
import cool.kolya.graph.Graph;
import cool.kolya.graph.model.GraphModel;

public class NeuralNetwork<I, O> {

    private final Graph graph;
    private final InputConverter<I> inputConverter;
    private final OutputConverter<O> outputConverter;

    public NeuralNetwork(GraphModel graphModel, InputConverter<I> inputConverter,
                         OutputConverter<O> outputConverter) {
        this.graph = graphModel.createGraph();
        this.inputConverter = inputConverter;
        this.outputConverter = outputConverter;
    }

    public O push(I input) {
        double[] inputVec = inputConverter.convert(input);
        double[] outputVec = graph.feedForward(inputVec);
        return outputConverter.convert(outputVec);
    }

    public Graph getGraph() {
        return graph;
    }

    public InputConverter<I> getInputConverter() {
        return inputConverter;
    }

    public OutputConverter<O> getOutputConverter() {
        return outputConverter;
    }
}
