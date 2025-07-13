//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        ChessBoard.makeEverything();

        boolean orientation = true;

        // ChessGame game = new ChessGame(new MyStockfish(true), new Human(false));
        ChessGame game = new ChessGame(new MyStockfish(true), new MyStockfish(false));
        ChessObserver watching = new ChessObserver(game, true, 1000, orientation);
        System.out.println(watching.play());
    }
}