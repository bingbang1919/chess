package ui;

import chess.ChessGame.TeamColor;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.*;

import java.io.*;
import java.net.*;
import java.util.Collection;

public class ServerFacade {

    private final String serverURL;
    private final ChessClient client;

    public ServerFacade(String url, ChessClient c) {
        serverURL = url;
        client = c;
    }

    public AuthData register(UserData user) throws Exception {
        try {
            var path = "/user";
            return makeRequest("POST", path, user, AuthData.class);
        } catch (IllegalAccessException e) {
            throw new Exception("Error: bad request.");
        } catch (IllegalArgumentException e) {
            throw new Exception("Error: already taken.");
        } catch (RuntimeException e) {
            throw new Exception(e.getMessage());
        }
    }

    public AuthData login(LoginRequest request) throws Exception {
        try {
            var path = "/session";
            return makeRequest("POST", path, request, AuthData.class);
        } catch (DataAccessException e) {
            throw new Exception("Error: unauthorized.");
        } catch (RuntimeException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void logout() throws Exception {
        try {
            var path = "/session";
            makeRequest("DELETE", path, null, null);
        } catch (DataAccessException e) {
            throw new Exception("Error: unauthorized.");
        } catch (RuntimeException e) {
            throw new Exception(e.getMessage());
        }
    }

    public Collection<GameData> listGames() throws Exception {
        try {
            var path = "/game";
            return makeRequest("GET", path, null, ListGamesResponse.class).games();
        } catch (DataAccessException e) {
            throw new Exception("Error: unauthorized.");
        } catch (RuntimeException e) {
            throw new Exception(e.getMessage());
        }
    }

    public CreateGameResponse createGame(String name) throws Exception {
        try {
            var path = "/game";
            CreateGameRequest request = new CreateGameRequest(name);
            return makeRequest("POST", path, request, CreateGameResponse.class);
        } catch (IllegalAccessException e) {
            throw new Exception("Error: bad request.");
        }
        catch (DataAccessException e) {
            throw new Exception("Error: unauthorized.");
        }
        catch (RuntimeException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void joinGame(Integer gameID, TeamColor color) throws Exception {
        try {
            var path = "/game";
            JoinGameRequest request = new JoinGameRequest(color, gameID);
            makeRequest("PUT", path, request, null);
        } catch (IllegalAccessException e) {
            throw new Exception("Error: bad request.");
        }
        catch (DataAccessException e) {
            throw new Exception("Error: unauthorized.");
        }
        catch (IllegalArgumentException e) {
            throw new Exception("Error: already taken");
        }
        catch (RuntimeException e) {
            throw new Exception(e.getMessage());
        }
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws IllegalAccessException, IOException, DataAccessException, URISyntaxException {
        URL url = (new URI(serverURL + path)).toURL();
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod(method);
        http.setDoOutput(true);
        http.addRequestProperty("authorization", client.authToken);
        writeBody(request, http);
        http.connect();
        if (http.getResponseCode() != 200) {
            if (http.getResponseCode() == 400) {
                throw new IllegalAccessException();
            } else if (http.getResponseCode() == 401) {
                throw new DataAccessException("");
            } else if (http.getResponseCode() == 403) {
                throw new IllegalArgumentException();
            } else if (http.getResponseCode() == 500) {
                throw new RuntimeException();
            }
        }
        return readBody(http, responseClass);
    }

    private void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }


    private <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
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
