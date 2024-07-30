package dataaccess;

import com.google.gson.Gson;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;
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
        UserData result = null;
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT UserData FROM users WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1,username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()){
                        result = readUser(rs);
                    }
                    if (result == null) {
                        throw new Exception();
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Could not get user");
        }
        return result;
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        var json = rs.getString("UserData");
        return new Gson().fromJson(json, UserData.class);
    }

    @Override
    public void addUser(UserData user) throws DataAccessException {
        try {
            var statement = "INSERT INTO users (username, UserData) VALUES (?, ?)";
            var json = new Gson().toJson(user);
            executeUpdate(statement, user.username(), json);
        } catch (Exception e) {
            throw  new DataAccessException("could not add user to db");
        }
    }
}
