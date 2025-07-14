public class MoveGenerationSpeedTest {
    
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        ChessBoard.makeEverything();
        ChessBoard board = new ChessBoard();
        
        long absStartTime = System.nanoTime();

        for (int i = 1; i < 6; i++) {
            long startTime = System.nanoTime();

            for (int n = 0; n < 10e5; n++) {
                int[] moves = board.nGetMoves(false, 100);
            }

            long endTime = System.nanoTime();

            System.out.println("Trial " + i + ": " + (endTime - startTime) / 10e5 + " nanoseconds per generation" );
        }

        long absEndTime = System.nanoTime();

        System.out.println("Average time: " + (absEndTime - absStartTime) / 50e5 + "nanoseconds");
    }
}
