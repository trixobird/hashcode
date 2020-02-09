package core;

import lombok.NonNull;
import org.aeonbits.owner.ConfigFactory;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
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

    private static Logger logger = LoggerFactory.getLogger(AllSteps.class);
    private final String pkgName;
    private final IParser parser;
    private final IPrinter printer;
    private final IProcessor processor;
    private final CoreConfig cfg;

    public AllSteps(String pkgName, IParser parser, IProcessor processor, IPrinter printer) {
        this.parser = parser;
        this.processor = processor;
        this.printer = printer;
        this.pkgName = pkgName;
        cfg = initProperties(pkgName);
    }

    public void calculate() {
        findInputFiles(pkgName, cfg.inputSuffix()).forEach(this::processOneFile);
    }

    public void calculate(String filename) {
        processOneFile(getFileOrDirFromResources(pkgName + '/' + filename));
    }

    private void processOneFile(Path inputFile) {
        Object input = parse(inputFile);
        Object processedObj = processor.process(input);
        String outputFileName = inputFile.toString()
                                         .substring(getResources().toString().length())
                                         .replace(cfg.inputSuffix(), cfg.outputSuffix());
        createOutput(outputFileName, processedObj);
    }

    private List<Path> findInputFiles(String directory, String extension) {

        Path resourcesPath = getResources();

        try (Stream<Path> walk = Files.walk(resourcesPath)) {

            Path dir = walk.filter(p -> p.toFile().isDirectory())
                    .filter(x -> x.getFileName().toString().equals(directory))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException(MessageFormat.format("The directory: {} was not found", directory)));

            try (Stream<Path> walkFiles = Files.walk(dir)) {
                return walkFiles.filter(p -> p.toFile().isFile())
                                .filter(p -> FilenameUtils.getExtension(p.getFileName().toString()).equals(extension))
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
                ret = parser.parseHeader(it.next().split(cfg.token()));
            }

            while (it.hasNext()) {
                ret = parser.parseBody(it.next().split(cfg.token()));
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

    private Path getPropertiesPath(String pkgName) {
        Path fileOrDirFromResources;
        try {
            fileOrDirFromResources = getFileOrDirFromResources(pkgName + '/' + pkgName + ".properties");
        } catch (Exception e) {
            fileOrDirFromResources = findInputFiles(pkgName, "properties").get(0);
        }
        return fileOrDirFromResources;
    }

    private CoreConfig initProperties(String pkgName) {
        CoreConfig config;
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream(getPropertiesPath(pkgName).toString()));
            config = ConfigFactory.create(CoreConfig.class, prop);
        } catch (Exception e) {
            config = ConfigFactory.create(CoreConfig.class);
        }
        return config;
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
