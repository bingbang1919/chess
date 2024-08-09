package ui;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import websocket.commands.LeaveCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.ResignCommand;
import websocket.commands.UserGameCommand;

import javax.websocket.*;
import java.net.URI;
import java.util.Arrays;

public class WebSocketClient {

    private final Session session;
    private String authtoken = null;
    public Integer gameID;
    public WebSocketClient(String url) throws Exception {
        URI uri = new URI("ws://localhost:7389/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            /**
             * @param message Takes in a serialized Json message from the server. This function should parse the message and execute the logic that's needed.
             */
            public void onMessage(String message) {
                System.out.println(message);
            }
        });
    }

    /**
     *
     * @param msg Going to be in Serialized Json. Sends the Serialized string to the Server,
     * which can then parse it and use it.
     *
     */
    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }


    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            // TODO: set the switch case to grab an output.
            return switch (cmd) {
                case "redraw" -> redrawBoard();
                case "leave" -> leave();
                case "move" -> makeMove();
                case "resign" -> resign();
                case "highlight" -> highlightLegalMoves();
                default -> throw new IllegalStateException("Unexpected value: " + cmd);
            };
        } catch (Exception e) {
            return "Something went terribly wrong with eval: " + e.getMessage();
        }
    }

    /**
     * Allows the user to input the piece for which they want to highlight legal moves.
     * The selected piece’s current square and all squares it can legally move to are highlighted.
     * This is a local operation and has no effect on remote users’ screens.
     */
    private String highlightLegalMoves() {
        throw new RuntimeException("Not yet implemented.");
    }
    /**
     * Prompts the user to confirm they want to resign. If they do, the user forfeits the game and the game is over.
     * Does not cause the user to leave the game.
     */
    private String resign() throws Exception {
        try {
            ResignCommand msg = new ResignCommand(UserGameCommand.CommandType.LEAVE, authtoken, gameID);
            send(new Gson().toJson(msg));
            return "RESIGNED";
        } catch (Exception e) {
            throw new Exception("Something went wrong with RESIGN");
        }
    }

    /**
     * Allow the user to input what move they want to make. The board is updated to reflect the result of the move,
     * and the board automatically updates on all clients involved in the game.
     */
    private String makeMove(String start, String end, String promotion) throws Exception {
        ChessPosition startPosition = parsePosition(start);
        ChessPosition endPosition = parsePosition(end);
        ChessPiece.PieceType promotionPiece = getPromotionPiece(promotion);
        ChessMove move = new ChessMove(startPosition, endPosition, promotionPiece);
        try {
            MakeMoveCommand msg = new MakeMoveCommand(UserGameCommand.CommandType.LEAVE, authtoken, gameID, move);
            send(new Gson().toJson(msg));
            return "LEFT";
        } catch (Exception e) {
            throw new Exception("Something went wrong with LEAVE");
        }
    }

    private ChessPiece.PieceType getPromotionPiece(String promotion) {
        if (promotion==null) {return null;}
        return switch (promotion.toLowerCase()) {
            case "p" -> ChessPiece.PieceType.PAWN;
            case "r" -> ChessPiece.PieceType.ROOK;
            case "n" -> ChessPiece.PieceType.KNIGHT;
            case "b" -> ChessPiece.PieceType.BISHOP;
            case "q" -> ChessPiece.PieceType.QUEEN;
            case "k" -> ChessPiece.PieceType.KING;
            default -> throw new IllegalStateException("Please enter a Promotion piece type as a single letter as represented on the board.");
        };
    }

    private ChessPosition parsePosition(String code) throws Exception {
        if (code.length() == 2) {
            char col = code.charAt(0);
            char row = code.charAt(1);
            if (Character.isAlphabetic(col) && Character.isDigit(row)) {
                int colVal;
                colVal = switch (col) {
                    case 'a' -> 1;
                    case 'b' -> 2;
                    case 'c' -> 3;
                    case 'd' -> 4;
                    case 'e' -> 5;
                    case 'f' -> 6;
                    case 'g' -> 7;
                    case 'h' -> 8;
                    default -> throw new IllegalStateException("Unexpected column value: " + col);
                };
                int rowVal = Character.getNumericValue(row)+1;
                return new ChessPosition(rowVal, colVal);
            } else {
                throw new Exception("Input the start and end positions as <LETTER><NUMBER>");
            }
        } else {
            throw new Exception("Please input the position as <column><row>");
        }
    }

    public boolean isStringInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    /**
     * Removes the user from the game (whether they are playing or observing the game). The client transitions back to the Post-Login UI.
     *
     * @return
     */
    private String leave() throws Exception {
        try {
            LeaveCommand msg = new LeaveCommand(UserGameCommand.CommandType.LEAVE, authtoken, gameID);
            send(new Gson().toJson(msg));
            return "LEFT";
        } catch (Exception e) {
            throw new Exception("Something went wrong with LEAVE");
        }
    }

    /**
     * Redraws the chess board upon the user’s request.
     */
    private String redrawBoard() {
        throw new RuntimeException("Not yet implemented.");
    }

}