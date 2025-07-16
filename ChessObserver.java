import javax.management.RuntimeErrorException;
import javax.swing.Timer;

public class ChessObserver {
    private ChessGame game;
    private ChessFrame frame;
    private ChessPanel screen;
    private boolean watchable;

    private Timer refreshTimer;

    private int gameStatus = 1; // -1 is win/loss, 0 is stalemate, 1 is ongoing
    private boolean nextMove = true;

    /**
     * Constructs an unwatchable game
     */
    public ChessObserver(ChessGame game) {
        this.game = game;
        this.watchable = false;
    }

    /**
     * Constructs a watchable game
     * @param width Width of screen
     * @param height Height of screen
     * @param perspective Controls which side's pieces are on the bottom (true is white, false is black)
     */
    public ChessObserver(ChessGame game, int width, int height, boolean perspective) {
        this.game = game;
        this.frame = new ChessFrame(width, height, game.getWhiteName(), game.getBlackName());
        this.screen = new ChessPanel(game, perspective);

        this.refreshTimer = new Timer(500, e -> screen.repaint());
        this.watchable = true;

        frame.add(screen);
        frame.setVisible(true);
        refreshTimer.start();
    }

    /**
     * Prints result of game
     */
    public void play() {
        while (gameStatus > 0) {
            if (nextMove) {
                nextMove = false;
                game.moveVisual(this);
            }
            // game.move(this);
            try {
                Thread.sleep(600); // Wait 100ms between each move
            } catch (InterruptedException e) {

            }
        }

        if (game.getWinner() == -1) {
            System.out.println("Black won");
        } else if (game.getWinner() == 1) {
            System.out.println("White won");
        } else {
            System.out.println("Stalemate");
        }
    }

    public void setGameStatus(int status) {
        this.gameStatus = status;
    }

    public void nextMove() {
        nextMove = true;
    }
}