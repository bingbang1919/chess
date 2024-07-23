package service;


import chess.ChessGame;
import dataaccess.*;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

class ServiceTests {

    private DataAccessObjects.AuthDAO authDAO;
    private DataAccessObjects.GameDAO gameDAO;
    private DataAccessObjects.UserDAO userDAO;
    private final UserService userService = new UserService();
    private final GameService gameService = new GameService();

    @BeforeEach
    public void setup() {
        authDAO = MemoryAuthDAO.getInstance();
        userDAO = MemoryUserDAO.getInstance();
        gameDAO = MemoryGameDAO.getInstance();
    }

    @AfterEach
    public void breakdown() throws DataAccessException {
        authDAO.clear();
        userDAO.clear();
        gameDAO.clear();
    }

    @Test
    void clearAllDaos() {

    }

    @Test
    void registerNormalUser() throws DataAccessException, IllegalAccessException {
        UserData testUser = new UserData("Ethan", "a_password", "lol@byu.edu");
        AuthData result = userService.register(testUser, authDAO, userDAO);
        Assertions.assertEquals(testUser.username(), result.username());
    }

    @Test
    void registerNoEmail() {
        UserData testUser = new UserData("Ethan", "a_password", null);
        Exception exception =  Assertions.assertThrows(IllegalAccessException.class, () -> userService.register(testUser, authDAO, userDAO));
        Assertions.assertTrue(exception.getMessage().contains("Error: bad request"));
    }

    @Test
    void loginNormalUser() throws DataAccessException, IllegalAccessException {
        UserData testUser = new UserData("Ethan", "a_password", "lol@byu.edu");
        userService.register(testUser, authDAO, userDAO);
        LoginRequest request = new LoginRequest("Ethan", "a_password");
        AuthData response = userService.login(request, authDAO, userDAO);
        Assertions.assertEquals(authDAO.getAuth(response.authToken()), response);
    }

    @Test
    void loginWrongUsername() throws DataAccessException, IllegalAccessException {
        UserData testUser = new UserData("Ethan", "a_password", "lol@byu.edu");
        userService.register(testUser, authDAO, userDAO);
        LoginRequest request = new LoginRequest("Eathan", "a_password");
        Assertions.assertThrows(DataAccessException.class, () -> userService.login(request, authDAO, userDAO));
//        Assertions.assertTrue(exception.getMessage().contains("Error: bad request"));
    }

