package NumberGuess;


import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;


public class ResultDialog extends JDialog {

    public ResultDialog(JFrame owner, boolean win, int secretNumber, int attempts,
                         boolean newBestScore, int streak, long elapsedMillis,
                         Runnable onPlayAgain, Runnable onMainMenu) {
        super(owner, true);
        setUndecorated(true);
        setSize(420, 380);
        setLocationRelativeTo(owner);
        setShape(new RoundRectangle2D.Double(0, 0, 420, 380, 34, 34));

        JPanel content = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color top = win ? new Color(6, 214, 160) : new Color(131, 56, 236);
                Color bottom = win ? new Color(6, 255, 165) : new Color(255, 110, 199);
                GradientPaint gp = new GradientPaint(0, 0, top, 0, getHeight(), bottom);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 34, 34);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        content.setBounds(0, 0, 420, 380);
        content.setOpaque(false);

        JLabel title = new JLabel(win ? "You Got It!" : "Out of Attempts", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        title.setBounds(0, 30, 420, 40);
        content.add(title);

        JLabel emoji = new JLabel(win ? "\uD83C\uDF89" : "\uD83D\uDE22", SwingConstants.CENTER);
        emoji.setFont(new Font("SansSerif", Font.PLAIN, 50));
        emoji.setBounds(0, 70, 420, 60);
        content.add(emoji);

        StringBuilder info = new StringBuilder("<html><center>");
        if (win) {
            info.append("The number was <b>").append(secretNumber).append("</b><br>");
            info.append("Solved in <b>").append(attempts).append("</b> attempts<br>");
            info.append("Time: <b>").append(elapsedMillis / 1000).append("s</b><br>");
            if (newBestScore) {
                info.append("<span style='color:#FFD60A;'>New Best Score!</span><br>");
            }
            info.append("Current Streak: <b>").append(streak).append("</b> \uD83D\uDD25");
        } else {
            info.append("The number was <b>").append(secretNumber).append("</b><br>");
            info.append("So close! Try again \u2014 you've got this.");
        }
        info.append("</center></html>");

        JLabel infoLabel = new JLabel(info.toString(), SwingConstants.CENTER);
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setBounds(20, 140, 380, 120);
        content.add(infoLabel);

        ModernButton playAgainBtn = new ModernButton(win ? "Play Again" : "Try Again",
                new Color(255, 159, 28), new Color(255, 110, 199));
        playAgainBtn.setBounds(60, 270, 300, 46);
        playAgainBtn.addActionListener(e -> {
            dispose();
            onPlayAgain.run();
        });
        content.add(playAgainBtn);

        JButton mainMenuBtn = new JButton("Main Menu");
        mainMenuBtn.setBorderPainted(false);
        mainMenuBtn.setContentAreaFilled(false);
        mainMenuBtn.setForeground(Color.WHITE);
        mainMenuBtn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        mainMenuBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mainMenuBtn.setBorder(BorderFactory.createEmptyBorder());
        mainMenuBtn.setBounds(150, 330, 120, 30);
        mainMenuBtn.addActionListener(e -> {
            dispose();
            onMainMenu.run();
        });
        content.add(mainMenuBtn);

        setContentPane(content);
    }
}
