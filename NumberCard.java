
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
        //Numbered Card have no special effect
        return true;
    }


    //This decides whether the card can legally be played on top of the current top card in the discard pile.
    @Override
    public boolean playableOnTop(Card otherCard){
//        return lightColor == otherCard.getColor() || darkColor == otherCard.getColor()
//                || lightType == otherCard.getType() || darkType == otherCard.getType();
        return getColor() == otherCard.getColor() || getType() == otherCard.getType(); //this works because it checks which color: LIGHT or DARK is activated
    }
}
