public class ChessPlayer {
    private boolean side;

    public ChessPlayer() {
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
     * @param side true/false. true is white, false is black.
     */

    public void setSide(boolean side) {
        this.side = side;
    }

    /**
     * @return Side of player
     */
    public boolean getSide() {
        return side;
    }

    /**
     * Set ChessPanel for Human player
     */
    public void setScreen(ChessPanel screen) {

    }
}
