package clean.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CleanHttpRequest {
    private static final String POST_METHOD = "POST";
    private final BufferedReader in;
    private final ConcurrentHashMap<String, SessionData> sessionsData;
    private String method;
    private String path;
    private String protocol;
    private final Map<String, String> headers = new HashMap<>();
    private String body;
    private Map<String, String> postData = new HashMap<>();
    private Map<String, String> cookies;
    private Map<String, String> queryParams;

    public CleanHttpRequest(BufferedReader in, ConcurrentHashMap<String, SessionData> sessionsData) throws Exception {
        this.in = in;
        this.sessionsData = sessionsData;
        setRequestProperties();
    }

    public String getMethod() {
        return this.method;
    }

    public String getPath() {
        return this.path;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getPostData() {
        return postData;
    }

    public Map<String, String> getCookies () {
        return cookies;
    }

    public Map<String, String> getQueryParams () {
        return queryParams;
    }

    public ConcurrentHashMap<String, SessionData> getSessionData() {
        return sessionsData;
    }

    private void setRequestProperties() throws Exception {
        setMethodPathAndProtocol();
        parseAndSetHeaders();
        parseAndSetBodyData();
        parseAndSetCookies();
    }

    private void parseAndSetBodyData() throws IOException {
        if (getMethod().equals(POST_METHOD) && getHeaders().containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(getHeaders().get("Content-Length"));
            StringBuilder b = new StringBuilder();
            for (int i = 0; i < contentLength; i++) {
                b.append((char) in.read());
            }
            body = b.toString();
            String[] bodyData = body.split("&");
            getPostData().putAll(parseInputData(bodyData));
        }
    }

    private void parseAndSetHeaders() throws Exception {
        for (String line = in.readLine(); line != null && !line.isEmpty(); line = in.readLine()) {
            String[] items = line.split(": ");
            if (items.length == 1) {
                throw new Exception("No value for header key \n\t" + line);
            }
            String value = items[1];
            for (int i = 2; i < items.length; i++) {
                value += ": " + items[i];
            }
            getHeaders().put(items[0], value);
        }
    }

    private void setMethodPathAndProtocol() throws Exception {
        String firstLine = in.readLine();
        if (firstLine == null) {
            throw new Exception("Request is null.");
        }
        while (firstLine.isEmpty()) {
            firstLine = in.readLine();
        }
        String[] requestInfo = firstLine.trim().split(" ");
        if (requestInfo.length != 3) {
            throw new Exception("Request is malformed.");
        }
        setRequestMethod(requestInfo[0].toUpperCase());
        setPath(requestInfo[1]);
        setQueryParams(requestInfo[1]);
        setRequestProtocol(requestInfo[2]);
    }

    private void setQueryParams(String path) throws UnsupportedEncodingException {
        this.queryParams = parseQueryParams(path);
    }

    private void parseAndSetCookies() {
        var headers = getHeaders();
        var cookiesRawHeader = headers.get("Cookie");
        Map<String, String> cookies =  new HashMap<>();
        if(cookiesRawHeader == null) return;
        var rawCookies = cookiesRawHeader.trim().split(";");
        for (String rawCookie : rawCookies) {
            var cookieKeyValue = rawCookie.trim().split("=");
            if (cookieKeyValue.length == 2)
                cookies.put(cookieKeyValue[0].trim(), cookieKeyValue[1].trim());
        }
        this.cookies = cookies;
    }

    private void setRequestProtocol(String protocol) {
        this.protocol = protocol;
    }

    private void setPath(String path) {
        this.path = path.split("\\?")[0];
    }

    private static Map<String, String> parseQueryParams(String path) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        System.out.println(path);
        int q = path.lastIndexOf('?');
        if (q == -1) {
            return query_pairs;
        }
        String query = path.substring(q + 1);
        System.out.println(query);
        if(query == null) return query_pairs;
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }

    private void setRequestMethod(String method) {
        this.method = method;
    }

    private Map<String, String> parseInputData(String[] data) throws UnsupportedEncodingException {
        Map<String, String> inputData = new HashMap<String, String>();
        for (String item : data) {
            if (!item.contains("=")) {
                inputData.put(item, null);
                continue;
            }
            String value = item.substring(item.indexOf('=') + 1);
            value = URLDecoder.decode(value, StandardCharsets.UTF_8);
            inputData.put(item.substring(0, item.indexOf('=')), value);
        }
        return inputData;
    }
}



