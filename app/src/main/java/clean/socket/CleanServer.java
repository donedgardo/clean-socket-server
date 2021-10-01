package clean.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CleanServer {
    private static int port;
    private static String publicDirectory;
    private ServerSocket serverSocket;

    public static void main(String[] args) {
        try {
            setParams(args);
            CleanServer server = new CleanServer();
            server.start(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            ConcurrentHashMap<String, SessionData> sessionData = new ConcurrentHashMap<>();
            while (true)
                new CleanClientHandler(serverSocket.accept(), publicDirectory, sessionData).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, List<String>> setParams(String[] args) throws Exception {
        final Map<String, List<String>> params = new HashMap<>();
        List<String> options = null;
        for (int i = 0; i < args.length; i++) {
            final String a = args[i];
            if (a.charAt(0) == '-') {
                if (a.length() < 2) {
                    System.err.println("Error at argument " + a);
                    return params;
                }
                options = new ArrayList<>();
                params.put(a.substring(1), options);
            } else if (options != null) {
                options.add(a);
            } else {
                System.err.println("Illegal parameter usage");
            }
        }
        List<String> portArg = params.get("p");
        if(portArg == null) {
            String errorMsg = "Required port -p argument not provided.";
            System.err.println(errorMsg);
            throw new Exception(errorMsg);
        }

        List<String> dirArg = params.get("r");
        if(dirArg == null) {
            String errorMsg = "Required root directory -r argument not provided.";
            System.err.println(errorMsg);
            throw new Exception(errorMsg);
        }
        port = Integer.parseInt(portArg.get(0));
        publicDirectory = dirArg.get(0);
        return params;
    }

}
