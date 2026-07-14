package NumberGuess;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;


public class AttemptsBar extends JPanel {

    private float currentFraction = 1f;
    private float targetFraction = 1f;
    private Timer timer;

    public AttemptsBar() {
        setOpaque(false);
        setPreferredSize(new Dimension(300, 18));
        timer = new Timer(15, e -> {
            currentFraction += (targetFraction - currentFraction) * 0.15f;
            if (Math.abs(targetFraction - currentFraction) < 0.002f) {
                currentFraction = targetFraction;
                timer.stop();
            }
            repaint();
        });
    }

    public void setFraction(float fraction) {
        targetFraction = Math.max(0f, Math.min(1f, fraction));
        if (!timer.isRunning()) {
            timer.start();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        g2.setColor(new Color(255, 255, 255, 70));
        g2.fillRoundRect(0, 0, w, h, h, h);

        int filledW = (int) (w * currentFraction);
        Color barColor;
        if (currentFraction > 0.6f) {
            barColor = new Color(6, 255, 165);   // mint green
        } else if (currentFraction > 0.3f) {
            barColor = new Color(255, 159, 28);  // orange
        } else {
            barColor = new Color(255, 71, 87);   // warm red
        }
        g2.setColor(barColor);
        if (filledW > 0) {
            g2.fillRoundRect(0, 0, filledW, h, h, h);
        }

        g2.dispose();
    }
}