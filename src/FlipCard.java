
public class FlipCard extends Card {

    public FlipCard(Color light_color, Color dark_color) {
        isLightSideActive = true;
        this.lightColor = light_color;
        this.darkColor = dark_color;
        this.lightType = CardType.FLIP;
        this.darkType = CardType.DARK_FLIP;
    }

    @Override
    public boolean action(UNO_Game model, Player player){
        isLightSideActive = !isLightSideActive;

        // Flip the rest of the cards
        model.flipGameSide();
        return true;
    }

    // Card can be played if top card is same type or same color
    @Override
    public boolean playableOnTop(Card otherCard){
        return getColor() == otherCard.getColor() || getType() == otherCard.getType();
    }

}
