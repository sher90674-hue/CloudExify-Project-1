package NumberGuess;

import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;


 
public class ModernButton extends JButton {

    private final Color colorStart;
    private final Color colorEnd;
    private float hoverProgress = 0f; // 0 = resting, 1 = fully hovered
    private boolean hovering = false;
    private Timer animationTimer;

    public ModernButton(String text, Color colorStart, Color colorEnd) {
        super(text);
        this.colorStart = colorStart;
        this.colorEnd = colorEnd;

        setFont(new Font("SansSerif", Font.BOLD, 16));
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(14, 28, 14, 28));

        animationTimer = new Timer(15, e -> {
            float target = hovering ? 1f : 0f;
            hoverProgress += (target - hoverProgress) * 0.25f;
            if (Math.abs(target - hoverProgress) < 0.01f) {
                hoverProgress = target;
                animationTimer.stop();
            }
            repaint();
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovering = true;
                if (!animationTimer.isRunning()) {
                    animationTimer.start();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovering = false;
                if (!animationTimer.isRunning()) {
                    animationTimer.start();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        float scale = 1f + (0.04f * hoverProgress);
        int scaledW = (int) (w * scale);
        int scaledH = (int) (h * scale);
        int offsetX = (w - scaledW) / 2;
        int offsetY = (h - scaledH) / 2;

        RoundRectangle2D shape = new RoundRectangle2D.Float(offsetX, offsetY, scaledW, scaledH, h, h);

        GradientPaint gradient = new GradientPaint(0, 0, colorStart, w, h, colorEnd);
        g2.setPaint(gradient);
        g2.fill(shape);

        if (hoverProgress > 0.01f) {
            g2.setColor(new Color(255, 255, 255, (int) (90 * hoverProgress)));
            g2.setStroke(new BasicStroke(2f));
            g2.draw(shape);
        }

        g2.dispose();
        super.paintComponent(g);
    }
}