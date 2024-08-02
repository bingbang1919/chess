import com.google.gson.Gson;
import model.*;

import java.io.*;
import java.net.*;
import java.util.Collection;

public class ServerFacade {

    private final String serverURL;

    public ServerFacade(String url) { serverURL = url;}

    public AuthData register(UserData user) throws IllegalAccessException {
        var path = "/user";
        return makeRequest("POST", path, user, AuthData.class);
    }

    public AuthData login(LoginRequest request) throws IllegalAccessException {
        var path = "/session";
        return makeRequest("POST", path, request, AuthData.class);
    }

    public void logout() throws IllegalAccessException {
        throw new RuntimeException("Not implemented yet");
    }

    public Collection<GameData> listGames() throws IllegalAccessException {
        throw new RuntimeException("Not implemented yet");
    }

    public GameData createGame() throws IllegalAccessException {
        throw new RuntimeException("Not implemented yet");
    }

    public void joinGame() throws IllegalAccessException {
        throw new RuntimeException("Not implemented yet");
    }


    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws IllegalAccessException {
        try {
            URL url = (new URI(serverURL + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new IllegalAccessException("Make Request is not make requesting");
        }
    }

    private static void writeHeaders(Object request, HttpURLConnection http) {
        if (request.getClass() == SingleAuthentication.class) {
            http.addRequestProperty("authorization", ((SingleAuthentication) request).authentication());
        }
        else if (request.getClass() == CreateGameRequest.class) {
//            http.addRequestProperty("authorization", );
        }

    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            writeHeaders(request, http);
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, IllegalAccessException {
        var status = http.getResponseCode();
        if (status != 200) {
            throw new IllegalAccessException("");
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }
}
