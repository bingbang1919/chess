package service;

import model.*;

import java.util.UUID;

public class Service {
        protected String makeAuthToken() {
            return UUID.randomUUID().toString();
        }
            public void clear() {

        }
}
