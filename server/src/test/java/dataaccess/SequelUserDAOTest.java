package dataaccess;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.UserData;

class SequelUserDAOTest {

    DataAccessObjects.UserDAO userDao;

    @BeforeEach
    void setUp() throws DataAccessException {
        userDao = SequelUserDAO.getInstance();
        userDao.addUser(new UserData("Ethan", "something secure", "lol@byu.edu"));
        userDao.addUser(new UserData("Tyler", "something else secure", "hags@byu.edu"));
    }

    @AfterEach
    void tearDown() throws DataAccessException {
        userDao.clear();
    }

    @Test
    void getInstance() {
        Assertions.assertNotEquals(userDao, new SequelGameDAO());
    }

    @Test
    void clear() throws DataAccessException {
        userDao.clear();
        Assertions.assertThrows(DataAccessException.class, () -> userDao.getUser("Ethan"));
    }

    @Test
    void getUser() throws DataAccessException {
        Assertions.assertEquals("lol@byu.edu", userDao.getUser("Ethan").email());
    }

    @Test
    void getUserNullField() {
        Assertions.assertThrows(DataAccessException.class, () -> userDao.getUser(null));
    }

    @Test
    void addUser() throws DataAccessException {
        userDao.addUser(new UserData("Arthur", "another thing", "chaos@hotmail.com"));
        Assertions.assertTrue("another thing".equals(userDao.getUser("Arthur").password()));
    }

    @Test
    void addNullUser() {
        Assertions.assertThrows(DataAccessException.class, () -> userDao.addUser(null));
    }
}