public class ChessGame {
    private ChessPlayer white;
    private ChessPlayer black;

    private TimeControl white_time;
    private TimeControl black_time;

    private int turn = 0; // even is white, odd is black
    private int last_move = 100; // 100 is null move

    private int game_status = 0; // -1 is win/los, 0 is stalemate, 1 is ongoing
    private int winner = 0;
    
    private ChessBoard board = new ChessBoard();

    /**
     * Create a game with unlimited time.
     */
    public ChessGame(ChessPlayer white, ChessPlayer black) {
        this.white = white;
        this.black = black;
        white_time = new TimeControl();
        black_time = new TimeControl();
    }
        
    /**
     * Create a game with both sides given equal time. Time controls are in seconds.
     */
    public ChessGame(ChessPlayer white, ChessPlayer black, long time, long delay, long increment) {
        this.white = white;
        this.black = black;
        white_time = new TimeControl(time, delay, increment);
        black_time = new TimeControl(time, delay, increment);
    }

    /**
     * Create a game with both sides given equal time. Time controls are in seconds.
     */
    public ChessGame(ChessPlayer white, ChessPlayer black, long time) {
        this.white = white;
        this.black = black;
        white_time = new TimeControl(time, 0, 0);
        black_time = new TimeControl(time, 0, 0);
    }
    
    /**
     * Create a game with both sides given unequal time. Time controls are in seconds.
     */
    public ChessGame(ChessPlayer white, ChessPlayer black, long white_time, long black_time) {
        this.white = white;
        this.black = black;
        this.white_time = new TimeControl(white_time, 0, 0);
        this.black_time = new TimeControl(black_time, 0, 0);
    }

    /**
     * Create a game with both sides given unequal time. Time controls are in seconds.
     */
    public ChessGame(ChessPlayer white, ChessPlayer black, long white_time, long white_delay, long white_increment, long black_time, long black_delay, long black_increment) {
        this.white = white;
        this.black = black;
        this.white_time = new TimeControl(white_time, white_delay, white_increment);
        this.black_time = new TimeControl(black_time, black_delay, black_increment);
    }

    /**
     * @return Status of game. -1 is win/loss, 0 is stalemate, 1 is ongoing
     */
    public int move() {
        boolean white_turn = getTurn();

        int move;
        long startTime = System.currentTimeMillis();

        if (turn == 0) {
            move = white.findMove(board);
        } else if (white_turn) {
            move = white.findMove(board, last_move);
        } else {
            move = black.findMove(board, last_move);
        }
        
        long endTime = System.currentTimeMillis();

        if (white_turn) {
            if (!white_time.updateClock(endTime - startTime)) {
                winner = -1; // Black wins, white is out of time
                return -1; // Game over
            }
        } else {
            if (!black_time.updateClock(endTime - startTime)) {
            winner = 1; // White wins, black is out of time
            return -1; // Game over
           }
        }

        board.move(move);
        turn++;
        last_move = move;

        game_status = board.gameState(move); // checks if last move made resulted in checkmate
        winner = game_status * ((turn % 2 == 0) ? 1 : -1); // if it is checkmate, the current player lost because turn is updated

        return game_status;
    }

    public ChessBoard getBoard() {
        return board;
    }

    /**
     * @return -1 is black win, 0 is stalemate, 1 is white win
     */
    public int getWinner() {
        return winner;
    }

    public TimeControl getBlackTimeControl() {
        return black_time;
    }
    
    public TimeControl getWhiteTimeControl() {
        return white_time;
    }

    /**
     * @return true/false. True is white's turn, false is black's turn.
     */
    public boolean getTurn() {
        return (turn % 2 == 0);
    }
}
