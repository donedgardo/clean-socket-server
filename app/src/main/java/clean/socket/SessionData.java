package clean.socket;

import java.util.Random;

public class SessionData {
    public static final int MAX_ATTEMPTS = 7;
    public static final int MAX_NUMBER_TO_GUESS = 100;
    public int guessAttemptCount;
    public int numberToGuess;
    public String guessMessage = "";

    public SessionData() {
        this.guessAttemptCount = 0;
        generateGuessNumber();
    }

    public void attemptGuess(String guess) {
        if (guess == null) {
            guessMessage = "";
            return;
        }
        guessAttemptCount++;
        if (isCorrectGuess(guess)) {
            resetSessionData();
            guessMessage = guess + " is correct!";
        } else if (wasLastAttempt()) {
            guessMessage = "No more guesses left. The number to guess was " + numberToGuess +
                    " . Play Again!";
            resetSessionData();
        } else {
            updateGuessMessage(guess);
        }
    }

    private void updateGuessMessage(String guess) {
        String guessRange;
        if (Integer.parseInt(guess) < numberToGuess) {
            guessRange = "bigger";
        } else {
            guessRange = "smaller";
        }
        guessMessage = guess + " is incorrect. The number to guess is " +
                guessRange + " than " + guess + ". You have " +
                (MAX_ATTEMPTS - guessAttemptCount) + " attempts left.";
    }

    private void resetSessionData() {
        guessAttemptCount = 0;
        generateGuessNumber();
    }

    private boolean wasLastAttempt() {
        return guessAttemptCount >= MAX_ATTEMPTS;
    }

    private boolean isCorrectGuess(String guess) {
        return Integer.parseInt(guess) == numberToGuess;
    }

    private void generateGuessNumber() {
        Random r = new Random();
        this.numberToGuess = r.nextInt(MAX_NUMBER_TO_GUESS) + 1;
    }
}
