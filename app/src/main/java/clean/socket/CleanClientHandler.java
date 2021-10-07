package clean.socket;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;


class CleanClientHandler extends Thread {
    private Socket clientSocket;
    private BufferedReader in;
    private OutputStream out;
    private HashMap<String, RequestHandler> router;
    private final ConcurrentHashMap<String, SessionData> sessionsData;

    public CleanClientHandler(Socket socket, HashMap<String, RequestHandler> router, ConcurrentHashMap<String, SessionData> sessionsData) throws IOException {
        clientSocket = socket;
        this.router = router;
        this.sessionsData = sessionsData;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new DataOutputStream(clientSocket.getOutputStream());
    }

    public void run() {
        try {
            CleanHttpRequest request = new CleanHttpRequest(in, sessionsData);
            var route = request.getPath();
            if (!router.containsKey(route))
                route = "*";
            router.get(route).handle(request, out);
            out.flush();
            in.close();
            out.close();
            clientSocket.close();
        } catch (Exception e) {
            System.out.println("Error in run");
            e.printStackTrace();
        }
    }

}
