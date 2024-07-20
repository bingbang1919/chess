package dataaccess;

import chess.ChessGame;
import model.*;

import javax.xml.crypto.Data;
import java.util.Collection;

public interface DataAccessObjects {
    void clear() throws DataAccessException;
    interface UserDAO extends DataAccessObjects {
        UserData getUser(String username) throws DataAccessException;
        void addUser(String username, String password, String email) throws DataAccessException;
        void removeUser(String username) throws DataAccessException;
        Collection<UserData> listUsers() throws DataAccessException;
    }
    interface GameDAO extends DataAccessObjects {
        GameData getGame(int id) throws DataAccessException;
        void addGame(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException;
        void removeGame(int id) throws DataAccessException;
        Collection<GameData> listGames() throws DataAccessException;
    }
    interface AuthDAO extends DataAccessObjects {
        AuthData getAuth(String token) throws DataAccessException;
        void addAuth(String username, String token) throws DataAccessException;
        void removeUser(String token) throws DataAccessException;
        Collection<AuthData> listAuth() throws DataAccessException;
    }
}
