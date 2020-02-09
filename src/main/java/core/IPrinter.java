package core;

public interface IPrinter {
    String convertHeader();
    Iterable<String> convertBody();
    boolean hasHeader();
}
