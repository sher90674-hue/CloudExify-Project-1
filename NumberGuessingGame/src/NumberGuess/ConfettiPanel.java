package NumberGuess;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;


public class ConfettiPanel extends JPanel {

    public interface FinishListener {
        void onFinished();
    }

    private static final int LIFESPAN_TICKS = 90; // roughly 2.7 seconds at 30ms/tick
    private static final Color[] PALETTE = {
            new Color(255, 110, 199), // pink
            new Color(255, 159, 28),  // orange
            new Color(6, 255, 165),   // mint
            new Color(131, 56, 236),  // purple
            new Color(255, 214, 10)   // yellow
    };

    private final List<Piece> pieces = new ArrayList<>();
    private Timer timer;
    private int ticks = 0;
    private FinishListener finishListener;

    public ConfettiPanel(int width, int height) {
        setOpaque(false);
        for (int i = 0; i < 120; i++) {
            pieces.add(new Piece(width));
        }
        timer = new Timer(30, e -> {
            ticks++;
            for (Piece p : pieces) {
                p.update();
            }
            repaint();
            if (ticks >= LIFESPAN_TICKS) {
                timer.stop();
                if (finishListener != null) {
                    finishListener.onFinished();
                }
            }
        });
    }

    public void setFinishListener(FinishListener listener) {
        this.finishListener = listener;
    }

    public void burst() {
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (Piece p : pieces) {
            g2.setColor(p.color);
            g2.rotate(Math.toRadians(p.rotation), p.x, p.y);
            g2.fillRect((int) p.x, (int) p.y, p.size, p.size);
            g2.rotate(-Math.toRadians(p.rotation), p.x, p.y);
        }
        g2.dispose();
    }

    /** A single confetti square: position, velocity, spin, and color. */
    private static class Piece {
        float x;
        float y;
        final float vx;
        final float vy;
        final int size;
        float rotation;
        final float rotationSpeed;
        final Color color;

        Piece(int width) {
            x = (float) (Math.random() * width);
            y = (float) (Math.random() * -200);
            vx = (float) (Math.random() * 4 - 2);
            vy = (float) (2 + Math.random() * 3);
            size = 6 + (int) (Math.random() * 8);
            rotation = (float) (Math.random() * 360);
            rotationSpeed = (float) (Math.random() * 10 - 5);
            color = PALETTE[(int) (Math.random() * PALETTE.length)];
        }

        void update() {
            x += vx;
            y += vy;
            rotation += rotationSpeed;
        }
    }
}