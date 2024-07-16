package service;

import java.util.UUID;
import dataaccess.*;

public class Service {
        protected String makeAuthToken() {
            return UUID.randomUUID().toString();
        }
        public void clear() {

        }
}
