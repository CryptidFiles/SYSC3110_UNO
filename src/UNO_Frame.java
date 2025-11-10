import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.util.ArrayList;

public class UNO_Frame extends JFrame implements UNO_View{
    private JPanel mainPanel;
    private JPanel playerHandPanel;
    private JPanel playAreaPanel;
    private JPanel playerInfoPanel;
    private JLabel topCardLabel;
    private JButton drawButton;
    private JLabel currentPlayerLabel;
    private JLabel directionLabel;
    private JScrollPane handScrollPane;

    private UNO_Game model;
    private UNO_Controller controller;

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
                numPlayers = Integer.parseInt(inputValue.trim());
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

        initializeUI();
        setupLayout();


        this.setVisible(true);

        model = new UNO_Game(numPlayers, playerNames);
        model.addUnoView(this);
        controller = new UNO_Controller(model, this);

        model.startNewRound();
        updateGameState();

    }

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
        playerInfoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

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
        directionLabel.setFont(new Font("Arial", Font.PLAIN, 12));

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

    public void setupLayout() {
        // Set up play area (center)
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
        displayPlayerHand(currentPlayer);
        highlightCurrentPlayer();
        showCardPlayed(model.topCard());
        updateScores();
    }

    /**
     * Displays the given player's hand on the GUI.
     * @param player the Player whose hand is currently being displayed
     */
    public void displayPlayerHand(Player player){
        playerHandPanel.removeAll();
        playerHandPanel.add(drawButton, BorderLayout.NORTH);

        ArrayList<Card> hand = player.getHand();
        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.get(i);
            CardComponent cardComp = new CardComponent(card, i, controller);

            // Highlight playable cards
            cardComp.setPlayable(model.isPlayable(card));

            playerHandPanel.add(cardComp);
        }

        playerHandPanel.revalidate();
        playerHandPanel.repaint();
    }

    /**
     * Highlights the current player's name in the GUI.
     */
    public void highlightCurrentPlayer(){
        Player currentPlayer = model.getCurrentPlayer();
        currentPlayerLabel.setText("Current Player: " + currentPlayer.getName());
        directionLabel.setText("Direction: " + model.getDirection().toString());

        playerInfoPanel.setBackground(Color.YELLOW);
    }

    /**
     * Displays the most recently played card in the play area.
     * @param card the Card currently on top of the play pile
     */
    public void showCardPlayed(Card card){
        playAreaPanel.removeAll();

        JPanel cardHolder = new JPanel();
        cardHolder.setLayout(new FlowLayout(FlowLayout.CENTER));
        cardHolder.setPreferredSize(new Dimension(150, 300)); // Adjust size

        if (card != null) {
            CardComponent topCardComponent = new CardComponent(card, -1, controller);

            // cannot play the top card and make it invisible
            topCardComponent.setPlayable(false);
            topCardComponent.getUseButton().setVisible(false);
            cardHolder.add(topCardComponent);
            playAreaPanel.add(topCardComponent, BorderLayout.CENTER);
        } else {
            cardHolder.add(new JLabel("No card in play."));
        }

        playAreaPanel.add(cardHolder, BorderLayout.CENTER);
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

    public void displayMessage(String message){

    }

    public void updateScores(){

    }

    public void showWinner(Player player){

    }

    public void showRoundWinner(Player player) {

    }



    public JButton getDrawButton(){
        return drawButton;
    }

    public static void main(String[] args) {
        UNO_Frame frame = new UNO_Frame();
    }

}
