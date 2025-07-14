import javax.swing.Timer;

public class ChessObserver {
    private ChessGame game;
    private boolean watchable;
    private boolean white_perspective; // Determines which side is on the bottom. true means white pieces are on the bottom, false means black pieces are on the bottom.

    private ChessPanel screen;
    private Timer screenTimer = new Timer(500, e -> screen.repaint());

    private int gameStatus = 1; // -1 is win/loss, 0 is stalemate, 1 is ongoing
    private boolean nextMove = true;

    /**
     * @param game Game board
     * @param watchable Controls whether a screen pops up
     * @param perspective Controls which side's pieces are on the bottom (true is white, false is black)
     */
    public ChessObserver(ChessGame game, boolean watchable, boolean perspective) {
        this.game = game;
        this.watchable = watchable;
        this.white_perspective = perspective;
        this.screen = new ChessPanel(game.getBoard(), game, white_perspective);
    }

    /**
     * Prints result of game
     * @throws InterruptedException
     */
    public void play() throws InterruptedException {
        if (watchable) {
            ChessFrame frame = new ChessFrame();
            frame.add(screen);

            screen.revalidate();
            screenTimer.start();
        }

        while (gameStatus > 0) {
            if (nextMove) {
                nextMove = false;
                game.move(this);
            }
            Thread.sleep(100); // Wait 100ms between each move
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
