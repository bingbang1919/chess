package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn = TeamColor.WHITE;
    private ChessBoard board = new ChessBoard();

    public ChessGame() {
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        return board.getPiece(startPosition).pieceMoves(board,startPosition);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessBoard cloneboard = board.clone();
        ChessPosition startPosition = move.getStartPosition();
        if (board.getPiece(startPosition) == null)
            throw new InvalidMoveException("There is no piece at the starting position");
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece piece = board.getPiece(startPosition).clone();

        // Checks if the move is even possible for the given piece
        ArrayList<ChessMove> possibleMoves = (ArrayList<ChessMove>) piece.pieceMoves(board,startPosition);
        if (!possibleMoves.contains(move)) {
            throw new InvalidMoveException("This is not a valid move for this piece");
        }
        // Checks if the move doesn't put the king at risk
        board.squares[endPosition.getRow()-1][endPosition.getColumn()-1] = null;
            // verifies and executes a pawn promotion
            if (move.getPromotionPiece() != null)
                piece = new ChessPiece(teamTurn, move.getPromotionPiece());
        board.addPiece(endPosition, piece);
        board.squares[startPosition.getRow()-1][startPosition.getColumn()-1] = null;
        if (isInCheck(teamTurn)) {
            setBoard(cloneboard);
            throw new InvalidMoveException("That's gonna put you in check");
        }
        if (teamTurn == TeamColor.WHITE) teamTurn = TeamColor.BLACK; else teamTurn = TeamColor.WHITE;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        ChessPosition kingPosition = null;
        for (int i=0;i<8;i++) {
            for (int j=0;j<8;j++) {
                ChessPosition currentPosition = new ChessPosition(i+1,j+1);
                if (board.squares[i][j] != null) {
                    if (board.getPiece(currentPosition).getTeamColor() != teamColor) {
                        possibleMoves.addAll(board.getPiece(currentPosition).pieceMoves(board, currentPosition));
                    }
                    if (board.getPiece(currentPosition).getTeamColor() == teamTurn  && board.getPiece(currentPosition).getPieceType() == ChessPiece.PieceType.KING) {
                        kingPosition = currentPosition;
                    }
                }
            }
        }
        for (ChessMove move : possibleMoves) {
            if (move.getEndPosition().equals(kingPosition))
                return true;
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */

    // This is going to use the makeMove method to essentially make every possible move and if any of them result in no Check, then it returns false.
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
