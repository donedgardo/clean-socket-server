package clean.socket;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class CleanClient {

    public Map<String, String> getHttp(String path) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://127.0.0.1:3000" + path))
                .build();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        Map<String, String> responseWithHeader = new HashMap<String, String>();
        responseWithHeader.put("headers" , response.headers().map().toString());
        responseWithHeader.put("body" , response.body());
        return responseWithHeader;
    }

}
