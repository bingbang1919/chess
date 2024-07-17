package service;

import java.util.UUID;
import dataaccess.*;
import dataaccess.MemoryGameDAO.*;

public class Service implements DataAccessObjects{
        protected String makeAuthToken() {
            return UUID.randomUUID().toString();
        }
        public void clear() throws DataAccessException {
            MemoryUserDAO.getInstance().clear();
            MemoryGameDAO.getInstance().clear();
            MemoryAuthDAO.getInstance().clear();
        }
}
