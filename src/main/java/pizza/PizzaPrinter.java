package pizza;

import core.IPrinter;

import java.util.Collection;
import java.util.Collections;

public class PizzaPrinter implements IPrinter {

    @Override
    public String convertHeader(Object obj) {
        Collection<Integer> pizzas = (Collection<Integer>)obj;
        return String.valueOf(pizzas.size());
    }

    @Override
    public Iterable<String> convertBody(Object obj) {
        Collection<Integer> pizzas = (Collection<Integer>)obj;
        StringBuilder sb = new StringBuilder();
        pizzas.forEach(integer -> {
            sb.append(integer);
            sb.append(" ");
        });
        return Collections.singleton(sb.toString());
    }

    @Override
    public boolean hasHeader() {
        return true;
    }
}
