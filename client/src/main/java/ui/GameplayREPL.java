package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class GameplayREPL {
    private final int CHESS_BOARD_SIZE = 8;

    public void run(ChessBoard board, boolean whiteView) {
        drawBoard(board, whiteView);
    }

    private void drawBoard(ChessBoard board, boolean whiteView) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        String[] headers = { "   "," A ", " B ", " C ", " D ", " E ", " F ", " G ", " H ", "   " };
        drawHeader(out, headers, whiteView);
        String[] rowMarkers = { " 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 " };
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
    }

    private void drawHeader(PrintStream out, String[] headers, boolean whiteView) {
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

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private void printPosition(PrintStream out, ChessBoard board, int row, int col) {
        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
        String character = null;
        if ((row % 2 == 0 && col % 2 == 1) || (row % 2 == 1 && col % 2 == 0)) {
            out.print(SET_BG_COLOR_LIGHT_GREY);
        } else {
            out.print(SET_BG_COLOR_BLACK);
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
