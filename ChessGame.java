public class ChessGame {
    private ChessPlayer white;
    private ChessPlayer black;

    private TimeControl white_time;
    private TimeControl black_time;

    private int turn = 0; // even is white, odd is black
    private int last_move = 100;

    private int result = 0;
    private int winner = 0;
    
    private ChessBoard board;

    public ChessGame(ChessPlayer w, ChessPlayer b) {
        white = w;
        black = b;
        white_time = black_time = new TimeControl();
        turn = 0;
        result = 0;
        winner = 0;
        last_move = 100;
        board = new ChessBoard();
    }

    public ChessGame(ChessPlayer w, ChessPlayer b, TimeControl tc) {
        white = w;
        black = b;
        white_time = black_time = tc;
        turn = 0;
        result = 0;
        winner = 0;
        last_move = 100;
        board = new ChessBoard();
    }

    public ChessGame(ChessPlayer w, ChessPlayer b, TimeControl tca, TimeControl tcb) {
        white = w;
        black = b;
        white_time = tca;
        black_time = tcb;
        turn = 0;
        result = 0;
        winner = 0;
        last_move = 100;
        board = new ChessBoard();
    }

    public int move() {
        int move;

        if (turn == 0) {
            white_time.start();
            move = white.findMove(board);
            white_time.end();
        }
        else if (turn % 2 == 0) {
            white_time.start();
            move = white.findMove(board, last_move);
            white_time.end();
        } else {
            black_time.start();
            move = black.findMove(board, last_move);
            black_time.end();
        }

        board.move(move);
        turn++;
        last_move = move;

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
