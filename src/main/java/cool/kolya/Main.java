package cool.kolya;

import cool.kolya.convert.InputConverter;
import cool.kolya.convert.OutputConverter;
import cool.kolya.graph.model.GraphModels;
import cool.kolya.train.BackpropagationAlgorithm;
import cool.kolya.train.BatchedTrainProcessor;
import cool.kolya.train.Data;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;


//TODO matrices implementation and GPU calculation
public class Main {

    public static void main(String[] args) {
        //init
        NeuralNetwork<BufferedImage, Integer> neuralNetwork = initNeuralNetwork();

        //train
        Data<BufferedImage>[] dataset = loadDataset();
        BackpropagationAlgorithm<BufferedImage, Integer> learningAlgorithm = new BackpropagationAlgorithm<>(0.001);
        BatchedTrainProcessor<BufferedImage, Integer> trainProcessor = new BatchedTrainProcessor<>(3000,
                100, learningAlgorithm);
        trainProcessor.train(neuralNetwork, dataset);

        //start app
        TestApp app = new TestApp(neuralNetwork);
        new Thread(app).start();
    }

    public static NeuralNetwork<BufferedImage, Integer> initNeuralNetwork() {
        int imageSideLength = 28;
        int pixelsAmount = imageSideLength * imageSideLength;

        InputConverter<BufferedImage> imageToScale = (image) -> {
            int[] rgb = image.getRGB(0, 0, imageSideLength, imageSideLength, null, 0, imageSideLength);
            return Arrays.stream(rgb).mapToDouble(it -> (it & 0xff) / 255.0).toArray();
        };

        OutputConverter<Integer> activationsToDigit = (activations) -> {
            int largerIndex = 0;
            for (int i = 1; i < activations.length; i++) {
                if (activations[i] > activations[largerIndex]) largerIndex = i;
            }
            return largerIndex;
        };

        return new NeuralNetwork<>(GraphModels.perceptronLayers(pixelsAmount, 512, 256, 128, 32, 10),
                imageToScale, activationsToDigit);
    }

    public static Data<BufferedImage>[] loadDataset() {
        File[] imagesFiles = new File("train").listFiles();
        assert imagesFiles != null;
        int imagesAmount = imagesFiles.length;
        //noinspection unchecked
        Data<BufferedImage>[] dataset = new Data[imagesAmount];
        for (int i = 0; i < imagesAmount; i++) {
            File file = imagesFiles[i];

            BufferedImage image;
            try {
                image = ImageIO.read(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            int correspondedNeuronIndex = Integer.parseInt(String.valueOf(file.getName().charAt(10)));

            dataset[i] = new Data<>(correspondedNeuronIndex, image);
        }
        return dataset;
    }
}
