package ui;

public class PostloginREPL {
    // TODO: You've got to get your stuff together and make a map that contains the authtokens that you might need to use

    public  PostloginREPL(String serverURL, ChessClient client) {
        this.client = client;
    }

    private final ChessClient client;
    public void run() {
        System.out.println("Welcome to the results of my life during Summer Term!");
        while (true) {
            if (!PreloginREPL.loggedIn) {
                return;
            }

        }
    }

}
