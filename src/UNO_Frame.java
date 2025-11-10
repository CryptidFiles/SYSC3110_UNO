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

    }

    public void initializeUI() {
        // Set up the main window
        setTitle("UNO Flip!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
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

    }

    public void setupLayout() {
        // Set up play area (center) ORGANIZES WHERE EVERYTHING GOES ON THE SCREEN
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 20));
        centerPanel.setBackground(playAreaPanel.getBackground());

        // Draw deck placeholder
        JLabel drawDeckLabel = new JLabel("DRAW DECK", SwingConstants.CENTER);
        drawDeckLabel.setFont(new Font("Arial", Font.BOLD, 12));
        drawDeckLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        drawDeckLabel.setPreferredSize(new Dimension(100, 150));
        drawDeckLabel.setOpaque(true);
        drawDeckLabel.setBackground(Color.DARK_GRAY);
        drawDeckLabel.setForeground(Color.WHITE);

        centerPanel.add(drawDeckLabel);
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
        mainPanel.add(playerHandPanel, BorderLayout.SOUTH);    // Hand at bottom

        // Add main panel to frame
        add(mainPanel);
    }

    public void updateGameState(){
        Player currentPlayer = model.getCurrentPlayer();
        displayPlayerHand(currentPlayer);
        highlightCurrentPlayer();
        showCardPlayed(model.topCard());
        updateScores();
    }

    public void displayPlayerHand(Player player){
        playerHandPanel.removeAll();

        ArrayList<Card> hand = player.getHand();
        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.get(i);
            JButton cardButton = new JButton(card.toString());
            int cardIndex = i;

            // Send event to controller when clicked
            cardButton.addActionListener(controller);
            playerHandPanel.add(cardButton);
        }

        playerHandPanel.revalidate();
        playerHandPanel.repaint();
    }

    public void highlightCurrentPlayer(){
        Player currentPlayer = model.getCurrentPlayer();
        currentPlayerLabel.setText("Current Player: " + currentPlayer.getName());

        playerInfoPanel.setBackground(Color.YELLOW);
    }

    public void showCardPlayed(Card card){

    }

    public void showWildColorSelection(){

    }

    public void displayMessage(String message){

    }

    public void updateScores(){

    }

    public void showWinner(Player player){

    }

    @Override
    public void showRoundWinner(Player player) {

    }


    public static void main(String[] args) {
        UNO_Frame frame = new UNO_Frame();
    }

}
