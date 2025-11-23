// BasicAIStrategy.java - Keep it simple
import java.util.*;
import java.util.Random;

public class BasicAIStrategy implements AIStrategy {
    private Random random = new Random();
    private int delay_ms;

    public BasicAIStrategy(int delay_ms) {
        this.delay_ms = delay_ms;
    }

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
    public CardColor chooseWildColor(Player aiPlayer, boolean isLightSide) {
        // Count color frequencies in hand
        Map<CardColor, Integer> colorCounts = new HashMap<>();

        for (Card card : aiPlayer.getHand()) {
            CardColor color = card.getColor();
            if (color != CardColor.WILD) { // Don't count wild cards
                colorCounts.put(color, colorCounts.getOrDefault(color, 0) + 1);
            }
        }

        // Choose the most frequent color
        CardColor bestColor = null;
        int maxCount = -1;

        for (Map.Entry<CardColor, Integer> entry : colorCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                bestColor = entry.getKey();
            }
        }

        // If no colors found (only wild cards), choose a default
        if (bestColor == null) {
            CardColor[] availableColors = isLightSide ?
                    new CardColor[]{CardColor.RED, CardColor.BLUE, CardColor.GREEN, CardColor.YELLOW} :
                    new CardColor[]{CardColor.TEAL, CardColor.ORANGE, CardColor.PURPLE, CardColor.PINK};
            bestColor = availableColors[0];
        }

        return bestColor;
    }


    @Override
    public int getDelayMilliseconds() {
        return delay_ms;
    }

    @Override
    public void setDelayMilliseconds(int delayMilliseconds){
        this.delay_ms = delayMilliseconds;
    }

}