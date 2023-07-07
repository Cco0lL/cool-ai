package cool.kolya.convert;

@FunctionalInterface
public interface OutputConverter<T> {

    T convert(double[] outputVec);

}
