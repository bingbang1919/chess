package ui;

import chess.ChessGame.TeamColor;
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
        var path = "/session";
        makeRequest("DELETE", path, null, null);
    }

    public Collection<GameData> listGames() throws IllegalAccessException {
        var path = "/game";
        return makeRequest("GET", path, null, ListGamesResponse.class).games();
    }

    public CreateGameResponse createGame(String name) throws IllegalAccessException {
        var path = "/game";
        CreateGameRequest request = new CreateGameRequest(name);
        return makeRequest("POST", path, request, CreateGameResponse.class);
    }

    public void joinGame(Integer gameID, TeamColor color) throws IllegalAccessException {
        var path = "/game";
        JoinGameRequest request = new JoinGameRequest(color, gameID);
        makeRequest("PUT", path, request, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws IllegalAccessException {
        try {
            URL url = (new URI(serverURL + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            http.addRequestProperty("authorization", ChessClient.authToken);
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new IllegalAccessException("Make Request is not make requesting: " + ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
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
