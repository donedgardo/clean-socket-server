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
        client.sendMessage("/bye");
        client.stopConnection();
    }

    @Test
    public void welcomeScreen() {
        String response = client.sendMessage("/hello");
        assertEquals("welcome screen", response);
    }

    @Test
    public void handlesMultipleConnections() {
        CleanClient client2 = new CleanClient();
        client2.startConnection("127.0.0.1", 3000);
        String response = client2.sendMessage("/hello");
        assertEquals("welcome screen", response);
    }
}
