package lessons.formatter;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class IntegerFormatter implements Formatter<Integer> {

    @Override
    public Integer parse(String text, Locale locale) throws ParseException {
        return Integer.valueOf(text.split(" ")[1]);
    }

    @Override
    public String print(Integer integer, Locale locale) {
        return "Integer: " + integer;
    }
}
