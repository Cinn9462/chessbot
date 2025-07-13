import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class MyStockfish extends ChessPlayer{
    private long nodeCount = 0;

    public MyStockfish(boolean s) {
        super(s);
    }

    public int findMove(ChessBoard b) {

        ChessBoard bord = new ChessBoard(b);

        nodeCount = 0;
        long n = System.nanoTime();
        long moveval = pvs(bord, 100, 6, getSide(), Integer.MIN_VALUE + 1, Integer.MAX_VALUE);
        System.out.println((int) (moveval));
        System.out.println("I spent " + (System.nanoTime() - n) / nodeCount + " nanoseconds per node on " + nodeCount + " nodes");
        return (int) (moveval >>> 32);
    }

    public int findMove(ChessBoard b, int lastMove) {

        ChessBoard bord = new ChessBoard(b);

        nodeCount = 0;
        long n = System.nanoTime();
        long moveval = pvs(bord, lastMove, 6, getSide(), Integer.MIN_VALUE + 1, Integer.MAX_VALUE);
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
            return evaluate(b, getSide(), moveList);
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

    private long pvs(ChessBoard b, int lastMove, int depth, boolean white, int alpha, int beta) {
        int croist = -1;

        // croist is the croissant variable passed into nGetMoves()
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

        // best is the best move, and val is the value of this node
        int val = 0;
        int best = 0;

        nodeCount++;

        int[] moveList = b.nGetMoves(white, croist);
        if (depth == 0 || moveList[0] == 1 || moveList[0] == 2) {
            return ((long) evaluate(b, white, moveList)) & 0x00000000ffffffffL;
        }

        long[] toBeRead = new long[moveList.length];
        for (int i = 0; i < moveList.length; i++) {
            toBeRead[i] = evaluateMoves(moveList[i]) | (((long) moveList[i]) & 0x00000000ffffffffL);
        }
        Arrays.sort(toBeRead); // sorted in ascending order

        boolean first = true;
        for (int i = toBeRead.length - 1; i > - 1; i--) {
            int thisMove = (int) (toBeRead[i] & (0xffffffffL));
            byte states = b.getPieceStates();
            b.move(thisMove);
            if (first) {
                val = -(int) (pvs(b, thisMove,depth - 1, !white, -beta, -alpha) & 0xffffffffL);

                first = false;
            }
            else {
                val = -(int) (pvs(b, thisMove,depth - 1, !white, -alpha - 1, -alpha) & 0xffffffffL);

                if (val > alpha && val < beta) {
                    val = -(int) (pvs(b, thisMove,depth - 1, !white, -beta, -val) & 0xffffffffL);
                }
            }
            b.unmove(thisMove, states);

            if (val > alpha) {
                alpha = val;
                best = thisMove;
            }

            if (val >= beta) {
                return beta;
            }
        }

        return ((long) best) << 32 | (((long) val) & 0xffffffffL);
    }

    private int evaluate(ChessBoard b, boolean white, int[] possibleMoves) {
        long[] boardd = b.getBoard();
        int multi = ((white && getSide()) || (!white && !getSide())) ? 1 : -1;

        if (possibleMoves[0] == 2) {
            return multi * -(100000);
        }
        if (possibleMoves[0] == 1) {
            return 0;
        }

        int eval = Long.bitCount(boardd[0]) - Long.bitCount(boardd[1]) +
                Long.bitCount(boardd[2]) * 3 - Long.bitCount(boardd[3]) * 3 +
                Long.bitCount(boardd[4]) * 4 - Long.bitCount(boardd[5]) * 4 +
                Long.bitCount(boardd[6]) * 5 - Long.bitCount(boardd[7]) * 5 +
                Long.bitCount(boardd[8]) * 9 - Long.bitCount(boardd[9]) * 9;

        eval *= 100;

        return (getSide() ? eval : -eval) + (possibleMoves.length);
    }

    private long evaluateMoves(int move) {
        int from = move >>> 26;
        int to = (move << 6) >>> 26;
        int piece = (move << 12) >>> 29;
        int side = (move << 15) >>> 31;
        int promotion = (move << 16) >>> 29;
        int ep = (move << 19) >>> 31;
        int eaten_piece = (move << 20) >>> 29;

        long value = 0;
        if (eaten_piece != 5) {
            value += (100) * (eaten_piece - piece);
        }
        if (promotion > 0) {
            value += (10) * promotion;
        }

        value += 10000;

        // evaluations will leave 32 bits of empty space at the end
        return value << 32;
    }
}

