
public class ReverseCard extends Card {

    public ReverseCard(Color light_color, Color dark_color) {
        isLightSideActive = true;
        this.lightColor = light_color;
        this.darkColor = dark_color;
        this.lightType = CardType.LIGHT_REVERSE;
        this.darkType = CardType.DARK_REVERSE;
    }

    @Override
    public boolean action(UNO_Game model, Player player){
        model.flipDirection();
        return true;
    }

    // Card can be played if top card is same type or same color
    @Override
    public boolean playableOnTop(Card otherCard){
        return getColor() == otherCard.getColor() || getType() == otherCard.getType();
    }

}
