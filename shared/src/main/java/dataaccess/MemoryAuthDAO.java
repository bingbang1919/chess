package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.Map;

public class MemoryAuthDAO implements DataAccessObjects.AuthDAO {

    private Map<String, AuthData> authTokens;


    public Map<String, AuthData> getAuthTokens() {
        return authTokens;
    }

    public void setAuthTokens(Map<String, AuthData> authTokens) {
        this.authTokens = authTokens;
    }
}