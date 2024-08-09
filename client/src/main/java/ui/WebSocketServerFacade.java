package ui;

public class WebSocketServerFacade {
    String url;
    WebSocketClient client;
    public WebSocketServerFacade(String url, WebSocketClient client) {
        this.url = url;
        this.client = client;
    }

    // TODO: You need to set this up like the other server facade, where error catching logic handles what needs to get done.
    /**
     * Allows the user to input the piece for which they want to highlight legal moves.
     * The selected piece’s current square and all squares it can legally move to are highlighted.
     * This is a local operation and has no effect on remote users’ screens.
     */
    private void highlightLegalMoves() {
        throw new RuntimeException("Not yet implemented.");
    }

    /**
     * Prompts the user to confirm they want to resign. If they do, the user forfeits the game and the game is over.
     * Does not cause the user to leave the game.
     */
    private void resign() {
        throw new RuntimeException("Not yet implemented.");
    }

    /**
     * Allow the user to input what move they want to make. The board is updated to reflect the result of the move,
     * and the board automatically updates on all clients involved in the game.
     */
    private void makeMove() {
        throw new RuntimeException("Not yet implemented.");
    }

    /**
     * Removes the user from the game (whether they are playing or observing the game). The client transitions back to the Post-Login UI.
     */
    private void leave() {
        throw new RuntimeException("Not yet implemented.");
    }

    /**
     * Redraws the chess board upon the user’s request.
     */
    private void redrawBoard() {
        throw new RuntimeException("Not yet implemented.");
    }
}
