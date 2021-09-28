package clean.socket;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CleanServerTest {

    private CleanClient client;

    @Before
    public void setup(){
        client = new CleanClient();
        client.startConnection("127.0.0.1", 3000);
    }

    @After
    public void tearDown(){
        client.stopConnection();
    }

    @Test
    public void welcomeScreen() {
        String request = "GET /hello HTTP/1.1 \r\n" +
                "Host: localhost:3000 \r\n" +
                "Accept-Encoding: gzip, deflate \r\n" +
                "Accept-Language: en-US,en;q=0.9 \r\n";
        String response = client.sendMessage(request);
        String expectedResponse = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: 50\r\n" +
                "Connection: keep-alive\r\n\r\n" +
                "<html><body><p>Welcome Screen!</p></body></html>\r\n";
        assertEquals(expectedResponse, response);
    }

    @Test
    public void handlesMultipleConnections() {
        CleanClient client2 = new CleanClient();
        client2.startConnection("127.0.0.1", 3000);
        String request = "GET / HTTP/1.1 \r\n" +
                "Host: localhost:3000 \r\n" +
                "Accept-Encoding: gzip, deflate \r\n" +
                "Accept-Language: en-US,en;q=0.9 \r\n";
        String response = client2.sendMessage(request);
        assertNotNull(response);
        client2.stopConnection();
    }
}
