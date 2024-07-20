package service;

import java.util.UUID;
import dataaccess.*;

public class Service{


    protected String makeAuthToken() {
            return UUID.randomUUID().toString();
        }
    public void clear(DataAccessObjects authDao, DataAccessObjects gameDao, DataAccessObjects userDao) throws DataAccessException {
        authDao.clear();
        gameDao.clear();
        userDao.clear();
    }

}
