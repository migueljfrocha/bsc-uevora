package model;



/**
 * Class that represents a word in the game.
 */
public class Word {

    private String word;
    private StringBuilder hiddenWord;
    private boolean isGuessed;


    /**
     * Constructor of the class Word.
     * 
     * @param word - The word
     */
    public Word(String word) {
        if(word == null || word.isEmpty()){
            throw new IllegalArgumentException("A palavra não pode ser nula ou vazia.");
        }
        this.word = word;
        hiddenWord = new StringBuilder();
        for (int i = word.length(); i > 0; i--) {
            hiddenWord.append("_");
        }

        this.isGuessed = false;
    }


    /**
     * Constructor of the class Word with the possibility of setting the word as guessed.
     * Used when loading a game.
     * 
     * @param word - The word
     * @param isGuessed - If the word is guessed
     */
    public Word(String word, boolean isGuessed) {
        if(word == null || word.isEmpty()){
            throw new IllegalArgumentException("A palavra não pode ser nula ou vazia.");
        }
        this.word = word;
        if(!isGuessed){
            hiddenWord = new StringBuilder();
            for (int i = word.length(); i > 0; i--) {
                hiddenWord.append("_");
            }
        }
        else {
            this.hiddenWord = new StringBuilder(word);
        }
        this.isGuessed = isGuessed;
    }


    /**
     * Method that returns the word if it has been guessed, otherwise returns the hidden word.
     * 
     * @return The word
     */
    public String getWord() {

        if (this.isGuessed) {
            return this.word;
        }
        else {
            return this.hiddenWord.toString().replaceAll(".", "$0 "); // All the characters in the hidden word are separated by a space
        }
    }


    /**
     * Method that applies a hint to the word.
     * 
     * @param index - The index of the letter to reveal
     * @return The hidden word with the revealed letter
     * 
     * @throws IllegalArgumentException - If the index is not valid or if the letter has already been revealed
     */
    public String applyHint(int index) {
        if (index < 0 || index >= this.word.length()) {
            throw new IllegalArgumentException("O índice não é válido.");
        }

        if (this.hiddenWord.charAt(index) != '_') {
            throw new IllegalArgumentException("A letra já foi revelada.");
        }

        this.hiddenWord.setCharAt(index, this.word.charAt(index));

        if (this.hiddenWord.indexOf("_") == -1) { // If there are no more "_" in the word, the word has been guessed
            this.isGuessed = true;
        }

        return this.hiddenWord.toString();
    }


    /**
     * Method that checks if the word has been guessed.
     * 
     * @return True if the word has been guessed, false otherwise
     */
    public boolean isGuessed() {
        return this.isGuessed;
    }


    /**
     * Method that sets the word as guessed.
     * 
     * @param guessed - True if the word has been guessed, false otherwise
     */
    public void setGuessed(boolean guessed) {
        this.isGuessed = guessed;
    }


    /**
     * Method that checks if the word is equal to a given string.
     * @param str - The string to compare
     * 
     * @return True if the word is equal to the string, false otherwise
     */
    public boolean equals(String str){
        return this.word.equals(str);
    }


    /**
     * Method that checks if the word is equal to another word.
     * 
     * @param w - The word to compare
     * 
     * @return True if the word is equal to the other word, false otherwise
     */
    public boolean equals(Word w){
        return this.word.equals(w.getWord());
    }


    /**
     * Method that returns the word.
     * 
     * @return The word
     */
    public String toString(){
        return this.word;
    }
}