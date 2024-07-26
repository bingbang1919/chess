package dataaccess;

import model.AuthData;

public class SequelAuthDAO implements DataAccessObjects.AuthDAO {

    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public AuthData getAuth(String token) throws DataAccessException {
        return null;
    }

    @Override
    public void addAuth(AuthData auth) throws DataAccessException {

    }

    @Override
    public void removeUser(String token) throws DataAccessException {

    }
}
