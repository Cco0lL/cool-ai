package cool.kolya.train;

import cool.kolya.NeuralNetwork;
import cool.kolya.graph.Graph;

public class BatchedTrainProcessor<I, O> {

    private final int epochs;
    private final int batchSizeInOneEpoch;
    private final LearningAlgorithm<I, O> learningAlgorithm;

    public BatchedTrainProcessor(int epochs, int batchSizeInOneEpoch, LearningAlgorithm<I, O> learningAlgorithm) {
        this.epochs = epochs;
        this.batchSizeInOneEpoch = batchSizeInOneEpoch;
        this.learningAlgorithm = learningAlgorithm;
    }

    public void train(NeuralNetwork<I, O> nn, Data<I>[] dataset) {
        for (int i = 1; i < epochs; i++) {
            //debug values start
            int right = 0;
            double errorSum = 0.0;
            //debug values end
            for (int j = 0; j < batchSizeInOneEpoch; j++) {
                int randomIndex = (int) (Math.random() * dataset.length);
                Data<I> data = dataset[randomIndex];
                nn.push(data.value());

                //debug calculations start
                Graph graph = nn.getGraph();

                int rightAnswer = data.correspondingNeronIndex();
                int result = graph.selectedNeuronForResponse();
                if (rightAnswer == result) {
                    right++;
                }

                double[] outputActivations = graph.outputActivations();
                for (int k = 0; k < outputActivations.length; k++) {
                    int target = k == result ? 1 : 0;
                    errorSum += (target - outputActivations[k]) * (target - outputActivations[k]);
                }
                //debug calculations end
                learningAlgorithm.learn(nn, data.correspondingNeronIndex());
            }
            System.out.println(i + " epoch, right: " + right + ", errors sum: " + errorSum);
        }
    }
}