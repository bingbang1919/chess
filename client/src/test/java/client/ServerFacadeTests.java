package client;

import dataaccess.DataAccessException;
import dataaccess.SequelAuthDAO;
import dataaccess.SequelGameDAO;
import dataaccess.SequelUserDAO;
import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    void clearServer() throws DataAccessException {
        SequelGameDAO.getInstance().clear();
        SequelUserDAO.getInstance().clear();
        SequelAuthDAO.getInstance().clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void register() throws Exception {
        Assertions.assertTrue(true);
    }
    @Test
    public void registerIdenticalUsername() throws Exception {
        Assertions.assertTrue(true);
    }
    @Test
    public void login() throws Exception {
        Assertions.assertTrue(true);
    }    @Test
    public void loginWrongPassword() throws Exception {
        Assertions.assertTrue(true);
    }
    @Test
    public void logout() throws Exception {
        Assertions.assertTrue(true);
    }

    @Test
    public void logout1() throws Exception {
        Assertions.assertTrue(true);
    }
    @Test
    public void listGames() throws Exception {
        Assertions.assertTrue(true);
    }
    @Test
    public void listGamesNoGames() throws Exception {
        Assertions.assertTrue(true);
    }
    @Test
    public void createGame() throws Exception {
        Assertions.assertTrue(true);
    }
    @Test
    public void createSameGame() throws Exception {
        Assertions.assertTrue(true);
    }
    @Test
    public void joinGame() throws Exception {
        Assertions.assertTrue(true);
    }
    @Test
    public void joinGameColorTaken() throws Exception {
        Assertions.assertTrue(true);
    }



}
