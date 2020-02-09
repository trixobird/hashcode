package pizza;

import core.IPrinter;

import java.util.Collection;
import java.util.Collections;

public class PizzaPrinter implements IPrinter {

    private final Collection<Integer> pizzas;

    public PizzaPrinter(Collection<Integer> pizzas) {
        this.pizzas = pizzas;
    }

    @Override
    public String convertHeader() {
        return String.valueOf(pizzas.size());
    }

    @Override
    public Iterable<String> convertBody() {
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
