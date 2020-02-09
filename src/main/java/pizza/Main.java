package pizza;

import core.AllSteps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Main.class);
        logger.info("Hello World");
        new AllSteps(new PizzaParser(), new PizzaProcessor(), new PizzaPrinter()).calculate("pizza");
    }
}
