package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class GameplayREPL {
    private static final int CHESS_BOARD_SIZE = 8;
    private final WebSocketClient client;
    private static final ArrayList<ChessPosition> HIGHLIGHTED_SPOTS = new ArrayList<>();

    GameplayREPL(WebSocketClient client) {
        this.client = client;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.print(client.help());
        while (true){
            System.out.println("[IN-GAME] Enter a response: ");
            String response = scanner.nextLine();
            String output = client.eval(response);
            System.out.println(output);
            if (Objects.equals(output, "LEFT")) {
                return;
            }
        }
    }

    public static void drawBoard(ChessBoard board, boolean whiteView, ArrayList<ChessMove> moves) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        if (moves != null) {
            for (ChessMove move : moves) {
                HIGHLIGHTED_SPOTS.add(move.getEndPosition());
            }
        }
        String[] headers = { "   "," A ", " B ", " C ", " D ", " E ", " F ", " G ", " H ", "   " };
        String[] rowMarkers = { " 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 " };
        out.println();
        drawHeader(out, headers, whiteView);
        if (whiteView) {
            for (int i = CHESS_BOARD_SIZE-1; i >=0 ; i--) {
                out.print(SET_BG_COLOR_DARK_GREY);
                out.print(rowMarkers[i]);
                out.print(SET_BG_COLOR_BLACK);
                for (int j=0; j<CHESS_BOARD_SIZE; j++) {
                    printPosition(out, board, i+1, j+1);
                }
                out.print(SET_BG_COLOR_DARK_GREY);
                out.print(rowMarkers[i]);
                out.print(SET_BG_COLOR_BLACK);
                out.println();
            }
        } else {
            for (int i = 0; i < CHESS_BOARD_SIZE ; i++) {
                out.print(SET_BG_COLOR_DARK_GREY);
                out.print(rowMarkers[i]);
                out.print(SET_BG_COLOR_BLACK);
                for (int j= CHESS_BOARD_SIZE-1; j>=0; j--) {
                    printPosition(out, board, i+1, j+1);
                }
                out.print(SET_BG_COLOR_DARK_GREY);
                out.print(rowMarkers[i]);
                out.print(SET_BG_COLOR_BLACK);
                out.println();
            }
        }
        drawHeader(out, headers, whiteView);
        HIGHLIGHTED_SPOTS.clear();
    }

    private static void drawHeader(PrintStream out, String[] headers, boolean whiteView) {
        String[] columnMarkers = {"   ", " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", "   "};
        out.print(SET_BG_COLOR_DARK_GREY);
        if (whiteView) {
            for (int i=0; i<CHESS_BOARD_SIZE+2; i++) {
                out.print(columnMarkers[i]);
            }
        } else {
            for (int i=CHESS_BOARD_SIZE+1; i>=0; i--) {
                out.print(columnMarkers[i]);
            }
        }
        out.print(SET_BG_COLOR_BLACK);
        out.println();

    }

    private static void printPosition(PrintStream out, ChessBoard board, int row, int col) {
        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
        String character = null;
        if ((row % 2 == 0 && col % 2 == 1) || (row % 2 == 1 && col % 2 == 0)) {
            ChessPosition position = new ChessPosition(row, col);
            out.print(SET_BG_COLOR_LIGHT_GREY);
            if (HIGHLIGHTED_SPOTS != null && HIGHLIGHTED_SPOTS.contains(position)) {
                out.print(SET_BG_COLOR_GREEN);
            }
        } else {
            ChessPosition position = new ChessPosition(row, col);
            out.print(SET_BG_COLOR_BLACK);
            if (HIGHLIGHTED_SPOTS != null && HIGHLIGHTED_SPOTS.contains(position)) {
                out.print(SET_BG_COLOR_DARK_GREEN);
            }
        }
        if (piece != null) {
            if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                out.print(SET_TEXT_COLOR_BLUE);
            } else {
                out.print(SET_TEXT_COLOR_RED);
            }
            switch (piece.getPieceType()) {
                case PAWN -> character = " P ";
                case ROOK -> character = " R ";
                case KNIGHT -> character = " N ";
                case BISHOP -> character = " B ";
                case QUEEN -> character = " Q ";
                case KING -> character = " K ";
            }
        } else {
            character = "   ";
        }
        out.print(character);
        out.print(SET_TEXT_COLOR_WHITE);
    }

}
