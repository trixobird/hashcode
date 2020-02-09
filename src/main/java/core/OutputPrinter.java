package core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.*;

public class OutputPrinter {

    private final IPrinter printer;
    private static Logger logger = LoggerFactory.getLogger(OutputPrinter.class);

    public OutputPrinter(IPrinter printer) {
        this.printer = printer;
    }

    public void createOutput(String filename) throws URISyntaxException {

        Path another = Paths.get(Paths.get(Objects.requireNonNull(this.getClass().getClassLoader().getResource("")).toURI())
                                      .toString(), filename);

        StringBuilder sb = new StringBuilder();

        if (printer.hasHeader()) {
            sb.append(printer.convertHeader());
            sb.append("\n");
        }
        printer.convertBody().forEach(l -> {
            sb.append(l);
            sb.append("\n");
        });

        try {
            Files.write(another, sb.toString().getBytes(), CREATE, TRUNCATE_EXISTING);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }
}
