package dataaccess;

import com.google.gson.Gson;
import model.GameData;

import java.sql.SQLException;
import java.util.Collection;

public class SequelGameDAO implements DataAccessObjects.GameDAO {

    private static SequelGameDAO instance;

    public static SequelGameDAO getInstance() throws DataAccessException {
        if (instance == null) {
            instance = new SequelGameDAO();
        }
        return instance;
    }

    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public GameData getGame(int id) throws DataAccessException {
        return null;
    }

    @Override
    public void addGame(GameData data) throws DataAccessException {
        var statement = "INSERT INTO games (gameID, GameData) VALUES (?, ?)";
        var json = new Gson().toJson(data);
        executeUpdate(statement, data.gameID(), json);
    }

    @Override
    public void removeGame(int id) throws DataAccessException {

    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return null;
    }
}
