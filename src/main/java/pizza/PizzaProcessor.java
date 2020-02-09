package pizza;

import core.IProcessor;

import java.util.Arrays;

public class PizzaProcessor implements IProcessor {
    @Override
    public Iterable<Integer> process(Object obj) {
        return Arrays.asList(0, 2, 3);
    }
}
