package dataaccess;

import com.google.gson.Gson;
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
        var statement = "TRUNCATE users";
        executeUpdate(statement);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void addUser(UserData user) throws DataAccessException {
        var statement = "INSERT INTO users (username, UserData) VALUES (?, ?)";
        var json = new Gson().toJson(user);
        executeUpdate(statement, user.username(), json);
    }
}
