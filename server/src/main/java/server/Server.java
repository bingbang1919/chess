package server;
import com.google.gson.Gson;
import service.*;
import service.Service;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();
        Spark.delete("/db", this::clear);
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object clear(Request req, Response res) throws RuntimeException {
        final Gson gson = new Gson();
        try {
            Service service = new Service();
            service.clear();
            res.status(200);
            return "{}";
        } catch (Exception e) {
//            System.out.println(e.getMessage());
            res.status(500);
            return gson.toJson(e.getMessage());
        }
    }

}
