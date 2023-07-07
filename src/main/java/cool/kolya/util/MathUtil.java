package cool.kolya.util;

import java.util.function.UnaryOperator;

public class MathUtil {

    public static UnaryOperator<Double> SIGMOID_FUNC = x -> 1 / (1 + Math.exp(-x));
    public static UnaryOperator<Double> DSIGMOID_FUNC = y -> y * (1 - y);

    public static double random() {
        return Math.random() * 2.0 - 1.0;
    }
}
