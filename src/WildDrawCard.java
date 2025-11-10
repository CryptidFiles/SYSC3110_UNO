public class WildDrawCard extends Card {
    public WildDrawCard() {
        isLightSideActive = true;
        this.lightColor = CardColor.WILD;
        this.darkColor = CardColor.WILD;
        this.lightType = CardType.DRAW_TWO;
        this.darkType = CardType.DRAW_COLOR;
    }

    @Override
    public boolean action(UNO_Game model, Player player) {
        // Trigger color selection
        model.triggerColorSelection();
        return true; // Draw action will happen after color selection
    }

    /**
     * Executes the draw action after color is chosen
     */
    public void executeDrawAction(CardColor chosenColor, boolean isLightSide, UNO_Game model, Player player) {
        if (isLightSide) {
            this.lightColor = chosenColor;
            this.darkColor = chosenColor.getDarkCounterpart();

            // Light side: next player picks up two cards
            Player nextPlayer = model.getNextPlayer(player);
            model.notifyMessage(nextPlayer.getName() + " draws 2 cards!");

            for(int i = 0; i < 2; i++){
                Card card = model.getPlayDeck().drawCard();
                if (card == null) break;
                nextPlayer.drawCard(card);
            }

            model.addSkip(1); // Skip the next player

        } else {
            this.darkColor = chosenColor;
            this.lightColor = chosenColor.getLightCounterpart();

            // Dark side: next player keeps drawing until they get the chosen color
            Player nextPlayer = model.getNextPlayer(player);
            boolean foundColor = false;
            int cardsDrawn = 0;

            while (!foundColor) {
                Card drawnCard = model.getPlayDeck().drawCard();
                if (drawnCard == null) {
                    model.notifyMessage("No more cards in deck!");
                    break;
                }

                cardsDrawn++;
                nextPlayer.drawCard(drawnCard);

                if (drawnCard.getColor() == this.darkColor) {
                    foundColor = true;
                    model.notifyMessage(nextPlayer.getName() + " found the chosen color after drawing " + cardsDrawn + " cards!");
                }
            }

            model.addSkip(1); // Skip the next player
        }
    }

    @Override
    public boolean playableOnTop(Card otherCard) {
        return true;
    }
}