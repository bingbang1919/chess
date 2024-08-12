package ui;

import chess.*;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import websocket.commands.LeaveCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.ResignCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;


public class WebSocketClient extends Endpoint {

    private final Session session;
    public String authtoken = null;
    public Integer gameID;
    private ChessGame game;
    public boolean whiteView;
    public boolean spectating=false;
    public WebSocketClient() throws Exception {
        URI uri = new URI("ws://localhost:7389/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);
        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                try {
                    Gson gson = new Gson();
                    ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
                    switch (serverMessage.getServerMessageType()) {
                        case NOTIFICATION -> handleNotification(message);
                        case ERROR -> handleError(message);
                        case LOAD_GAME -> handleLoadGame(message);
                    }
                } catch (JsonSyntaxException e) {
                    System.out.println(message);
                }
            }
            private void handleLoadGame(String message) {
                Gson gson = new Gson();
                LoadGameMessage loadGameMessage = gson.fromJson(message, LoadGameMessage.class);
                game = loadGameMessage.getGame();
                GameplayREPL.drawBoard(game.getBoard(), whiteView, null);
            }
            private void handleNotification(String message) {
                Gson gson = new Gson();
                NotificationMessage notification = gson.fromJson(message, NotificationMessage.class);
                System.out.println(notification.getMessage());
            }
            private void handleError(String message) {
                Gson gson = new Gson();
                ErrorMessage error = gson.fromJson(message, ErrorMessage.class);
                System.out.println(error.getErrorMessage());
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
                case "move" -> {
                    String promotion = null;
                    if (params.length == 3) {
                        promotion = params[2];
                    }
                    yield makeMove(params[0], params[1], promotion);
                }
                case "resign" -> resign();
                case "highlight" -> highlightLegalMoves(params[0]);
                case "help" -> help();
                default -> throw new IllegalStateException("Unexpected value: " + cmd);
            };
        } catch (Exception e) {
            return "Something went terribly wrong with eval: " + e.getMessage();
        }
    }

    public String  help() {
        String info = """
                 * help - Displays possible actions
                 * redraw - Redraws the chess board
                 * leave - leave game
                 * move <Start> <End> - moves the piece from one valid space to another. Format: LetterNumber (i.e. b3)
                 * resign - resign game (ends the game)
                 * highlight <position> - highlights possible moves for a valid piece in that position.
                """;
        return info;
    }


    /**
     * Allows the user to input the piece for which they want to highlight legal moves.
     * The selected piece’s current square and all squares it can legally move to are highlighted.
     * This is a local operation and has no effect on remote users’ screens.
     */
    public String highlightLegalMoves(String location) throws Exception {
        ChessPosition position = parsePosition(location);
        ChessPiece piece = game.getBoard().getPiece(position);
        ArrayList<ChessMove> moves = (ArrayList<ChessMove>) piece.pieceMoves(game.getBoard(), position);
        GameplayREPL.drawBoard(game.getBoard(), whiteView, moves);
        return "Highlighted";
    }
    /**
     * Prompts the user to confirm they want to resign. If they do, the user forfeits the game and the game is over.
     * Does not cause the user to leave the game.
     */
    public String resign() throws Exception {
        if (spectating) {
            return "You are a spectator, you can't lose a game you ain't playing";
        }
        try {
            ResignCommand msg = new ResignCommand(UserGameCommand.CommandType.RESIGN, authtoken, gameID);
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
    public String makeMove(String start, String end, String promotion) throws Exception {
        if (spectating) {
            return "You are a spectator, you can't make a move";
        }
        ChessPosition startPosition = parsePosition(start);
        ChessPosition endPosition = parsePosition(end);
        ChessPiece.PieceType promotionPiece = getPromotionPiece(promotion);
        ChessMove move = new ChessMove(startPosition, endPosition, promotionPiece);
        try {
            MakeMoveCommand msg = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE, authtoken, gameID, move);
            send(new Gson().toJson(msg));
            return "";
        } catch (Exception e) {
            throw new Exception("Something went wrong with MAKEMOVE");
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
                int rowVal = Character.getNumericValue(row);
                return new ChessPosition(rowVal, colVal);
            } else {
                throw new Exception("Input the start and end positions as <LETTER><NUMBER>");
            }
        } else {
            throw new Exception("Please input the position as <column><row>");
        }
    }

    /**
     * Removes the user from the game (whether they are playing or observing the game). The client transitions back to the Post-Login UI.
     *
     */
    public String leave() throws Exception {
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
    public String redrawBoard() {
        GameplayREPL.drawBoard(game.getBoard(), whiteView, null);
        return "";
    }
}