import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.Toolkit;
import java.util.ArrayList;

/**
 * The UNO_Frame class represents the main graphical user interface (GUI)
 * for the UNO Flip! game.
 * It serves as the concrete implementation of the {@link UNO_View} interface
 * and forms the “View” in (MVC) architecture.
 * This class is responsible for visually displaying the game state, player hands,
 * the play area, and score updates, as well as relaying user input to the
 * {@link UNO_Controller}.
 * Through its integration with {@link UNO_Model} (model) and
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
    private JButton nextPlayerButton; //button to move to next player
    private JLabel currentPlayerLabel; //shows whose turn it is
    private JLabel directionLabel; //shows turn direction (clockwise or counterclockwise)
    private JLabel messageLabel; //shows status or game messages
    private JLabel scoreLabel; //shows player scores

    private UNO_Model model; //UNO game logic
    private UNO_Controller controller; //listens for buttons or card clicks, and updates the model

    /**
     * Constructs the main game window for the UNO Flip! application.
     * This constructor initializes the user interface, prompts the user for
     * the number of players and their names, and sets up the core MVC connections
     * between the {@link UNO_Model} model and the {@link UNO_Controller}.
     * After initializing the layout, it starts a new round and updates
     * the game state and score display to reflect the initial setup.
     */
    public UNO_Frame() {
        // Player Setup Dialog
        ArrayList<String> playerNames = new ArrayList<>();
        ArrayList<Boolean> playerIsAI = new ArrayList<>();

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

            int choice = JOptionPane.showConfirmDialog(null, "Should " + name + " be an AI player?", "Player Type", JOptionPane.YES_NO_OPTION );
            boolean isAI = (choice == JOptionPane.YES_OPTION);
            if (isAI){
                name += " (AI)";
            }

            playerNames.add(name.trim());
            playerIsAI.add(isAI);
        }

        //Set up the GUI, calls helper methods o create and organize the layout, and makes the window visible
        initializeUI();

        model = new UNO_Model(numPlayers, playerNames, playerIsAI);
        model.addUnoView(this);

        // Show player configuration summary
        showPlayerConfigurationSummary();

        // Set up controller
        controller = new UNO_Controller(model, this);
        // Set up action listeners (will be connected to controller later)
        drawButton.addActionListener(controller);
        nextPlayerButton.addActionListener(controller);

        setupLayout();
        this.setVisible(true);

        // Initial scoreboard update
        updateScores();

        /** Start AI turn if first player is AI
        Player firstPlayer = model.getCurrentPlayer();
        if (firstPlayer.isPlayerAI()) {
            model.executeAITurn();
        }*/
    }

    private void showPlayerConfigurationSummary() {
        StringBuilder summary = new StringBuilder("Game Setup Complete!\t\n\n");
        ArrayList<Player> players = model.getPlayers();

        for (Player player : players) {
            summary.append("•").append(player.getName());
            if (!player.isPlayerAI()) {
                summary.append(" (Human)");
            }
            summary.append("\n");
        }

        displayMessage(summary.toString());

        // Show brief popup
        JOptionPane.showMessageDialog(this, summary.toString(), "Game Setup", JOptionPane.INFORMATION_MESSAGE);
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

        Toolkit tk = Toolkit.getDefaultToolkit();
        int xSize = ((int) tk.getScreenSize().getWidth() - 100);
        int ySize = ((int) tk.getScreenSize().getHeight() - 100);
        setSize(xSize,ySize);
        setUndecorated(false);

        //setSize(900, 600);
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

        nextPlayerButton =  new JButton("Next Player");
        nextPlayerButton.setFont(new Font("Arial", Font.BOLD, 14));
        nextPlayerButton.setPreferredSize(new Dimension(120, 40));
        nextPlayerButton.setEnabled(false);

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
        //buttonPanel.add(drawButton);
        buttonPanel.add(nextPlayerButton);

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
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel,  BorderLayout.CENTER);
        getContentPane().add(handScrollPane, BorderLayout.SOUTH);
        //mainPanel.add(handScrollPane, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);
    }


    @Override
    public void handleGameEvent(GameEvent event) {
        // Use SwingUtilities to ensure thread safety
        SwingUtilities.invokeLater(() -> processGameEvent(event));
    }

    private void processGameEvent(GameEvent event) {
        if (event.getCurrentPlayer().isPlayerAI()){
            drawButton.setEnabled(false);
            nextPlayerButton.setEnabled(false);

        }
        switch (event.getType()) {
            case GAME_STATE_CHANGED:
                updateGameState(event);
                break;

            case CARD_PLAYED:
                showCardPlayed(event.getCard());
                displayMessage(event.getMessage());
                setHandEnabled(false);
                setNextPlayerButtonEnabled(true);
                break;

            case CARD_DRAWN:
                displayMessage(event.getMessage());
                setHandEnabled(false);
                setNextPlayerButtonEnabled(true);
                setDrawButtonEnabled(false);

                break;

            case PLAYER_CHANGED:
                highlightCurrentPlayer(event.getCurrentPlayer());
                showCardPlayed(event.getCard());
                Direction dir = event.getDirection();
                if (dir != null) {
                    updateDirectionDisplay(dir);
                }
                break;

            case ROUND_WON:
                showRoundWinner(event.getCurrentPlayer());
                updateScores();

                // Use a timer to delay the new round initiation slightly
                Timer roundTimer = new Timer(1000, e -> {
                    initiateNewRound();
                });
                roundTimer.setRepeats(false);
                roundTimer.start();
                break;

            case GAME_WON:
                showWinner(event.getWinningPlayer());
                break;

            case AI_THINKING:
                showAITurnIndicator(event.getCurrentPlayer());
                break;

            case MESSAGE:
                displayMessage(event.getMessage());
                break;

            case COLOR_SELECTION_NEEDED:
                // Disable hand immediately when color selection is needed
                setHandEnabled(false);
                setDrawButtonEnabled(false);
                showWildColorSelection(event);
                break;

            case COLOR_SELECTION_COMPLETE:
                // Disable the hand after color selection
                setHandEnabled(false);
                setDrawButtonEnabled(false);
                setNextPlayerButtonEnabled(true);
                displayMessage(event.getMessage());
                break;

            case SCORES_UPDATED:
                updateScores();
                break;

            case DIRECTION_FLIPPED:
                dir = event.getDirection();
                if (dir != null) {
                    updateDirectionDisplay(dir);
                    displayMessage(event.getMessage());
                }
                break;
        }
    }

    private void updateGameState(GameEvent event) {
        Player currentPlayer = event.getCurrentPlayer();
        if (currentPlayer != null) {
            displayPlayerHand(currentPlayer);
            highlightCurrentPlayer(currentPlayer);
        }

        Card topCard = event.getCard();
        if (topCard != null) {
            showCardPlayed(topCard);
        }

        // Set draw button
        boolean hasPlayable = model.hasPlayableHand(currentPlayer);
        boolean canDraw = !hasPlayable && !model.hasActedThisTurn();
        drawButton.setEnabled(canDraw);

        // Handle additional data
        if (event.isEnableNextPlayer()) {
            setNextPlayerButtonEnabled(true);
        }
    }

    private void updateDirectionDisplay(Direction direction) {
        directionLabel.setText("Direction: " + direction.toString());
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
            boolean playable = model.isPlayable(card);
            cardComp.setPlayable(playable);
            if(player.isPlayerAI()){
                cardComp.getUseButton().setEnabled(false);
            }
            else {
                cardComp.getUseButton().setEnabled(playable);
            }

            if (!playable){
                cardComp.getUseButton().setForeground(Color.GRAY);
            }

            playerHandPanel.add(cardComp);
        }

        //rebuild and repaint the hand area so the new cards actually show
        playerHandPanel.revalidate();
        playerHandPanel.repaint();
    }

    /**
     * Highlights the current player's name in the GUI.
     */
    public void highlightCurrentPlayer(Player currentPlayer){
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
    public void showWildColorSelection(GameEvent event){
        Card topCard = event.getCard();
        Player currentPlayer = event.getCurrentPlayer();

        if (topCard instanceof WildCard || topCard instanceof WildDrawCard) {
            boolean isLightSide = topCard.isLightSideActive;

            String[] colors;
            if (isLightSide) {
                colors = new String[]{"RED", "BLUE", "GREEN", "YELLOW"};
            } else {
                colors = new String[]{"TEAL", "ORANGE", "PURPLE", "PINK"};
            }

            // Manually ask the real players for wild card selection
            if(!currentPlayer.isPlayerAI()) {
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
            } else {
                // Automatically select the colour for AI bots based of strategy
                CardColor chosenAIWildColor = event.getWildColorChoice();
                controller.handleWildColorSelection(chosenAIWildColor);
                showAIColorSelection(currentPlayer, chosenAIWildColor);

            }
        }
    }

    public void showAIColorSelection(Player aiPlayer, CardColor chosenColor) {
        String message = aiPlayer.getName() + " chooses " + chosenColor + " color!";
        displayMessage(message);

        // Visual indicator
        messageLabel.setForeground(Color.MAGENTA);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Reset formatting after delay
        Timer timer = new Timer(2000, e -> {
            messageLabel.setForeground(Color.BLACK);
            messageLabel.setFont(new Font("Arial", Font.BOLD, 14));
        });
        timer.setRepeats(false);
        timer.start();
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
    }

    /**
     *Updates the score display in the top information panel
     *
     */
    public void updateScores(){
        if (model == null || scoreLabel == null) {
            return;
        }
        StringBuilder score = new StringBuilder("Scores: ");

        ArrayList<Player> players = model.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            Player currentPlayer = players.get(i);
            score.append(currentPlayer.getName()).append(": ").append(currentPlayer.getScore()).append(" ");

            if (i != players.size() - 1) {
                score.append(" | ");
            }
        }

        scoreLabel.setText(score.toString());

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

        String winnerMessage = player.getName() + " wins the game with " + player.getScore() + " points!";
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
     * Enables or disables the "Next Player" button in the game interface.
     * This method is used to control when players are allowed to proceed
     * to the next turn. After a card is played or a card is drawn, this
     * button becomes enabled; once pressed, it is disabled again until
     * the next valid action.
     * @param enabled true to enable "Next player" button, false to disable
     */
    public void setNextPlayerButtonEnabled(boolean enabled){
        nextPlayerButton.setEnabled(enabled);
    }

    /**
     * Enables or disables all "Use" buttons in the current player's hand.
     * This method is typically called after a player plays a card, to prevent
     * multiple plays in the same turn. It is re-enabled at the start of the
     * next player’s turn to allow them to play or draw a card.
     * @param enabled ture to enable all "use" buttons, false to disable them
     */
    public void setHandEnabled(boolean enabled){
        Component[] components = playerHandPanel.getComponents();
        for (Component component : components) {
            if (component instanceof CardComponent) {
                CardComponent cardComponent = (CardComponent) component;
                cardComponent.getUseButton().setEnabled(enabled);
            }
        }
    }

    public void setDrawButtonEnabled(boolean enabled){
        drawButton.setEnabled(enabled);
    }
    /**
     * Returns the button used by players to move to next player
     * This button is accessed by the {@link UNO_Controller} to attach event listeners
     * and handle moving to next player.
     *
     * @return {@link JButton}. The button component representing the next player button
     */
    public JButton getNextPlayerButton(){
        return nextPlayerButton;
    }


    public void showAITurnIndicator(Player aiPlayer) {
        String message = aiPlayer.getName() + " is thinking...";
        displayMessage(message);

        // Visual indicator
        messageLabel.setForeground(Color.BLUE);
        messageLabel.setFont(new Font("Arial", Font.ITALIC, 14));
    }

    public void showAICardSelection(Player aiPlayer, int cardIndex, Card selectedCard) {
        if (cardIndex == 0) {
            displayMessage(aiPlayer.getName() + " draws a card");
        } else {
            String cardInfo = selectedCard.getColor() + " " + selectedCard.getType();
            displayMessage(aiPlayer.getName() + " plays " + cardInfo);
        }

        // Highlight the selected card briefly
        if (cardIndex > 0) {
            highlightSelectedCard(cardIndex - 1); // Convert to 0-based
        }
    }



    private void highlightSelectedCard(int cardIndex) {
        Component[] components = playerHandPanel.getComponents();
        if (cardIndex > 0 && cardIndex < components.length) {
            Component comp = components[cardIndex + 1];
            if (comp instanceof CardComponent) {
                CardComponent cardComp = (CardComponent) comp;
                cardComp.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 4));

                // Reset border after delay
                Timer timer = new Timer(1000, e -> {
                    cardComp.setPlayable(cardComp.isPlayable()); // Reset to normal border
                });
                timer.setRepeats(false);
                timer.start();
            }
        }
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
