
abstract public class Card {
    protected boolean isLightSideActive;

    // Attributes shared by ALL cards. Each card object has both a color and a type for each side
    protected Color lightColor;
    protected Color darkColor;

    protected CardType lightType;
    protected CardType darkType;

    // Blueprint for acting upon the play of the card
    public abstract boolean action(UNO_Game model, Player player); //must be implemented by subclass, each subclass implements it differently, returns boolean if action performed

    // Determine if card can be played on the top of the stack
    public abstract boolean playableOnTop(Card otherCard); //checks whether this card can be played on top of the card in play pile

    //CardType and getColor return the correct color/type depending on which side is active.
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
    //switches the side of the card
    public void flip() {
        isLightSideActive = !isLightSideActive;
    }
    //returns which side is active dark or light
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
