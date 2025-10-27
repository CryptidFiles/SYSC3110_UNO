
public class NumberCard extends Card {

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


    // Card can be played if top card is same type or same color
    @Override
    public boolean playableOnTop(Card otherCard){
        return getColor() == otherCard.getColor() || getType() == otherCard.getType();
    }
}
