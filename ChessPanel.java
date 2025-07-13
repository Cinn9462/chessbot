import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ChessPanel extends JPanel{

    public static int[] mouseClick;
    public static int clicks;

    private ChessBoard board;
    private boolean white;
    private float scale;

    public ChessPanel(ChessBoard b, boolean w) {
        scale = 0.8f;
        mouseClick = new int[]{-1, -1};
        clicks = 0;
        board = b;
        white = w;
        MouseListener myMouse = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                int[] smallClick = new int[]{e.getX() / (int) (100 * scale), e.getY() / (int) (100 * scale)};
                if (white) {
                    mouseClick = smallClick;
                }
                else {
                    mouseClick = new int[]{7 - smallClick[0], 7 - smallClick[1]};
                }
                clicks++;
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
        this.addMouseListener(myMouse);
        this.setPreferredSize(new Dimension((int) (800 * scale), (int) (800 * scale)));
    }

    public void paint(Graphics g) {
        super.paint(g);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                g.setColor((i + j) % 2 == 0 ? new Color(242, 225, 195) : new Color(195, 160, 130));
                g.fillRect((int) (i * 100 * scale), (int) (j * 100 * scale), (int) (100 * scale), (int) (100 * scale));
            }
        }

        try {
            drawPieces(board, g);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // System.out.println(mouseClick[0] / (100 * scale) + " " + mouseClick[1] / (100 * scale));
    }

    public void drawPieces(ChessBoard b, Graphics g) throws IOException {
        BufferedImage image = ImageIO.read(new File("chess PIECES2.png"));
        long[] pieces = b.getBoard();

        int[][][] allPositions = new int[12][][];

        for (int i = 0; i < 12; i++) {
            allPositions[i] = longToPosition(pieces[i]);
        }

        for (int[] i : allPositions[0]) {
            Image smallImage = image.getSubimage(750, 0, 150, 150);
            g.drawImage(smallImage, (white ? i[0] : 7 - i[0]) * (int) (100 * scale), (white ? i[1] : 7 - i[1]) * (int) (100 * scale), (int) (100 * scale), (int) (100 * scale), null);
        }
        for (int[] i : allPositions[1]) {
            Image smallImage = image.getSubimage(750, 150, 150, 150);
            g.drawImage(smallImage, (white ? i[0] : 7 - i[0]) * (int) (100 * scale), (white ? i[1] : 7 - i[1]) * (int) (100 * scale), (int) (100 * scale), (int) (100 * scale),null);
        }

        for (int[] i : allPositions[2]) {
            Image smallImage = image.getSubimage(450, 0, 150, 150);
            g.drawImage(smallImage, (white ? i[0] : 7 - i[0]) * (int) (100 * scale), (white ? i[1] : 7 - i[1]) * (int) (100 * scale), (int) (100 * scale), (int) (100 * scale), null);
        }
        for (int[] i : allPositions[3]) {
            Image smallImage = image.getSubimage(450, 150, 150, 150);
            g.drawImage(smallImage, (white ? i[0] : 7 - i[0]) * (int) (100 * scale), (white ? i[1] : 7 - i[1]) * (int) (100 * scale), (int) (100 * scale), (int) (100 * scale),null);
        }

        for (int[] i : allPositions[4]) {
            Image smallImage = image.getSubimage(300, 0, 150, 150);
            g.drawImage(smallImage, (white ? i[0] : 7 - i[0]) * (int) (100 * scale), (white ? i[1] : 7 - i[1]) * (int) (100 * scale), (int) (100 * scale), (int) (100 * scale), null);
        }
        for (int[] i : allPositions[5]) {
            Image smallImage = image.getSubimage(300, 150, 150, 150);
            g.drawImage(smallImage, (white ? i[0] : 7 - i[0]) * (int) (100 * scale), (white ? i[1] : 7 - i[1]) * (int) (100 * scale), (int) (100 * scale), (int) (100 * scale),null);
        }

        for (int[] i : allPositions[6]) {
            Image smallImage = image.getSubimage(600, 0, 150, 150);
            g.drawImage(smallImage, (white ? i[0] : 7 - i[0]) * (int) (100 * scale), (white ? i[1] : 7 - i[1]) * (int) (100 * scale), (int) (100 * scale), (int) (100 * scale), null);
        }
        for (int[] i : allPositions[7]) {
            Image smallImage = image.getSubimage(600, 150, 150, 150);
            g.drawImage(smallImage, (white ? i[0] : 7 - i[0]) * (int) (100 * scale), (white ? i[1] : 7 - i[1]) * (int) (100 * scale), (int) (100 * scale), (int) (100 * scale),null);
        }

        for (int[] i : allPositions[10]) {
            Image smallImage = image.getSubimage(0, 0, 150, 150);
            g.drawImage(smallImage, (white ? i[0] : 7 - i[0]) * (int) (100 * scale), (white ? i[1] : 7 - i[1]) * (int) (100 * scale), (int) (100 * scale), (int) (100 * scale), null);
        }
        for (int[] i : allPositions[11]) {
            Image smallImage = image.getSubimage(0, 150, 150, 150);
            g.drawImage(smallImage, (white ? i[0] : 7 - i[0]) * (int) (100 * scale), (white ? i[1] : 7 - i[1]) * (int) (100 * scale), (int) (100 * scale), (int) (100 * scale),null);
        }

        for (int[] i : allPositions[8]) {
            Image smallImage = image.getSubimage(150, 0, 150, 150);
            g.drawImage(smallImage, (white ? i[0] : 7 - i[0]) * (int) (100 * scale), (white ? i[1] : 7 - i[1]) * (int) (100 * scale), (int) (100 * scale), (int) (100 * scale), null);
        }
        for (int[] i : allPositions[9]) {
            Image smallImage = image.getSubimage(150, 150, 150, 150);
            g.drawImage(smallImage, (white ? i[0] : 7 - i[0]) * (int) (100 * scale), (white ? i[1] : 7 - i[1]) * (int) (100 * scale), (int) (100 * scale), (int) (100 * scale),null);
        }
    }

    public void updateBoard(ChessBoard b) {
        board = b;
    }

    private int[][] longToPosition(long loc) {
        int bits = Long.bitCount(loc);
        int[][] positions = new int[bits][2];

        for (int i = 0; i < bits; i++) {
            int count = Long.numberOfLeadingZeros(loc);
            positions[i] = new int[]{count % 8, count / 8};
            loc ^= (0x8000000000000000L >>> count);
        }

        return positions;
    }
}
