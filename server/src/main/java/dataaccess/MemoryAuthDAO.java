package dataaccess;

import model.AuthData;
import dataaccess.*;

import java.util.Collection;
import java.util.Map;

public class MemoryAuthDAO implements DataAccessObjects.AuthDAO {

    private Map<String, AuthData> authTokens;

    @Override
    public AuthData getAuth(String token) throws DataAccessException {
        return null;
    }

    @Override
    public void addAuth(String username, String token) throws DataAccessException {

    }

    @Override
    public void removeUser(String token) throws DataAccessException {

    }

    @Override
    public Collection<AuthData> listAuth() throws DataAccessException {
        return null;
    }

    @Override
    public void clear() throws DataAccessException {

    }
}