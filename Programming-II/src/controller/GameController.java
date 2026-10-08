package controller;


import java.util.ArrayList;

import model.Dictionary;
import model.Level;
import model.Player;
import utils.FileHandler;
import view.*;

/**
 * GameController class is responsible for controlling the game flow.
 */
public class GameController {

    public static final int HINT_COST = 100;
    public static final int WORD_REWARD = 10;
    public static final int MAX_LEVELS = 15;
    public static final int START_COINS = 100; 

    private Player player;
    private Dictionary dictionary;
    private ArrayList<Level> levels;
    private int levelIndex;
    private View view;


    /**
     * Constructor for New Game
     * Used when the player wants to start a new game
     * If the player name is null or empty, the player name is set to "Jogador"
     * 
     * @param playerName - The name of the player
     * @param isLevel - If the player wants to play with the predefined levels
     */
    public GameController(String playerName, boolean isLevel) {
        if (playerName == null || playerName.isEmpty()) {
            playerName = "Jogador";
        }

        this.player = new Player(START_COINS, playerName, isLevel); 
        this.dictionary = FileHandler.readDictionary();

        if (isLevel) {
            this.levels = FileHandler.loadLevels(0);
        }
        else {
            this.levels = this.dictionary.generateLevels(MAX_LEVELS, 0);
            FileHandler.saveLevels(this.levels);
        }

        this.levelIndex = 0;
    }

    
    /**
     * Constructor for Continue Game
     * Used when the player wants to continue a game
     * 
     * @param saveFilePath - The path to the save file
     */
    public GameController (String saveFilePath) {
        this.player = FileHandler.loadPlayerData(saveFilePath);
        this.dictionary = FileHandler.readDictionary();
        this.levels = new ArrayList<>();
        Level currentLevel = FileHandler.loadLastLevel(saveFilePath);
        this.levels.add(currentLevel);

        if(this.player.isLevel()){
            this.levels.addAll(FileHandler.loadLevels(currentLevel.getLevelNumber()));
        }else{
            int nLevels = currentLevel.getLevelNumber();
            ArrayList<Level> newLevels = this.dictionary.generateLevels(MAX_LEVELS - nLevels, nLevels);
            FileHandler.saveLevels(newLevels);
            this.levels.addAll(newLevels);
        }
        this.levelIndex = 0;
    }


    /**
     * Starts the game
     * The game can be started in console view or GUI view
     * 
     * @param viewType - The type of view to start the game: 0 for console view, 1 for GUI view
     */
    public void startGame(int viewType) {
        //Start the game in console view
        if (viewType == 0) {
            this.view = new ConsoleView();
            this.play();
        }
        //Start the game in GUI view
        else if (viewType == 1) {
            this.view = new GraphicalView(this.player.getName(), this.levels.get(this.levelIndex).getLevelNumber(), this.player.getCoins());
            this.play();
        }
        else {
            throw new IllegalArgumentException("Tipo de vista inválido.");
        }

    }


    /**
     * Level Up
     * Increases the level index and prints a message
     */
    private void levelUp() {
        this.levelIndex++;
        if (this.view instanceof GraphicalView) {
            this.view.printMessage("Nível Completo!");
        }
        else{
            this.view.printMessage("\n\n*** Nível Completo! ***\n\n");
        }
    }


    /**
     * Play the game
     * The game is played level by level
     * Main loop of the game
     */
    private void play() {
        Level currentLevel = this.levels.get(this.levelIndex);

        while (this.levelIndex < this.levels.size()) {

            currentLevel = this.levels.get(this.levelIndex);

            this.view.printLevel(this.player.getName(), currentLevel.getLevelNumber(), currentLevel.getLettersString(), currentLevel.getWordsToGuess(), this.player.getCoins());
            String guess = this.view.askGuess();
            if(guess.matches("[0]")){
                if(this.saveGame()){
                    break;
                }
                continue;
            }else if(guess.matches("[1]")){
                this.askHint();
                continue;
            }

            if(!currentLevel.isValidInput(guess)){
                this.view.printMessage("Input inválido.");
                continue;
            }
            
            if (currentLevel.checkWord(guess)) {
                this.player.addCoins(WORD_REWARD);
                view.printMessage("Palavra correta! +" + WORD_REWARD + " moedas");
            }

            else if(this.dictionary.contains(guess)) {
                    if (currentLevel.checkDictGuessed(guess)){
                        view.printMessage("A palavra não está na lista de palavras a adivinhar mas está correta. +" + WORD_REWARD + " moedas");
                        player.addCoins(WORD_REWARD);
                    }
                    else {
                        view.printMessage(guess + " já foi adivinhado.");
                    }
            }
            else {
                view.printMessage("Palavra incorreta.");
            }

            if (currentLevel.isCompleted()) {
                this.levelUp();
            }    
        }
        
        this.view.printLevel(this.player.getName(), currentLevel.getLevelNumber(), currentLevel.getLettersString(), currentLevel.getWordsToGuess(), this.player.getCoins());

        if(this.levelIndex >= this.levels.size()){
            this.view.printMessage("Parabéns! Completou todos os níveis.");
        }else{
            if(this.view instanceof GraphicalView){
                ((GraphicalView)this.view).close();
            }
        }
    }


    /**
     * Save the game
     * 
     * @return true if the game was saved successfully, false otherwise
     */
    private boolean saveGame() {
        if(this.view.askConfirmation("sair")){
            try{
                String dir = FileHandler.saveGame(this.player, this.levels.get(this.levelIndex));
                this.view.printMessage("O jogo foi guardado em: " + dir);
                return true;
            }
            catch(RuntimeException e){
                System.out.println(e.getMessage());
            }
        }
        return false;
    }


    /**
     * Ask for a hint
     * Asks the player for a hint and checks if the hint is valid
     * If the hint is valid, the the hnt is applied and the player pays the hint cost
     */
    private void askHint(){
        if(this.view.askConfirmation("uma dica")){
            try{
                this.player.addCoins(-HINT_COST); 
            }catch (IllegalArgumentException e){
                this.view.printMessage("Não tem moedas suficientes para pedir uma dica");
                return;
            }

            int[] hintInfo = this.view.askHintInfo();

            try{
                this.levels.get(this.levelIndex).getHint(hintInfo[0], hintInfo[1]);
            }
            catch (IllegalArgumentException e){
                this.view.printMessage("");
                this.view.printMessage(e.getMessage());
                this.player.addCoins(HINT_COST); //Reembolsar o jogador se a dica não for válida
                return;
            }

            if (this.levels.get(this.levelIndex).isWordGuessed(hintInfo[0])){ // Check if the word has been guessed when the last letter is revealed

                this.view.printMessage("\nPalavra correta! +" + WORD_REWARD + " moedas\n");
                this.player.addCoins(WORD_REWARD);

                if (this.levels.get(this.levelIndex).isCompleted()){
                    this.levelUp();
                }
            }
        }
    }
}