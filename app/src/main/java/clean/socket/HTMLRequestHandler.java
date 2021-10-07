package clean.socket;

import java.io.OutputStream;

public class HTMLRequestHandler extends RequestHandler {
    private String htmlContent;

    public HTMLRequestHandler(String htmlContent) {
        this.htmlContent = htmlContent;
    }


    public void handle(CleanHttpRequest request, OutputStream out) throws Exception {
        String responseHtml = "<html><body><p>" + htmlContent + "</p></body></html>";
        String responseLength = "Content-Length: " + responseHtml.length() + "\r\n";
        String rawResponse = responseStatusHeader + responseTypeHeader +
                responseLength + responseConnection + responseHtml;
        out.write(rawResponse.getBytes());
    }
}
