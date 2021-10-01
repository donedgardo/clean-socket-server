package clean.socket;

import org.junit.*;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.*;

public class CleanServerTest {

    private static CleanClient client;

    @BeforeClass
    public static void setup() {
        client = new CleanClient();
    }

    @Test
    public void welcomeScreen() {
        Map<String, String> response = null;
        try {
            response = client.getHttp("/hello");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals("{connection=[keep-alive], content-length=[48], content-type=[text/html]}",
                response.get("headers"));
        assertEquals("<html><body><p>Welcome Screen!</p></body></html>",
                response.get("body"));
    }

    @Test
    public void rendersIndexHtml() throws IOException, InterruptedException {
        Map<String, String> response = client.getHttp("/");
        assertEquals("{connection=[keep-alive], content-length=[44], content-type=[text/html]}", response.get("headers"));
        assertEquals("<html><body><p>index.html!</p></body></html>", response.get("body"));
    }

    @Test
    public void rendersDirectoryIndex() throws IOException, InterruptedException {
        Map<String, String> response = client.getHttp("/dir");
        assertEquals("{connection=[keep-alive], content-length=[49], content-type=[text/html]}", response.get("headers"));
        assertEquals("<html><body><p>Html inside dir!</p></body></html>", response.get("body"));
    }

    @Test
    public void rendersDirectoryList() throws IOException, InterruptedException {
        Map<String, String> response = client.getHttp("/list");
        assertEquals("{connection=[keep-alive], content-length=[102], content-type=[text/html]}",
                response.get("headers"));
        assertEquals("<html><body><p>test.gif</p><p>test.jpeg</p><p>test.png</p><p>test.pdf</p><p>test.txt</p></body></html>",
                response.get("body"));
    }

    @Test
    public void rendersPing() throws IOException, InterruptedException {
        Map<String, String> response = client.getHttp("/ping");
        assertEquals("{connection=[keep-alive], content-length=[49], content-type=[text/html]}",
                response.get("headers"));
    }

    @Test
    public void rendersPngFile() throws IOException, InterruptedException {
        Map<String, String> response = client.getHttp("/list/test.png");
        assertEquals("{connection=[keep-alive], content-length=[1772], content-type=[image/png]}",
                response.get("headers"));
        assertNotNull(response.get("body"));
    }

    @Test
    public void rendersJpegFile() throws IOException, InterruptedException {
        Map<String, String> response = client.getHttp("/list/test.jpeg");
        assertEquals("{connection=[keep-alive], content-length=[12302], content-type=[image/jpeg]}",
                response.get("headers"));
        assertNotNull(response.get("body"));
    }

    @Test
    public void rendersGifFile() throws IOException, InterruptedException {
        Map<String, String> response = client.getHttp("/list/test.gif");
        assertEquals("{connection=[keep-alive], content-length=[151178], content-type=[image/gif]}",
                response.get("headers"));
        assertNotNull(response.get("body"));
    }

    @Test
    public void rendersPDFFile() throws IOException, InterruptedException {
        Map<String, String> response = client.getHttp("/list/test.pdf");
        assertEquals("{connection=[keep-alive], content-length=[3017303], content-type=[application/pdf]}",
                response.get("headers"));
        assertNotNull(response.get("body"));
    }

}
