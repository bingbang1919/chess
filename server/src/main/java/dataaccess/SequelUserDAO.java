package dataaccess;

import model.UserData;

import java.sql.SQLException;

public class SequelUserDAO implements DataAccessObjects.UserDAO{

    private static SequelUserDAO instance;

    public static SequelUserDAO getInstance() throws DataAccessException {
        if (instance == null) {
            instance = new SequelUserDAO();
        }
        return instance;
    }



    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void addUser(UserData user) throws DataAccessException {

    }


}
