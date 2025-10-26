
public class NumberCard extends Card {
    private int number;

    public NumberCard(Color light_color, Color dark_color, CardType number) {
        isLightSideActive = true;
        this.lightColor = light_color;
        this.darkColor = dark_color;
        this.lightType = number;
        this.darkType = number;
    }

    @Override
    public boolean action(UNO_Game model, Player player){
        return true;
    }


    // Determine if card can be played on the top of the stack
    @Override
    public boolean playableOnTop(Card otherCard){
        return lightColor == otherCard.getColor() || darkColor == otherCard.getColor()
                || lightType == otherCard.getType() || darkType == otherCard.getType();
    }
}
