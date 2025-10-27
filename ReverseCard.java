
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

    // Determine if card can be played on the top of the stack
    @Override
    public boolean playableOnTop(Card otherCard){
//        return lightColor == otherCard.getColor() || darkColor == otherCard.getColor()
//                || lightType == otherCard.getType() || darkType == otherCard.getType();
        return getColor() == otherCard.getColor() || getType() == otherCard.getType(); //t
    }

}
