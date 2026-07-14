package NumberGuess;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BasicStroke;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class NumberGuessingGameGUI extends JFrame {

    private static final int WIDTH = 920;
    private static final int HEIGHT = 680;

    private final Game game = new Game();

    private final JLayeredPane layeredPane = new JLayeredPane();
    private final AnimatedBackground background = new AnimatedBackground();
    private final JPanel cardsPanel = new JPanel(new CardLayout());
    private final CardLayout cardLayout = (CardLayout) cardsPanel.getLayout();

    private JLabel difficultyTitleLabel;
    private JLabel bestScoreValueLabel;
    private JLabel streakValueLabel;
    private JLabel rangeLabel;
    private JLabel temperatureEmojiLabel;
    private JLabel temperatureTextLabel;
    private JLabel attemptsLabel;
    private JTextField guessField;
    private AttemptsBar attemptsBar;
    private JPanel historyChips;

    public NumberGuessingGameGUI() {
        super(" Number Guessing Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        layeredPane.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        background.setBounds(0, 0, WIDTH, HEIGHT);
        cardsPanel.setBounds(0, 0, WIDTH, HEIGHT);
        cardsPanel.setOpaque(false);

        layeredPane.add(background, Integer.valueOf(0));
        layeredPane.add(cardsPanel, Integer.valueOf(1));

        cardsPanel.add(buildMenuPanel(), "menu");
        cardsPanel.add(buildGamePanel(), "game");

        setContentPane(layeredPane);
        pack();
        setLocationRelativeTo(null);
    }

   
    // Menu screen
  
    private JPanel buildMenuPanel() {
        JPanel panel = new JPanel(null);
        panel.setOpaque(false);
        panel.setBounds(0, 0, WIDTH, HEIGHT);


        JLabel subtitle = new JLabel("Number Guessing Game", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 22));
        subtitle.setForeground(new Color(255, 255, 255, 220));
        subtitle.setBounds(0, 150, WIDTH, 34);
        panel.add(subtitle);

        JLabel tagline = new JLabel("Pick a vibe. Guess the number. Chase the streak.", SwingConstants.CENTER);
        tagline.setFont(new Font("SansSerif", Font.ITALIC, 15));
        tagline.setForeground(new Color(255, 255, 255, 180));
        tagline.setBounds(0, 190, WIDTH, 26);
        panel.add(tagline);

        int btnWidth = 260;
        int btnHeight = 70;
        int gap = 30;
        int totalWidth = (btnWidth * 3) + (gap * 2);
        int startX = (WIDTH - totalWidth) / 2;
        int y = 300;

        ModernButton easyBtn = new ModernButton("Easy  1-50", new Color(6, 214, 160), new Color(6, 255, 165));
        easyBtn.setBounds(startX, y, btnWidth, btnHeight);
        easyBtn.addActionListener(e -> startGame(Game.Difficulty.EASY));
        panel.add(easyBtn);

        ModernButton mediumBtn = new ModernButton("Medium  1-100", new Color(255, 159, 28), new Color(255, 202, 58));
        mediumBtn.setBounds(startX + btnWidth + gap, y, btnWidth, btnHeight);
        mediumBtn.addActionListener(e -> startGame(Game.Difficulty.MEDIUM));
        panel.add(mediumBtn);

        ModernButton hardBtn = new ModernButton("Hard  1-200", new Color(255, 71, 87), new Color(131, 56, 236));
        hardBtn.setBounds(startX + (btnWidth + gap) * 2, y, btnWidth, btnHeight);
        hardBtn.addActionListener(e -> startGame(Game.Difficulty.HARD));
        panel.add(hardBtn);



        return panel;
    }

    
    // Game screen
  
    private JPanel buildGamePanel() {
        JPanel panel = new JPanel(null);
        panel.setOpaque(false);
        panel.setBounds(0, 0, WIDTH, HEIGHT);

        GlassCard card = new GlassCard();
        card.setBounds(60, 40, WIDTH - 120, HEIGHT - 80);
        card.setLayout(null);
        panel.add(card);

        int cardWidth = card.getWidth();

        difficultyTitleLabel = new JLabel("Easy", SwingConstants.LEFT);
        difficultyTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        difficultyTitleLabel.setForeground(Color.WHITE);
        difficultyTitleLabel.setBounds(40, 24, 300, 32);
        card.add(difficultyTitleLabel);

        JButton backBtn = new JButton("\u2190 Menu");
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setBounds(cardWidth - 300, 28, 100, 26);
        backBtn.addActionListener(e -> cardLayout.show(cardsPanel, "menu"));
        card.add(backBtn);

        JLabel bestLabel = new JLabel("Best:");
        bestLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        bestLabel.setForeground(new Color(255, 255, 255, 200));
        bestLabel.setBounds(cardWidth - 200, 28, 40, 26);
        card.add(bestLabel);

        bestScoreValueLabel = new JLabel("--");
        bestScoreValueLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        bestScoreValueLabel.setForeground(new Color(255, 214, 10));
        bestScoreValueLabel.setBounds(cardWidth - 160, 28, 60, 26);
        card.add(bestScoreValueLabel);

        JLabel streakIcon = new JLabel("Streak:");
        streakIcon.setFont(new Font("SansSerif", Font.PLAIN, 14));
        streakIcon.setForeground(new Color(255, 255, 255, 200));
        streakIcon.setBounds(cardWidth - 100, 28, 55, 26);
        card.add(streakIcon);

        streakValueLabel = new JLabel("0");
        streakValueLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        streakValueLabel.setForeground(Color.WHITE);
        streakValueLabel.setBounds(cardWidth - 40, 28, 30, 26);
        card.add(streakValueLabel);

        rangeLabel = new JLabel("Guess a number between 1 and 50", SwingConstants.CENTER);
        rangeLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        rangeLabel.setForeground(new Color(255, 255, 255, 230));
        rangeLabel.setBounds(0, 80, cardWidth, 24);
        card.add(rangeLabel);

        temperatureEmojiLabel = new JLabel("\uD83C\uDFAF", SwingConstants.CENTER);
        temperatureEmojiLabel.setFont(new Font("SansSerif", Font.PLAIN, 64));
        temperatureEmojiLabel.setBounds(0, 115, cardWidth, 90);
        card.add(temperatureEmojiLabel);

        temperatureTextLabel = new JLabel("Take a guess!", SwingConstants.CENTER);
        temperatureTextLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        temperatureTextLabel.setForeground(Color.WHITE);
        temperatureTextLabel.setBounds(0, 205, cardWidth, 36);
        card.add(temperatureTextLabel);

        guessField = new JTextField();
        guessField.setFont(new Font("SansSerif", Font.BOLD, 20));
        guessField.setHorizontalAlignment(JTextField.CENTER);
        guessField.setBounds(cardWidth / 2 - 160, 260, 200, 46);
        guessField.setBorder(new EmptyBorder(8, 12, 8, 12));
        guessField.addActionListener(e -> handleGuess());
        card.add(guessField);

        ModernButton submitBtn = new ModernButton("Guess", new Color(131, 56, 236), new Color(255, 110, 199));
        submitBtn.setBounds(cardWidth / 2 + 50, 254, 140, 58);
        submitBtn.addActionListener(e -> handleGuess());
        card.add(submitBtn);

        attemptsLabel = new JLabel("Attempts left: 10 / 10", SwingConstants.CENTER);
        attemptsLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        attemptsLabel.setForeground(new Color(255, 255, 255, 220));
        attemptsLabel.setBounds(0, 330, cardWidth, 22);
        card.add(attemptsLabel);

        attemptsBar = new AttemptsBar();
        attemptsBar.setBounds(cardWidth / 2 - 200, 356, 400, 16);
        card.add(attemptsBar);

        JLabel historyTitle = new JLabel("Your guesses:");
        historyTitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        historyTitle.setForeground(new Color(255, 255, 255, 190));
        historyTitle.setBounds(40, 400, 200, 22);
        card.add(historyTitle);

        historyChips = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        historyChips.setOpaque(false);

        JScrollPane historyScroll = new JScrollPane(historyChips);
        historyScroll.setOpaque(false);
        historyScroll.getViewport().setOpaque(false);
        historyScroll.setBorder(BorderFactory.createEmptyBorder());
        historyScroll.setBounds(40, 425, cardWidth - 80, 90);
        card.add(historyScroll);

        return panel;
    }

    // Game flow
    private void startGame(Game.Difficulty difficulty) {
        game.startNewGame(difficulty);

        difficultyTitleLabel.setText(difficulty.label);
        rangeLabel.setText("Guess a number between " + difficulty.min + " and " + difficulty.max);
        temperatureEmojiLabel.setText("\uD83C\uDFAF");
        temperatureTextLabel.setText("Take a guess!");
        temperatureTextLabel.setForeground(Color.WHITE);
        attemptsLabel.setText("Attempts left: " + difficulty.maxAttempts + " / " + difficulty.maxAttempts);
        attemptsBar.setFraction(1f);
        guessField.setText("");
        historyChips.removeAll();
        historyChips.revalidate();
        historyChips.repaint();

        Integer best = game.getBestScore(difficulty);
        bestScoreValueLabel.setText(best == null ? "--" : String.valueOf(best));
        streakValueLabel.setText(String.valueOf(game.getStreak()));

        cardLayout.show(cardsPanel, "game");
        guessField.requestFocusInWindow();
    }

    private void handleGuess() {
        String text = guessField.getText().trim();
        if (text.isEmpty()) {
            flashMessage("Type a number first!", new Color(255, 214, 10));
            return;
        }

        int guess;
        try {
            guess = Integer.parseInt(text);
        } catch (NumberFormatException ex) {
            flashMessage("Numbers only, please!", new Color(255, 71, 87));
            guessField.setText("");
            return;
        }

        Game.Difficulty d = game.getDifficulty();
        if (guess < d.min || guess > d.max) {
            flashMessage("Stay between " + d.min + " and " + d.max + "!", new Color(255, 71, 87));
            guessField.setText("");
            return;
        }

        Game.GuessResult result = game.submitGuess(guess);
        addHistoryChip(guess, result);
        guessField.setText("");

        attemptsLabel.setText("Attempts left: " + result.attemptsLeft + " / " + d.maxAttempts);
        attemptsBar.setFraction((float) result.attemptsLeft / d.maxAttempts);

        if (result.correct) {
            temperatureEmojiLabel.setText("\uD83C\uDF89");
            temperatureTextLabel.setText("Correct!");
            temperatureTextLabel.setForeground(new Color(6, 255, 165));
            celebrate();
            streakValueLabel.setText(String.valueOf(game.getStreak()));
            Integer best = game.getBestScore(d);
            bestScoreValueLabel.setText(best == null ? "--" : String.valueOf(best));

            ResultDialog dialog = new ResultDialog(this, true, game.getSecretNumber(), result.attemptsUsed,
                    result.newBestScore, game.getStreak(), result.elapsedMillis,
                    () -> startGame(d), () -> cardLayout.show(cardsPanel, "menu"));
            SwingUtilities.invokeLater(() -> dialog.setVisible(true));
        } else if (result.gameOver) {
            temperatureEmojiLabel.setText("\uD83D\uDE22");
            temperatureTextLabel.setText("Out of attempts");
            temperatureTextLabel.setForeground(new Color(255, 71, 87));

            ResultDialog dialog = new ResultDialog(this, false, game.getSecretNumber(), result.attemptsUsed,
                    false, game.getStreak(), result.elapsedMillis,
                    () -> startGame(d), () -> cardLayout.show(cardsPanel, "menu"));
            SwingUtilities.invokeLater(() -> dialog.setVisible(true));
        } else {
            updateTemperatureDisplay(result);
        }
    }

    private void updateTemperatureDisplay(Game.GuessResult result) {
        String emoji;
        Color color;
        switch (result.temperature) {
            case "On Fire":
                emoji = "\uD83D\uDD25";
                color = new Color(255, 71, 87);
                break;
            case "Hot":
                emoji = "\u2668";
                color = new Color(255, 159, 28);
                break;
            case "Warm":
                emoji = "\u2600";
                color = new Color(255, 214, 10);
                break;
            case "Cool":
                emoji = "\uD83C\uDF43";
                color = new Color(6, 255, 165);
                break;
            case "Cold":
                emoji = "\u2744";
                color = new Color(100, 200, 255);
                break;
            default:
                emoji = "\uD83E\uDDCA";
                color = new Color(180, 220, 255);
                break;
        }
        temperatureEmojiLabel.setText(emoji);
        String direction = result.tooHigh ? " (go lower)" : " (go higher)";
        temperatureTextLabel.setText(result.temperature + direction);
        temperatureTextLabel.setForeground(color);
    }

    private void flashMessage(String message, Color color) {
        temperatureEmojiLabel.setText("\u26A0");
        temperatureTextLabel.setText(message);
        temperatureTextLabel.setForeground(color);
    }

    private void addHistoryChip(int guess, Game.GuessResult result) {
        JLabel chip = new JLabel(String.valueOf(guess));
        chip.setOpaque(true);
        chip.setFont(new Font("SansSerif", Font.BOLD, 13));
        chip.setForeground(Color.WHITE);
        chip.setBorder(new EmptyBorder(6, 14, 6, 14));
        if (result.correct) {
            chip.setBackground(new Color(6, 214, 160));
        } else if (result.tooHigh) {
            chip.setBackground(new Color(255, 159, 28));
        } else {
            chip.setBackground(new Color(131, 56, 236));
        }
        historyChips.add(chip);
        historyChips.revalidate();
        historyChips.repaint();
    }

    private void celebrate() {
        ConfettiPanel confetti = new ConfettiPanel(WIDTH, HEIGHT);
        confetti.setBounds(0, 0, WIDTH, HEIGHT);
        layeredPane.add(confetti, Integer.valueOf(20));
        confetti.setFinishListener(() -> {
            layeredPane.remove(confetti);
            layeredPane.repaint();
        });
        confetti.burst();
    }

    // A simple translucent rounded "glass" panel used as the main card
    private static class GlassCard extends JPanel {
        GlassCard() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255, 255, 255, 40));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 36, 36);
            g2.setColor(new Color(255, 255, 255, 90));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 36, 36);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NumberGuessingGameGUI gui = new NumberGuessingGameGUI();
            gui.setVisible(true);
        });
    }
}