package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SequelAuthDAOTest {

    DataAccessObjects.AuthDAO authDao;
    @BeforeEach
    void setUp() throws DataAccessException {
        authDao = SequelAuthDAO.getInstance();
        authDao.addAuth(new AuthData("this", "bingbang1919"));
        authDao.addAuth(new AuthData("something else", "someone else"));
    }

    @AfterEach
    void tearDown() throws DataAccessException {
        authDao.clear();
    }

    @Test
    void getInstance() {
        Assertions.assertNotEquals(authDao, new SequelAuthDAO());
    }

    @Test
    void clear() throws DataAccessException {
        authDao.clear();
        Assertions.assertThrows(DataAccessException.class,() -> authDao.getAuth("this"));
    }

    @Test
    void getCorrectAuth() throws DataAccessException {
        Assertions.assertEquals(new AuthData("this", "bingbang1919"), authDao.getAuth("this"));
    }

    @Test
    void getWrongAuthToken() throws DataAccessException {
        authDao.clear();
        Assertions.assertThrows(DataAccessException.class, () -> authDao.getAuth("this"));
    }

    @Test
    void addValidAuth() throws DataAccessException {
        authDao.addAuth(new AuthData("someone else", "I am a user"));
        Assertions.assertEquals(new AuthData("someone else", "I am a user"), authDao.getAuth("someone else"));
    }

    @Test
    void addBadAuth() throws DataAccessException {
        Assertions.assertThrows(DataAccessException.class, () -> authDao.addAuth(null));
    }

    @Test
    void removeUserValid() throws DataAccessException {
        authDao.removeUser("this");
        Assertions.assertThrows(DataAccessException.class, () -> authDao.getAuth("this"));
    }

    @Test
    void removeWrongUser() throws DataAccessException {
        authDao.removeUser("THAT");
        Assertions.assertEquals(new AuthData("this", "bingbang1919"), authDao.getAuth("this"));
    }

}