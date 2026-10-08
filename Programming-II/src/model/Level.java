package model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


/**
 * Class that represents a level of the game.
 */
public class Level {

    private int levelNumber;
    private String letters;
    private ArrayList<Word> wordsToGuess;
    private HashSet<String> dictGuessed;
    private boolean isCompleted;


    /**
     * Constructor for the Level class.
     * 
     * @param level - level number
     * @param wordsToGuess - array of Words to guess
     * @param letters - array of letters
     * 
     * @throws IllegalArgumentException if the level is less than 1, the wordsToGuess is null or empty or the letters is null or empty
     */
    public Level(int level, ArrayList<Word> wordsToGuess, String[] letters){
        if(level < 1){
            throw new IllegalArgumentException("O nível tem de ser superior a 0.");
        }
        if(wordsToGuess == null || wordsToGuess.isEmpty()){
            throw new IllegalArgumentException("A lista de palavras a adivinhar não pode ser nula ou vazia.");
        }
        if(letters == null || letters.length == 0){
            throw new IllegalArgumentException("A lista de letras não pode ser nula ou vazia.");
        }
        this.levelNumber = level;
        this.wordsToGuess = wordsToGuess;
        this.dictGuessed = new HashSet<>();

        this.letters = "";
        for(String s : letters){
            this.letters += s;
        }

        this.isCompleted = false;
    }


    /**
     * Constructor for the Level class to use when loading a game.
     * 
     * @param level - level number
     * @param wordsToGuess - array of Words to guess
     * @param letters - array of letters
     * @param dictGuessed - HashSet of guessed words
     * 
     * @throws IllegalArgumentException if the level is less than 1, the wordsToGuess is null or empty, the letters is null or empty or the dictGuessed is null
     */
    public Level(int level, ArrayList<Word> wordsToGuess, String[] letters, HashSet<String> dictGuessed){
        this(level, wordsToGuess, letters);
        if(dictGuessed == null){
            throw new IllegalArgumentException("O dicionário de palavras adivinhadas não pode ser nulo.");
        }
        this.dictGuessed = dictGuessed;
    }


    /**
     * Method to get the letters of the level.
     * 
     * @return the letters of the level
     */
    public String getLetters() {
        return this.letters;
    }


    /**
     * Method to get the level number.
     * 
     * @return the level number
     */
    public int getLevelNumber() {
        return this.levelNumber;
    }


    /**
     * Checks if the input from the user is valid.
     * It checks if the input is null, empty or has more characters than the letters.
     * It also checks if the input has more of a character than the letters.
     * 
     * 
     * @param input - the input from the user
     * 
     * @return true if the input is valid, false otherwise
     */
    public boolean isValidInput(String input) {
        if (input == null || input.isEmpty() || input.length() > this.letters.length()) {
            return false;
        }

        // Get the count of each character in the letters
        HashMap<Character, Integer> charCount = new HashMap<>(this.letters.length());
        for (char c: letters.toCharArray()) {
            charCount.put(c, charCount.getOrDefault(c, 0) + 1);
        }

        // Check if the input has more of a character than the letters
        for (char c: input.toCharArray()) {
            int count = charCount.getOrDefault(c, 0) - 1; // Get the count of the character and decrement it, if it doesn't exist it's 0
            if (count < 0) {
                return false;
            }
            charCount.put(c, count);
        }

        return true;

    }


    /**
     * Method to get the words to guess in a string format.
     * 
     * @return the words to guess
     */
    public String getWordsToGuess() {
        String s = "";
        int wordCounter = 1;
        for (Word w: this.wordsToGuess) {
            s += wordCounter + ": " + w.getWord() + "\n";
            wordCounter++;
        }

        return s;
    }


    /**
     * Checks if a word with a certain number was guessed.
     * 
     * @param wordNumber - the number of the word
     * @return true if the word was guessed, false otherwise
     */
    public boolean isWordGuessed(int wordNumber) {
        wordNumber--; //Número -> índice
        return this.wordsToGuess.get(wordNumber).isGuessed();
    }


    /**
     * Checks if a word has already been guessed.
     * 
     * @param word - the word to check
     * @return true if the word was guessed, false otherwise
     */
    public boolean checkDictGuessed (String word) {

        if (!this.dictGuessed.contains(word)){
            this.dictGuessed.add(word);
            return true;
        }

        return false;
    }


    /**
     * Checks if a word is in the list of words to guess.
     * If the word is in the list and hasn't been guessed yet, it sets the word as guessed.
     * 
     * @param word - the word to check
     * @return true if the word is in the list and hasn't been guessed yet, false otherwise
     */    
    public boolean checkWord(String word){
        for(Word s: this.wordsToGuess){
            if(s.equals(word) && !s.isGuessed()){
                s.setGuessed(true);
                this.dictGuessed.add(word); // To avoid the same word being guessed twice
                return true;
            }
        }
        return false;
    }


    /**
     * Checks if the level is completed.
     * 
     * @return true if the level is completed, false otherwise
     */
    public boolean isCompleted(){
        for(Word s: this.wordsToGuess){
            if(!s.isGuessed()){
                return this.isCompleted;
            }
        }
        this.isCompleted = true;
        return true;
    }


    /**
     * Method to get the letters of the level in a string format.
     * Used for the save method.
     * 
     * @return the letters of the level in a string format
     */
    public String getLettersString(){
        String s = "";
        for(int i = 0; i < this.letters.length(); i++){
            s += this.letters.charAt(i) + " ";
        }
        return s;
    }


    /**
     * Method to get the words to guess in a string format.
     * Used for the save method.
     * 
     * @return the words to guess in a string format
     */
    public String getWordsString() {
        String s = "";
        for(Word w: wordsToGuess){
            if(w.isGuessed()){
                s += w + "#\n";
            }else{
                s += w + "\n";
            }
        }
        return s;
    }


    /**
     * Method to get the guessed words in a string format.
     * Used for the save method.
     * 
     * @return the guessed words in a string format
     */
    public String getDictGuessedString() {
        String s = "";
        for(String w: dictGuessed){
            s += w + "%\n";
        }
        return s;
    }


    /**
     * Method to get a hint for a word.
     * It gets the hint for a word with a certain number and letter.
     * 
     * @param wordNumber - the number of the word
     * @param letterNumber - the number of the letter
     * 
     * @return the hint for the word
     * 
     * @throws IllegalArgumentException if the wordNumber is less than 1 or greater than the number of words to guess or if the word was already guessed
     */
    public String getHint (int wordNumber, int letterNumber) throws IllegalArgumentException {
        wordNumber--; //Número -> índice
        letterNumber--; //Número -> índice

        if (wordNumber < 0 || wordNumber >= wordsToGuess.size()) {
            throw new IllegalArgumentException("O número da palavra tem de ser superior a 0 e inferior ou igual ao número de palavras.");
        }

        if (wordsToGuess.get(wordNumber).isGuessed()) {
            throw new IllegalArgumentException("A palavra já foi adivinhada.");
        }

        return wordsToGuess.get(wordNumber).applyHint(letterNumber);
    }


    /**
     * Method to get the level in a string format.
     * 
     * @return the level in a string format
     */
    public String toString(){
        String s = "";
        String letter = "";
        for(Word w: wordsToGuess){
            s += "\n" + w;
        }
        letter = this.getLettersString();
        
        return "Nível " + levelNumber + ":\nLetras: " + letter + "\nPalavras a adivinhar: "+ s;
    }
}