package ui;

import java.util.Objects;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class PostloginREPL {
    // TODO: You've got to get your stuff together and make a map that contains the authtokens that you might need to use

    public  PostloginREPL(String serverURL, ChessClient client) {
        this.client = client;
    }

    private final ChessClient client;
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(client.help());
        while (true) {
            if (!PreloginREPL.isLoggedIn) {
                return;
            }
            System.out.print(SET_TEXT_BOLD + SET_TEXT_COLOR_YELLOW + "LOGGED IN, enter a response: ");
            String response = scanner.nextLine();
            String output = client.eval(response);
            if (Objects.equals(output, "quit")) {
                System.out.println("Thank you for a-playing my game!");
                System.exit(0);
            }
            System.out.println(output);
        }
    }
}
