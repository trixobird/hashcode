import core.AllSteps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pizza.PizzaParser;
import pizza.PizzaPrinter;
import pizza.PizzaProcessor;

public class Main {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Main.class);
        logger.info("Hello World");
        new AllSteps("pizza",
                     new PizzaParser(),
                     new PizzaProcessor(),
                     new PizzaPrinter())
                .calculate();
    }
}
