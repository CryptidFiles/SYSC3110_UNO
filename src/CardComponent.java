import java.awt.*;
import javax.swing.*;

// Physical UNO card component with "Use" button
public class CardComponent extends JPanel {
    private Card card;           // The actual card data from model
    private int cardIndex;       // Position in player's hand
    private boolean isPlayable;  // Highlight if playable
    private JButton useButton;

    public CardComponent(Card card, int index, UNO_Controller controller) {
        this.card = card;
        this.cardIndex = index;
        this.isPlayable = false;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(100, 140));

        initializeComponents(controller);
        setupLayout();
    }

    private void initializeComponents(UNO_Controller controller) {
        // Create the "Use" button
        useButton = new JButton("Use");
        useButton.setPreferredSize(new Dimension(80, 25));
        useButton.setEnabled(false); // Initially disabled

        // Directly connect to controller - no need for listener list!
        useButton.addActionListener(e -> {
            controller.handleCardPlay(cardIndex);
        });
    }

    private void setupLayout() {
        // Main card display panel
        JPanel cardDisplayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawCard(g);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(100, 100);
            }
        };

        cardDisplayPanel.setLayout(new BorderLayout());

        // Add card info labels
        JLabel typeLabel = new JLabel(card.getType().toString(), SwingConstants.CENTER);
        JLabel colorLabel = new JLabel(card.getColor().toString(), SwingConstants.CENTER);
        JLabel sideLabel = new JLabel(card.getActiveSide() ? "LIGHT" : "DARK", SwingConstants.CENTER);

        typeLabel.setOpaque(true);
        colorLabel.setOpaque(true);
        sideLabel.setOpaque(true);

        // Set colors based on card
        Color bgColor = convertToAWTColor(card.getColor());
        typeLabel.setBackground(bgColor);
        colorLabel.setBackground(bgColor);
        sideLabel.setBackground(bgColor);

        // Make text readable on dark backgrounds
        if (isDarkColor(bgColor)) {
            typeLabel.setForeground(Color.WHITE);
            colorLabel.setForeground(Color.WHITE);
            sideLabel.setForeground(Color.WHITE);
        }

        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.add(typeLabel);
        infoPanel.add(colorLabel);
        infoPanel.add(sideLabel);

        cardDisplayPanel.add(infoPanel, BorderLayout.CENTER);

        // Add index label for debugging
        JLabel indexLabel = new JLabel("Card " + (cardIndex + 1), SwingConstants.CENTER);
        cardDisplayPanel.add(indexLabel, BorderLayout.NORTH);

        // Assemble the component
        add(cardDisplayPanel, BorderLayout.CENTER);
        add(useButton, BorderLayout.SOUTH);

        updateBorder();
    }

    private void drawCard(Graphics g) {
        // Draw card background
        Color cardColor = convertToAWTColor(card.getColor());
        g.setColor(cardColor);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw border
        if (isPlayable) {
            g.setColor(Color.GREEN);
            g.drawRect(0, 0, getWidth()-1, getHeight()-1);
        } else {
            g.setColor(Color.BLACK);
            g.drawRect(0, 0, getWidth()-1, getHeight()-1);
        }
    }

    private java.awt.Color convertToAWTColor(CardColor unoColor) {
        return switch (unoColor) {
            case RED -> java.awt.Color.RED;
            case BLUE -> java.awt.Color.BLUE;
            case YELLOW -> java.awt.Color.YELLOW;
            case GREEN -> java.awt.Color.GREEN;
            case PURPLE -> new java.awt.Color(128, 0, 128);
            case PINK -> java.awt.Color.PINK;
            case ORANGE -> java.awt.Color.ORANGE;
            case TEAL -> java.awt.Color.CYAN;
            case WILD -> java.awt.Color.BLACK;
        };
    }

    private boolean isDarkColor(Color color) {
        double brightness = (color.getRed() * 0.299 + color.getGreen() * 0.587 + color.getBlue() * 0.114);
        return brightness < 128;
    }

    private void updateBorder() {
        if (isPlayable) {
            setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
        } else {
            setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
    }

    // Getters and setters
    public int getCardIndex() { return cardIndex; }
    public Card getCard() { return card; }

    public void setPlayable(boolean playable) {
        this.isPlayable = playable;
        useButton.setEnabled(playable);
        updateBorder();
        repaint();
    }

    public boolean isPlayable() { return isPlayable; }
}