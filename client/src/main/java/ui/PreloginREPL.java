package ui;

import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.*;


public class PreloginREPL {

    private final String serverURL;
    public PreloginREPL(String URL, ChessClient client){
        this.client = client;
        serverURL = URL;
    }
    private final ChessClient client;
    public static boolean isLoggedIn = false;
    public static boolean running = true;

    public void run() {
        System.out.print(SET_TEXT_COLOR_WHITE + "Welcome to the results of my life during Summer Term!");
        Scanner scanner = new Scanner(System.in);
        System.out.println(client.help());
        while (running) {
            if (isLoggedIn) {
                new PostloginREPL(serverURL, client).run();
            }
            System.out.print(SET_TEXT_BOLD + SET_BG_COLOR_WHITE + SET_TEXT_COLOR_LIGHT_GREY + "[LOGGED OUT]" + SET_BG_COLOR_BLACK +  SET_TEXT_COLOR_WHITE + " Enter a Response: ");
            String response = scanner.nextLine();
            String output = client.eval(response);
            if (Objects.equals(output, "quit")) {
                System.out.println(SET_TEXT_BLINKING + SET_TEXT_COLOR_MAGENTA + "Have a nice day!");
                System.exit(0);
            }
            if (output.equals("LOGGEDIN")) {
                isLoggedIn = true;
            }
            else {
                System.out.println(output);
            }
        }
    }
}
