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

    int getDelayMilliseconds();
    void setDelayMilliseconds(int delayMilliseconds);
}
