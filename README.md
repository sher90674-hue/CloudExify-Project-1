# CloudExify-Project-1
<br>
# Project 1 Report – Number Guessing Game (GUI Edition)

**CloudExify Java Internship – Month 1**

## Introduction

For my first internship project, I developed a **Number Guessing Game** in Java. The original task could have been completed as a console application, but I decided to challenge myself by building a desktop application using **Java Swing**. Since I had already worked with Swing in some of my university projects, I thought it would be a good opportunity to improve my GUI development skills while completing the internship assignment.

The game follows the basic idea of a traditional number guessing game. A random number is generated, and the player has to guess it within a limited number of attempts. Instead of only displaying "Too High" or "Too Low," I added a temperature-based feedback system. Depending on how close the player's guess is to the correct number, the game displays messages such as **Freezing, Cold, Cool, Warm, Hot,** and **On Fire**. To implement this feature, I calculated how close each guess was to the target number and converted that value into one of these temperature levels.

## Features

The game includes several additional features beyond the basic requirements:

* Three difficulty levels: Easy (1–50), Medium (1–100), and Hard (1–200).
* A limited number of attempts for each difficulty level.
* A win streak counter that records consecutive wins.
* Best score tracking for each difficulty, stored in a text file (`bestscore.txt`) so that scores remain available after restarting the application.
* A history panel showing every guess made during the current game, with different colors indicating whether the guess was too high, too low, or correct.
* An animated attempts progress bar that changes as the player runs out of guesses.
* A simple confetti animation displayed after winning a round.
* A custom user interface with rounded buttons and an animated gradient background instead of the default Swing appearance.

These additions made the game more interactive and gave me practical experience working with Swing components and simple animations.

## Project Structure

To keep the project organized, I separated the program into multiple classes instead of placing everything inside a single file.

* **Game.java** contains the main game logic, including random number generation, attempt counting, score calculation, and saving/loading the best scores.
* **NumberGuessingGameGUI.java** manages the main application window and user interaction.
* **ModernButton.java**, **AnimatedBackground.java**, **AttemptsBar.java**, **ConfettiPanel.java**, and **ResultDialog.java** are responsible for different graphical components and animations.

Separating the logic from the graphical interface made the project easier to understand and maintain. It also follows good object-oriented programming practices.

## Challenges Faced

While developing the project, I encountered a few problems during compilation and testing.

One issue was that a class filename did not exactly match its public class name. Java requires both names to be identical, so correcting the filename solved the problem.

Another issue involved the `Timer` objects used for animations. I had declared some of them as `final`, but because they referenced themselves while being initialized, Eclipse reported compilation errors. After understanding the cause, I removed the `final` keyword, and the animations worked correctly.

I also experienced package-related errors after moving files between folders. Once I placed all related classes in the same package, the project compiled successfully.

Although these problems took some time to solve, they helped me become more confident in reading compiler errors and debugging Java applications.

## Future Improvements

If I continue working on this project, I would like to add a few more features. A hint button would make the gameplay more interesting, and a leaderboard that stores the top five scores would improve the scoring system. I would also like to include sound effects for correct guesses, incorrect guesses, and winning a game to make the application feel more complete.

## Conclusion

This project gave me valuable hands-on experience with Java programming. Besides practicing object-oriented programming concepts, I also learned more about Java Swing, event handling, file handling, animations, and organizing a project into multiple classes.

Building the GUI version required more effort than creating a simple console application, but I enjoyed the process and learned a lot from it. Overall, this project strengthened both my Java programming skills and my confidence in developing desktop applications.
