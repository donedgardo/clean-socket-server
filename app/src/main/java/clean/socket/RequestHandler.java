package clean.socket;


import java.io.IOException;
import java.io.OutputStream;

public abstract class RequestHandler {
    protected String responseStatusHeader = "HTTP/1.1 200 OK\r\n";
    protected String responseTypeHeader = "Content-Type: text/html\r\n";
    protected String responseConnection = "Connection: keep-alive\r\n\r\n";
    protected String cookieHeader = "";
    protected String body = "";
    protected String responseLength = "";

    public void handle(CleanHttpRequest request, OutputStream out) throws Exception {}

    public void setStatusHeader(Number status) {
        this.responseStatusHeader = "HTTP/1.1 " + status + " OK \r\n";
    }

    public void addCookie(String cookie) {
        String newHeader = "Set-Cookie: " + cookie + "\r\n";
        this.cookieHeader += newHeader;
    }

    public void setBody(String body) {
        this.body = body;
        this.responseLength = "Content-Length: " + body.length() + "\r\n";
    };

    public void send(OutputStream out) throws IOException {
        String rawResponse = responseStatusHeader + responseTypeHeader +
                responseLength + cookieHeader + responseConnection + body;
        out.write(rawResponse.getBytes());
        this.cookieHeader = "";
    };
}
