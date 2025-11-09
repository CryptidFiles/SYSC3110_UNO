import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Color;

// Physical UNO card component that you can see and click
public class CardComponent extends JComponent {
    private Card card;           // The actual card data from model
    private int cardIndex;       // Position in player's hand
    private boolean isSelected;  // Visual feedback
    private boolean isPlayable;  // Highlight if playable

    public CardComponent(Card card, int index) {
        this.card = card;
        this.cardIndex = index;
        this.isSelected = false;
        this.isPlayable = false;

        // Make it clickable
        //this.addActionListener();
        this.setPreferredSize(new Dimension(80, 120)); // Standard card size
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw card background based on color
        Color cardColor = convertToAWTColor(card.getColor());
        g.setColor(cardColor);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw border
        if (isSelected) {
            g.setColor(Color.YELLOW);
            g.drawRect(2, 2, getWidth()-4, getHeight()-4);
        }

        if (isPlayable) {
            g.setColor(Color.GREEN);
            g.drawRect(1, 1, getWidth()-2, getHeight()-2);
        }

        // Draw card type/number
        g.setColor(Color.BLACK);
        g.drawString(card.getType().toString(), 10, 20);

        // Draw which side is active
        String side = card.getActiveSide() ? "LIGHT" : "DARK";
        g.drawString(side, 10, 40);
    }

    private java.awt.Color convertToAWTColor(CardColor unoColor) {
        // Convert color attribute of card to visual java awt color
        // Used fully qualified names to distinguish between your enum and AWT Color
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

    // Getters and setters
    public int getCardIndex() { return cardIndex; }
    public Card getCard() { return card; }
    public void setPlayable(boolean playable) {
        this.isPlayable = playable;
        repaint(); // Refresh visual
    }
    public void setSelected(boolean selected) {
        this.isSelected = selected;
        repaint();
    }

    // Handle clicks on this card
        //@Override
        //public void mouseClicked(MouseEvent e) {
            // Notify controller that this card was clicked
            // This would trigger model.playCard(cardIndex)
        //}

}
