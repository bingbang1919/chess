package ui;

import chess.ChessGame;

import java.util.Objects;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class PostloginREPL {
    public  PostloginREPL(String serverURL, ChessClient client) {
        this.client = client;
    }

    private final ChessClient client;
    public static boolean inGame = false;
    public ChessGame.TeamColor color;
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(client.help());
        while (true) {
            if (!PreloginREPL.isLoggedIn) {
                return;
            }
            if (inGame) {
                new GameplayREPL(client.wbClient, color).run();
            }
            System.out.print(SET_TEXT_BOLD + SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLACK + "[LOGGED IN]" + SET_BG_COLOR_BLACK +
                    SET_TEXT_COLOR_WHITE + " Enter a Response: ");
            String response = scanner.nextLine();
            String output = client.eval(response);
            if (Objects.equals(output, "quit")) {
                System.out.println("Thank you so much for a-playing my game!");
                System.exit(0);
            }
            System.out.println(output);
        }
    }
}
