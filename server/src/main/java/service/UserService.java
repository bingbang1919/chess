package service;

import model.*;
import dataaccess.*;

public class UserService extends Service{
    public AuthData register(UserData user, DataAccessObjects.AuthDAO authDao, DataAccessObjects.UserDAO userDao) throws DataAccessException, IllegalAccessException {
        String username = user.username();
        if (username == null || user.password() == null || user.email() == null)
            throw new IllegalAccessException("Error: bad request");
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
        // TODO: make error response objects
    }

    public AuthData login(LoginRequest user, DataAccessObjects.AuthDAO authDao, DataAccessObjects.UserDAO userDao) throws DataAccessException, IllegalAccessException {
        String username = user.username();
        String password = user.password();
        UserData dbUser = userDao.getUser(username);
        if (dbUser.password().equals(password)) {
            String token = makeAuthToken();
            AuthData newAuth = new AuthData(token, username);
            authDao.addAuth(newAuth);
            return newAuth;
        }
        else
            throw new DataAccessException("Error: unauthorized");
        // TODO: make error response objects and throw
    }
    public void logout(SingleAuthentication authToken, DataAccessObjects.AuthDAO authDao) throws DataAccessException {
        String token = authToken.authentication();
        authDao.getAuth(token);
        authDao.removeUser(token);
    }
}
