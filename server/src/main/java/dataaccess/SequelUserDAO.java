package dataaccess;

import model.UserData;

public class SequelUserDAO implements DataAccessObjects.UserDAO{
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
