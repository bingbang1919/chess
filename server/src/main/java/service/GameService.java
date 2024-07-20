package service;

import chess.ChessGame;
import model.*;
import dataaccess.*;

import java.util.Collection;

public class GameService extends Service{
    private int gameIdCounter = 1;
    public Collection<GameData> listGames(SingleAuthentication authToken, DataAccessObjects.AuthDAO authDAO, DataAccessObjects.GameDAO gameDAO) throws DataAccessException {
        String token = authToken.authentication();
        authDAO.getAuth(token);
        return gameDAO.listGames();
    }

    public GameData createGame(String authToken, CreateGameRequest gameRequest, DataAccessObjects.AuthDAO authDAO, DataAccessObjects.GameDAO gameDAO) throws DataAccessException {
        authDAO.getAuth(authToken);
        // TODO: possible that you use the right authtoken on the wrong person?
        int id = gameIdCounter;
        gameIdCounter += 1;
        GameData data = new GameData(id, null, null, gameRequest.gameName(), new ChessGame());
        gameDAO.addGame(data);
        return data;
    }

    public void joinGame(JoinGameRequest userRequest, String authToken, DataAccessObjects.AuthDAO authDAO,
                         DataAccessObjects.GameDAO gameDAO) throws DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        // TODO: possible that you use the right authtoken on the wrong person?
        ChessGame.TeamColor color = userRequest.playerColor();
        String username = authData.username();
        int id = userRequest.gameID();
        GameData gameData = gameDAO.getGame(id);
        String white = gameData.whiteUsername();
        String black = gameData.blackUsername();
        String name = gameData.gameName();
        ChessGame game = gameData.game();
        // TODO: Need to create cases where color is already taken.
        switch(color) {
            case WHITE -> white = username;
            case BLACK -> black = username;
        }
        gameData = new GameData(id, white, black, name, game);
        gameDAO.removeGame(id);
        gameDAO.addGame(gameData);
    }
}
