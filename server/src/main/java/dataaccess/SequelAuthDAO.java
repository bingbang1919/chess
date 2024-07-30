package dataaccess;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;

import java.sql.ResultSet;
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
        var statement = "TRUNCATE authentication";
        executeUpdate(statement);
    }

    @Override
    public AuthData getAuth(String token) throws DataAccessException {
        AuthData result = null;
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT AuthData FROM authentication WHERE token=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1,token);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()){
                        result = readUser(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Could not get game");
        }
        return result;
    }

    private AuthData readUser(ResultSet rs) throws SQLException {
        var json = rs.getString("AuthData");
        return new Gson().fromJson(json, AuthData.class);
    }

    @Override
    public void addAuth(AuthData auth) throws DataAccessException {
        var statement = "INSERT INTO authentication (token, AuthData) VALUES (?, ?)";
        var json = new Gson().toJson(auth);
        executeUpdate(statement, auth.authToken(), json);
    }

    @Override
    public void removeUser(String token) throws DataAccessException {
        var statement = "DELETE FROM authentication WHERE token=?";
        executeUpdate(statement, token);
    }
}
