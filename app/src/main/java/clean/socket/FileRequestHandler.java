package clean.socket;

import java.io.*;
import java.nio.file.Files;

public class FileRequestHandler extends RequestHandler {
    private final String publicDirectory;
    private final CleanHttpRequest request;

    public FileRequestHandler(OutputStream out, CleanHttpRequest request, String publicDirectory) {
        this.publicDirectory = publicDirectory;
        this.request = request;
        this.out = out;
    }

    public void handle() throws Exception {
        File f = new File(publicDirectory + request.getPath() + "/index.html");
        if (f.exists()) {
            FileInputStream fileStream = new FileInputStream(publicDirectory + request.getPath() + "/index.html");
            String responseLength = "Content-Length: " + fileStream.available() + "\r\n";
            String rawResponse = responseStatusHeader + responseTypeHeader + responseLength + responseConnection;
            out.write(rawResponse.getBytes());
            sendFile(fileStream);
        }
        File dir = new File(publicDirectory + request.getPath());
        if (dir.isDirectory()) {
            String listFilesHtmlStart = "<html><body>";
            String listFilesHtmlEnd = "</body></html>";
            StringBuilder fileNames = new StringBuilder();
            Files.list(dir.toPath()).forEach(path -> {
                fileNames.append("<p>").append(path.getFileName()).append("</p>");
            });
            String responseHtml = listFilesHtmlStart + fileNames + listFilesHtmlEnd;
            String responseLength = "Content-Length: " + responseHtml.length() + "\r\n";
            String rawResponse = responseStatusHeader + responseTypeHeader +
                    responseLength + responseConnection + responseHtml;
            out.write(rawResponse.getBytes());
        }
        if (dir.isFile()) {
            FileInputStream fileStream = new FileInputStream(publicDirectory + request.getPath());
            String responseLength = "Content-Length: " + fileStream.available() + "\r\n";
            String mimeType = "Content-Type:" + Files.probeContentType(dir.toPath()) + "\r\n";
            String rawResponse = responseStatusHeader + mimeType + responseLength + responseConnection;
            out.write(rawResponse.getBytes());
            sendFile(fileStream);
        }
    }
    public void sendFile(FileInputStream fin) throws Exception {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fin.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        fin.close();
    }
}
