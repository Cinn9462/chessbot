import javax.swing.Timer;

public class ChessObserver {
    private ChessGame game;
    private boolean watchable;
    private boolean white_perspective; // Determines which side is on the bottom. true means white pieces are on the bottom, false means black pieces are on the bottom.

    private ChessPanel screen;
    private Timer screenTimer = new Timer(1000, e -> screen.repaint());

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
        
        int game_status = 1; // -1 is win/loss, 0 is stalemate, 1 is ongoing

        if (watchable) {
            ChessFrame frame = new ChessFrame();
            frame.add(screen);

            screen.revalidate();
            screenTimer.start();

            while (game_status > 0) {
                screen.repaint();
                game_status = game.move();
        
                Thread.sleep(200); // Wait 200ms between each move
            }
        }

        else {
            while (game_status > 0) {
                game_status = game.move();

                Thread.sleep(200); // Wait 200ms between each move
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
}
