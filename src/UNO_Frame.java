import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.util.ArrayList;

/**
 * The UNO_Frame class represents the main graphical user interface (GUI)
 * for the UNO Flip! game.
 * It serves as the concrete implementation of the {@link UNO_View} interface
 * and forms the “View” in (MVC) architecture.
 * This class is responsible for visually displaying the game state, player hands,
 * the play area, and score updates, as well as relaying user input to the
 * {@link UNO_Controller}.
 * Through its integration with {@link UNO_Game} (model) and
 * {@link UNO_Controller} (controller), this class updates the GUI in real time
 * as the game progresses, ensuring a clear separation between logic and presentation
 *
 * @author Ahmad El-Jabi 101303269
 * @author Atik Mahmud 101318070
 * @author Aryan Singh 101299776
 * @author Jonathan Gitej 101294584
 *
 * @version 2.0, November 10, 2025
 */
public class UNO_Frame extends JFrame implements UNO_View{
    private JPanel mainPanel; //container that holds everything
    private JPanel playerHandPanel; //bottom area showing the players cards
    private JPanel playAreaPanel; //middle area showing draw deck and top card
    private JPanel playerInfoPanel; //top area showing current player and direction
    private JLabel topCardLabel; //shows which card is on top of the play pile
    private JScrollPane handScrollPane;
    private JButton drawButton; //button to draw a card
    private JLabel currentPlayerLabel; //shows whose turn it is
    private JLabel directionLabel; //shows turn direction (clockwise or counetrclockwise)
    private JLabel messageLabel; //shows status or game messages
    private JLabel scoreLabel; //shows player scores

    private UNO_Game model; //UNO game logic
    private UNO_Controller controller; //listens for buttons or card clicks, and updates the model

