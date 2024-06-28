package chess;

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
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        int col = myPosition.getColumn();
        int row = myPosition.getRow();
        int[][] coordinateArray = {
                {row-1, col-1},
                {row, col-1},
                {row+1, col-1},
                {row-1, col},
                {row+1, col},
                {row-1, col+1},
                {row, col+1},
                {row+1, col+1}
        };
        for (int[] coordinate : coordinateArray) {
            row = coordinate[0];
            col = coordinate[1];
            if (row >8 || row <= 0 || col > 8 || col <= 0)
                continue;
            ChessPosition nextPosition = new ChessPosition(row, col);
            longMoveHelper(board, myPosition, nextPosition, possibleMoves);
        }
        return possibleMoves;
    }
    private Collection<ChessMove> queenFindMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        // move top right
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        while (col <= 8  && row <= 8) {
            ChessPosition nextPosition = new ChessPosition(row, col);
            if (longMoveHelper(board, myPosition, nextPosition, possibleMoves))
                break;
            col++;
            row++;
        }
        // move top left
        row = myPosition.getRow();
        col = myPosition.getColumn();
        while (col >= 1 && row <= 8) {
            ChessPosition nextPosition = new ChessPosition(row, col);
            if (longMoveHelper(board, myPosition, nextPosition, possibleMoves))
                break;
            col--;
            row++;
        }
        // move bottom right
        row = myPosition.getRow();
        col = myPosition.getColumn();
        while (col <= 8 && row >= 1) {
            ChessPosition nextPosition = new ChessPosition(row, col);
            if (longMoveHelper(board, myPosition, nextPosition, possibleMoves))
                break;
            row--;
            col++;
        }
        // move bottom left
        row = myPosition.getRow();
        col = myPosition.getColumn();
        while (col >= 1 && row >= 1) {
            ChessPosition nextPosition = new ChessPosition(row, col);
            if (longMoveHelper(board, myPosition, nextPosition, possibleMoves))
                break;
            row--;
            col--;
        }
        row = myPosition.getRow();
        col = myPosition.getColumn();
        while (col <= 8) {
            ChessPosition nextPosition = new ChessPosition(row, col);
            if (longMoveHelper(board, myPosition, nextPosition, possibleMoves))
                break;
            col++;
        }
        // move left
        col = myPosition.getColumn();
        while (col >= 1) {
            ChessPosition nextPosition = new ChessPosition(row, col);
            if (longMoveHelper(board, myPosition, nextPosition, possibleMoves))
                break;
            col--;
        }
        // move up
        col = myPosition.getColumn();
        while (row <= 8) {
            ChessPosition nextPosition = new ChessPosition(row, col);
            if (longMoveHelper(board, myPosition, nextPosition, possibleMoves))
                break;
            row++;
        }
        // move down
        row = myPosition.getRow();
        while (row >= 1) {
            ChessPosition nextPosition = new ChessPosition(row, col);
            if (longMoveHelper(board, myPosition, nextPosition, possibleMoves))
                break;
            row--;
        }
        return possibleMoves;
    }


    private Collection<ChessMove> bishopFindMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        // move top right
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        while (col <= 8  && row <= 8) {
            ChessPosition nextPosition = new ChessPosition(row, col);
            if (longMoveHelper(board, myPosition, nextPosition, possibleMoves))
                break;
            col++;
            row++;
        }
        // move top left
        row = myPosition.getRow();
        col = myPosition.getColumn();
        while (col >= 1 && row <= 8) {
            ChessPosition nextPosition = new ChessPosition(row, col);
            if (longMoveHelper(board, myPosition, nextPosition, possibleMoves))
                break;
            col--;
            row++;
        }
        // move bottom right
        row = myPosition.getRow();
        col = myPosition.getColumn();
        while (col <= 8 && row >= 1) {
            ChessPosition nextPosition = new ChessPosition(row, col);
            if (longMoveHelper(board, myPosition, nextPosition, possibleMoves))
                break;
            row--;
            col++;
        }
        // move bottom left
        row = myPosition.getRow();
        col = myPosition.getColumn();
        while (col >= 1 && row >= 1) {
            ChessPosition nextPosition = new ChessPosition(row, col);
            if (longMoveHelper(board, myPosition, nextPosition, possibleMoves))
                break;
            row--;
            col--;
        }

