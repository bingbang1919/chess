package ui;

public class PreloginREPL {

    private String serverURL;
    public PreloginREPL(String serverURL){
        serverURL = serverURL;
    }

    public static boolean loggedIn = false;

    public void run() {
        System.out.println("Welcome to the results of my life during Summer Term!");
        while (true) {
            if (loggedIn) {
                new PostloginREPL(serverURL).run();
            }
        }
    }

    private void help() {

    }

    private void quit() {

    }

    private void login() {

    }

    private void register() {

    }

}
