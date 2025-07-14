//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {
        ChessBoard.makeEverything();

        // ChessGame game = new ChessGame(new MyStockfish(true), new Human(false));
        ChessGame game = new ChessGame(new Human(true), new MyStockfish(false));
        ChessObserver watching = new ChessObserver(game, true, true);
        watching.play();
    }
}