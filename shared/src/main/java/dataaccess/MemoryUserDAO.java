package dataaccess;

import model.UserData;
import java.util.Map;


public class MemoryUserDAO implements DataAccessObjects.UserDAO {

    private Map<String, UserData> users;

    public Map<String, UserData> getUsers() {
        return users;
    }

    public void setUsers(Map<String, UserData> users) {
        this.users = users;
    }
}
