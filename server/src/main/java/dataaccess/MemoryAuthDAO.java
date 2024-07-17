package dataaccess;

import model.AuthData;
import dataaccess.*;
import model.UserData;

import java.util.Collection;
import java.util.Map;

public class MemoryAuthDAO implements DataAccessObjects.AuthDAO {

    private Map<String, AuthData> authTokens;

    @Override
    public AuthData getAuth(String token) throws DataAccessException {
        AuthData data = authTokens.get(token);
        if (data != null)
            return data;
        throw new DataAccessException("AuthToken queried does not exist");
    }

    @Override
    public void addAuth(String username, String token) throws DataAccessException {
        AuthData data = new AuthData(username, token);
        authTokens.put(username, data);
        if (authTokens.get(token) == null)
            throw new DataAccessException("Auth Data was not correctly registered.");
    }

    @Override
    public void removeUser(String token) throws DataAccessException {
        authTokens.remove(token);
        if (authTokens.get(token) == null)
            throw new DataAccessException("Auth Data was not correctly removed.");
    }

    @Override
    public Collection<AuthData> listAuth() throws DataAccessException {
        return authTokens.values();
    }

    @Override
    public void clear() throws DataAccessException {
        authTokens.clear();
    }
}