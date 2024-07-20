package service;

import model.*;
import dataaccess.*;
import java.util.UUID;

public class UserService extends Service{
    public AuthData register(UserData user) {
        String username = user.username();
        String authToken = makeAuthToken();
        AuthData authData = new AuthData(authToken, username);
        return authData;
    }
    public AuthData login(UserData user) {
        String username = user.username();
        String authToken = makeAuthToken();
        return new AuthData(authToken, username);
    }
    public void logout(UserData user) {
    }
}
