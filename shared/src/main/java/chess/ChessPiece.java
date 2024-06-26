package chess;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        //  This switch case segment finds the respective piece type and returns the result of the proper function, in the form of an ArrayList
        return switch (type) {
            case KING -> kingFindMoves(board, myPosition);
            case QUEEN -> queenFindMoves(board, myPosition);
            case BISHOP -> bishopFindMoves(board, myPosition);
            case KNIGHT -> knightFindMoves(board, myPosition);
            case ROOK -> rookFindMoves(board, myPosition);
            case PAWN -> pawnFindMoves(board, myPosition);
            default -> throw new RuntimeException("Didn't find a compatible type for this piece");
        };
    }
    private Collection<ChessMove> kingFindMoves(ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented");
    }
    private Collection<ChessMove> queenFindMoves(ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented");
    }
    private Collection<ChessMove> bishopFindMoves(ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented");
    }
    private Collection<ChessMove> knightFindMoves(ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented");
    }
    private Collection<ChessMove> rookFindMoves(ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented");
    }
    private Collection<ChessMove> pawnFindMoves(ChessBoard board, ChessPosition myPosition) {
//        OKAY so we're making a pretty big assumption that the player is always oriented facing the top,
//        so we're going to assume that if the pawn is on row 2, that it is in the starting position and can thus
//        feasibly move 2 spots forward, rather than just one.
        throw new RuntimeException("Not implemented");
//        ArrayList<>
        // Checks if
//        if ()
    }








}
