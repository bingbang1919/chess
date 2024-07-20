package dataaccess;

import model.UserData;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class MemoryUserDAO implements DataAccessObjects.UserDAO {

    private Map<String, UserData> users = new HashMap<>();
    private static MemoryUserDAO instance;

    public static MemoryUserDAO getInstance() {
        if (instance == null)
            instance = new MemoryUserDAO();
        return instance;
    }

    public UserData getUser(String username) throws DataAccessException {
        UserData user = users.get(username);
        if (user != null)
            return user;
        throw new DataAccessException("Username queried does not exist");
    }

    public void addUser(UserData user) throws DataAccessException {
        String username = user.username();
        users.put(username, user);
        if (users.get(username) == null)
            throw new DataAccessException("User Data was not correctly registered.");
    }

    public void removeUser(String username) throws DataAccessException {
        users.remove(username);
        if (users.get(username) != null)
            throw new DataAccessException("User Data was not correctly removed.");
    }

    public Collection<UserData> listUsers() throws DataAccessException{
        return users.values();
    }

    public void clear() throws DataAccessException{
        users.clear();
    }
}
