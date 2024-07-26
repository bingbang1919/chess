package dataaccess;

import model.GameData;

import java.util.Collection;

public class SequelGameDAO implements DataAccessObjects.GameDAO {
    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public GameData getGame(int id) throws DataAccessException {
        return null;
    }

    @Override
    public void addGame(GameData data) throws DataAccessException {

    }

    @Override
    public void removeGame(int id) throws DataAccessException {

    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return null;
    }
}
