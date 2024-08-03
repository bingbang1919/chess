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
        System.out.print(SET_TEXT_COLOR_BLUE + "Welcome to the results of my life during Summer Term!");
        Scanner scanner = new Scanner(System.in);
        System.out.println(client.help());
        while (running) {
            if (isLoggedIn) {
                new PostloginREPL(serverURL, client).run();
            }
            System.out.print(SET_TEXT_BOLD + SET_TEXT_COLOR_RED + "LOGGED OUT, enter a response: ");
            String response = scanner.nextLine();
            String output = client.eval(response);
            if (Objects.equals(output, "quit")) {
                System.out.println("Thank you for a-playing my game!");
                System.exit(0);
            }
            if (output.equals("LOGGEDIN")) {
                isLoggedIn = true;
            }
            System.out.println(output);
        }
    }
}
