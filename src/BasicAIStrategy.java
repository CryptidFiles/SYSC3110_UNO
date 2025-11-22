import java.util.*;

/**
 * Basic AI strategy that plays the first valid card found.
 */
public class BasicAIStrategy implements AIStrategy {
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

    @Override
    public CardColor chooseWildColor(Player player, UNO_Model game) {
        // Choose the most common color in hand
        Map<CardColor, Integer> colorCount = new HashMap<>();
        for (Card card : player.getHand()) {
            CardColor color = card.getColor();
            colorCount.put(color, colorCount.getOrDefault(color, 0) + 1);
        }

        return colorCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(CardColor.RED);
    }
}