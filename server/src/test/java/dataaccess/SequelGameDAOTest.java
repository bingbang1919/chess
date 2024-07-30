package dataaccess;

import chess.ChessGame;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class SequelGameDAOTest {

    DataAccessObjects.GameDAO gameDao;
    @AfterEach
    void tearDown() throws DataAccessException {
        gameDao.clear();
    }

    @BeforeEach
    void setUp() throws DataAccessException {
        gameDao = SequelGameDAO.getInstance();
        gameDao.addGame(new GameData(1, "Ethan", "Tyler", "first game", new ChessGame()));
        gameDao.addGame(new GameData(2, "Rowan", "Dunn", "second game", new ChessGame()));
    }

    @Test
    void getInstance() {
        Assertions.assertNotEquals(gameDao, new SequelGameDAO());
    }

    @Test
    void clear() throws DataAccessException {
        gameDao.clear();
        Assertions.assertEquals(0, gameDao.listGames().size());
    }

    @Test
    void getGame() throws DataAccessException {
        Assertions.assertEquals("Ethan", gameDao.getGame(1).whiteUsername());
    }

    @Test
    void wrongGameID() {
        Assertions.assertThrows(DataAccessException.class, () -> gameDao.getGame(3));
    }

    @Test
    void addGame() throws DataAccessException{
        gameDao.addGame(new GameData(6, "Olivia", "Kenny", "first game", new ChessGame()));
        Assertions.assertEquals(3, gameDao.listGames().size());
    }

    @Test
    void addNullGame() {
        Assertions.assertThrows(DataAccessException.class, () -> gameDao.addGame(null));
    }

    @Test
    void removeGame() throws DataAccessException {
        gameDao.removeGame(1);
        Assertions.assertEquals(1, gameDao.listGames().size());
    }

    @Test
    void removeNonExistentGame() throws DataAccessException {
        gameDao.removeGame(5);
        Assertions.assertEquals(2, gameDao.listGames().size());

    }

    @Test
    void listCorrectNumberGames() throws DataAccessException {
        Assertions.assertEquals(2, gameDao.listGames().size());
    }

    @Test
    void listNoGames() throws  DataAccessException {
        gameDao.clear();
        Assertions.assertNotEquals(2, gameDao.listGames().size());
    }

}