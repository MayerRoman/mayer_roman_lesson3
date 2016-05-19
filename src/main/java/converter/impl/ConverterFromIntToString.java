package converter.impl;

import converter.Converter;

/**
 * Created by Mayer Roman on 17.05.2016.
 */
public class ConverterFromIntToString implements Converter<Integer, String> {

    @Override
    public String apply(Integer inputElement) {
        String convertedElement = "#" + inputElement.toString();
        return convertedElement;
    }
}
