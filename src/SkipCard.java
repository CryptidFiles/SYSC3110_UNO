
public class SkipCard extends Card {

    public SkipCard(Color light_color, Color dark_color) {
        isLightSideActive = true;
        this.lightColor = light_color;
        this.darkColor = dark_color;
        this.lightType = CardType.SKIP;
        this.darkType = CardType.SKIP_EVERYONE;
    }

    @Override
    public boolean action(UNO_Game model, Player player){
        if (isLightSideActive) {
            // Light side: Regular Skip - skip next player
            Player nextPlayer = model.getNextPlayer(player);
            System.out.println("Skipping " + nextPlayer.getName() + "!");
            model.addSkip(1);

        } else {
            // Dark side: Skip All - skip all other players
            System.out.println("Skipping all other players!");
            model.skipAllPlayers();
        }

        return true;
    }

    // Determine if card can be played on the top of the stack
    @Override
    public boolean playableOnTop(Card otherCard){
        return lightColor == otherCard.getColor() || darkColor == otherCard.getColor()
                || lightType == otherCard.getType() || darkType == otherCard.getType();
    }

}
