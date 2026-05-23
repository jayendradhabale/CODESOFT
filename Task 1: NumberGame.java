import java.util.Random;
import java.util.Scanner;

public class NumberGame {

    static final int MIN        = 1;
    static final int MAX        = 100;
    static final int MAX_TRIES  = 7;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random  random  = new Random();

        int totalRounds = 0;
        int roundsWon   = 0;
        int totalGuesses = 0;

        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║      NUMBER GUESSING GAME              ║");
        System.out.println("╚══════════════════════════════════╝");
        System.out.printf("Guess a number between %d and %d.%n", MIN, MAX);
        System.out.printf("You have %d attempts per round.%n%n", MAX_TRIES);

        boolean playAgain = true;

        while (playAgain) {
            totalRounds++;
            int target   = random.nextInt(MAX - MIN + 1) + MIN;
            int attempts = 0;
            boolean won  = false;

            System.out.printf("─── Round %d ───%n", totalRounds);

            while (attempts < MAX_TRIES) {
                int remaining = MAX_TRIES - attempts;
                System.out.printf("Attempts remaining: %d  →  Your guess: ", remaining);

                int guess;
                try {
                    guess = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("  ⚠  Please enter a valid integer.");
                    continue;
                }

                if (guess < MIN || guess > MAX) {
                    System.out.printf("  ⚠  Enter a number between %d and %d.%n", MIN, MAX);
                    continue;
                }

                attempts++;
                totalGuesses++;

                if (guess == target) {
                    System.out.printf("%n  ✓  Correct! The number was %d.%n", target);
                    System.out.printf("  You got it in %d attempt%s!%n%n",
                            attempts, attempts == 1 ? "" : "s");
                    won = true;
                    roundsWon++;
                    break;
                } else if (guess < target) {
                    System.out.println("  ↑  Too low — guess higher.");
                } else {
                    System.out.println("  ↓  Too high — guess lower.");
                }
            }

            if (!won) {
                System.out.printf("%n  ✗  Out of attempts! The number was %d.%n%n", target);
            }

            // Score for this round
            int roundScore = won ? Math.max(10, 70 - (attempts - 1) * 10) : 0;
            System.out.printf("  Round score: %d pts%n%n", roundScore);

            // Play again?
            System.out.print("Play another round? (yes / no): ");
            String answer = scanner.nextLine().trim().toLowerCase();
            playAgain = answer.equals("yes") || answer.equals("y");
            System.out.println();
        }

        //  Final scoreboard 
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║           FINAL SCORE            ║");
        System.out.println("╚══════════════════════════════════╝");
        System.out.printf("  Rounds played : %d%n", totalRounds);
        System.out.printf("  Rounds won    : %d%n", roundsWon);
        System.out.printf("  Win rate      : %.0f%%%n",
                totalRounds == 0 ? 0.0 : (roundsWon * 100.0 / totalRounds));
        System.out.printf("  Total guesses : %d%n", totalGuesses);
        double avg = roundsWon == 0 ? 0 : (double) totalGuesses / totalRounds;
        System.out.printf("  Avg guesses/round: %.1f%n%n", avg);
        System.out.println("Thanks for playing!");

        scanner.close();
    }
}
