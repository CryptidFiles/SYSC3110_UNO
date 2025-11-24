import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * The CardComponent class represents the visual and interactive
 * user-interface element of a single UNO card within the game.
 * Each card component displays the appropriate card image depending on its
 * active side, shows a green highlight if playable, and provides a "Use" button
 * to allow the player to play the card directly.
 *
 * @author Ahmad El-Jabi 101303269
 * @author Atik Mahmud 101318070
 * @author Aryan Singh 101299776
 * @author Jonathan Gitej 101294584
 *
 * @version 3.0, November 24, 2025
 */
public class CardComponent extends JPanel {
    private Card card;           // The actual card data from model
    private int cardIndex;       // Position in player's hand
    private boolean isPlayable;  // Highlight if playable
    private JButton useButton;  //has its own use button
    private BufferedImage cardImage;

    /**
     * Constructs a new CardComponent for the specified {@link Card}.
     *
     * @param card the UNO card data model represented by this component
     * @param index the index of this card in the player's hand
     * @param controller the {@link UNO_Controller} responsible for handling user actions
     */
    public CardComponent(Card card, int index, UNO_Controller controller) {
        this.card = card;
        this.cardIndex = index;
        this.isPlayable = false;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(100, 140)); //size of card

        loadCardImage();
        initializeComponents(controller);
        setupLayout();
    }

    /**
     * Initializes the UI elements for this card component, including
     * the "Use" button and its event handler.
     *
     * @param controller the game controller to handle play actions
     */
    private void initializeComponents(UNO_Controller controller) {
        // Create the "Use" button
        useButton = new JButton("Use");
        useButton.setPreferredSize(new Dimension(80, 25));
        useButton.setEnabled(false); // Initially disabled

        // Add the use button as an action listener
        useButton.addActionListener(controller);
    }

    /**
     * Builds the visual layout of this component, including the image panel and button placement.
     *
     */
    private void setupLayout() { //builds what the card looks like visually
        // Main card display panel
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                stylizeCard(g);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(100, 130);
            }
        };

        add(imagePanel, BorderLayout.CENTER);
        add(useButton, BorderLayout.SOUTH);
        updateBorder();
    }

    /**
     * Draws the card image, border, and highlights on the panel.
     *
     * @param g the {@link Graphics} context for drawing
     */
    private void stylizeCard(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (cardImage != null) {
            int panelWidth = getWidth();
            int panelHeight = getHeight() - 25; //minus 25 to account for button height

            int imgWidth = cardImage.getWidth();
            int imgHeight = cardImage.getHeight();

            // Calculate scale factor to fit image within the box while keeping aspect ratio
            double scale = Math.min(
                    (double) panelWidth / imgWidth,
                    (double) panelHeight / imgHeight
            );

            int scaledWidth = (int) (imgWidth * scale);
            int scaledHeight = (int) (imgHeight * scale);

            // Center the image
            int x = (panelWidth - scaledWidth) / 2;
            int y = (panelHeight - scaledHeight) / 2;

            g2.drawImage(cardImage, x, y, scaledWidth, scaledHeight, null);
        } else {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.BLACK);
            g.drawString("Missing", 20, 50);
        }

        // Draw border if playable
        if (isPlayable) {
            g2.setColor(Color.GREEN);
            g2.setStroke(new BasicStroke(4));
            g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
    }


    /**
     * Updates the border color to visually indicate whether the card is playable.
     *
     */
    private void updateBorder() {
        if (isPlayable) {//if playable then make it green
            setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
        } else {
            setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
    }


    /**
     * Returns the index of this card in the player's hand.
     *
     * @return the card's index position
     */
    public int getCardIndex() {
        return cardIndex;
    }

    /**
     * Returns the {@link Card} object associated with this component.
     *
     * @return {@link Card} the corresponding Card
     */
    public Card getCard() {
        return card;
    }

    /**
     * Sets whether this card is playable and updates its visuals accordingly.
     *
     * @param playable is to mark this card as playable; false otherwise
     */
    public void setPlayable(boolean playable) {
        this.isPlayable = playable;
        useButton.setEnabled(playable); //enable button to use
        updateBorder();
        repaint();
    }

    /**
     * Returns the "Use" button component associated with this card.
     *
     * @return {@link JButton} the Use button for playing this card
     */
    public JButton getUseButton() {
        return useButton;
    }


    /**
     * Checks whether this card is currently playable.
     *
     * @return boolean. true if playable; false otherwise
     */
    public boolean isPlayable() {
        return isPlayable;
    }

    /**
     * Generates the image path based on the card’s color, type, and active side.
     *
     * @return the relative file path of the card image
     */
    private String getImagePath() {
        String sideFolder = card.getActiveSide() ? "light" : "dark";
        String basePath = "assets/" + sideFolder + "/";


        String colorFolder = switch (card.getColor()) {
            case RED -> "red";
            case BLUE -> "blue";
            case YELLOW -> "yellow";
            case GREEN -> "green";
            case WILD -> "wild";
            case PURPLE -> "purple";
            case PINK -> "pink";
            case ORANGE -> "orange";
            case TEAL -> "teal";
        };

        // Map special names
        String typeName = switch (card.getType()) {
            case ONE, WILD, DARK_WILD -> "0";
            case TWO, DRAW_TWO, DRAW_COLOR -> "1";
            case THREE -> "2";
            case FOUR -> "3";
            case FIVE -> "4";
            case SIX -> "5";
            case SEVEN -> "6";
            case EIGHT -> "7";
            case NINE -> "8";
            case DRAW_ONE, DRAW_FIVE -> "10";
            case FLIP, DARK_FLIP -> "9";
            case SKIP, SKIP_EVERYONE -> "11";
            case LIGHT_REVERSE, DARK_REVERSE -> "12";
        };
        return basePath + colorFolder + "/" + typeName + ".png";
    }

    /**
     * Loads the appropriate image for the card
     * If the image cannot be found, logs a warning message and leaves
     * the card with a placeholder display.
     */
    private void loadCardImage() {
        String imagePath = getImagePath();
        try {
            cardImage = ImageIO.read(new File(imagePath));
        } catch (Exception e) {
            System.out.println("Could not load card image: " + imagePath);
            cardImage = null;
        }
    }
}