    /**
     * Constructs the main game window for the UNO Flip! application.
     * This constructor initializes the user interface, prompts the user for
     * the number of players and their names, and sets up the core MVC connections
     * between the {@link UNO_Game} model and the {@link UNO_Controller}.
     * After initializing the layout, it starts a new round and updates
     * the game state and score display to reflect the initial setup.
     */
    public UNO_Frame() {

        ArrayList<String> playerNames = new ArrayList<>();
        int numPlayers = 0;

        while (true) {
            String inputValue = JOptionPane.showInputDialog(null,"Please enter the number of players (2–4):");

            // if cancel or red x pressed stop program
            if (inputValue == null) {
                JOptionPane.showMessageDialog(null, "Game setup cancelled.");
                return;
            }

            try {
                numPlayers = Integer.parseInt(inputValue.trim()); //converts input to an integer
                if (numPlayers >= 2 && numPlayers <= 4) {
                    break;
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a number between 2 and 4.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number.");
            }
        }

        for (int i = 1; i <= numPlayers; i++) {
            String name = JOptionPane.showInputDialog("Enter name for Player " + i + ":");
            if (name == null || name.trim().isEmpty()) {
                name = "Player " + i; // default name if user cancels or leaves blank
            }
            playerNames.add(name.trim());
        }

        //Set up the GUI, calls helper methods o create and organize the layout, and makes the window visible
        initializeUI();
        setupLayout();
        this.setVisible(true);

        model = new UNO_Game(numPlayers, playerNames);
        model.addUnoView(this);
        controller = new UNO_Controller(model, this);

        model.startNewRound();
        // Initial game state update
        updateGameState();
        // Initial scoreboard update
        updateScores();

    }

    /**
     * Initializes the main user interface components and panels for the UNO game window.
     * This method sets up all Swing components (panels, buttons, and labels), applies
     * layout managers, visual styling (colors, borders, and fonts), and configures
     * scroll behavior for the player hand area.
     */
    public void initializeUI() {
        // Set up the main window
        setTitle("UNO Flip!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null); // Center the window

        // Initialize panels
        mainPanel = new JPanel(new BorderLayout());
        playerHandPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        playAreaPanel = new JPanel(new BorderLayout());
        playerInfoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        // Set preferred size to ensure enough height
        playerInfoPanel.setPreferredSize(new Dimension(900, 100)); // Increased height

        // Initialize components
        topCardLabel = new JLabel("Top Card: ", SwingConstants.CENTER);
        topCardLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topCardLabel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 2));
        topCardLabel.setPreferredSize(new Dimension(120, 160));
        topCardLabel.setOpaque(true);
        topCardLabel.setBackground(new Color(255, 255, 255));

        drawButton = new JButton("Draw Card");
        drawButton.setFont(new Font("Arial", Font.BOLD, 14));
        drawButton.setPreferredSize(new Dimension(120, 40));

        currentPlayerLabel = new JLabel("Current: ", SwingConstants.CENTER);
        currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 14));

        directionLabel = new JLabel("Direction: ↻", SwingConstants.CENTER);
        directionLabel.setFont(new Font("Arial", Font.BOLD, 13));

        scoreLabel = new JLabel("Scores: ", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 13));
        scoreLabel.setForeground(Color.BLACK);

        messageLabel = new JLabel("Welcome to UNO Flip!", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 14));
        messageLabel.setForeground(Color.BLACK);

        // Set up action listeners (will be connected to controller later)
        drawButton.addActionListener(controller);

        // Style panels
        playerHandPanel.setBackground(new Color(240, 240, 240));
        playerHandPanel.setBorder(BorderFactory.createTitledBorder("Your Hand"));

        playAreaPanel.setBackground(new Color(200, 230, 200));
        playAreaPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        playerInfoPanel.setBackground(new Color(220, 220, 255));
        playerInfoPanel.setBorder(BorderFactory.createTitledBorder("Game Info"));

        handScrollPane = new JScrollPane(playerHandPanel);
        handScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        handScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        handScrollPane.setPreferredSize(new Dimension(850, 180)); // adjust height as needed
        handScrollPane.getHorizontalScrollBar().setUnitIncrement(20); // smoother scrolling


    }

    /**
     * Builds and arranges the layout of all major panels and components in the main game window.
     * This method positions the player information area, play area, and player hand panel
     * using layout managers. It ensures that all interface elements are correctly aligned
     * and visible within the main window.
     */
    public void setupLayout() {
        // Set up play area (center) ORGANIZES WHERE EVERYTHING GOES ON THE SCREEN
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 20));
        centerPanel.setBackground(playAreaPanel.getBackground());
        centerPanel.add(topCardLabel);

        playAreaPanel.add(centerPanel, BorderLayout.CENTER);

        // Set up control buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(playAreaPanel.getBackground());
        buttonPanel.add(drawButton);

        playAreaPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Set up player info panel
        playerInfoPanel.add(currentPlayerLabel);
        playerInfoPanel.add(directionLabel);
        playerInfoPanel.add(scoreLabel);
        playerInfoPanel.add(messageLabel);

        // Assemble main layout
        mainPanel.add(playerInfoPanel, BorderLayout.NORTH);    // Game info at top
        mainPanel.add(playAreaPanel, BorderLayout.CENTER);     // Play area in middle
        //mainPanel.add(playerHandPanel, BorderLayout.SOUTH);    // Hand at bottom
        mainPanel.add(handScrollPane, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);
    }

    /**
     * Updates the game's visual state in the GUI.
     */
    public void updateGameState(){
        Player currentPlayer = model.getCurrentPlayer();
        displayPlayerHand(currentPlayer); //to show their hand on screen
        highlightCurrentPlayer(); //Highlight their turn
        showCardPlayed(model.topCard()); //show the top of the play pile on the screen
    }

    /**
     * Displays the given player's hand on the GUI.
     * @param player the Player whose hand is currently being displayed
     */
    public void displayPlayerHand(Player player){
        playerHandPanel.removeAll(); //clears the bottom panel, basically clears old hand from the screen
        playerHandPanel.add(drawButton, BorderLayout.NORTH); //so the player can draw a card if they cant play

        ArrayList<Card> hand = player.getHand();
        for (int i = 0; i < hand.size(); i++) { //for each card in the current players hand
            Card card = hand.get(i);

            //create a visual component (cardComponent) for each card
            CardComponent cardComp = new CardComponent(card, i + 1, controller);

            // Highlight playable cards
            cardComp.setPlayable(model.isPlayable(card));

            playerHandPanel.add(cardComp);
        }

        //rebuild and repain the hand area so the new cards actually show
        playerHandPanel.revalidate();
        playerHandPanel.repaint();
    }

    /**
     * Highlights the current player's name in the GUI.
     */
    public void highlightCurrentPlayer(){
        Player currentPlayer = model.getCurrentPlayer();

        //update the label text to show that player's name
        currentPlayerLabel.setText("Current Player: " + currentPlayer.getName());

        //update the label text to show which direction
        directionLabel.setText("Direction: " + model.getDirection().toString());

        playerInfoPanel.setBackground(new Color(150, 230, 153));
    }

    /**
     * Displays the most recently played card in the play area.
     * @param card the Card currently on top of the play pile
     */
    public void showCardPlayed(Card card){
        playAreaPanel.removeAll();

        if (card != null) {
            //create a card component to visually represent that card. -1 means it's not from the players hand, its the top card
            CardComponent topCardComponent = new CardComponent(card, -1, controller);
            topCardComponent.setPlayable(false); // cannot play the top card
            topCardComponent.getUseButton().setVisible(false); // set use button to invisible
            playAreaPanel.add(topCardComponent, BorderLayout.CENTER);
        } else {
            playAreaPanel.add(new JLabel("No card in play."));
        }

        playAreaPanel.revalidate();
        playAreaPanel.repaint();
    }

    /**
     * Prompts the player to select a color when a wild card is played.
     */
    public void showWildColorSelection(){
        Card topCard = model.topCard();
        if (topCard instanceof WildCard || topCard instanceof WildDrawCard) {
            boolean isLightSide = topCard.isLightSideActive;

            String[] colors;
            if (isLightSide) {
                colors = new String[]{"RED", "BLUE", "GREEN", "YELLOW"};
            } else {
                colors = new String[]{"TEAL", "ORANGE", "PURPLE", "PINK"};
            }

            String chosen = (String) JOptionPane.showInputDialog(this,
                    "Choose a color:",
                    "Wild Card Color Selection",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    colors,
                    colors[0]);

            if (chosen != null) {
                try {
                    CardColor chosenColor = CardColor.valueOf(chosen);
                    controller.handleWildColorSelection(chosenColor);
                } catch (IllegalArgumentException e) {
                    displayMessage("Invalid color selection!");
                }
            }
        }
    }

    /**
     * Updates the message label on the GUI to show the given text
     *
     * @param message the text to display in the message label
     */
    public void displayMessage(String message){
        if (messageLabel != null) {
            messageLabel.setText(message);
        }
        //        JOptionPane.showMessageDialog(this, message, "UNO Game", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     *Updates the score display in the top information panel
     *
     */
    public void updateScores(){
        if (model == null || scoreLabel == null) {
            return;
        }
        String score = "Scores: ";

        ArrayList<Player> players = model.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            Player currentPlayer = players.get(i);
            score += currentPlayer.getName() + ": " + currentPlayer.getScore() + " ";

            if (i != players.size() - 1) {
                score += " | ";
            }
        }

        scoreLabel.setText(score);

        playerInfoPanel.revalidate();
        playerInfoPanel.repaint();
    }

    /**
     * Displays the overall game winner in the GUI when a player reaches the winning score
     *
     * @param player The player who won the whole game, scored more than 500 points
     */
    public void showWinner(Player player){
        if (player == null){
            return;
        }

        String winnerMessage = player.getName() + " win the game with " + player.getScore() + " points!";
        displayMessage(winnerMessage);
        messageLabel.setForeground(Color.RED);

        JOptionPane.showMessageDialog(this, winnerMessage, "Game Over!", JOptionPane.INFORMATION_MESSAGE);

        if (drawButton != null) {
            drawButton.setEnabled(false);
        }

        playerHandPanel.removeAll();
        playerHandPanel.revalidate();
        playerHandPanel.repaint();
    }

    /**
     * Displays the winner of the current round when a player empties their hand
     *
     * @param player The player who one that round
     */
    @Override
    public void showRoundWinner(Player player) {
        if (player == null){
            return;
        }
        String winnerMessage = player.getName() + " wins this round!" + " With total score: " + player.getScore() + " points.";
        displayMessage(winnerMessage);
        messageLabel.setForeground(Color.BLUE);

        JOptionPane.showMessageDialog(this, winnerMessage, "Round Over!", JOptionPane.INFORMATION_MESSAGE);

        updateScores();

        drawButton.setEnabled(false); //disables drawing until the next round starts

    }

    /**
     * Prompts the user to start a new round after one concludes, or exit the game.
     * Displays a confirmation dialog asking whether to continue playing.
     */
    public void initiateNewRound(){
        // Ask if user wants to continue to next round
        int option = JOptionPane.showConfirmDialog(this,
                "Start next round?",
                "Continue Playing",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            // Re-enable the draw button for the new round
            drawButton.setEnabled(true);
            displayMessage("New round started!");
            messageLabel.setForeground(Color.BLACK);
            // Start new round through controller
            controller.handleNewRound();
        } else {
            // Or exit the game
            System.exit(0);
        }


    }


    /**
     * Returns the button used by players to draw a card from the deck.
     * This button is accessed by the {@link UNO_Controller} to attach event listeners
     * and handle draw actions during gameplay.
     *
     * @return {@link JButton}. The button component representing the draw button
     */
    public JButton getDrawButton(){
        return drawButton;
    }

    /**
     * The main entry point for the UNO Flip! game application.
     * Launches the game window and initializes the game by creating
     * a new {@link UNO_Frame} instance.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        UNO_Frame frame = new UNO_Frame(); //to create the game
    }

}
