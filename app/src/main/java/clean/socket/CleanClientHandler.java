package clean.socket;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;


class CleanClientHandler extends Thread {
    private final String publicDirectory;
    private final ConcurrentHashMap<String, SessionData> sessionData;
    private Socket clientSocket;
    private BufferedReader in;
    private OutputStream out;

    public CleanClientHandler(Socket socket, String publicDirectory, ConcurrentHashMap<String, SessionData> sessionData) throws IOException {
        clientSocket = socket;
        this.publicDirectory = publicDirectory;
        this.sessionData = sessionData;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new DataOutputStream(clientSocket.getOutputStream());
    }

    public void run() {
        try {
            CleanHttpRequest request = new CleanHttpRequest(in);
            var router = new HashMap<String, RequestHandler>() {{
                put("/hello", new HTMLRequestHandler(out, "Welcome Screen!"));
                put("/ping", new PingRequestHandler(out));
                put("/guess", new GuessRequestHandler(out, request, sessionData));
                put("*", new FileRequestHandler(out, request, publicDirectory));
            }};
            var route = request.getPath();
            if (!router.containsKey(route))
                route = "*";
            router.get(route).handle();
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
