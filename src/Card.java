
abstract public class Card {
    protected boolean isLightSideActive;

    // Attributes shared by ALL cards. Each card object has both a color and a type for each side
    protected CardColor lightColor;
    protected CardColor darkColor;

    protected CardType lightType;
    protected CardType darkType;

    // Blueprint for acting upon the play of the card
    public abstract boolean action(UNO_Game model, Player player);

    // Determine if card can be played on the top of the card in play pile stack
    public abstract boolean playableOnTop(Card otherCard);


    //CardType return the correct type depending on which side is active.
    public CardType getType() {
        if (isLightSideActive) {
            return lightType;
        } else {
            return darkType;
        }
    }

    // getColor return the correct color depending on which side is active.
    public CardColor getColor() {
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

    // Returns if the card is currently on light or dark side
    public boolean getActiveSide() {
        return isLightSideActive;
    }

    // Print out card's representation depending on which side is active
    public void printCard(){
        if(isLightSideActive){
            System.out.println(this.lightColor + "  " + this.lightType);
        } else if (!isLightSideActive) {
            System.out.println(this.darkColor + "  " + this.darkType);
        }
    }
}
