package ui;

import static ui.EscapeSequences.*;


public class PreloginREPL {

    private String serverURL;
    public PreloginREPL(String serverURL){
        serverURL = serverURL;
    }

    public static boolean loggedIn = false;

    public void run() {
        System.out.println(SET_TEXT_COLOR_BLUE + "Welcome to the results of my life during Summer Term!");

        while (true) {
            if (loggedIn) {
                new PostloginREPL(serverURL).run();
            }

            return;
        }
    }



}
