package core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

public class InputReader {

    private static final String TOKEN = " ";
    private static Logger logger = LoggerFactory.getLogger(InputReader.class);
    private final IParser parser;
    public InputReader(IParser parser) {
        this.parser = parser;
    }

    public Object parse(String filename) {

        Path path = null;
        try {
            path = Paths.get(Objects.requireNonNull(this.getClass().getClassLoader().getResource(filename)).toURI());
        } catch (URISyntaxException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        Object ret = new Object();

        try (Stream<String> stream = Files.lines(Objects.requireNonNull(path))) {

            Iterator<String> it = stream.iterator();

            if (!it.hasNext()) {
                return ret;
            }

            if (parser.hasHeader()) {
                ret = parser.parseHeader(it.next().split(TOKEN));
            }

            while (it.hasNext()) {
                ret = parser.parseBody(it.next().split(TOKEN));
            }
            String strInput = ret.toString();
            logger.debug(strInput);
            return ret;

        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return ret;
    }
}
