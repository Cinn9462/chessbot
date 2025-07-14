import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class ChessGame {
    private ChessPlayer white;
    private ChessPlayer black;

    private TimeControl white_time;
    private TimeControl black_time;

    private int last_move = 100;

    private int turn = 0; // even is white, odd is black

    private int game_status = 0; // -1 is win/los, 0 is stalemate, 1 is ongoing
    private int winner = 0;
    
    private ChessBoard board = new ChessBoard();

    private Timer timer;

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
    public void move(ChessObserver obs) {
        boolean white_turn = getTurn();
        TimeControl currentTimeControl = (white_turn) ? white_time: black_time;

        timer =  new Timer(50, e -> {
            currentTimeControl.updateClock(50);
        });

        timer.start();

        new Thread(() -> {
            int move;
            if (turn == 0) {
                move = white.findMove(board);
            } else {
                move = (white_turn) ? white.findMove(board, last_move) : black.findMove(board, last_move);
            }

            SwingUtilities.invokeLater(() -> {
                timer.stop();
                board.move(move);
                turn++;
                last_move = move;

                game_status = board.gameState(move); // checks if last move made resulted in checkmate
                winner = game_status * ((turn % 2 == 0) ? 1 : -1); // if it is checkmate, the current player lost because turn is updated
                
                if (currentTimeControl.getTime() == 0) {
                    game_status = -1;
                    winner = white_turn ? -1 : 1;
                }

                obs.setGameStatus(game_status);

                obs.nextMove();
            });
        }).start();
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
