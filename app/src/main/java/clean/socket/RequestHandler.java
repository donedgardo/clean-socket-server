package clean.socket;


import java.io.OutputStream;

public abstract class RequestHandler {
    protected String responseStatusHeader = "HTTP/1.1 200 OK\r\n";
    protected String responseTypeHeader = "Content-Type: text/html\r\n";
    protected String responseConnection = "Connection: keep-alive\r\n\r\n";

    public void handle(CleanHttpRequest request, OutputStream out) throws Exception {}
}
