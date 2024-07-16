package dataaccess;

import model.GameData;
import dataaccess.*;
import java.util.Map;

public class MemoryGameDAO implements DataAccessObjects.GameDAO {

    private Map<String, GameData> games;


    public Map<String, GameData> getGames() {
        return games;
    }

    public void setGames(Map<String, GameData> games) {
        this.games = games;
    }
}
