package dataaccess;

import model.GameData;
import dataaccess.*;

import java.util.Collection;
import java.util.Map;

public class MemoryGameDAO implements DataAccessObjects.GameDAO {
    MemoryGameDAO() {}
    private Map<String, GameData> games;

    @Override
    public GameData getGame(int id) throws DataAccessException {
        return null;
    }

    @Override
    public void addGame(String name, int id, String whiteUser, String blackUser) throws DataAccessException {

    }

    @Override
    public void removeGame(int id) throws DataAccessException {

    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return null;
    }

    @Override
    public void clear() throws DataAccessException {

    }

}
