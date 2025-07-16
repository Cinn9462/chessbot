public class ChessPlayer {
    private boolean side;

    /**
     * @param side true/false. true is white, false is black.
     */
    public ChessPlayer(boolean side) {
        this.side = side;
    }

    /**
     * @param board Curent chessboard
     * @param lastMove Last move made (for determining en passant), 100 for none
     * @return Move generated
     */
    public int findMove(ChessBoard board, int lastMove) {
        return 0;
    }
    
    /**
     * @return Name of player (default "Player")
     */

    public String getName() {
        return "Player";
    }

    /**
     * @return Side of player
     */
    public boolean getSide() {
        return side;
    }
}
