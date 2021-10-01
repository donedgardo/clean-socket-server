package clean.socket;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class PingRequestHandler extends RequestHandler {

    public PingRequestHandler(OutputStream out) {
        this.out = out;
    }

    public void handle() throws IOException, InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String responseHtml = "<html><body><p>Time: " + dtf.format(now) + "</p></body></html>\r\n";
        String responseLength = "Content-Length: " + responseHtml.length() + "\r\n";
        String rawResponse = responseStatusHeader + responseTypeHeader +
                responseLength + responseConnection + responseHtml;
        out.write(rawResponse.getBytes());
    }
}