    @Test
    void logoutNormalUser() throws DataAccessException, IllegalAccessException {
        UserData testUser = new UserData("Ethan", "a_password", "lol@byu.edu");
        userService.register(testUser, authDAO, userDAO);
        LoginRequest request = new LoginRequest("Ethan", "a_password");
        AuthData response = userService.login(request, authDAO, userDAO);
        userService.logout(new SingleAuthentication(response.authToken()), authDAO);
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.getAuth(response.authToken()));
    }

    @Test
    void logoutAlreadyLoggedOut() throws DataAccessException, IllegalAccessException {
        UserData testUser = new UserData("Ethan", "a_password", "lol@byu.edu");
        userService.register(testUser, authDAO, userDAO);
        LoginRequest request = new LoginRequest("Ethan", "a_password");
        AuthData response = userService.login(request, authDAO, userDAO);
        userService.logout(new SingleAuthentication(response.authToken()), authDAO);
        Assertions.assertThrows(DataAccessException.class, () -> userService.logout(new SingleAuthentication(response.authToken()), authDAO));
    }


    @Test
    void listGames3Games() throws DataAccessException, IllegalAccessException {
        UserData testUser = new UserData("Ethan", "a_password", "lol@byu.edu");
        userService.register(testUser, authDAO, userDAO);
        LoginRequest request = new LoginRequest("Ethan", "a_password");
        AuthData response = userService.login(request, authDAO, userDAO);
        CreateGameRequest gameRequest = new CreateGameRequest("the same game name");
        GameData game1 = gameService.createGame(1, response.authToken(), gameRequest, authDAO, gameDAO);
        GameData game2 = gameService.createGame(2, response.authToken(), gameRequest, authDAO, gameDAO);
        GameData game3 = gameService.createGame(3, response.authToken(), gameRequest, authDAO, gameDAO);
        Collection<GameData> games = gameService.listGames(new SingleAuthentication(response.authToken()), authDAO, gameDAO);
        Assertions.assertTrue(games.contains(game1) && games.contains(game2) && games.contains(game3));
    }

    @Test
    void listGamesNoAuthToken() throws DataAccessException, IllegalAccessException {
        UserData testUser = new UserData("Ethan", "a_password", "lol@byu.edu");
        userService.register(testUser, authDAO, userDAO);
        LoginRequest request = new LoginRequest("Ethan", "a_password");
        AuthData response = userService.login(request, authDAO, userDAO);
        CreateGameRequest gameRequest = new CreateGameRequest("the same game name");
        gameService.createGame(1, response.authToken(), gameRequest, authDAO, gameDAO);
        gameService.createGame(2, response.authToken(), gameRequest, authDAO, gameDAO);
        gameService.createGame(3, response.authToken(), gameRequest, authDAO, gameDAO);
        Assertions.assertThrows(DataAccessException.class, () -> gameService.listGames(null, authDAO, gameDAO));
    }

    @Test
    void createGame2GamesSameName() throws DataAccessException, IllegalAccessException {
        UserData testUser = new UserData("Ethan", "a_password", "lol@byu.edu");
        userService.register(testUser, authDAO, userDAO);
        LoginRequest request = new LoginRequest("Ethan", "a_password");
        AuthData response = userService.login(request, authDAO, userDAO);
        CreateGameRequest gameRequest = new CreateGameRequest("the same game name");
        gameService.createGame(1, response.authToken(), gameRequest, authDAO, gameDAO);
        gameService.createGame(2, response.authToken(), gameRequest, authDAO, gameDAO);
        Assertions.assertNotEquals(gameDAO.getGame(1), gameDAO.getGame(2));
    }

    @Test
    void createGameWrongAuthToken() throws DataAccessException, IllegalAccessException {
        UserData testUser = new UserData("Ethan", "a_password", "lol@byu.edu");
        userService.register(testUser, authDAO, userDAO);
        LoginRequest request = new LoginRequest("Ethan", "a_password");
        userService.login(request, authDAO, userDAO);
        CreateGameRequest gameRequest = new CreateGameRequest("the same game name");
        Assertions.assertThrows(DataAccessException.class, () -> gameService.createGame(1,
                "Not this auth token" ,gameRequest, authDAO, gameDAO));
    }

    @Test
    void joinGameNormalUser() throws DataAccessException, IllegalAccessException {
        UserData testUser = new UserData("Ethan", "a_password", "lol@byu.edu");
        userService.register(testUser, authDAO, userDAO);
        LoginRequest request = new LoginRequest("Ethan", "a_password");
        AuthData response = userService.login(request, authDAO, userDAO);
        CreateGameRequest gameRequest = new CreateGameRequest("the same game name");
        gameService.createGame(1, response.authToken(), gameRequest, authDAO, gameDAO);
        gameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.WHITE, 1), response.authToken(), authDAO, gameDAO);
        Assertions.assertEquals(gameDAO.getGame(1).whiteUsername(), "Ethan");
    }

    @Test
    void joinGameNoColorSpecified() throws DataAccessException, IllegalAccessException {
        UserData testUser = new UserData("Ethan", "a_password", "lol@byu.edu");
        userService.register(testUser, authDAO, userDAO);
        LoginRequest request = new LoginRequest("Ethan", "a_password");
        AuthData response = userService.login(request, authDAO, userDAO);
        CreateGameRequest gameRequest = new CreateGameRequest("the same game name");
        gameService.createGame(1, response.authToken(), gameRequest, authDAO, gameDAO);
        Assertions.assertThrows(IllegalArgumentException.class, () -> gameService.joinGame(new JoinGameRequest(null,
                1), response.authToken(), authDAO, gameDAO));

    }
}