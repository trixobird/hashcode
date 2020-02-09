package pizza;

import core.IParser;
import pizza.model.Pizza;

import java.util.Arrays;
import java.util.stream.Collectors;

public class PizzaParser implements IParser {

    private Pizza pizza = new Pizza();
    @Override
    public Object parseHeader(String[] strings) {
        pizza.setMaxSlices(Integer.parseInt(strings[0]));
        return pizza;
    }

    @Override
    public Object parseBody(String[] strings) {
        pizza.setPizzasSlices(Arrays.stream(strings).map(Integer::parseInt).collect(Collectors.toList()));
        return pizza;
    }

    @Override
    public boolean hasHeader() {
        return true;
    }
}
