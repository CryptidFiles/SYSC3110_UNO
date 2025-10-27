
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


    // Determine if card can be played on the top of the stack
    @Override
    public boolean playableOnTop(Card otherCard){
        //this works because it checks which color: LIGHT or DARK is activated
        return getColor() == otherCard.getColor() || getType() == otherCard.getType();
    }
}
