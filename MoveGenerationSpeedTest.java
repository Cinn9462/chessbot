/* 
 * Test the speed of the Move generation method, by running five trials of
 * a million simulations of the move generation method and printing the
 * average time in nanoseconds
 */

public class MoveGenerationSpeedTest {
    
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        ChessBoard board = new ChessBoard();
        
        long absStartTime = System.nanoTime();

        for (int i = 1; i < 6; i++) {
            long startTime = System.nanoTime();

            int[] moves = new int[256];
            for (int n = 0; n < 1e6; n++) {
                board.nGetMoves(false, 100, moves);
            }

            long endTime = System.nanoTime();

            System.out.println("Trial " + i + ": " + (endTime - startTime) / 10e5 + " nanoseconds per generation" );
        }

        long absEndTime = System.nanoTime();

        System.out.println("Average time: " + (absEndTime - absStartTime) / 50e5 + " nanoseconds");
    }
}
