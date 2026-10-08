package view;

import java.util.Scanner;

/**
 * Class that represents the view of the game in the console.
 */
public class ConsoleView implements View {
    
    private Scanner sc;

    /**
     * Constructor for the ConsoleView class.
     */
    public ConsoleView() {
        this.sc = new Scanner(System.in);
    }

    /**
     * Method to print a message to the console.
     * 
     * @param message - message to print
     */
    public void printMessage (String message) {
        System.out.println(message);
    }

    /**
     * Method that asks the player for a guess.
     * 
     * @return The guess of the player or the command to leave or ask for a hint.
     */
    public String askGuess() {
        System.out.println("0 para sair e guardar; 1 para pedir dica");
        System.out.print ("Insira uma nova palavra: ");
        return sc.nextLine().toUpperCase();
    }

    /**
     * Method that asks the player for a confirmation to an action.
     * 
     * @param action - action to confirm
     * 
     * @return True if the player confirms the action, false otherwise.
     */
    public boolean askConfirmation(String action){
        System.out.print("Tem a certeza que deseja " + action + "? (S/N): ");
        if(this.sc.nextLine().toUpperCase().matches("[S]")){
            return true;
        }
        return false;
    }

    /**
     * Method that asks the player for the number of word and letter to give a hint.
     * 
     * @return An array with the number of the word and the number of the letter.
     */
    public int[] askHintInfo(){
        int[] hint = new int[2];
        try {
            System.out.print("Indique o número da palavra: ");
            hint[0] = this.sc.nextInt();
            System.out.print("Indique o número da letra: ");
            hint[1] = this.sc.nextInt();
        } catch (Exception e) {
            System.out.println("Erro ao ler a entrada. Tente novamente.");
            this.sc.nextLine(); // Clear the buffer
            return askHintInfo();
        }
        this.sc.nextLine(); // Clear the buffer
        return hint;
    }

    /**
     * Method to print the state of the game to the console.
     * 
     * @param player - the name of the player
     * @param level - the current level of the game
     * @param letters - the letters of the current level
     * @param words - the words to guess of that level
     * @param coins - the number of coins of the player
     */
    public void printLevel (String player, int level, String letters, String words, int coins) {
        String s = "--------------------------------\n\n" +
                   "Jogador: " + player + "\n" +
                   "Moedas: " + coins + "\n" +
                   "Nível " + level + "\n" +
                   //"\n" +
                   "   " +
                   letters + "\n" +
                   words + "\n" +
                   "--------------------------------";
        System.out.println(s);
    }

}