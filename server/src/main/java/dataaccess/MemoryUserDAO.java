package dataaccess;

import model.UserData;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class MemoryUserDAO implements DataAccessObjects.UserDAO {

    private Map<String, UserData> users = new HashMap<>();

    public UserData getUser(String username) throws DataAccessException {
        UserData user = users.get(username);
        if (user != null)
            return user;
        throw new DataAccessException("Username queried does not exist");
    }

    public void addUser(String username, String password, String email) throws DataAccessException {
        UserData data = new UserData(username, password, email);
        users.put(username, data);
        if (users.get(username) == null)
            throw new DataAccessException("User Data was not correctly registered.");
    }

    public void removeUser(String username) throws DataAccessException {
        users.remove(username);
        if (users.get(username) == null)
            throw new DataAccessException("User Data was not correctly removed.");
    }

    public Collection<UserData> listUsers() throws DataAccessException {
        return users.values();
    }

    public void clear() throws DataAccessException {
        users.clear();
    }
}
