package dataaccess;

import com.google.gson.Gson;
import model.GameData;

import java.beans.PropertyEditorSupport;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
        var statement = "TRUNCATE games";
        executeUpdate(statement);
    }

    @Override
    public GameData getGame(int id) throws DataAccessException {
        GameData result = null;
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT GameData FROM games WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1,id);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()){
                        result = readGame(rs);
                    }
                    if (result == null) {
                        throw new Exception();
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Could not get game");
        }
        return result;
    }

    @Override
    public void addGame(GameData data) throws DataAccessException {
        try{
            var statement = "INSERT INTO games (gameID, GameData) VALUES (?, ?)";
            var json = new Gson().toJson(data);
            executeUpdate(statement, data.gameID(), json);
        } catch (Exception e) {
            throw new DataAccessException("could not add game");
        }
    }

    @Override
    public void removeGame(int id) throws DataAccessException {
        try {
            var statement = "DELETE FROM games WHERE gameID=?";
            executeUpdate(statement, id);
        } catch (Exception e) {
            throw new DataAccessException("could not remove game");
        }
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT GameData FROM games";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Could not list games");
        }
        return result;
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        var json = rs.getString("GameData");
        return new Gson().fromJson(json, GameData.class);
    }

}
