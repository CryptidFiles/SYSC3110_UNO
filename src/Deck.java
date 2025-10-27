import java.util.*;

public class Deck {
    //Holds every card that’s still in the deck either not dealt, not in hands, or not on the display pile.
    private ArrayList<Card> deck;

    public Deck() {
        deck = new ArrayList<>();
        initializeDeck();
    }

    private void initializeDeck() {
        //Go through every card type (e.g. NUMBER, SKIP, REVERSE) to ensure 112 card deck
        for (CardType type : CardType.values()) {

            // Number cards
            if (type.isNumberCard()) {

                // For each color of specific number card
                for (Color color : Color.values()) {
                    if (color.isLight()) {
                        deck.add(new NumberCard(color, color.getDarkCounterpart(), type));
                        deck.add(new NumberCard(color, color.getDarkCounterpart(), type));
                    }
                }
            }

            // Action Cards as outlined in the CardType enum
            if (type.isActionCard()) {
                switch (type){
                    // Takes care of DRAW_ONE and DRAW_FIVE cards as they are two sides of the same card
                    case DRAW_ONE -> {
                        for (Color color : Color.values()) {
                            if (color.isLight()) {
                                deck.add(new DrawXCard(color, color.getDarkCounterpart()));
                                deck.add(new DrawXCard(color, color.getDarkCounterpart()));
                            }
                        }
                    }

                    // Takes care of the reverse cards
                    case LIGHT_REVERSE -> {
                        for (Color color : Color.values()) {
                            if (color.isLight()) {
                                deck.add(new ReverseCard(color, color.getDarkCounterpart()));
                                deck.add(new ReverseCard(color, color.getDarkCounterpart()));
                            }
                        }
                    }

                    // Takes care of the skip and skip all cards
                    case SKIP -> {
                        for (Color color : Color.values()) {
                            if (color.isLight()) {
                                deck.add(new SkipCard(color, color.getDarkCounterpart()));
                                deck.add(new SkipCard(color, color.getDarkCounterpart()));
                            }
                        }
                    }

                    // Takes care of the flip cards
                    case FLIP -> {
                        for (Color color : Color.values()) {
                            if (color.isLight()) {
                                deck.add(new FlipCard(color, color.getDarkCounterpart()));
                                deck.add(new FlipCard(color, color.getDarkCounterpart()));
                            }
                        }
                    }

                    // Takes care of the wild change color cards
                    case WILD -> {
                        for (int i = 0; i < 4; i++) {
                            deck.add(new WildCard());
                        }
                    }

                    // Takes care of the wild draw two and wild draw color cards
                    case WILD_DRAW_TWO -> {
                        for (int i = 0; i < 4; i++) {
                            deck.add(new WildDrawCard());
                        }
                    }

                }
            }
        }

        // Shuffles arraylist of cards
        Collections.shuffle(deck);
    }

    // Adds a card to the deck
    public void addCard(Card card) {
        deck.add(card);
    }

    // Returns the current deck
    public ArrayList<Card> getDeck() {
        return deck;
    }

    // Sets the deck
    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }

    // Method handles drawing a card from it
    public Card drawCard() {
        if (deck.isEmpty()) {
            return null;
        }
        return deck.removeLast(); // Draw from end
    }

    // Shuffles deck for randomness
    public void shuffle() {
        Collections.shuffle(deck);
    }

}