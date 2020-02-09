package pizza;

import core.OutputPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import core.InputReader;
import pizza.model.Pizza;

import java.net.URISyntaxException;
import java.util.Collection;

public class Main {

    public static void main(String[] args) throws URISyntaxException {
        Logger logger = LoggerFactory.getLogger(Main.class);
        logger.info("Hello World");
        Pizza parse = (Pizza) new InputReader(new PizzaParser()).parse("pizza/a_example.in");
        Iterable<Integer> process = new PizzaProcessor().process(parse);
        new OutputPrinter(new PizzaPrinter((Collection<Integer>) process)).createOutput("pizza/a_example.out");
    }
}
