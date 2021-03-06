package clean.socket;

import java.io.OutputStream;
import java.util.UUID;

public class GuessRequestHandler extends RequestHandler {

    private String sessionId;
    private SessionData sessionData;

    public void handle(CleanHttpRequest request, OutputStream out) throws Exception {
        setSessionId(request);
        setSessionData(request);
        attemptGuess(request);

        this.addCookie("guessSession=" + sessionId);
        this.setStatusHeader(200);
        this.setBody("<html><body><form action='/guess' method='Post'>" +
                "<p><i>" + sessionData.guessMessage + "</i></p>" +
                "<label for='guess'>Guess a number from 1 to 100:</label><br/>" +
                "<input autofocus id='guess' name='guess' type='text'/>" +
                "</form></body></html>");
        this.send(out);
    }

    private void attemptGuess(CleanHttpRequest request) {
        var postData = request.getPostData();
        if(postData != null && sessionData != null) {
            sessionData.attemptGuess(postData.get("guess"));
        }
    }

    private void setSessionData(CleanHttpRequest request) {
        var sessions = request.getSessionData();
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
