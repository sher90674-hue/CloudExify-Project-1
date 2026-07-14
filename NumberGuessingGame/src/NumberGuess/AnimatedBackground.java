package NumberGuess;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;


public class AnimatedBackground extends JPanel {

    private float phase = 0f;
    private final FloatingOrb[] orbs;

    public AnimatedBackground() {
        setOpaque(true);
        orbs = new FloatingOrb[6];
        for (int i = 0; i < orbs.length; i++) {
            orbs[i] = new FloatingOrb();
        }
        Timer timer = new Timer(40, e -> {
            phase += 0.01f;
            for (FloatingOrb orb : orbs) {
                orb.update();
            }
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

                float t = (float) (Math.sin(phase) * 0.5 + 0.5);
        Color top = blend(new Color(123, 44, 191), new Color(255, 110, 199), t);
        Color bottom = blend(new Color(255, 110, 199), new Color(255, 159, 28), t);

        GradientPaint gp = new GradientPaint(0, 0, top, w, h, bottom);
        g2.setPaint(gp);
        g2.fillRect(0, 0, w, h);

        for (FloatingOrb orb : orbs) {
            float x = orb.x * w;
            float y = orb.y * h;
            float r = orb.radius;
            if (r <= 0) {
                continue;
            }
            RadialGradientPaint glow = new RadialGradientPaint(
                    new Point2D.Float(x, y), r,
                    new float[]{0f, 1f},
                    new Color[]{new Color(255, 255, 255, 70), new Color(255, 255, 255, 0)});
            g2.setPaint(glow);
            g2.fillOval((int) (x - r), (int) (y - r), (int) (r * 2), (int) (r * 2));
        }

        g2.dispose();
    }

    private Color blend(Color a, Color b, float t) {
        int r = (int) (a.getRed() + (b.getRed() - a.getRed()) * t);
        int g = (int) (a.getGreen() + (b.getGreen() - a.getGreen()) * t);
        int bl = (int) (a.getBlue() + (b.getBlue() - a.getBlue()) * t);
        return new Color(r, g, bl);
    }

    /** A single slow-drifting glow circle used purely for background depth. */
    private static class FloatingOrb {
        float x = (float) Math.random();
        float y = (float) Math.random();
        final float radius = 60 + (float) Math.random() * 90;
        final float speed = 0.0006f + (float) Math.random() * 0.0008f;

        void update() {
            y -= speed;
            if (y < -0.2f) {
                y = 1.1f;
                x = (float) Math.random();
            }
        }
    }
}