package clean.socket;

import java.io.IOException;
import java.io.OutputStream;

public class HTMLRequestHandler extends RequestHandler {
    private String htmlContent;

    public HTMLRequestHandler(OutputStream out, String htmlContent) {
        this.htmlContent = htmlContent;
        this.out = out;
    }

    public void handle() throws IOException {
        String responseHtml = "<html><body><p>" + htmlContent + "</p></body></html>";
        String responseLength = "Content-Length: " + responseHtml.length() + "\r\n";
        String rawResponse = responseStatusHeader + responseTypeHeader +
                responseLength + responseConnection + responseHtml;
        out.write(rawResponse.getBytes());
    }
}
