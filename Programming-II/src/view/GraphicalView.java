package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GraphicalView class is responsible for the graphical interface of the game.
 */
public class GraphicalView implements View {

    public final static String FONT = "Arial";
    private final static Color BACKGROUND_COLOR = new Color(248, 225, 183);

    private JFrame frame;

    // Output components
    private JLabel playerName;
    private JLabel levelNumber;
    private JTextArea gameLevel;
    private JTextArea gameMessages;
    private JLabel coinsNumber;

    // Input components
    private JButton submitButton;
    private JButton hintButton;
    private JButton saveButton;
    private JTextField inputField;
    
    private String input;

    /**
     * Constructor for the GraphicalView class.
     * 
     * @param playerName - The name of the player
     * @param levelNumber - The number of the current level
     * @param coins - The number of coins of that player
     */
    public GraphicalView (String playerName, int levelNumber, int coins){

        this.initFrame();
        this.initWelcomePanel(playerName);
        this.initGameFields(playerName, levelNumber, coins);

        this.frame.setVisible(true);
        this.frame.setAlwaysOnTop(true);
        this.frame.toFront();
        this.frame.requestFocus();
        this.frame.setAlwaysOnTop(false);
    }


    /**
     * Method that initializes the components of the game, and adds them to the frame.
     * 
     * @param playerName - The name of the player
     * @param levelNumber - The number of the current level
     * @param coins - The number of coins of that player
     */
    private void initGameFields(String playerName, int levelNumber, int coins) {
        JPanel gamePanel = new JPanel(new GridBagLayout());

        gamePanel.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 2, 5, 2); // Add padding

        // Create the Player Info Row
        JPanel playerNamePanel = this.createNamePanel(playerName);
        playerNamePanel.setBackground(BACKGROUND_COLOR);

        // Create Coins number panel
        JPanel coinsPanel = this.createCoinsPanel(coins);
        coinsPanel.setBackground(BACKGROUND_COLOR);

        // Initialize the output and input fields
        this.initOutputFields(playerName, levelNumber);
        this.initInputComponents();


        // Add the player name panel to the frame
        gbc.gridx = 0; //Column number
        gbc.gridy = 0; //Row number
        gbc.gridwidth = 1; //Number of columns the component will span
        gbc.gridheight = 1; //Number of rows the component will span
        gbc.fill = GridBagConstraints.HORIZONTAL; //Fill the component horizontally
        gamePanel.add(playerNamePanel, gbc);


        // LEVEL NUMBER
        gbc.gridx = 2;
        gbc.gridy = 0;
        gamePanel.add(this.levelNumber, gbc);

        // GAME LEVEL TEXT AREA
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gamePanel.add(this.gameLevel, gbc);


        // GAME MESSAGES
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gamePanel.add(this.gameMessages, gbc);

        // INPUT FIELD
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gamePanel.add(this.inputField, gbc);
    

        // SUBMIT BUTTON
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gamePanel.add(this.submitButton, gbc);


        //COIN ROW
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gamePanel.add(coinsPanel, gbc);


        // HINT BUTTON
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gamePanel.add(this.hintButton, gbc);


        //SAVE BUTTON
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gamePanel.add(this.saveButton, gbc);


