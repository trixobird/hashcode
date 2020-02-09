package core;

public interface IParser {
    Object parseHeader(String[] strings);
    Object parseBody(String[] strings);
    boolean hasHeader();
}
