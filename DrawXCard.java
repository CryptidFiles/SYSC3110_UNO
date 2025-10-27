//So it’s a child class, a specific type of Card that has the “draw card” effect (like Draw 1 on the light side, Draw 5 on the dark side).
public class DrawXCard extends Card {

    public DrawXCard(Color light_color, Color dark_color) {
        isLightSideActive = true;
        this.lightColor = light_color;
        this.darkColor = dark_color;
        this.lightType = CardType.DRAW_ONE;
        this.darkType = CardType.DRAW_FIVE;
    }

    @Override
    public boolean action(UNO_Game model, Player player){
        return true;
    }

    // Determine if card can be played on the top of the stack
    @Override
    public boolean playableOnTop(Card otherCard){
//        return lightColor == otherCard.getColor() || darkColor == otherCard.getColor()
//                || lightType == otherCard.getType() || darkType == otherCard.getType();
        return getColor() == otherCard.getColor() || getType() == otherCard.getType();
    }

}
