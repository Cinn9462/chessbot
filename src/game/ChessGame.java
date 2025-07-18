package game;
import player.*;
import boardUI.*;

import javax.swing.Timer;

@SuppressWarnings("unused")
public class ChessGame {
    private ChessPlayer white;
    private ChessPlayer black;

    private TimeControl whiteTime;
    private TimeControl blackTime;

    private int lastMove = 100;

    private int turn = 0; // even is white, odd is black

    private int gameStatus = 0; // -1 is win/los, 0 is stalemate, 1 is ongoing
    private int winner = 0;
    
    private ChessBoard board = new ChessBoard();

    private Timer timer;

    /**
     * Create a game with unlimited time.
     */
    public ChessGame(ChessPlayer white, ChessPlayer black) {
        this.white = white;
        this.black = black;

        white.setSide(true);
        black.setSide(false);

        whiteTime = new TimeControl();
        blackTime = new TimeControl();
    }
        
    /**
     * Create a game with both sides given equal time. Time controls are in seconds.
     */
    public ChessGame(ChessPlayer white, ChessPlayer black, long time, long delay, long increment) {
        this.white = white;
        this.black = black;

        white.setSide(true);
        black.setSide(false);

        whiteTime = new TimeControl(time, delay, increment);
        blackTime = new TimeControl(time, delay, increment);
    }

    /**
     * Create a game with both sides given equal time. Time controls are in seconds.
     */
    public ChessGame(ChessPlayer white, ChessPlayer black, long time) {
        this.white = white;
        this.black = black;

        white.setSide(true);
        black.setSide(false);

        whiteTime = new TimeControl(time, 0, 0);
        blackTime = new TimeControl(time, 0, 0);
    }
    
    /**
     * Create a game with both sides given unequal time. Time controls are in seconds.
     */
    public ChessGame(ChessPlayer white, ChessPlayer black, long white_time, long black_time) {
        this.white = white;
        this.black = black;

        white.setSide(true);
        black.setSide(false);

        this.whiteTime = new TimeControl(white_time, 0, 0);
        this.blackTime = new TimeControl(black_time, 0, 0);
    }

    /**
     * Create a game with both sides given unequal time. Time controls are in seconds.
     */
    public ChessGame(ChessPlayer white, ChessPlayer black, long white_time, long white_delay, long white_increment, long black_time, long black_delay, long black_increment) {
        this.white = white;
        this.black = black;

        white.setSide(true);
        black.setSide(false);

        this.whiteTime = new TimeControl(white_time, white_delay, white_increment);
        this.blackTime = new TimeControl(black_time, black_delay, black_increment);
    }

    public int move() {

        boolean whiteTurn = getTurn();
        TimeControl currentTimeControl = (whiteTurn) ? whiteTime: blackTime;

        timer =  new Timer(50, e -> {
            currentTimeControl.updateClock(50);
        });

        timer.start();

        int move = (whiteTurn) ? white.findMove(board, lastMove) : black.findMove(board, lastMove);

        timer.stop();
        board.move(move);
        turn++;
        lastMove = move;

        gameStatus = board.gameState(move); // checks if last move made resulted in checkmate
        winner = gameStatus * ((turn % 2 == 0) ? 1 : -1); // if it is checkmate, the current player lost because turn is updated
        
        if (currentTimeControl.getTime() == 0) {
            System.out.println("Player has run out of time");
            winner = whiteTurn ? -1 : 1;
            return -1;
        }

        return gameStatus;
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

    public ChessPlayer getWhite() {
        return white;
    }

    public ChessPlayer getBlack() {
        return black;
    }

    public TimeControl getBlackTimeControl() {
        return blackTime;
    }
    
    public TimeControl getWhiteTimeControl() {
        return whiteTime;
    }

    /**
     * @return true/false. True is white's turn, false is black's turn.
     */
    public boolean getTurn() {
        return (turn % 2 == 0);
    }

    public String getWhiteName() {
        return white.getName();

    }

    public String getBlackName() {
        return black.getName();
    }
}
