import java.util.Scanner;

public class Human extends ChessPlayer {

    public Human(boolean s) {
        super(s);
    }

    public int findMove(ChessBoard b) {
        boolean onPiece = false;
        int[] square = new int[]{-1, -1};
        int type = -1;
        int curClicks = ChessPanel.clicks;

        int[] legalMoves = b.nGetMoves(getSide(), -1);

        big:
        while (true) {
            if (ChessPanel.clicks > curClicks) {
                int[] loc = ChessPanel.mouseClick;
                if (loc[0] < 0 || loc[1] < 0) {
                    onPiece = false;
                    continue;
                }
                long[] boardd = b.getBoard();
                if (onPiece) {
                    if (square[0] == loc[0] && square[1] == loc[1]) {
                        onPiece = false;
                        continue;
                    }
                    for (int i = 0; i < boardd.length; i++) {
                        if (((0x8000000000000000L >>> (loc[0] + loc[1] * 8)) & boardd[i]) != 0) {
                            if ((i % 2 == 0 && getSide()) || (i % 2 == 1 && !getSide())) {
                                square = loc;
                                type = i / 2;
                                continue big;
                            }
                        }
                    }

                    int f = square[0] + square[1] * 8;
                    int t = loc[0] + loc[1] * 8;
                    int ty = type;
                    int s = getSide() ? 1 : 0;
                    int p = 0;

                    if (ty == 0 && (loc[1] == 7 - (7 * s))) {
                        Scanner scan = new Scanner(System.in);
                        String typeOfProm = scan.nextLine();
                        if (typeOfProm.equalsIgnoreCase("knight")) {
                            p = 1;
                        }
                        else if (typeOfProm.equalsIgnoreCase("bishop")) {
                            p = 2;
                        }
                        else if (typeOfProm.equalsIgnoreCase("rook")) {
                            p = 3;
                        }
                        else {
                            p = 4;
                        }
                    }
                    int move = f << 26 | t << 20 | ty << 17 | s << 16 | p << 13 | 5 << 9;
                    for (int m : legalMoves) {
                        if (move == m) {
                            return move;
                        }
                    }
                }
                else {
                    for (int i = 0; i < boardd.length; i++) {
                        if (((0x8000000000000000L >>> (loc[0] + loc[1] * 8)) & boardd[i]) != 0) {
                            if ((i % 2 == 0 && getSide()) || (i % 2 == 1 && !getSide())) {
                                square = loc;
                                type = i / 2;
                                onPiece = true;
                            }
                        }
                    }
                }
                curClicks++;
            }
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int findMove(ChessBoard b, int lastMove) {
        boolean onPiece = false;
        int[] square = new int[]{-1, -1};
        int type = -1;
        int otherType = 5;
        int curClicks = ChessPanel.clicks;

        int from = lastMove >>> 26;
        int to = (lastMove << 6) >>> 26;
        int piece = (lastMove << 12) >>> 29;

        int croist = -1;
        if (piece == 0) {
            if (to - from == 16 || from - to == 16) {
                croist = to % 8;
            }
        }

        int[] legalMoves = b.nGetMoves(getSide(), croist);

        big:
        while (true) {
            if (ChessPanel.clicks > curClicks) {
                int[] loc = ChessPanel.mouseClick;
                if (loc[0] < 0 || loc[1] < 0) {
                    onPiece = false;
                    otherType = 5;
                    continue;
                }
                long[] boardd = b.getBoard();
                if (onPiece) {
                    if (square[0] == loc[0] && square[1] == loc[1]) {
                        onPiece = false;
                        otherType = 5;
                        continue;
                    }
                    for (int i = 0; i < boardd.length; i++) {
                        if (((0x8000000000000000L >>> (loc[0] + loc[1] * 8)) & boardd[i]) != 0) {
                            if ((i % 2 == 0 && getSide()) || (i % 2 == 1 && !getSide())) {
                                square = loc;
                                type = i / 2;
                                otherType = 5;
                                continue big;
                            }
                            otherType = i / 2;
                        }
                    }

                    int f = square[0] + square[1] * 8;
                    int t = loc[0] + loc[1] * 8;
                    int ty = type;
                    int s = getSide() ? 1 : 0;
                    int p = 0;
                    int e = 0;
                    int c = otherType;
                    if (ty == 0) {
                        if (croist > -1) {
                            if (f - to == 1 || f - to == -1) {
                                if (getSide()) {
                                    if (to - t == 8) {
                                        e = 1;
                                        c = 0;
                                    }
                                } else {
                                    if (t - to == 8) {
                                        e = 1;
                                        c = 0;
                                    }
                                }
                            }
                        }
                    }
                    if (ty == 0 && (loc[1] == 7 - (7 * s))) {
                        Scanner scan = new Scanner(System.in);
                        String typeOfProm = scan.nextLine();
                        if (typeOfProm.equalsIgnoreCase("knight")) {
                            p = 1;
                        }
                        else if (typeOfProm.equalsIgnoreCase("bishop")) {
                            p = 2;
                        }
                        else if (typeOfProm.equalsIgnoreCase("rook")) {
                            p = 3;
                        }
                        else {
                            p = 4;
                        }
                    }
                    int move = f << 26 | t << 20 | ty << 17 | s << 16 | p << 13 | e << 12 | c << 9;
                    for (int m : legalMoves) {
                        if (move == m) {
                            return move;
                        }
                    }
                }
                else {
                    for (int i = 0; i < boardd.length; i++) {
                        if (((0x8000000000000000L >>> (loc[0] + loc[1] * 8)) & boardd[i]) != 0) {
                            if ((i % 2 == 0 && getSide()) || (i % 2 == 1 && !getSide())) {
                                square = loc;
                                type = i / 2;
                                onPiece = true;
                                break;
                            }
                        }
                    }
                }
                curClicks++;
            }
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
