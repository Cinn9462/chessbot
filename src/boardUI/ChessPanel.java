package boardUI;
import game.*;
import player.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.*;
import java.util.concurrent.*;


@SuppressWarnings("unused")
public class ChessPanel extends JPanel {

    private final BlockingQueue<int[]> clicks = new LinkedBlockingDeque<>(); 

    private ChessBoard board;
    private ChessGame game;
    private TimeControl white_time;
    private TimeControl black_time;


    private boolean whitePerspective;
    private float scale = 0.8f;

    private Map<Integer, BufferedImage> pieceImages = new HashMap<>();

    /**
     * 
     * @param white true/false. Determines whether white is on the bottom
     * @param white_time White time control
     * @param black_time Black time control
     */
    public ChessPanel(ChessGame game, boolean white) {
        this.board = game.getBoard();
        this.game = game;
        this.white_time = game.getWhiteTimeControl();
        this.black_time = game.getBlackTimeControl();
        this.whitePerspective = white;
        this.addMouseListener(createMouseListener());
        this.setPreferredSize(new Dimension((int) (800 * scale), (int) (800 * scale)));
        this.loadPieceImages();
    }
    
    /**
     * Creates mouse listener that updates mouse click with coordinates in which mouse was clicked, and clicks with how many times a click occurs
     * @return New Mouse Listener
     */
    private MouseAdapter createMouseListener() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX() / (int) (100 * scale);
                int y = e.getY() / (int) (100 * scale);
                
                if (!whitePerspective) {
                    x = 7 - x;
                    y = 7 - y;
                }
                
                clicks.offer(new int[]{x, y});
            }
        };
    }

    /**
     * Populates pieceMap with the piece images
     */
    private void loadPieceImages() {
        try {
            BufferedImage pieces = ImageIO.read(new File("src/resources/pieces.png"));

            pieceImages.put(0, pieces.getSubimage(750, 0, 150, 150));   // White Pawn
            pieceImages.put(1, pieces.getSubimage(750, 150, 150, 150)); // Black Pawn
            pieceImages.put(2, pieces.getSubimage(450, 0, 150, 150));   // White Knight
            pieceImages.put(3, pieces.getSubimage(450, 150, 150, 150)); // Black Knight
            pieceImages.put(4, pieces.getSubimage(300, 0, 150, 150));   // White Bishop
            pieceImages.put(5, pieces.getSubimage(300, 150, 150, 150)); // Black Bishop
            pieceImages.put(6, pieces.getSubimage(600, 0, 150, 150));   // White Rook
            pieceImages.put(7, pieces.getSubimage(600, 150, 150, 150)); // Black Rook
            pieceImages.put(8, pieces.getSubimage(150, 0, 150, 150));   // White Queen
            pieceImages.put(9, pieces.getSubimage(150, 150, 150, 150)); // Black Queen
            pieceImages.put(10, pieces.getSubimage(0, 0, 150, 150));    // White King
            pieceImages.put(11, pieces.getSubimage(0, 150, 150, 150));  // Black King

        } catch (IOException e) {
            throw new RuntimeException("Pieces failed to load", e);
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        drawBoard(g);
        drawPieces(board, g);
        drawClocks(g);
    }

    public void drawBoard(Graphics g) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                g.setColor((i + j) % 2 == 0 ? new Color(242, 225, 195) : new Color(195, 160, 130));
                g.fillRect((int) (i * 100 * scale), (int) (j * 100 * scale), (int) (100 * scale), (int) (100 * scale));
            }
        }
    }

    public void drawPieces(ChessBoard b, Graphics g) {
        long[] pieces = b.getBoard();

        for (int i = 0; i < 12; i++) {
            int[][] positions = longToPosition(pieces[i]);

            for (int[] pos : positions) {
                g.drawImage(pieceImages.get(i), (whitePerspective ? pos[0] : 7 - pos[0]) * (int) (100 * scale), (whitePerspective ? pos[1] : 7 - pos[1]) * (int) (100 * scale), (int) (100 * scale), (int) (100 * scale), null);
            }
        }
    }

    /**
     *  Converts bitboard into an array including all piece locations in [x, y]
     *  @param bitBoard Bitboard to convert
     */
    private int[][] longToPosition(long bitBoard) {
        int count = Long.bitCount(bitBoard);
        int[][] positions = new int[count][2];

        for (int i = 0; i < count; i++) {
            int pos = Long.numberOfLeadingZeros(bitBoard);
            positions[i] = new int[]{pos % 8, pos / 8};
            bitBoard ^= (0x8000000000000000L >>> pos);
        }

        return positions;
    }

    public void drawClocks(Graphics g) {
        String time;
        Color clockColor;

        g.setFont(new Font("Segoe UI Mono", Font.PLAIN, 50));
        
        if (whitePerspective) {
    
            clockColor = game.getTurn() ? Color.BLACK : Color.GRAY;
            g.setColor(clockColor);
            time = String.format("%02d:%02d", Math.min(white_time.getTime() / 60000, 99), ((white_time.getTime() / 1000) % 60));
            if (white_time.getTime() > 1e9) {
                time = "Infinity";
            }
            g.drawString(time, (int) (810 * scale), (int) (775 * scale));

            clockColor = !game.getTurn() ? Color.BLACK : Color.GRAY;
            g.setColor(clockColor);
            time = String.format("%02d:%02d", Math.min(black_time.getTime() / 60000, 99), ((black_time.getTime() / 1000) % 60));

                        if (black_time.getTime() > 1e9) {
                time = "Infinity";
            }
            g.drawString(time, (int) (810 * scale), (int) (75 * scale));

        } else if (!whitePerspective) {

            clockColor = game.getTurn() ? Color.BLACK : Color.GRAY;
            g.setColor(clockColor);
            time = String.format("%02d:%02d", Math.min(black_time.getTime() / 60000, 99), ((black_time.getTime() / 1000) % 60));
            if (black_time.getTime() > 1e9) {
                time = "Infinity";
            }
            g.drawString(time, (int) (810 * scale), (int) (775 * scale));
            
            clockColor = !game.getTurn() ? Color.BLACK : Color.GRAY;
            g.setColor(clockColor);
            time = String.format("%02d:%02d", Math.min(white_time.getTime() / 60000, 99), ((white_time.getTime() / 1000) % 60));
            if (white_time.getTime() > 1e9) {
                time = "Infinity";
            }
            g.drawString(time, (int) (810 * scale), (int) (75 * scale));
        }
    }

    public int[] getNextClick() throws InterruptedException {
        return clicks.take();
    }


}
