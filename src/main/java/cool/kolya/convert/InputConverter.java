package cool.kolya.convert;

@FunctionalInterface
public interface InputConverter<T> {

    double[] convert(T t);
}
