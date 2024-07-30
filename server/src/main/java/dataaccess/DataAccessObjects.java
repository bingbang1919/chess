package dataaccess;

import model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public interface DataAccessObjects {
    void clear() throws DataAccessException;
     default void executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    switch (param) {
                        case String p -> ps.setString(i + 1, p);
                        case Integer p -> ps.setInt(i + 1, p);
                        default -> throw new IllegalStateException("Unexpected value: " + param);
                    }
                }
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    interface UserDAO extends DataAccessObjects {
        UserData getUser(String username) throws DataAccessException;
        void addUser(UserData user) throws DataAccessException;
    }
    interface GameDAO extends DataAccessObjects {
        GameData getGame(int id) throws DataAccessException;
        void addGame(GameData data) throws DataAccessException;
        void removeGame(int id) throws DataAccessException;
        Collection<GameData> listGames() throws DataAccessException;
    }
    interface AuthDAO extends DataAccessObjects {
        AuthData getAuth(String token) throws DataAccessException;
        void addAuth(AuthData auth) throws DataAccessException;
        void removeUser(String token) throws DataAccessException;
    }
}
