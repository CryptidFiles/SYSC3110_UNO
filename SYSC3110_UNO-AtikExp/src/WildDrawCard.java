
public class WildDrawCard extends Card {

    public WildDrawCard() {
        isLightSideActive = true;
        this.lightColor = Color.WILD;
        this.darkColor = Color.WILD;
        this.lightType = CardType.WILD_DRAW_TWO;
        this.darkType = CardType.WILD_DRAW_COLOR;
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
