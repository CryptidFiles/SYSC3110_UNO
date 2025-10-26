
abstract public class Card {
    protected boolean isLightSideActive;

    // Attributes shared by ALL cards
    protected Color lightColor;
    protected Color darkColor;

    protected CardType lightType;
    protected CardType darkType;

    // Blueprint for acting upon the play of the card
    public abstract boolean action(UNO_Game model, Player player);

    // Determine if card can be played on the top of the stack
    public abstract boolean playableOnTop(Card otherCard);


    public CardType getType() {
        if (isLightSideActive) {
            return lightType;
        } else {
            return darkType;
        }
    }

    public Color getColor() {
        if (isLightSideActive) {
            return lightColor;
        } else {
            return darkColor;
        }
    }

    public void flip() {
        isLightSideActive = !isLightSideActive;
    }

    public boolean getActiveSide() {
        return isLightSideActive;
    }

    public void printCard(){
        if(isLightSideActive){
            System.out.println(this.lightColor + "  " + this.lightType);
        } else if (!isLightSideActive) {
            System.out.println(this.darkColor + "  " + this.darkType);
        }
    }
}
