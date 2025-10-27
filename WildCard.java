
public class WildCard extends Card {

    public WildCard() {
        isLightSideActive = true;
        this.lightColor = Color.WILD;
        this.darkColor = Color.WILD;
        this.lightType = CardType.WILD;
        this.darkType = CardType.DARK_WILD;
    }

    @Override
    public boolean action(UNO_Game model, Player player){
        return true;
    }

    // Determine if card can be played on the top of the stack
    @Override
    public boolean playableOnTop(Card otherCard){
        return true;
    }

}
