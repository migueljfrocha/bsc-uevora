package utils;

import java.io.*;
import java.lang.IllegalArgumentException;
import java.util.ArrayList;
import java.util.HashSet;

import model.Dictionary;
import model.Level;
import model.Player;
import model.Word;

import java.lang.RuntimeException;


/**
 * FileHandler class is responsible for reading and writing all the game files.
 */
public class FileHandler {

    // Game files
    public static final String DICTIONARY_NAME = "portuguese-large.txt";
    public static final String LEVELS = "ficheiro-niveis.txt";

    // Save/Load files
    public static final String GEN_LEVELS = "ficheiro-niveis-gerados.txt";
    public static final String LAST_LEVEL = "last_level.txt";
    public static final String SAVE_FILE = "player_save.txt";


    /**
     * Reads the dictionary file and returns a Dictionary object.
     * It ignores words with hyphens.
     * 
     * @return Dictionary object that contains all the words from the dictionary file.
     */
    public static Dictionary readDictionary() {
        Dictionary dictionary = new Dictionary();

        try {
            InputStream is = FileHandler.class.getResourceAsStream("/resources/" + DICTIONARY_NAME);
            if (is == null) {
                throw new IOException("Resource not found: " + DICTIONARY_NAME);
            }
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(is));
            String line;

            line = bufReader.readLine();
            while (line != null) {
                
                if (!line.isEmpty()) {
                    // Ignorar palavras com hífen 
                    if (line.contains("-")) {
                        line = bufReader.readLine();
                    }
                    else  {
                            line = line.trim().toUpperCase();
                            dictionary.add(line);
                            line = bufReader.readLine();
                    }
                } 
                else {
                    line = bufReader.readLine();
                }
            }
        }
        catch (IOException e) {
            System.out.println("Erro ao ler o ficheiro de dicionário: " + DICTIONARY_NAME);
            System.out.println("Por favor, verifique se o ficheiro existe no diretório do programa.");
            System.exit(1);
        }

