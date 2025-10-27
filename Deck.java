import java.util.*;

public class Deck {
    //Holds every card that’s still in the deck either not dealt, not in hands, or not on the display pile.
    private ArrayList<Card> deck;

    public Deck() {
        deck = new ArrayList<>();
        initializeDeck();
    }

    private void initializeDeck() {
        for (CardType type : CardType.values()) { //goes through every card type (NUMBER, SKIP, REVERSE)

            //Number cards
            if (type.isNumberCard()) { //if card is a number not Action Type, make card for each color

                // For each color of specific number card
                for (Color color : Color.values()) {
                    if (color.isLight()) { //only use light colors, we create their dark version indirectly
                        deck.add(new NumberCard(color, color.getDarkCounterpart(), type)); //first parameter if light, second param is dark, third is type value
                        deck.add(new NumberCard(color, color.getDarkCounterpart(), type)); //because in real UNO you have two of each number.
                    }
                }
            }

            // Reverse Card
            if (type.isActionCard()) { //if current type is a special card
                if (type == CardType.DRAW_ONE) {//Takes care of DRAW_ONE and DRAW_% at same time, cause card has two sides, one of each. this makes one side DRAWONE light and DRAWFIVE dark
                    for (Color color : Color.values()) {
                        deck.add(new DrawXCard(color, color.getDarkCounterpart()));
                        deck.add(new DrawXCard(color, color.getDarkCounterpart()));
                    }
                }

                if (type == CardType.LIGHT_REVERSE) { //takes care of LIGHT_REVERSE and DARK_REVERSE Each card has two sides, one for the light color, and one for its dark twin, Two copies per color again.
                    for (Color color : Color.values()) {
                        deck.add(new ReverseCard(color, color.getDarkCounterpart()));
                        deck.add(new ReverseCard(color, color.getDarkCounterpart()));
                    }
                }

                if (type == CardType.SKIP) { //same pattern as LIGHT_REVERSE just its SKIP NOW also takes care of Skip everyone
                    for (Color color : Color.values()) {
                        deck.add(new SkipCard(color, color.getDarkCounterpart()));
                        deck.add(new SkipCard(color, color.getDarkCounterpart()));
                    }
                }

                if (type == CardType.WILD) {
                    for (int i = 0; i < 4; i++) { //no colors needed
                        deck.add(new WildCard());
                        deck.add(new WildCard());
                    }
                }

                if (type == CardType.WILD_DRAW_TWO) {
                    for (int i = 0; i < 4; i++) {
                        deck.add(new WildDrawCard());
                        deck.add(new WildDrawCard());
                    }
                }
            }
        }

        Collections.shuffle(deck);
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    // Basic drawCard method
    public Card drawCard() {
        if (deck.isEmpty()) {
            return null; // or handle reshuffling from discard pile
        }
        return deck.removeLast(); // Draw from top
    }

}