package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import dataaccess.*;

import java.util.Collection;
import java.util.Map;

public class MemoryGameDAO implements DataAccessObjects.GameDAO {
    MemoryGameDAO() {}
    private Map<Integer, GameData> games;

    public GameData getGame(int id) throws DataAccessException {
        GameData data = games.get(id);
        if (data != null)
            return data;
        throw new DataAccessException("Game ID queried does not exist");
    }

    public void addGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException {
        GameData data = new GameData(gameID, whiteUsername, blackUsername, gameName, game);
        games.put(gameID, data);
        if (games.get(gameID) == null)
            throw new DataAccessException("Game Data was not correctly registered.");
    }

    public void removeGame(int id) throws DataAccessException {
        games.remove(id);
        if (games.get(id) == null)
            throw new DataAccessException("Game Data was not correctly removed.");
    }

    public Collection<GameData> listGames() throws DataAccessException {
        return games.values();
    }

    public void clear() throws DataAccessException {
        games.clear();
    }

}
