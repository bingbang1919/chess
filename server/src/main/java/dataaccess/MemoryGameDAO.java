package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements DataAccessObjects.GameDAO {
    private Map<Integer, GameData> games = new HashMap<>();
    private static MemoryGameDAO instance;


    public static MemoryGameDAO getInstance() {
        if (instance == null) {
            instance = new MemoryGameDAO();
        }
        return instance;
    }

    public GameData getGame(int id) throws DataAccessException {
        GameData data = games.get(id);
        if (data != null) {
            return data;
        }
        throw new DataAccessException("Game ID queried does not exist");
    }

    public void addGame(GameData data) throws DataAccessException {
        int gameID = data.gameID();
        games.put(gameID, data);
        if (games.get(gameID) == null) {
            throw new DataAccessException("Game Data was not correctly registered.");
        }
    }

    public void removeGame(int id) throws DataAccessException {
        games.remove(id);
        if (games.get(id) != null) {
            throw new DataAccessException("Game Data was not correctly removed.");
        }
    }

    public Collection<GameData> listGames() throws DataAccessException {
        return games.values();
    }

    public void clear() throws DataAccessException {
        games.clear();
    }
}
