package service;

import model.*;
import dataaccess.*;
import org.mindrot.jbcrypt.BCrypt;

public class UserService extends Service{
    public AuthData register(UserData user, DataAccessObjects.AuthDAO authDao, DataAccessObjects.UserDAO userDao)
            throws DataAccessException, IllegalAccessException {
        if (user.username() == null || user.password() == null || user.email() == null) {
            throw new IllegalAccessException("Error: bad request");
        }
        String username = user.username();
        String password = hashPassword(user.password());
        String email = user.email();
        user = new UserData(username, password, email);
        try {
            userDao.getUser(username);
        } catch (DataAccessException e) {
            String authToken = makeAuthToken();
            AuthData registerResponse = new AuthData(authToken, username);
            authDao.addAuth(registerResponse);
            userDao.addUser(user);
            return registerResponse;
        }
        throw new DataAccessException("Error: already taken");
    }

    public AuthData login(LoginRequest user, DataAccessObjects.AuthDAO authDao, DataAccessObjects.UserDAO userDao)
            throws DataAccessException {
        String username = user.username();
        String password = user.password();
        UserData dbUser = userDao.getUser(username);
        if (passwordMatches(password, dbUser.password())) {
            String token = makeAuthToken();
            AuthData newAuth = new AuthData(token, username);
            authDao.addAuth(newAuth);
            return newAuth;
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }
    }
    public void logout(SingleAuthentication authToken, DataAccessObjects.AuthDAO authDao) throws DataAccessException {
        String token = authToken.authentication();
        authDao.getAuth(token);
        authDao.removeUser(token);
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private boolean passwordMatches(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }

}
