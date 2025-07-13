public class MyStockfish extends ChessPlayer{
    private long nodeCount = 0;

    public MyStockfish(boolean s) {
        super(s);
    }

    public int findMove(ChessBoard b) {

        ChessBoard bord = new ChessBoard(b);

        nodeCount = 0;
        long n = System.nanoTime();
        long moveval = (alphabeta(bord, 100, true, 6, getSide(), Integer.MIN_VALUE, Integer.MAX_VALUE));
        System.out.println((int) (moveval));
        System.out.println("I spent " + (System.nanoTime() - n) / nodeCount + " nanoseconds per node on " + nodeCount + " nodes");
        return (int) (moveval >>> 32);
    }

    public int findMove(ChessBoard b, int lastMove) {

        ChessBoard bord = new ChessBoard(b);

        nodeCount = 0;
        long n = System.nanoTime();
        long moveval = (alphabeta(bord, lastMove, true, 6, getSide(), Integer.MIN_VALUE, Integer.MAX_VALUE));
        System.out.println((int) (moveval));
        System.out.println("I spent " + (System.nanoTime() - n) / nodeCount + " nanoseconds per node on " + nodeCount + " nodes");
        return (int) (moveval >>> 32);
    }

    private long alphabeta(ChessBoard b, int lastMove, boolean max, int depth, boolean white, int alpha, int beta) {
        int croist = -1;
        int best = -1;
        int val;
        nodeCount++;
        if (lastMove != 100) {
            int from = lastMove >>> 26;
            int to = (lastMove << 6) >>> 26;
            int piece = (lastMove << 12) >>> 29;

            if (piece == 0) {
                if (to - from == 16 || from - to == 16) {
                    croist = to % 8;
                }
            }
        }
        int[] moveList = b.nGetMoves(white, croist);
        if (depth == 0 || moveList[0] == 1 || moveList[0] == 2) {
            return evaluate(b, getSide());
        }
        else {
            byte thinngngn = b.getPieceStates();
            if (max) {
                val = Integer.MIN_VALUE;
                for (int i = moveList.length - 1; i > -1; i--) {
                    b.move(moveList[i]);
                    long movevalue = alphabeta(b, moveList[i], false, depth - 1, !white, alpha, beta);
                    b.unmove(moveList[i], thinngngn);
                    if ((int) movevalue > val) {
                        val = (int) movevalue;
                        best = moveList[i];
                    }
                    if (val > beta) {
                        break;
                    }
                    alpha = Math.max(alpha, val);
                }
            }
            else {
                val = Integer.MAX_VALUE;
                for (int i = moveList.length - 1; i > -1; i--) {
                    b.move(moveList[i]);
                    long movevalue = alphabeta(b, moveList[i], true, depth - 1, !white, alpha, beta);
                    b.unmove(moveList[i], thinngngn);
                    if ((int) movevalue < val) {
                        val = (int) movevalue;
                        best = moveList[i];
                    }
                    if (alpha > val) {
                        break;
                    }
                    beta = Math.min(beta, val);
                }
            }
        }

        return (((long) best) << 32) | ((long) val & 0x00000000ffffffffL);
    }

    private int evaluate(ChessBoard b, boolean white) {
        long[] boardd = b.getBoard();
        int[] moves = b.nGetMoves(white, -1);
        int multi = ((white && getSide()) || (!white && !getSide())) ? 1 : -1;

        if (moves[0] == 2) {
            return multi * -(100000);
        }
        if (moves[0] == 1) {
            return 0;
        }

        int eval = Long.bitCount(boardd[0]) - Long.bitCount(boardd[1]) +
                Long.bitCount(boardd[2]) * 3 - Long.bitCount(boardd[3]) * 3 +
                Long.bitCount(boardd[4]) * 4 - Long.bitCount(boardd[5]) * 4 +
                Long.bitCount(boardd[6]) * 5 - Long.bitCount(boardd[7]) * 5 +
                Long.bitCount(boardd[8]) * 9 - Long.bitCount(boardd[9]) * 9;

        eval *= 100;

        return (getSide() ? eval : -eval) + (moves.length - 1);
    }

    private int moveOrdering(int move) {

    }
}

