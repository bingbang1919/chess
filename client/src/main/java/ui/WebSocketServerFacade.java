package ui;

public class WebSocketServerFacade {
    String url;
    WebSocketClient client;
    public WebSocketServerFacade(String url, WebSocketClient client) {
        this.url = url;
        this.client = client;
    }

}
