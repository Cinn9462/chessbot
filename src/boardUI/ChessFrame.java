package boardUI;
import game.*;
import player.*;

import javax.swing.*;

@SuppressWarnings("unused")
public class ChessFrame extends JFrame {
    private String whiteName;
    private String blackName;


    /**
     * @param width Default width of window (resizable)
     * @param height Default height of window (resizable)
     * @param whiteName String found via ChessPlayer.name()
     * @param blackName String found via ChessPlayer.name()
     */
    public ChessFrame(int width, int height, String whiteName, String blackName) {

        this.blackName = blackName + " (black)";
        this.whiteName = whiteName + " (white)";
    
        this.setTitle(this.whiteName + " vs. " + this.blackName);

        this.setSize(width, height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
