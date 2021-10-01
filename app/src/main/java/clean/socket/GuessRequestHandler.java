package clean.socket;

import java.io.OutputStream;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class GuessRequestHandler extends RequestHandler {

    private String sessionId;
    private SessionData sessionData;

    public GuessRequestHandler(OutputStream out, CleanHttpRequest request, ConcurrentHashMap<String, SessionData> sessions) {
        this.out = out;
        setSessionId(request);
        setSessionData(sessions);
        attemptGuess(request);
    }

    public void handle() throws Exception {
        String responseHtml = "<html><body><form action='/guess' method='Post'>" +
                "<p><i>" + sessionData.guessMessage + "</i></p>" +
                "<label for='guess'>Guess a number from 1 to 100:</label><br/>" +
                "<input autofocus id='guess' name='guess' type='text'/>" +
                "</form></body></html>";
        String responseLength = "Content-Length: " + responseHtml.length() + "\r\n";
        String cookieHeader = "Set-Cookie: guessSession=" + sessionId + "\r\n";
        String rawResponse = responseStatusHeader + responseTypeHeader +
                responseLength + cookieHeader + responseConnection + responseHtml;
        out.write(rawResponse.getBytes());
    }

    private void attemptGuess(CleanHttpRequest request) {
        var postData = request.getPostData();
        if(postData != null && sessionData != null) {
            sessionData.attemptGuess(postData.get("guess"));
        }
    }

    private void setSessionData(ConcurrentHashMap<String, SessionData> sessions) {
        if(sessions.containsKey(sessionId)) {
            sessionData = sessions.get(sessionId);
        } else {
            sessionData = new SessionData();
            sessions.put(sessionId, sessionData);
        }
    }

    private void setSessionId(CleanHttpRequest request) {
        var cookies = request.getCookies();
        if(cookies == null || !cookies.containsKey("guessSession")) {
            sessionId = UUID.randomUUID().toString();
        } else {
            sessionId = cookies.get("guessSession");
        }
    }


}
