public class Main {
    public static void main(String[] args) {
        
        ChessPlayer white = new MyStockfish();
        ChessPlayer black = new MyStockfish();
        int time = 600;
        
        new Thread(() -> {
            ChessGame game = new ChessGame(white, black, time);
            // ChessObserver observer = new ChessObserver(game);
            ChessObserver observer = new ChessObserver(game, 900, 740, true);
            observer.play();
        }).start();
        
    }
}