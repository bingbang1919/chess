package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class SequelAuthDAO implements DataAccessObjects.AuthDAO {

    private static SequelAuthDAO instance;

    public SequelAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  pet (
              `id` int NOT NULL AUTO_INCREMENT,
              `name` varchar(256) NOT NULL,
              `type` ENUM('CAT', 'DOG', 'FISH', 'FROG', 'ROCK') DEFAULT 'CAT',
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(type),
              INDEX(name)
            )
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }

    public static SequelAuthDAO getInstance() throws DataAccessException {
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

    }

    @Override
    public void removeUser(String token) throws DataAccessException {

    }
}
