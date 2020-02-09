package core;

public interface IPrinter {
    String convertHeader(Object obj);
    Iterable<String> convertBody(Object obj);
    boolean hasHeader();
}
