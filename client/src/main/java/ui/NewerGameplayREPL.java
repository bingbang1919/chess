package ui;

import chess.ChessBoard;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class NewerGameplayREPL {
    private final int CHESS_BOARD_SIZE = 8;

    public void run(ChessBoard board, boolean whiteView) {
        String[] headers = { "   "," A ", " B ", " C ", " D ", " E ", " F ", " G ", " H ", "   " };
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        drawHeader(out, headers, whiteView);
        drawBoard(out, board, whiteView);
        drawHeader(out, headers, whiteView);

    }

    private void drawBoard(PrintStream out, ChessBoard board, boolean whiteView) {
        String[] rowMarkers = { " 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 " };
        if (whiteView) {
            for (int i = CHESS_BOARD_SIZE-1; i >=0 ; i--) {
                out.print(rowMarkers[i]);
                for (int j=0; j<CHESS_BOARD_SIZE; j++) {
                    out.print(" H ");
                }
                out.println(rowMarkers[i]);
            }
        } else {
            for (int i = 0; i < CHESS_BOARD_SIZE ; i++) {
                out.print(rowMarkers[i]);
                for (int j= CHESS_BOARD_SIZE-1; j>=0; j--) {
                    out.print(" H ");
                }
                out.println(rowMarkers[i]);
            }
        }
    }

    private void drawHeader(PrintStream out, String[] headers, boolean whiteView) {
        String[] columnMarkers = {"   ", " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", "   "};
        if (whiteView) {
            for (int i=0; i<CHESS_BOARD_SIZE+2; i++) {
                out.print(columnMarkers[i]);
            }
            out.println();
        } else {
            for (int i=CHESS_BOARD_SIZE+1; i>=0; i--) {
                out.print(columnMarkers[i]);
            }
            out.println();
        }
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private void printPosition(PrintStream out, int row, int col) {

    }
}
