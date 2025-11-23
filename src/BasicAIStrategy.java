// BasicAIStrategy.java - Keep it simple
import java.util.*;
import java.util.Random;

public class BasicAIStrategy implements AIStrategy {
    private Random random = new Random();

    @Override
    public int chooseCard(Player player, Card topCard, UNO_Model game) {
        ArrayList<Card> hand = player.getHand();

        // Look for first playable card
        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.get(i);
            if (card.playableOnTop(topCard)) {
                return i + 1; // Convert to 1-based index
            }
        }

        // No playable card found, draw
        return 0;
    }

    /**
     * Chooses a color for wild cards based on the AI's hand
     */
    @Override
    public CardColor chooseWildColor(Player player, boolean isLightSide) {
        // Count colors in hand to make smart choice
        int[] colorCounts = new int[8]; // For all possible colors

        for (Card card : player.getHand()) {
            if (card.getColor() != CardColor.WILD) {
                int colorIndex = card.getColor().ordinal();
                colorCounts[colorIndex]++;
            }
        }

        // Find the most frequent color
        int maxCount = -1;
        CardColor bestColor = isLightSide ? CardColor.RED : CardColor.TEAL; // Default

        for (CardColor color : CardColor.values()) {
            if (color == CardColor.WILD) continue;

            // Filter by light/dark side
            if (isLightSide && color.isLight()) continue;
            if (!isLightSide && color.isDark()) continue;

            int count = colorCounts[color.ordinal()];
            if (count > maxCount) {
                maxCount = count;
                bestColor = color;
            }
        }

        return bestColor;
    }

    @Override
    public int getDelayMilliseconds() {
        return 1500; // 1.5 second delay for AI "thinking"
    }
}