        // Add grid panel to frame
        frame.add(gamePanel);
    }

    /**
     * Initializes the input components of the game.
     * Each button has his own ActionListener logic.
     * 
     */
    private void initInputComponents() {

        ActionListener submitListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                input = inputField.getText();
                inputField.setText("");
                synchronized (GraphicalView.this) {
                    GraphicalView.this.notify();
                }
            }
        };          
        // INPUT FIELD
        this.inputField = new JTextField(20);
        this.inputField.setFont(new Font(FONT, Font.PLAIN, 18));
        this.inputField.addActionListener(submitListener);

        // SUBMIT BUTTON
        this.submitButton = new JButton("Submeter");
        this.submitButton.addActionListener(submitListener);
        
        //HINT BUTTON
        ImageIcon hintIcon = new ImageIcon(getClass().getResource("/images/idea.png"));
        Image hintImage = hintIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        ImageIcon scaledHintIcon = new ImageIcon(hintImage);
        this.hintButton = new JButton(scaledHintIcon);
        this.hintButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                input = "1";
                synchronized (GraphicalView.this) {
                    GraphicalView.this.notify();
                }
            }
        });

        //SAVE BUTTON
        this.saveButton = new JButton("Guardar e Sair");
        this.saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                input = "0";
                synchronized (GraphicalView.this) {
                    GraphicalView.this.notify();
                }
            }
        });
        
        
    }

    /**
     * Creates the Coins panel with the number of coins of the player.
     * 
     * @param coins - The number of coins of the player
     * @return JPanel - The panel with the coins number
     */
    private JPanel createCoinsPanel (int coins) {

        // Create the Coins number panel
        JPanel coinsPanel = new JPanel();
        coinsPanel.setLayout(new BoxLayout(coinsPanel, BoxLayout.LINE_AXIS));

        // Create the Piggy Icon
        ImageIcon coinIcon = new ImageIcon(getClass().getResource("/images/piggyBank.png"));
        Image scaledCoinImage = coinIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        JLabel coinImage = new JLabel(new ImageIcon(scaledCoinImage));
        coinsPanel.add(coinImage);

        // Add some padding between the icon and the number
        coinsPanel.add(Box.createRigidArea(new Dimension(8, 0)));

        // Create the Coins number label    
        this.coinsNumber = new JLabel(Integer.toString(coins), JLabel.CENTER);
        this.coinsNumber.setFont(new Font(FONT, Font.BOLD, 18));
        this.coinsNumber.setPreferredSize(new Dimension(50, 32)); 
        this.coinsNumber.setHorizontalAlignment(SwingConstants.CENTER);
        this.coinsNumber.setVerticalAlignment(SwingConstants.CENTER);
        coinsPanel.add(this.coinsNumber);

        return coinsPanel;
    }

    /**
     * Initializes the output fields of the game.
     * 
     * @param playerName - The name of the player
     * @param levelNumber - The number of the current level
     */
    private void initOutputFields (String playerName, int levelNumber) {

        // Create the Level Info
        this.levelNumber = new JLabel("Nível " + levelNumber);
        this.levelNumber.setFont(new Font(FONT, Font.BOLD, 18));
        this.levelNumber.setHorizontalAlignment(SwingConstants.LEFT);

        // Create the Game Output Fields
        this.gameLevel = new JTextArea(8, 20); 
        this.gameLevel.setEditable(false);
        this.gameLevel.setFont(new Font(FONT, Font.PLAIN, 18));
        this.gameLevel.setLineWrap(true);
        this.gameLevel.setWrapStyleWord(true);
        this.gameLevel.setPreferredSize(new Dimension(400, 200)); 

        // Create the Game Messages Field
        this.gameMessages = new JTextArea(2, 20);
        this.gameMessages.setEditable(false);
        this.gameMessages.setLineWrap(true);
        this.gameMessages.setWrapStyleWord(true);
        this.gameMessages.setPreferredSize(new Dimension(400, 10));
        //Align
        this.gameMessages.setAlignmentY(SwingConstants.CENTER);
        this.gameMessages.setAlignmentX(SwingConstants.CENTER);

        //Change the background color
        this.gameMessages.setBackground(new Color(182, 203, 189));

    }

    /**
     * Creates the Player Name panel with the name of the player.
     * 
     * @param playerName - The name of the player
     * @return JPanel - The panel with the player name
     */
    private JPanel createNamePanel (String playerName){

        // Create the Player Name Panel
        JPanel playerNamePanel = new JPanel();
        playerNamePanel.setLayout(new BoxLayout(playerNamePanel, BoxLayout.LINE_AXIS));

        // Create the Player Icon
        ImageIcon playerIcon = new ImageIcon(getClass().getResource("/images/profile.png"));
        Image scaledImage = playerIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        JLabel playerImage = new JLabel(new ImageIcon(scaledImage));
        playerNamePanel.add(playerImage);

        // Add some padding between the icon and the name
        playerNamePanel.add(Box.createRigidArea(new Dimension(8, 0)));

        // Create the Player Name label
        this.playerName = new JLabel(playerName);
        this.playerName.setFont(new Font(FONT, Font.BOLD, 18));
        playerNamePanel.add(this.playerName);

        return playerNamePanel;
    }

    /**
     * Initializes the frame of the game.
     */
    private void initFrame() {

        this.frame = new JFrame("Guru2P2");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(512, 512); // Set the window size
        this.frame.setLocationRelativeTo(null); // Center the window
        this.frame.setLayout(new BoxLayout(this.frame.getContentPane(), BoxLayout.PAGE_AXIS));
        this.frame.setIconImage(new ImageIcon(getClass().getResource("/images/GameIcon.jpg")).getImage());

    }

    /**
     * Initializes the welcome panel of the game.
     * 
     * @param playerName - The name of the player to be welcomed
     */
    private void initWelcomePanel(String playerName) {
        // Create the initial popup window
        JDialog startDialog = new JDialog(this.frame, "Guru2P2", true);
        startDialog.setSize(300, 200);
        startDialog.setLocationRelativeTo(frame);
        startDialog.setLayout(new BorderLayout());
        startDialog.getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Add the welcome message
        JLabel welcomeLabel = new JLabel("Bem-Vindo, " + playerName, JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        startDialog.add(welcomeLabel, BorderLayout.NORTH);

        //Add the logo
        java.net.URL logoUrl = getClass().getResource("/images/palavraGuruLogo.png");
        ImageIcon logoIcon = null;
        if (logoUrl != null) {
            logoIcon = new ImageIcon(logoUrl);
        } else {
             // Fallback or empty icon if not found to prevent crash
            logoIcon = new ImageIcon(); 
        }
        
        Image image = logoIcon.getImage();
        Image scaledImage = image.getScaledInstance(250, 100, Image.SCALE_SMOOTH);
        ImageIcon scaledLogoIcon = new ImageIcon(scaledImage);
    
        JLabel logoLabel = new JLabel(scaledLogoIcon, JLabel.CENTER);
        startDialog.add(logoLabel, BorderLayout.CENTER);

        JButton startButton = new JButton("Começar");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startDialog.dispose();
            }
        });
        startDialog.add(startButton, BorderLayout.SOUTH);

        startDialog.setAlwaysOnTop(true);
        startDialog.setVisible(true); 

    }

    /**
     * Method that asks the player for a guess, awaiting for the player to click the submit button.
     * 
     * @return The guess of the player or the command to leave or ask for a hint
     */
    public String askGuess() {
        // Wait for a button to be clicked 
        synchronized (this) { // Synchronize to ensure that the userInput is not read before it is set
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Return the input when the button is clicked
        return this.input != null ? this.input.toUpperCase() : "";
    }

    /**
     * Method to print a message to the game messages field.
     * 
     * @param message - The message to print
     */
    public void printMessage(String message) {
        gameMessages.setText(message);
    }

    /**
     * Method to print the state of the game.
     * The level information are printed in the game level field.
     * The 
     * 
     * @param player - The name of the player
     * @param level - The number of the current level
     * @param letters - The letters of the level
     * @param words - The words of the level
     * @param coins - The number of coins of the player
     */
    public void printLevel(String player, int level, String letters, String words, int coins) {
        this.playerName.setText(player);
        this.levelNumber.setText("Nível " + level);
        this.coinsNumber.setText(Integer.toString(coins));
        gameLevel.setText("");
        gameLevel.append("   " + letters + "\n");
        gameLevel.append(words + "\n");
    }

    /**
     * Method that asks the player for a confirmation to an action.
     * 
     * @param text - The action to confirm
     * 
     * @return True if the player confirms the action, false otherwise.
     */
    public boolean askConfirmation(String text) {
        String message = "Tem a certeza que deseja " + text + "?";
    
        String[] options = {"Sim", "Não"};
        
        java.net.URL gameIconUrl = getClass().getResource("/images/GameIcon.jpg");
        ImageIcon gameIcon = null; // Declare variable
        if(gameIconUrl != null){
             gameIcon = new ImageIcon(gameIconUrl); 
        } else {
             gameIcon = new ImageIcon(); // Fallback
        }

        Image image = gameIcon.getImage();
        Image scaledImage = image.getScaledInstance(32, 32, Image.SCALE_SMOOTH); 
        ImageIcon scaledGameIcon = new ImageIcon(scaledImage);
    
        int option = JOptionPane.showOptionDialog(
            this.frame, // Parent component
            message, // Message
            "Confirmação", // Title
            JOptionPane.YES_NO_OPTION, // Option type
            JOptionPane.QUESTION_MESSAGE, // Message type
            scaledGameIcon, // Icon
            options, // Custom button texts "sim" and "não"
            options[0] // Default option
        );
        
        return option == JOptionPane.YES_OPTION;
    }

    /**
     * Method that creates a dialog to ask the player for the hint information.
     * 
     * @return An array with the word number and the letter number of the hint.
     */
    public int[] askHintInfo() {
        int[] hint = new int[2];
    
        // Create a new dialog
        JDialog hintDialog = new JDialog(frame, "Pedir Dica", true);
        hintDialog.setSize(400, 200);
        hintDialog.setLocationRelativeTo(frame);
        hintDialog.setLayout(new GridBagLayout());
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        // Create input fields and labels
        JLabel wordNumberLabel = new JLabel("Indique o número da palavra:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        hintDialog.add(wordNumberLabel, gbc);
    
        JTextField wordNumberField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 0;
        hintDialog.add(wordNumberField, gbc);
    
        JLabel letterNumberLabel = new JLabel("Indique o número da letra:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        hintDialog.add(letterNumberLabel, gbc);
    
        JTextField letterNumberField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 1;
        hintDialog.add(letterNumberField, gbc);
    
        // Create hint submit button
        JButton submitButton = new JButton("Pedir Dica");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        hintDialog.add(submitButton, gbc);
    
        // Add ActionListener to the submit button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    hint[0] = Integer.parseInt(wordNumberField.getText());
                    hint[1] = Integer.parseInt(letterNumberField.getText());
                    hintDialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(hintDialog, "Erro ao ler a entrada. Tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    
        // Show the dialog
        hintDialog.setVisible(true);
    
        return hint;
    }

    /**
     * Method that force closes the window.
     * It's used when the player wants to save the game.
     */
    public void close() {
        try{
            Thread.sleep(2000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        this.frame.dispose();
    }

}
