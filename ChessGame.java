public class ChessGame {
    private ChessPlayer white;
    private ChessPlayer black;
    private TimeControl timeControlA;
    private TimeControl timeControlB;
    private int turn;
    private int last;
    private int result;
    private int winner;
    private ChessBoard board;

    public ChessGame(ChessPlayer w, ChessPlayer b) {
        white = w;
        black = b;
        timeControlA = timeControlB = new TimeControl();
        turn = 0;
        result = 0;
        winner = 0;
        last = 100;
        board = new ChessBoard();
    }

    public ChessGame(ChessPlayer w, ChessPlayer b, TimeControl tc) {
        white = w;
        black = b;
        timeControlA = timeControlB = tc;
        turn = 0;
        result = 0;
        winner = 0;
        last = 100;
        board = new ChessBoard();
    }

    public ChessGame(ChessPlayer w, ChessPlayer b, TimeControl tca, TimeControl tcb) {
        white = w;
        black = b;
        timeControlA = tca;
        timeControlB = tcb;
        turn = 0;
        result = 0;
        winner = 0;
        last = 100;
        board = new ChessBoard();
    }

    public int move() {
        int move;

        if (turn == 0) {
            timeControlA.start();
            move = white.findMove(board);
            timeControlA.end();
        }
        else if (turn % 2 == 0) {
            timeControlA.start();
            move = white.findMove(board, last);
            timeControlA.end();
        } else {
            timeControlB.start();
            move = black.findMove(board, last);
            timeControlB.end();
        }

        board.move(move);
        turn++;
        last = move;

        result = board.gameState(move);
        winner = result * ((turn % 2 == 0) ? 1 : -1);
        return result;
    }

    public ChessBoard getBoard() {
        return board;
    }

    public int getResult() {
        return winner;
    }
}
