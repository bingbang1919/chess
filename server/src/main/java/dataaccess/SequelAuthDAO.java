package dataaccess;

import com.google.gson.Gson;
import model.AuthData;

import java.sql.SQLException;

public class SequelAuthDAO implements DataAccessObjects.AuthDAO {

    private static SequelAuthDAO instance;

    public static SequelAuthDAO getInstance() {
        if (instance == null) {
            instance = new SequelAuthDAO();
        }
        return instance;
    }

    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public AuthData getAuth(String token) throws DataAccessException {
        return null;
    }

    @Override
    public void addAuth(AuthData auth) throws DataAccessException {
        var statement = "INSERT INTO authentication (token, AuthData) VALUES (?, ?)";
        var json = new Gson().toJson(auth);
        executeUpdate(statement, auth.authToken(), json);
    }

    @Override
    public void removeUser(String token) throws DataAccessException {

    }
}
