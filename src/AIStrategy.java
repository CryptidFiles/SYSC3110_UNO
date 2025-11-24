/**
 * The interface defines the behavior and decision-making logic
 * for AI-controlled players in the UNO Flip! game.
 * Implementations of this interface encapsulate how an AI evaluates the game state,
 * chooses which card to play, selects wild card colors, and determines the delay
 * before performing an action. This allows different AI difficulty levels or play
 * styles to be added without modifying the core game logic.
 *
 * @author Ahmad El-Jabi 101303269
 * @author Atik Mahmud 101318070
 * @author Aryan Singh 101299776
 * @author Jonathan Gitej 101294584
 *
 * @version 3.0, November 24, 2025
 */
public interface AIStrategy {
    /**
     * Determines which card to play from the player's hand.
     *
     * @param player The AI player making the decision
     * @param topCard The current top card on the play pile
     * @param game The current game state
     * @return The index of the card to play (1-based), or 0 to draw a card
     */
    int chooseCard(Player player, Card topCard, UNO_Model game);

    /**
     * Chooses a color when playing a wild card.
     *
     * @param aiPlayer The AI player
     * @param isLightSide The current game state
     * @return The chosen color
     */
    CardColor chooseWildColor(Player aiPlayer, boolean isLightSide);

    /**
     * Retrieves the amount of time (in milliseconds) the AI should wait
     * before executing its turn.
     *
     * @return the delay duration in milliseconds
     */
    int getDelayMilliseconds();

    /**
     * Sets the delay duration for the AI's turn execution.
     *
     * @param delayMilliseconds the delay time to assign, in milliseconds
     */
    void setDelayMilliseconds(int delayMilliseconds);
}
