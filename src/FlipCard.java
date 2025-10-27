
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
        isLightSideActive = false;
        // Flip the rest of the cards
        model.flipGameSide();
        return true;
    }

    // Determine if card can be played on the top of the stack
    @Override
    public boolean playableOnTop(Card otherCard){
        return lightColor == otherCard.getColor() || darkColor == otherCard.getColor()
                || lightType == otherCard.getType() || darkType == otherCard.getType();
    }

}
