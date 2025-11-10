
public class DrawXCard extends Card {

    public DrawXCard(CardColor light_color, CardColor dark_color) {
        isLightSideActive = true;
        this.lightColor = light_color;
        this.darkColor = dark_color;
        this.lightType = CardType.DRAW_ONE;
        this.darkType = CardType.DRAW_FIVE;
    }

    @Override
    public boolean action(UNO_Game model, Player player){
        if(isLightSideActive) {
            // Light side: DRAW_ONE - next player draws one card and loses turn
            Player nextPlayer = model.getNextPlayer(player);

            Card card = model.getPlayDeck().drawCard();
            if (card == null) {
                model.notifyMessage("No more cards left!");
                return false;
            }

            nextPlayer.drawCard(card);
            model.notifyMessage(nextPlayer.getName() + " draws 1 card and loses their turn!");

            // Skip the next player's turn
            model.addSkip(1);

        } else {
            // Dark side: DRAW_FIVE - next player draws five cards and loses turn
            Player nextPlayer = model.getNextPlayer(player);

            for(int i = 0; i < 5; i++){
                Card card = model.getPlayDeck().drawCard();

                if (card == null) {
                    model.notifyMessage("No more cards left after drawing " + i + " cards!");
                    return false;
                }

                nextPlayer.drawCard(card);
            }
            model.notifyMessage(nextPlayer.getName() + " draws 5 cards and loses their turn!");

            // Skip the next player's turn
            model.addSkip(1);
        }

        return true;
    }

    // Card can be played if top card is same type or same color
    @Override
    public boolean playableOnTop(Card otherCard){
        return getColor() == otherCard.getColor() || getType() == otherCard.getType();
    }

}
