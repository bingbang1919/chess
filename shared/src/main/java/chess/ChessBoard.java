package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */

    public void resetBoard() {
        squares = new ChessPiece[8][8];
        for (int i=0; i<8; i++) {
            squares[1][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            squares[6][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }
        for (int i=0; i<2; i++) {
            int row;
            ChessGame.TeamColor teamColor;
            if (i==0) {
                teamColor = ChessGame.TeamColor.WHITE;
                row = 0;
            }
            else {
                teamColor = ChessGame.TeamColor.BLACK;
                row = 7;
            }
            squares[row][0] = new ChessPiece(teamColor, ChessPiece.PieceType.ROOK);
            squares[row][1] = new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT);
            squares[row][2] = new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP);
            squares[row][3] = new ChessPiece(teamColor, ChessPiece.PieceType.QUEEN);
            squares[row][4] = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
            squares[row][5] = new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP);
            squares[row][6] = new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT);
            squares[row][7] = new ChessPiece(teamColor, ChessPiece.PieceType.ROOK);
        }
    }

//    public void compareSquares(ChessBoard otherBoard) {
//        for (int i = 0; i < 8; i++) {
//            for (int j = 0; j < 8; j++) {
//                ChessPiece piece = this.squares[i][j];
//                ChessPiece otherPiece = otherBoard.squares[i][j];
//                if (piece != null && !piece.equals(otherPiece)) {
//                    System.out.printf("The Coordinates failed at ", i, j);
//                }
//            }
//        }
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

}
