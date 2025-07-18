import javax.management.RuntimeErrorException;
import javax.swing.JOptionPane;

public class Human extends ChessPlayer {

    ChessPanel screen;

    public Human() {
    }

    public String getName() {
        return "Human";
    }

    /**
     * Add screen to human so human can recieve inputs
     * @param screen Screen to link with Human player
     */
    public void setScreen(ChessPanel screen) {
        this.screen = screen;
    }

    public int findMove(ChessBoard b, int lastMove) {
        int enPassantFile = (
            lastMove != 100 && // Last move exists
            ((lastMove << 12) >>> 29) == 0 &&  // Pawn moved
            Math.abs(((lastMove << 6) >>> 26) - (lastMove >>> 26)) == 16)  // Move was 2 squares up
            ? ((lastMove << 6) >>> 26) % 8 : -1; // Cut the file that the pawn moved in

        int[] legalMoves = new int[256];
        b.nGetMoves(getSide(), enPassantFile, legalMoves);
        
        long[] board = b.getBoard();
        long ourPieces = (this.getSide()) ? 
            (board[0] | board[2] | board[4] | board[6] | board[8] | board[10]) :
            (board [1] | board[3] | board[5] | board[7] | board[9] | board[11]);

        int from = -1;
        int to;
        
        while (true) {
            try {
                int[] click = screen.getNextClick();

                if ((0x8000000000000000L >>> (click[0] + click[1] * 8) & ourPieces) != 0) { // If piece selected is player's own, it is the new from piece
                
                    from = click[0] + click[1] * 8;
                
                } else if (from != -1) { // If a piece is selected, test if the to square is a valid move
                    
                    to = click[0] + click[1] * 8;

                    for (int move : legalMoves) {
                        if (move >>> 20 == (from << 6 | to)) { // If there is a move in the legal list that matches the from and to

                            
                            if ((move << 16) >>> 29 != 0) { // If move is a promotion move

                                String[] options = {"Knight", "Bishop", "Rook", "Queen"};
                                int choice = JOptionPane.showOptionDialog(
                                    null,
                                    "Choose a piece to promote to:",
                                    "Promotion",
                                    JOptionPane.DEFAULT_OPTION,
                                    JOptionPane.PLAIN_MESSAGE,
                                    null,
                                    options,
                                    options[3]
                                );

                                move = (move & ~(0b111 << 13) | (choice + 1) << 13); // Change promotion piece to user selection
                            }
                        return move;    
                        }
                    }
                }
            } catch (InterruptedException e){
                throw new RuntimeErrorException(null, "Error in findMove method of Human");
            }
        }
    }
}