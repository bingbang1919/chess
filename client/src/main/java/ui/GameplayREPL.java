package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static ui.EscapeSequences.*;

public class GameplayREPL {

    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;

    private static HashMap<Integer, String> pieceLocations;

    public void run() {
        pieceLocations = new HashMap<>() {
            {
                put(1," R ");
                put(2," N ");
                put(3," B ");
                put(4," K ");
                put(5," Q ");
                put(6," B ");
                put(7," N ");
                put(8," R ");
            }
        };
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawHeaders(out, true);
        String[] rowMarkers = { " 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 " };
        for (int i=0; i<BOARD_SIZE_IN_SQUARES;i++) {
            printRow(out, i, rowMarkers[i], true);
            out.print('\n');
        }
        drawHeaders(out, true);
        out.println();
        drawHeaders(out);
        for (int i=BOARD_SIZE_IN_SQUARES-1; i>=0;i--) {
            printRow(out, i, rowMarkers[i], false);
            out.print('\n');
        }
        drawHeaders(out);
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeaders(PrintStream out) {
        drawHeaders(out, false);
    }

    private static void drawHeaders(PrintStream out, boolean reversed) {
        String[] headers = { "   "," A ", " B ", " C ", " D ", " E ", " F ", " G ", " H ", "   " };
        if (reversed) {
            for (int boardCol = BOARD_SIZE_IN_SQUARES+1; boardCol >=0; boardCol--) {
                printHeaderText(out, headers[boardCol]);
            }
        }
        else {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES+2; ++boardCol) {
                printHeaderText(out, headers[boardCol]);
            }
        }
        out.println();
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(player);
        setBlack(out);
    }

    private static void printRow(PrintStream out, int rowNum, String rowMarker, boolean whiteView) {
        Boolean lightSquare = (rowNum % 2 == 0);
        out.print(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_WHITE + rowMarker);
        if (whiteView) {
            for (int i = 0; i < BOARD_SIZE_IN_SQUARES; i++) {
                doRowConditions(out, rowNum, lightSquare, i);
                lightSquare = !lightSquare;
            }
        }
        else {
            for (int i = BOARD_SIZE_IN_SQUARES - 1; i >= 0; i--) {
                doRowConditions(out, rowNum, lightSquare, i);
                lightSquare = !lightSquare;
            }
        }
        out.print(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_WHITE + rowMarker);
        setBlack(out);
    }

    private static void doRowConditions(PrintStream out, Integer rowNum, Boolean lightSquare, Integer col) {
        String position;
        if (rowNum == 1) {
        } else if (rowNum == 6 ) {
            out.print(SET_TEXT_COLOR_RED);
            position = " p ";
        } else {position = "   ";}
        switch (rowNum) {
            case 0 -> {
                out.print(SET_TEXT_COLOR_BLUE);
                position = pieceLocations.get(col+1);
            } case 1 -> {
                out.print(SET_TEXT_COLOR_BLUE);
                position = " p ";
            } case 6 -> {
                out.print(SET_TEXT_COLOR_RED);
                position = " p ";
            } case 7 -> {
                out.print(SET_TEXT_COLOR_RED);
                position = pieceLocations.get(col+1);
            } default -> position = "   ";
        }
        if (lightSquare) {
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(position);
        } else  {
            out.print(SET_BG_COLOR_BLACK);
            out.print(position);
        }
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

}
