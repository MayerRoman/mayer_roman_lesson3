package converter;

/**
 * Created by Mayer Roman on 18.05.2016.
 */
public interface Converter<IN, OUT> {
    OUT apply(IN inputElement);
}
