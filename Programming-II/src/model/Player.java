package model;

/**
 * Class that represents a player in the game.
 */
public class Player {
    private String name;
    private int coins;
    private boolean isLevel; //true = normal mode; false = dictionary mode


    /**
     * Constructor for the Player class.
     * Used when loading a player from a file.
     * 
     * @param coins - number of coins the player has
     * @param name - name of the player
     * @param isLevel - true if the player is in normal mode, false if the player is in dictionary mode
     */
    public Player(int coins, String name, boolean isLevel){
        if(coins < 0){
            throw new IllegalArgumentException("Não pode ter menos de 0 moedas.");
        }
        this.coins = coins;
        this.name = name;
        this.isLevel = isLevel;
    }


    /**
     * Constructor for the Player class.
     * 
     * @param name - name of the player
     * @param isLevel - true if the player is in normal mode, false if the player is in dictionary mode
     */
    public Player(String name, boolean isLevel){
        this.coins = 100;
        this.name = name;
        this.isLevel = isLevel;
    }


    /**
     * Method that returns the number of coins the player has.
     * 
     * @return number of coins the player has
     */
    public int getCoins(){
        return this.coins;
    }


    /**
     * Method that returns the name of the player.
     * 
     * @return name of the player
     */
    public String getName(){
        return this.name;
    }


    /**
     * Method that returns the mode the player is in.
     * 
     * @return true if the player is in normal mode, false if the player is in dictionary mode
     */
    public boolean isLevel(){
        return this.isLevel;
    }


    /**
     * Method that adds coins to the player.
     * 
     * @param coins - number of coins to add
     */
    public void addCoins(int coins){
        int coinDiff = this.coins + coins;
        if(coinDiff < 0){
            throw new IllegalArgumentException("Não pode ter menos de 0 moedas.");
        }
        this.coins+=coins;
    }
}