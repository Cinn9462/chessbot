// javac -d out (Get-ChildItem -Recurse -Filter *.java -Path src | ForEach-Object { $_.FullName }); java -cp out main.Main


package main;
import boardUI.*;
import game.*;
import player.*;

@SuppressWarnings("unused")
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