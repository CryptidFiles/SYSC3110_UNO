import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

// Physical UNO card component with "Use" button
public class CardComponent extends JPanel {
    private Card card;           // The actual card data from model
    private int cardIndex;       // Position in player's hand
    private boolean isPlayable;  // Highlight if playable
    private JButton useButton;
    private BufferedImage cardImage;

    public CardComponent(Card card, int index, UNO_Controller controller) {
        this.card = card;
        this.cardIndex = index;
        this.isPlayable = false;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(120, 140));

        loadCardImage();
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
            // Debug output
            System.out.println("Use button clicked for card index: " + cardIndex);
            controller.handleCardPlay(cardIndex);
        });
    }

    private void setupLayout() {
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawCard(g);
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

    private void drawCard(Graphics g) {
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


    private void updateBorder() {
        if (isPlayable) {
            setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
        } else {
            setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
    }

    // Getters and setters
    public int getCardIndex() {
        return cardIndex;
    }

    public Card getCard() {
        return card;
    }

    public void setPlayable(boolean playable) {
        this.isPlayable = playable;
        useButton.setEnabled(playable);
        updateBorder();
        repaint();
    }

    public JButton getUseButton() {
        return useButton;
    }


    public boolean isPlayable() {
        return isPlayable;
    }

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



