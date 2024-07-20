package service;

import model.*;
import dataaccess.*;

public class UserService extends Service{
    public AuthData register(UserData user, DataAccessObjects.AuthDAO authDao, DataAccessObjects.UserDAO userDao) throws DataAccessException {
        String username = user.username();
        String authToken = makeAuthToken();
        AuthData registerResponse = new AuthData(authToken, username);
        authDao.addAuth(new AuthData(authToken, username));
        userDao.addUser(user);
        return registerResponse;
    }

    public AuthData login(UserData user) {
        String username = user.username();
        String authToken = makeAuthToken();
        return new AuthData(authToken, username);
    }
    public void logout(UserData user) {
    }
}
