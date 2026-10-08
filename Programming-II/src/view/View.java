package view;

/**
 * Interface that represents the view of the game.
 */
public interface View {
    String askGuess();
    void printMessage(String message);
    void printLevel(String player, int level, String letters, String words, int coins);
    boolean askConfirmation(String message);
    int[] askHintInfo();
}
