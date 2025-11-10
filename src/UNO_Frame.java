import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.util.ArrayList;

public class UNO_Frame extends JFrame implements UNO_View{
    private JPanel mainPanel; //container that holds everything
    private JPanel playerHandPanel; //bottom area showing the players cards
    private JPanel playAreaPanel; //middle area showing draw deck and top card
    private JPanel playerInfoPanel; //top area showing current player and direction
    private JLabel topCardLabel; //shows which card is on top of the play pile
    private JButton drawButton; //button to draw a card
    private JLabel currentPlayerLabel; //shows whose turn it is
    private JLabel directionLabel; //shows turn direction (clockwise or counetrclockwise)
    private JLabel messageLabel; //shows status or game messages
    private JLabel scoreLabel; //shows player scores

    private UNO_Game model; //UNO game logic
    private UNO_Controller controller; //listens for buttons or card clicks, and updates the model

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

        messageLabel = new JLabel("Welcome to UNO Flip!", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 14));
        messageLabel.setForeground(Color.DARK_GRAY);

        scoreLabel = new JLabel("Scores: ", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        scoreLabel.setForeground(Color.BLACK);

        // Set up action listeners (will be connected to controller later)
        drawButton.addActionListener(controller);

        // Style panels
        playerHandPanel.setBackground(new Color(240, 240, 240));
        playerHandPanel.setBorder(BorderFactory.createTitledBorder("Your Hand"));

        playAreaPanel.setBackground(new Color(200, 230, 200));
        playAreaPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        playerInfoPanel.setBackground(new Color(220, 220, 255));
        playerInfoPanel.setBorder(BorderFactory.createTitledBorder("Game Info"));

    }

    public void setupLayout() {
        // Set up play area (center) ORGANIZES WHERE EVERYTHING GOES ON THE SCREEN
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 20));
        centerPanel.setBackground(playAreaPanel.getBackground());

        // Draw deck placeholder
        //JLabel drawDeckLabel = new JLabel("DRAW DECK", SwingConstants.CENTER);
        //drawDeckLabel.setFont(new Font("Arial", Font.BOLD, 12));
        //drawDeckLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        //drawDeckLabel.setPreferredSize(new Dimension(100, 150));
        //drawDeckLabel.setOpaque(true);
        //drawDeckLabel.setBackground(Color.DARK_GRAY);
        //drawDeckLabel.setForeground(Color.WHITE);

        //centerPanel.add(drawDeckLabel);
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
        playerInfoPanel.add(messageLabel);
        playerInfoPanel.add(scoreLabel);

        // Assemble main layout
        mainPanel.add(playerInfoPanel, BorderLayout.NORTH);    // Game info at top
        mainPanel.add(playAreaPanel, BorderLayout.CENTER);     // Play area in middle
        mainPanel.add(playerHandPanel, BorderLayout.SOUTH);    // Hand at bottom

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
        updateScores(); //update players scores
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
            CardComponent cardComp = new CardComponent(card, i, controller);

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

        playerInfoPanel.setBackground(Color.YELLOW);
    }

    /**
     * Displays the most recently played card in the play area.
     * @param card the Card currently on top of the play pile
     */
    public void showCardPlayed(Card card){
        playAreaPanel.removeAll();

        if (card != null) {
            //create a cardcomponent to visually represent that card. -1 means its not from the players hand, its the top card
            CardComponent topCardComponent = new CardComponent(card, -1, controller);
            topCardComponent.setPlayable(false); // cannot play the top card
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
        String[] colors = {"RED", "BLUE", "GREEN", "YELLOW"};
        String chosen = (String) JOptionPane.showInputDialog(this,
                "Choose a color:",
                "Wild Card Color Selection",
                JOptionPane.PLAIN_MESSAGE,
                null,
                colors,
                colors[0]);

        if(model.topCard().isLightSideActive){

        }
        //idk the logic for dealing with wild card after picking colour



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
        String score = "Scores: ";

        ArrayList<Player> players = model.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            Player currentPlayer = players.get(i);
            score += currentPlayer.getName() + ": " + currentPlayer.getScore();

            if (i == players.size() - 1) {
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
        messageLabel.setForeground(Color.YELLOW);

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

        drawButton.setEnabled(false); //disables srawing until the next round starts
    }


    public static void main(String[] args) {
        UNO_Frame frame = new UNO_Frame();
    }

}
