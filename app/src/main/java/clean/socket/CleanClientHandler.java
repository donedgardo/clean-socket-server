package clean.socket;
import java.io.*;
import java.net.Socket;


class CleanClientHandler extends Thread {
    private Socket clientSocket;
    private BufferedReader in;
    private OutputStream outStream;

    public CleanClientHandler(Socket socket) throws IOException {
        clientSocket = socket;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        outStream = new DataOutputStream(clientSocket.getOutputStream());
    }

    public void run() {
        try {
            String rawRequest = getRawRequest();
            String responseStatusHeader = "HTTP/1.1 200 OK\r\n";
            String responseTypeHeader = "Content-Type: text/html\r\n";
            String responseHtml = "<html><body><p>Welcome Screen!</p></body></html>\r\n";
            String responseLength = "Content-Length: " + responseHtml.length() + "\r\n";
            String responseConnection = "Connection: keep-alive\r\n";
            String rawResponse = responseStatusHeader + responseTypeHeader +
                    responseLength + responseConnection + "\r\n" + responseHtml;
            outStream.write(rawResponse.getBytes());
            outStream.flush();
            in.close();
            outStream.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getRawRequest() throws IOException {
        StringBuilder rawRequest = new StringBuilder();
        while (in.ready()) {
            rawRequest.append(in.readLine()).append("\r\n");
        }
        return rawRequest.toString();
    }
}
