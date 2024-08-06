package ui;

import chess.ChessBoard;

import java.io.PrintStream;

import static ui.EscapeSequences.*;

public class NewerGameplayREPL {
    private final int CHESS_BOARD_SIZE = 8;

    public void run(ChessBoard board, boolean whiteView) {
        String[] headers = { "   "," A ", " B ", " C ", " D ", " E ", " F ", " G ", " H ", "   " };
        drawHeader(headers, whiteView);

        drawHeader(headers, whiteView);
    }

    private void drawBoard(ChessBoard board, boolean whiteView) {
        String[] rowMarkers = { " 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 " };
        for (int row = 0; row< CHESS_BOARD_SIZE+2; row++) {
            
        }
    }

    private void drawHeader(String[] headers, boolean whiteView) {
        if (whiteView) {

        }
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

}
