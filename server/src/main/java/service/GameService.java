package service;

import chess.ChessGame;
import model.*;
import dataaccess.*;

import java.util.Collection;

public class GameService extends Service{

    public Collection<GameData> listGames(SingleAuthentication authToken, DataAccessObjects.AuthDAO authDAO,
                                          DataAccessObjects.GameDAO gameDAO) throws DataAccessException {
        try {
            String token = authToken.authentication();
            authDAO.getAuth(token);
            return gameDAO.listGames();
        } catch (Exception e) {
            throw new DataAccessException("bad request");
        }
    }

    public GameData createGame(int gameCounter, String authToken, CreateGameRequest gameRequest, DataAccessObjects.AuthDAO authDAO,
                               DataAccessObjects.GameDAO gameDAO) throws DataAccessException, IllegalArgumentException {
        authDAO.getAuth(authToken);
        if (gameRequest.gameName() == null) {
            throw new IllegalArgumentException("Error: bad request");
        }
        GameData data = new GameData(gameCounter, null, null, gameRequest.gameName(), new ChessGame());
        gameDAO.addGame(data);
        return data;
    }

    public void joinGame(JoinGameRequest userRequest, String authToken, DataAccessObjects.AuthDAO authDAO,
                         DataAccessObjects.GameDAO gameDAO) throws DataAccessException, IllegalAccessException, IllegalArgumentException {
        if (userRequest.gameID() == null || userRequest.playerColor() == null) {
            throw new IllegalArgumentException("");
        }
        AuthData authData = authDAO.getAuth(authToken);
        ChessGame.TeamColor color = userRequest.playerColor();
        String username = authData.username();
        int id = userRequest.gameID();
        GameData gameData = gameDAO.getGame(id);
        String white = gameData.whiteUsername();
        String black = gameData.blackUsername();
        if (color == ChessGame.TeamColor.WHITE && white != null) {
            throw new IllegalAccessException("White is already taken.");
        }
        if (color == ChessGame.TeamColor.BLACK && black != null) {
            throw new IllegalAccessException("Black is already taken.");
        }
        String name = gameData.gameName();
        ChessGame game = gameData.game();
        switch(color) {
            case WHITE -> white = username;
            case BLACK -> black = username;
        }
        gameData = new GameData(id, white, black, name, game);
        gameDAO.removeGame(id);
        gameDAO.addGame(gameData);
    }
}
