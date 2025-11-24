// BasicAIStrategy.java - Keep it simple
import java.util.*;
import java.util.Random;

/**
 * This class provides a simple and efficient AI behavior
 * for UNO Flip! by always playing the first legally playable card in the hand,
 * drawing when no card can be played, and choosing a wild color based on the
 * most common color in its hand. This strategy is designed to be predictable,
 * fast, and beginner-friendly, with an optional delay to simulate thinking time.
 *
 * @author Ahmad El-Jabi 101303269
 * @author Atik Mahmud 101318070
 * @author Aryan Singh 101299776
 * @author Jonathan Gitej 101294584
 *
 * @version 3.0, November 24, 2025
 */
public class BasicAIStrategy implements AIStrategy {
    private Random random = new Random();
    private int delay_ms;

    /**
     * Constructs an instance with the specified
     * artificial decision delay.
     *
     * @param delay_ms the delay, in milliseconds, that the AI should wait before performing an action
     */
    public BasicAIStrategy(int delay_ms) {
        this.delay_ms = delay_ms;
    }

    /**
     * Determines which card the AI should play on its turn.
     * This strategy scans the AI player's hand from left to right and selects
     * the first card that is legally playable on the current top card of the
     * play pile. If no card can be played, the method returns 0,
     * indicating that the AI should draw a card instead.
     *
     * @param player  the AI player taking the turn
     * @param topCard the card currently on top of the play pile
     * @param game    the active game model, which may be used for more advanced strategies in future versions
     * @return the index of the selected card to play, or 0 to draw
     */
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
     * Determines the color to select when the AI plays a wild card.
     * This implementation examines the AI player's hand and selects the color
     * that appears most frequently. If the AI's hand contains only wild cards,
     * it defaults to the first available color on the current side of the UNO
     * Flip! deck (light or dark).
     *
     * @param aiPlayer    the AI-controlled player making the choice
     * @param isLightSide true if the light side of the deck is active, false if the dark side is active
     * @return the {@link CardColor} chosen for the wild card
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


    /**
     * Returns the artificial delay applied before the AI performs its action.
     * This delay simulates thinking time to make AI behavior appear more natural.
     *
     * @return the delay duration in milliseconds
     */
    @Override
    public int getDelayMilliseconds() {
        return delay_ms;
    }

    /**
     * Sets the artificial delay that the AI should wait before executing an action.
     * This allows configurable difficulty or pacing adjustments.
     *
     * @param delayMilliseconds the new delay time, in milliseconds
     */
    @Override
    public void setDelayMilliseconds(int delayMilliseconds){
        this.delay_ms = delayMilliseconds;
    }

}