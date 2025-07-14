public class ChessPlayer {
    private boolean side;

    /**
     * 
     * @param side true/false. True is white, false is black.
     */
    public ChessPlayer(boolean side) {
        this.side = side;
    }

    /**
     * 
     * @param board Curent chessboard
     * @param lastMove Last move made (for determining en passant)
     * @return Move generated
     */
    public int findMove(ChessBoard board, int lastMove) {
        return 0;
    }

    public boolean getSide() {
        return side;
    }
}