        return dictionary;
    }


    /**
     * Reads the levels file and returns an ArrayList of Level objects.
     * 
     * @param startLevel - The level to start reading from
     * @return ArrayList of Level objects that contains all the levels from the levels file.
     */
    public static ArrayList<Level> loadLevels(int startLevel){
        ArrayList<Level> levels = new ArrayList<>();
        try {
            java.io.InputStream is = FileHandler.class.getResourceAsStream("/resources/" + LEVELS);
            if (is == null) {
                throw new IOException("Resource not found: " + LEVELS);
            }
            BufferedReader bufReader = new BufferedReader(new java.io.InputStreamReader(is));
            int levelNumber = 1;
            ArrayList<Word> wordsToGuess = new ArrayList<>();
            String[] letters = null;

            String line;
            line = bufReader.readLine();

            // Skip levels until the startLevel
            while(line != null && levelNumber <= startLevel){
                line = line.trim();
                if(line.isEmpty()){
                    levelNumber++;
                }
                line = bufReader.readLine();
            }

            // Read levels in the file
            while(line != null){
                line = line.trim();
                if(!line.isEmpty()){
                    line = line.toUpperCase();
                    if(line.contains(" ")){
                        letters = line.split(" "); 
                    }
                    else{
                        wordsToGuess.add(new Word(line));
                    }
                }else{
                    levels.add(new Level(levelNumber, wordsToGuess, letters));
                    wordsToGuess = new ArrayList<>();
                    levelNumber++;
                }
                line = bufReader.readLine();
            }
            if (!wordsToGuess.isEmpty() && letters != null) {
                levels.add(new Level(levelNumber, wordsToGuess, letters));
            }
        }
        catch (IOException e) {
            System.out.println("Erro ao ler o ficheiro de níveis: " + LEVELS);
            System.out.println("Por favor, verifique se o ficheiro existe no diretório do programa.");
            System.exit(1);
        }
        catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return levels;
    }


    /**
     * Saves the player data to a file.
     * 
     * @param dirName - The directory name to save the player data
     * @param player - The player object to save
     */
    private static void savePlayerData (String dirName, Player player) {

        File outFile = new File(dirName + SAVE_FILE);

        if(!outFile.exists()){
            try{
                outFile.createNewFile();
            }
            catch (IOException e) {
                System.out.println("Erro ao criar o ficheiro do jogador.");
                System.out.println(e.getMessage());
                return;
            }
        }

        try(PrintWriter outStream = new PrintWriter(new FileOutputStream(outFile))){
            if(player.isLevel()) {
                outStream.println("N"); //Modo Normal
            } else {
                outStream.println("D"); //Modo Dicionario
            }
            outStream.println(player.getName());
            outStream.println(player.getCoins());
        } 
        catch (IOException e) {
            System.out.println("Erro ao guardar os dados do jogador.");
            System.exit(1);
        }
    }


    /**
     * Saves a level to a file.
     * The purpose of this method is to save the last level that is being played by the player.
     * 
     * @param level - The level object to save
     * @param dirPlayer - The directory name to save the last level
     */
    private static void saveLastLevel (Level level, String dirPlayer) {
        File outFile = new File(dirPlayer + LAST_LEVEL);

        if(!outFile.exists()){
            try{
                outFile.createNewFile();
            }
            catch (IOException e) {
                System.out.println("Erro ao criar o ficheiro do último nível.");
                System.out.println(e.getMessage());
                return;
            }
        }

        try(PrintWriter outStream = new PrintWriter(new FileOutputStream(outFile))){
            outStream.println(level.getLevelNumber());
            outStream.println(level.getLettersString());
            outStream.print(level.getWordsString());
            outStream.print(level.getDictGuessedString());
        } 
        catch (IOException e) {
            System.out.println("Erro ao guardar o último nível.");
            System.exit(1);
        }
    }


    /**
     * Saves the game/player data to a directory.
     * 
     * @param player - The player object to save
     * @param level - The level object to save
     * @return The directory name where the game was saved
     */
    public static String saveGame (Player player, Level level) {
        String dirName = player.getName() + "/";

        File f1 = new File(player.getName());
        if(!f1.exists()) {
            if(!f1.mkdir()) {
                throw new RuntimeException("Erro ao criar a pasta do jogador.");
            }
        }

        savePlayerData(dirName, player);
        saveLastLevel(level, dirName);
        return System.getProperty("user.dir") + "/" + dirName;
    }


    /**
     * Loads the player data from a directory.
     * 
     * @param dirName - The directory name to load the player data; In this case, is should be the player name
     * @return Player object that contains the player data
     */
    public static Player loadPlayerData(String dirName){
        File inFile = new File(dirName + "/" + SAVE_FILE);

        if(!inFile.exists()){
            throw new RuntimeException("Ficheiro de dados do jogador não encontrado.");
        }
        String name;
        int coins;
        boolean isLevel;

        try(BufferedReader bufReader = new BufferedReader(new FileReader(inFile))){
            String line = bufReader.readLine();
            if(line.equals("N")){
                isLevel = true;
            } else if(line.equals("D")){
                isLevel = false;
            } else {
                throw new RuntimeException("Erro ao ler o modo de jogo.");
            }

            name = bufReader.readLine();
            coins = Integer.parseInt(bufReader.readLine());
        }
        catch(IOException e){
            throw new RuntimeException("Erro ao ler o ficheiro de dados do jogador.");
        }
        Player player = new Player(coins, name, isLevel);
        return player;

    }


    /**
     * Loads the last level from a directory.
     * 
     * @param dirName - The directory name to load the last level
     * @return Level object that contains the last level data
     */
    public static Level loadLastLevel(String dirName){
        File inFile = new File(dirName + "/" + LAST_LEVEL);

        if(!inFile.exists()){
            throw new RuntimeException("Ficheiro do último nível não encontrado.");
        }

        int levelNumber;
        String letters;
        ArrayList<Word> wordsToGuess = new ArrayList<>();
        HashSet<String> dictGuessed = new HashSet<>();

        try(BufferedReader bufReader = new BufferedReader(new FileReader(inFile))){
            levelNumber = Integer.parseInt(bufReader.readLine());
            letters = bufReader.readLine().trim().toUpperCase() ;
            
            String line = bufReader.readLine();
            while(line != null){
                line = line.trim();
                if(!line.isEmpty()){
                    if(line.contains("#")){
                        line = line.replace("#", "");
                        wordsToGuess.add(new Word(line, true));
                    }
                    else if(line.contains("%")){
                        line = line.replace("%", "");
                        dictGuessed.add(line);
                
                    } else if(line.matches("[A-ZÀÁÂÃÉÊÍÓÔÕÚ]+")) {
                        wordsToGuess.add(new Word(line));
                    }
                }
                line = bufReader.readLine();
            }
        }
        catch(IOException e){
            throw new RuntimeException("Erro ao ler o ficheiro do último nível.");
        }
        Level level = new Level(levelNumber, wordsToGuess, letters.split(" "), dictGuessed);
        return level;

    }
    
    
    /**
     * Saves the levels from an array to a file.
     * 
     * @param level - The level object to save
     */
    public static void saveLevels(ArrayList<Level> level){
        File outFile = new File("resources/" + GEN_LEVELS);
        if(!outFile.exists()){
            try{
                outFile.createNewFile();
            }
            catch (IOException e) {
                System.out.println("Erro ao criar o ficheiro de níveis.");
                System.out.println(e.getMessage());
                return;
            }
        }

        try(PrintWriter outStream = new PrintWriter(new FileOutputStream(outFile))){
            for(Level l: level){
                outStream.println(l.getLettersString());
                outStream.println(l.getWordsString());
            }
        } 
        catch (IOException e) {
            System.out.println("Erro ao guardar o nível.");
            System.exit(1);
        }
    }
}
