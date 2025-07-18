package player;
import game.*;
import boardUI.*;

import java.util.Arrays;

@SuppressWarnings("unused")
public class MyStockfish extends ChessPlayer{
    private static int dep = 6;

    private static int safetyValues[] = {
            1, 2, 3, 4, 5, 6, 8, 9, 10, 12,
            14, 16, 18, 20, 23, 26, 29, 33, 36, 40,
            45, 50, 55, 60, 66, 72, 79, 86, 94, 102,
            111, 120, 130, 140, 151, 162, 175, 187, 201, 214,
            229, 244, 260, 277, 295, 313, 332, 352, 372, 395,
            415, 435, 452, 465, 475, 483, 488, 492, 495, 497,
            498, 499, 499, 500, 500, 500, 500, 500, 500, 500,
            500, 500, 500, 500, 500, 500, 500, 500, 500, 500,
            500, 500, 500, 500, 500, 500, 500, 500, 500, 500,
            500, 500, 500, 500, 500, 500, 500, 500, 500, 500,
    }; // Modeled after my desmos graph

    private static int[][] whiteKingZones = new int[][] {
            new int[]{16, 17, 24, 25, 32, 33, },
            new int[]{16, 17, 18, 24, 25, 26, 32, 33, 34, },
            new int[]{17, 18, 19, 25, 26, 27, 33, 34, 35, },
            new int[]{18, 19, 20, 26, 27, 28, 34, 35, 36, },
            new int[]{19, 20, 21, 27, 28, 29, 35, 36, 37, },
            new int[]{20, 21, 22, 28, 29, 30, 36, 37, 38, },
            new int[]{21, 22, 23, 29, 30, 31, 37, 38, 39, },
            new int[]{22, 23, 30, 31, 38, 39, },
            new int[]{24, 25, 32, 33, 40, 41, },
            new int[]{24, 25, 26, 32, 33, 34, 40, 41, 42, },
            new int[]{25, 26, 27, 33, 34, 35, 41, 42, 43, },
            new int[]{26, 27, 28, 34, 35, 36, 42, 43, 44, },
            new int[]{27, 28, 29, 35, 36, 37, 43, 44, 45, },
            new int[]{28, 29, 30, 36, 37, 38, 44, 45, 46, },
            new int[]{29, 30, 31, 37, 38, 39, 45, 46, 47, },
            new int[]{30, 31, 38, 39, 46, 47, },
            new int[]{32, 33, 40, 41, 48, 49, },
            new int[]{32, 33, 34, 40, 41, 42, 48, 49, 50, },
            new int[]{33, 34, 35, 41, 42, 43, 49, 50, 51, },
            new int[]{34, 35, 36, 42, 43, 44, 50, 51, 52, },
            new int[]{35, 36, 37, 43, 44, 45, 51, 52, 53, },
            new int[]{36, 37, 38, 44, 45, 46, 52, 53, 54, },
            new int[]{37, 38, 39, 45, 46, 47, 53, 54, 55, },
            new int[]{38, 39, 46, 47, 54, 55, },
            new int[]{40, 41, 48, 49, 56, 57, },
            new int[]{40, 41, 42, 48, 49, 50, 56, 57, 58, },
            new int[]{41, 42, 43, 49, 50, 51, 57, 58, 59, },
            new int[]{42, 43, 44, 50, 51, 52, 58, 59, 60, },
            new int[]{43, 44, 45, 51, 52, 53, 59, 60, 61, },
            new int[]{44, 45, 46, 52, 53, 54, 60, 61, 62, },
            new int[]{45, 46, 47, 53, 54, 55, 61, 62, 63, },
            new int[]{46, 47, 54, 55, 62, 63, },
    };
    private static int[][] blackKingZones = new int[][] {
            new int[]{0, 1, 8, 9, 16, 17, },
            new int[]{0, 1, 2, 8, 9, 10, 16, 17, 18, },
            new int[]{1, 2, 3, 9, 10, 11, 17, 18, 19, },
            new int[]{2, 3, 4, 10, 11, 12, 18, 19, 20, },
            new int[]{3, 4, 5, 11, 12, 13, 19, 20, 21, },
            new int[]{4, 5, 6, 12, 13, 14, 20, 21, 22, },
            new int[]{5, 6, 7, 13, 14, 15, 21, 22, 23, },
            new int[]{6, 7, 14, 15, 22, 23, },
            new int[]{8, 9, 16, 17, 24, 25, },
            new int[]{8, 9, 10, 16, 17, 18, 24, 25, 26, },
            new int[]{9, 10, 11, 17, 18, 19, 25, 26, 27, },
            new int[]{10, 11, 12, 18, 19, 20, 26, 27, 28, },
            new int[]{11, 12, 13, 19, 20, 21, 27, 28, 29, },
            new int[]{12, 13, 14, 20, 21, 22, 28, 29, 30, },
            new int[]{13, 14, 15, 21, 22, 23, 29, 30, 31, },
            new int[]{14, 15, 22, 23, 30, 31, },
            new int[]{16, 17, 24, 25, 32, 33, },
            new int[]{16, 17, 18, 24, 25, 26, 32, 33, 34, },
            new int[]{17, 18, 19, 25, 26, 27, 33, 34, 35, },
            new int[]{18, 19, 20, 26, 27, 28, 34, 35, 36, },
            new int[]{19, 20, 21, 27, 28, 29, 35, 36, 37, },
            new int[]{20, 21, 22, 28, 29, 30, 36, 37, 38, },
            new int[]{21, 22, 23, 29, 30, 31, 37, 38, 39, },
            new int[]{22, 23, 30, 31, 38, 39, },
            new int[]{24, 25, 32, 33, 40, 41, },
            new int[]{24, 25, 26, 32, 33, 34, 40, 41, 42, },
            new int[]{25, 26, 27, 33, 34, 35, 41, 42, 43, },
            new int[]{26, 27, 28, 34, 35, 36, 42, 43, 44, },
            new int[]{27, 28, 29, 35, 36, 37, 43, 44, 45, },
            new int[]{28, 29, 30, 36, 37, 38, 44, 45, 46, },
            new int[]{29, 30, 31, 37, 38, 39, 45, 46, 47, },
            new int[]{30, 31, 38, 39, 46, 47, },
    };

