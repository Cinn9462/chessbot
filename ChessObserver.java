public class ChessObserver {
    private ChessGame game;
    private boolean watchable;
    private long wait;
    private boolean rightSideUp;

    public ChessObserver(ChessGame g, boolean w, long ww) {
        game = g;
        watchable = w;
        wait = ww;
    }

    public ChessObserver(ChessGame g, boolean w, long ww, boolean o) {
        game = g;
        watchable = w;
        wait = ww;
        rightSideUp = o;
    }

    public int play() {
        int gameOver = 1;
        if (watchable) {
            ChessFrame frame = new ChessFrame();
            ChessPanel screen = new ChessPanel(new ChessBoard(), rightSideUp);
            frame.add(screen);
            while (gameOver > 0) {
                gameOver = game.move();
                ChessBoard b = game.getBoard();
                screen.updateBoard(b);
                screen.repaint();
                try {
                    Thread.sleep(wait);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        else {
            while (gameOver > 0) {
                gameOver = game.move();
                try {
                    Thread.sleep(wait);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return game.getResult();
    }
}
