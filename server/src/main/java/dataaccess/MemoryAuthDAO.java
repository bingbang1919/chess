package dataaccess;

import model.AuthData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements DataAccessObjects.AuthDAO {

    private Map<String, AuthData> authTokens = new HashMap<>();
    private static MemoryAuthDAO instance;


    public static MemoryAuthDAO getInstance() {
        if (instance == null)
            instance = new MemoryAuthDAO();
        return instance;
    }

    public AuthData getAuth(String token) throws DataAccessException {
        AuthData data = authTokens.get(token);
        if (data != null)
            return data;
        throw new DataAccessException("AuthToken queried does not exist");
    }

    public void addAuth(AuthData auth) throws DataAccessException {
        String token = auth.authToken();
        authTokens.put(token, auth);
        if (authTokens.get(token) == null)
            throw new DataAccessException("Auth Data was not correctly registered.");
    }

    public void removeUser(String token) throws DataAccessException {
        authTokens.remove(token);
        if (authTokens.get(token) == null)
            throw new DataAccessException("Auth Data was not correctly removed.");
    }

    public Collection<AuthData> listAuth() throws DataAccessException {
        return authTokens.values();
    }

    public void clear() throws DataAccessException {
        authTokens.clear();
    }
}