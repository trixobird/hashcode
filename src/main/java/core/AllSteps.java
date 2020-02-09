package core;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class AllSteps {

    private static final String TOKEN = " ";
    private static final String IN = ".in";
    private static final String OUT = ".out";
    private static Logger logger = LoggerFactory.getLogger(AllSteps.class);
    private final IParser parser;
    private final IPrinter printer;
    private final IProcessor processor;

    public AllSteps(IParser parser, IProcessor processor, IPrinter printer) {
        this.parser = parser;
        this.processor = processor;
        this.printer = printer;
    }

    public void calculate(String fileOrDirectory) {

        if (isFile(fileOrDirectory)) {
            processOneFile(getFileOrDirFromResources(fileOrDirectory));
        } else {
            findInputFiles(fileOrDirectory).forEach(this::processOneFile);
        }
    }

    private void processOneFile(Path inputFile) {
        Object input = parse(inputFile);
        Object processedObj = processor.process(input);
        String outputFileName = inputFile.toString()
                                         .substring(getResources().toString().length())
                                         .replace(IN, OUT);
        createOutput(outputFileName, processedObj);
    }

    private boolean isFile(String fileOrDirectory) {
        return getFileOrDirFromResources(fileOrDirectory).toFile().isFile();
    }

    private List<Path> findInputFiles(String directory) {

        Path resourcesPath = getResources();

        try (Stream<Path> walk = Files.walk(resourcesPath)) {

            Path dir = walk.filter(p -> p.toFile().isDirectory())
                    .filter(x -> x.getFileName().toString().equals(directory))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException(MessageFormat.format("The directory: {} was not found", directory)));

            try (Stream<Path> walkFiles = Files.walk(dir)) {
                return walkFiles.filter(p -> p.toFile().isFile())
                                .filter(p -> p.getFileName().toString().contains(IN))
                                .collect(Collectors.toList());
            }


        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return Collections.emptyList();
    }

    private Object parse(Path path) {

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

    private void createOutput(String filename, Object processedObj) {

        Path path = Paths.get(getResources().toString(), filename);

        StringBuilder sb = new StringBuilder();

        if (printer.hasHeader()) {
            sb.append(printer.convertHeader(processedObj));
            sb.append("\n");
        }
        printer.convertBody(processedObj).forEach(l -> {
            sb.append(l);
            sb.append("\n");
        });

        try {
            Files.write(path, sb.toString().getBytes(), CREATE, TRUNCATE_EXISTING);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    private Path getResources() {
        return getFileOrDirFromResources("");
    }

    @NonNull
    private Path getFileOrDirFromResources(String s) {
        try {
            return Paths.get(Objects.requireNonNull(this.getClass().getClassLoader().getResource(s)).toURI());
        } catch (URISyntaxException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        throw new NoSuchElementException(MessageFormat.format("The directory: {} was not found", s));
    }
}
