package client;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.SequelAuthDAO;
import dataaccess.SequelGameDAO;
import dataaccess.SequelUserDAO;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import service.UserService;
import ui.ChessClient;
import ui.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static ChessClient client;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        client = new ChessClient("http://localhost:8080/");
        facade = client.getFacade();
    }

    @BeforeEach
    void clearServer() throws Exception {
        facade.clear();
    }


    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void register() throws Exception {
        UserData testUser = new UserData("ethan","security","email");
        Assertions.assertEquals(facade.register(testUser).username(), "ethan");
    }
    @Test
    public void registerIdenticalUsername() throws Exception {
        UserData testUser = new UserData("ethan","security","email");
        facade.register(testUser);
        UserData badUser = new UserData("ethan","another","sameemail");
        Assertions.assertThrows(Exception.class,  () -> facade.register(badUser));
    }
    @Test
    public void login() throws Exception {
        UserData testUser = new UserData("ethan","security","email");
        facade.register(testUser);
        LoginRequest request = new LoginRequest("ethan", "security");
        Assertions.assertEquals(facade.login(request).username(), "ethan");
    }    @Test
    public void loginWrongPassword() throws Exception {
        UserData testUser = new UserData("ethan","security","email");
        facade.register(testUser);
        LoginRequest request = new LoginRequest("ethan", "wrong password");
        Assertions.assertThrows(Exception.class, () -> facade.login(request).username(), "ethan");
    }
    @Test
    public void logout() throws Exception {
        UserData testUser = new UserData("ethan","security","email");
        facade.register(testUser);
        LoginRequest request = new LoginRequest("ethan", "security");
        String token = facade.login(request).authToken();
        client.authToken = token;
        facade.logout();
        Assertions.assertThrows(DataAccessException.class, () -> SequelAuthDAO.getInstance().getAuth(token));
    }

    @Test
    public void logoutWrongAuth() throws Exception {
        UserData testUser = new UserData("ethan","security","email");
        facade.register(testUser);
        LoginRequest request = new LoginRequest("ethan", "security");
        facade.login(request);
        client.authToken = "this is the wrong auth token";
        Assertions.assertThrows(Exception.class, () -> facade.logout());
    }
    @Test
    public void listGames() throws Exception {
        UserData testUser = new UserData("ethan","security","email");
        facade.register(testUser);
        LoginRequest request = new LoginRequest("ethan", "security");
        client.authToken = facade.login(request).authToken();
        facade.createGame("one fish");
        facade.createGame("two fish");
        facade.createGame("red fish");
        facade.createGame("blue fish");
        Assertions.assertEquals(4, facade.listGames().size());
    }
    @Test
    public void listBadAuth() throws Exception {
        UserData testUser = new UserData("ethan","security","email");
        facade.register(testUser);
        LoginRequest request = new LoginRequest("ethan", "security");
        facade.login(request);
        client.authToken = "this is the wrong auth token";
        Assertions.assertThrows(Exception.class, () -> facade.listGames());
    }
    @Test
    public void createGame() throws Exception {
        UserData testUser = new UserData("ethan","security","email");
        facade.register(testUser);
        LoginRequest request = new LoginRequest("ethan", "security");
        client.authToken = facade.login(request).authToken();
        Assertions.assertEquals(7, facade.createGame("somegame").gameID());
    }
    @Test
    public void createBadAuth() throws Exception {
        UserData testUser = new UserData("ethan","security","email");
        facade.register(testUser);
        LoginRequest request = new LoginRequest("ethan", "security");
        facade.login(request);
        client.authToken = "this is the wrong auth token";
        Assertions.assertThrows(Exception.class, () -> facade.createGame("some name"));
    }
    @Test
    public void joinGame() throws Exception {
        UserData testUser = new UserData("ethan","security","email");
        facade.register(testUser);
        LoginRequest request = new LoginRequest("ethan", "security");
        client.authToken = facade.login(request).authToken();
        facade.createGame("somegame");
        facade.listGames();
        facade.joinGame(1, ChessGame.TeamColor.BLACK);
        Assertions.assertEquals("ethan", SequelGameDAO.getInstance().getGame(1).blackUsername());
    }
    @Test
    public void joinGameColorTaken() throws Exception {
        UserData testUser = new UserData("ethan","security","email");
        facade.register(testUser);
        LoginRequest request = new LoginRequest("ethan", "security");
        client.authToken = facade.login(request).authToken();
        CreateGameResponse data = facade.createGame("somegame");
        facade.listGames();
        facade.joinGame(data.gameID(), ChessGame.TeamColor.BLACK);
        Assertions.assertThrows(Exception.class, () -> facade.joinGame(1, ChessGame.TeamColor.BLACK));
    }



}