//        throw new RuntimeException("Not implemented");
        return possibleMoves;
    }
    private Collection<ChessMove> knightFindMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        int col = myPosition.getColumn();
        int row = myPosition.getRow();
        int[][] coordinateArray = {
                {row-1, col-2},
                {row+1, col-2},
                {row+2, col-1},
                {row+2, col+1},
                {row+1, col+2},
                {row-1, col+2},
                {row-2, col+1},
                {row-2, col-1}
        };
        for (int[] coordinate : coordinateArray) {
            row = coordinate[0];
            col = coordinate[1];
            if (row >8 || row <= 0 || col > 8 || col <= 0)
                continue;
            ChessPosition nextPosition = new ChessPosition(row, col);
            longMoveHelper(board, myPosition, nextPosition, possibleMoves);
        }
        return possibleMoves;
    }

    // Returns true if it needs to stop, returns false if it is to continue
    private boolean longMoveHelper(ChessBoard board, ChessPosition myPosition, ChessPosition nextPosition, ArrayList<ChessMove> possibleMoves) {
        // If the position in question is NOT the same as the one we're on, and the two positions have matching colors, then we can't move there. Or anywhere in that direction.
        if (!nextPosition.equals(myPosition) && board.getPiece(nextPosition) != null && board.getPiece(nextPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor())
            return true;
        else if (board.getPiece(nextPosition) != null && board.getPiece(nextPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
            ChessMove newMove = new ChessMove(myPosition, nextPosition, null);
            possibleMoves.add(newMove);
            return true;
        }
        else if (!nextPosition.equals(myPosition)){
            ChessMove newMove = new ChessMove(myPosition, nextPosition, null);
            possibleMoves.add(newMove);
        }
        return false;
    }
    private Collection<ChessMove> rookFindMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        // move right
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        while (col <= 8) {
            ChessPosition nextPosition = new ChessPosition(row, col);
            if (longMoveHelper(board, myPosition, nextPosition, possibleMoves))
                break;
            col++;
        }
        // move left
        col = myPosition.getColumn();
        while (col >= 1) {
            ChessPosition nextPosition = new ChessPosition(row, col);
            if (longMoveHelper(board, myPosition, nextPosition, possibleMoves))
                break;
            col--;
        }
        // move up
        col = myPosition.getColumn();
        while (row <= 8) {
            ChessPosition nextPosition = new ChessPosition(row, col);
            if (longMoveHelper(board, myPosition, nextPosition, possibleMoves))
                break;
            row++;
        }
        // move down
        row = myPosition.getRow();
        while (row >= 1) {
            ChessPosition nextPosition = new ChessPosition(row, col);
            if (longMoveHelper(board, myPosition, nextPosition, possibleMoves))
                break;
            row--;
        }
//        throw new RuntimeException("Not implemented");
        return possibleMoves;
    }

    private static void pawnMovementHelper(ChessPosition myPosition, ChessPosition nextPosition, ArrayList<ChessMove> possibleMoves, Boolean isWhite) {
        if ((nextPosition.getRow() == 8 && isWhite) || (nextPosition.getRow() == 1 && !isWhite)) {
            possibleMoves.add(new ChessMove(myPosition,nextPosition,PieceType.QUEEN));
            possibleMoves.add(new ChessMove(myPosition,nextPosition,PieceType.BISHOP));
            possibleMoves.add(new ChessMove(myPosition,nextPosition,PieceType.ROOK));
            possibleMoves.add(new ChessMove(myPosition,nextPosition,PieceType.KNIGHT));
        }
        else {
            possibleMoves.add(new ChessMove(myPosition,nextPosition,null));
        }
    }
    private Collection<ChessMove> pawnFindMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        int col = myPosition.getColumn();
        int row = myPosition.getRow();
        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
            // first we check if the front is clear
            ChessPosition frontPosition = new ChessPosition(row+1, col);
            if (board.getPiece(frontPosition) == null) {
                pawnMovementHelper(myPosition, frontPosition, possibleMoves, true);
                ChessPosition startJumpPosition = new ChessPosition(row+2, col);
                if (myPosition.getRow() == 2 && board.getPiece(startJumpPosition) == null) {
                    possibleMoves.add(new ChessMove(myPosition, startJumpPosition, null));
                }
            }
            ChessPosition topLeft = new ChessPosition(row+1, col-1);
            ChessPosition topRight = new ChessPosition(row+1, col+1);
            if (board.getPiece(topLeft) != null && board.getPiece(topLeft).getTeamColor() == ChessGame.TeamColor.BLACK) {
                pawnMovementHelper(myPosition, topLeft, possibleMoves, true);
            }
            if (board.getPiece(topRight) != null && board.getPiece(topRight).getTeamColor() == ChessGame.TeamColor.BLACK) {
                pawnMovementHelper(myPosition, topRight, possibleMoves, true);
            }
        }
        else {
            // first we check if the front is clear
            ChessPosition frontPosition = new ChessPosition(row-1, col);
            if (board.getPiece(frontPosition) == null) {
                pawnMovementHelper(myPosition, frontPosition, possibleMoves, false);
                ChessPosition startJumpPosition = new ChessPosition(row-2, col);
                if (myPosition.getRow() == 7 && board.getPiece(startJumpPosition) == null) {
                    possibleMoves.add(new ChessMove(myPosition, startJumpPosition, null));
                }
            }
            ChessPosition bottomLeft = new ChessPosition(row-1, col-1);
            ChessPosition bottomRight = new ChessPosition(row-1, col+1);
            if (board.getPiece(bottomLeft) != null && board.getPiece(bottomLeft).getTeamColor() == ChessGame.TeamColor.WHITE) {
                pawnMovementHelper(myPosition, bottomLeft, possibleMoves, false);
            }
            if (board.getPiece(bottomRight) != null && board.getPiece(bottomRight).getTeamColor() == ChessGame.TeamColor.WHITE) {
                pawnMovementHelper(myPosition, bottomRight, possibleMoves, false);
            }
        }
        return possibleMoves;
    }


}
