package NumberGuess;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Game {

    /** The three difficulty presets a player can choose from. */
    public enum Difficulty {
        EASY("Easy", 1, 50, 10),
        MEDIUM("Medium", 1, 100, 8),
        HARD("Hard", 1, 200, 6);

        public final String label;
        public final int min;
        public final int max;
        public final int maxAttempts;

        Difficulty(String label, int min, int max, int maxAttempts) {
            this.label = label;
            this.min = min;
            this.max = max;
            this.maxAttempts = maxAttempts;
        }
    }

    public static class GuessResult {
        public final boolean correct;
        public final boolean gameOver;      // true when out of attempts and not correct
        public final String temperature;    // "Freezing" ... "On Fire"
        public final boolean tooHigh;       // only meaningful when guess was wrong
        public final int attemptsUsed;
        public final int attemptsLeft;
        public final boolean newBestScore;
        public final long elapsedMillis;

        GuessResult(boolean correct, boolean gameOver, String temperature, boolean tooHigh,
                    int attemptsUsed, int attemptsLeft, boolean newBestScore, long elapsedMillis) {
            this.correct = correct;
            this.gameOver = gameOver;
            this.temperature = temperature;
            this.tooHigh = tooHigh;
            this.attemptsUsed = attemptsUsed;
            this.attemptsLeft = attemptsLeft;
            this.newBestScore = newBestScore;
            this.elapsedMillis = elapsedMillis;
        }
    }

    private static final String SCORE_FILE = "bestscore.txt";

    private final Random random = new Random();
    private final List<Integer> guessHistory = new ArrayList<>();
    private final Map<String, Integer> bestScores = new HashMap<>();

    private Difficulty difficulty;
    private int secretNumber;
    private int attempts;
    private long startTime;
    private int streak;

    public Game() {
        loadBestScores();
    }

    /** Starts a brand new round for the given difficulty. */
    public void startNewGame(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.secretNumber = random.nextInt(difficulty.max - difficulty.min + 1) + difficulty.min;
        this.attempts = 0;
        this.startTime = System.currentTimeMillis();
        this.guessHistory.clear();
    }

    /** Evaluates a guess and returns full feedback for the GUI to render. */
    public GuessResult submitGuess(int guess) {
        attempts++;
        guessHistory.add(guess);

        int distance = Math.abs(guess - secretNumber);
        int range = difficulty.max - difficulty.min;
        double closeness = 1.0 - ((double) distance / range); // 1.0 = exact match

        boolean correct = guess == secretNumber;
        boolean tooHigh = guess > secretNumber;
        int attemptsLeft = difficulty.maxAttempts - attempts;
        boolean gameOver = !correct && attemptsLeft <= 0;
        long elapsed = System.currentTimeMillis() - startTime;

        boolean newBest = false;
        if (correct) {
            streak++;
            Integer currentBest = bestScores.get(difficulty.name());
            if (currentBest == null || attempts < currentBest) {
                bestScores.put(difficulty.name(), attempts);
                saveBestScores();
                newBest = true;
            }
        } else if (gameOver) {
            streak = 0;
        }

        return new GuessResult(correct, gameOver, temperatureFor(closeness), tooHigh,
                attempts, Math.max(attemptsLeft, 0), newBest, elapsed);
    }

    private String temperatureFor(double closeness) {
        if (closeness >= 0.97) return "On Fire";
        if (closeness >= 0.85) return "Hot";
        if (closeness >= 0.65) return "Warm";
        if (closeness >= 0.40) return "Cool";
        if (closeness >= 0.15) return "Cold";
        return "Freezing";
    }

    public Integer getBestScore(Difficulty d) {
        return bestScores.get(d.name());
    }

    public int getStreak() {
        return streak;
    }

    public List<Integer> getGuessHistory() {
        return guessHistory;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public int getSecretNumber() {
        return secretNumber;
    }

    public int getAttempts() {
        return attempts;
    }


    private void loadBestScores() {
        File file = new File(SCORE_FILE);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    try {
                        bestScores.put(parts[0].trim(), Integer.parseInt(parts[1].trim()));
                    } catch (NumberFormatException ignored) {
                       
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("No previous scores found, starting fresh.");
        }
    }

    private void saveBestScores() {
        try (FileWriter writer = new FileWriter(SCORE_FILE)) {
            for (Map.Entry<String, Integer> entry : bestScores.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue() + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Could not save best score.");
        }
    }
}