    private long nodeCount = 0;
    private int[][] moveList = new int[dep + 1][256];
    private int[] opponentMoveList = new int[256];
    private long[][] toBeRead = new long[dep + 1][256];

    public MyStockfish() {
    }

    public String getName() {
        return "MyStockfish";
    }

    public int findMove(ChessBoard b, int lastMove) {

        ChessBoard bord = new ChessBoard(b);

        nodeCount = 0;
        long n = System.nanoTime();
        long moveval = pvs(bord, lastMove, dep, getSide(), Integer.MIN_VALUE + 1, Integer.MAX_VALUE);
        System.out.println((int) (moveval));
        System.out.println("I spent " + (System.nanoTime() - n) / nodeCount + " nanoseconds per node on " + nodeCount + " nodes");
        return (int) (moveval >>> 32);
    }

    // CODE DOES NOT WORK, I HAVE ALTERED THE MOVE LIST AND NOW THIS WILL BREAK, PLEASE DO NOT RUN THIS
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
        b.nGetMoves(white, croist, moveList[depth]);
        if (depth == 0 || moveList[depth][0] == 1 || moveList[depth][0] == 2) {
            return evaluate(b, getSide(), moveList[depth], depth);
        }
        else {
            byte thinngngn = b.getPieceStates();
            if (max) {
                val = Integer.MIN_VALUE;
                for (int i = moveList.length - 1; i > -1; i--) {
                    b.move(moveList[depth][i]);
                    long movevalue = alphabeta(b, moveList[depth][i], false, depth - 1, !white, alpha, beta);
                    b.unmove(moveList[depth][i], thinngngn);
                    if ((int) movevalue > val) {
                        val = (int) movevalue;
                        best = moveList[depth][i];
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
                    b.move(moveList[depth][i]);
                    long movevalue = alphabeta(b, moveList[depth][i], true, depth - 1, !white, alpha, beta);
                    b.unmove(moveList[depth][i], thinngngn);
                    if ((int) movevalue < val) {
                        val = (int) movevalue;
                        best = moveList[depth][i];
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

        b.nGetMoves(white, croist, moveList[depth]);
        if (moveList[depth][1] == 2) {
            moveList[depth][0] = moveList[depth][1] = 0;
            return -100000L * (depth + 1);
        }
        if (moveList[depth][1] == 1) {
            moveList[depth][0] = moveList[depth][1] = 0;
            return 0;
        }
        if (depth == 0) {
            return ((long) (evaluate(b, white, moveList[depth], depth))) & 0x00000000ffffffffL;
        }

        for (int i = 1; i <= moveList[depth][0]; i++) {
            toBeRead[depth][i - 1] = evaluateMoves(moveList[depth][i], (lastMove << 20) >>> 29) | (((long) moveList[depth][i]) & 0x00000000ffffffffL);
        }
        Arrays.sort(toBeRead[depth]); // sorted in ascending order

        boolean first = true;
        for (int i = 255; i > 255 - moveList[depth][0]; i--) {
            int thisMove = (int) (toBeRead[depth][i] & (0xffffffffL));
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
                Arrays.fill(moveList[depth], 0);
                Arrays.fill(toBeRead[depth], 0L);
                return beta;
            }
        }

        Arrays.fill(moveList[depth], 0);
        Arrays.fill(toBeRead[depth], 0L);

        return ((long) best) << 32 | (((long) val) & 0xffffffffL);
    }

    private int evaluate(ChessBoard b, boolean white, int[] possibleMoves, int depth) {
        long[] boardd = b.getBoard();

        if (possibleMoves[1] == 2) {
            Arrays.fill(possibleMoves, 0);
            return (white == getSide() ? 1 : -1) * -100000 * (depth + 1);
        }
        if (possibleMoves[1] == 1) {
            Arrays.fill(possibleMoves, 0);
            return 0;
        }

        b.nGetMoves(!white, -1, opponentMoveList);
        if (opponentMoveList[1] == 2) {
            Arrays.fill(possibleMoves, 0);
            opponentMoveList[0] = opponentMoveList[1] = 0;
            return (white == getSide() ? 1 : -1) * 100000 * (depth + 1);
        }

        int eval = 100 * (Long.bitCount(boardd[0]) - Long.bitCount(boardd[1]) +
                Long.bitCount(boardd[2]) * 3 - Long.bitCount(boardd[3]) * 3 +
                Long.bitCount(boardd[4]) * 3 - Long.bitCount(boardd[5]) * 3 +
                Long.bitCount(boardd[6]) * 5 - Long.bitCount(boardd[7]) * 5 +
                Long.bitCount(boardd[8]) * 9 - Long.bitCount(boardd[9]) * 9);

        int whiteKing = Long.numberOfLeadingZeros(boardd[white ? 10 : 11]);
        int blackKing = Long.numberOfLeadingZeros(boardd[white ? 11 : 10]);

        int whiteSafety = 0;
        int blackSafety = 0;

        if (blackKing < 32) {
            int curPiece = -1;
            int[] myMoveList = (white ? possibleMoves : opponentMoveList);
            for (int i = 1; i <= myMoveList[0]; i++) {
                int myMove = myMoveList[i];
                int from = myMove >>> 26;
                int to = (myMove << 6) >>> 26;
                int type = (myMove << 9) >>> 29;
                if (from != curPiece) {
                    if (Arrays.binarySearch(blackKingZones[blackKing], to) > -1) {
                        if (type == 1 || type == 2) {
                            blackSafety += 2;
                        }
                        else if (type == 3) {
                            blackSafety += 3;
                        }
                        else if (type == 4) {
                            blackSafety += 5;
                        }
                        curPiece = from;
                    }
                }
            }
        }

        if (whiteKing >= 32) {
            int curPiece = -1;
            int[] myMoveList = (!white ? possibleMoves : opponentMoveList);
            for (int i = 1; i <= myMoveList[0]; i++) {
                int myMove = myMoveList[i];
                int from = myMove >>> 26;
                int to = (myMove << 6) >>> 26;
                int type = (myMove << 9) >>> 29;
                if (from != curPiece) {
                    if (Arrays.binarySearch(whiteKingZones[whiteKing - 32], to) > -1) {
                        if (type == 1 || type == 2) {
                            whiteSafety += 2;
                        }
                        else if (type == 3) {
                            whiteSafety += 3;
                        }
                        else if (type == 4) {
                            whiteSafety += 5;
                        }
                        curPiece = from;
                    }
                }
            }
        }

        eval += safetyValues[blackSafety];
        eval -= safetyValues[whiteSafety];

        int size1 = possibleMoves[0];
        int size2 = opponentMoveList[0];

        Arrays.fill(possibleMoves, 0);
        Arrays.fill(opponentMoveList, 0);

        return (getSide() ? eval : -eval) + (size1) - (size2);
    }

    private long evaluateMoves(int move, int capture) {
        int from = move >>> 26;
        int to = (move << 6) >>> 26;
        int piece = (move << 12) >>> 29;
        int side = (move << 15) >>> 31;
        int promotion = (move << 16) >>> 29;
        int ep = (move << 19) >>> 31;
        int eaten_piece = (move << 20) >>> 29;

        long value = 0;
        if (capture != 5) {
            if (eaten_piece != 5) {
                value += (long) (100) * (eaten_piece - capture);
            }
        }
        if (promotion > 0) {
            value += (10) * promotion;
        }

        value += 10000;

        // evaluations will leave 32 bits of empty space at the end
        return value << 32;
    }
}

