import java.util.*;

public class Deck {

    private ArrayList<Card> deck;

    public Deck() {
        deck = new ArrayList<>();
        initializeDeck();
    }

    private void initializeDeck() {
        for (CardType type : CardType.values()) {

            //Number cards
            if (type.isNumberCard()) {

                // For each color of specific number card
                for (Color color : Color.values()) {
                    if (color.isLight()) {
                        deck.add(new NumberCard(color, color.getDarkCounterpart(), type));
                        deck.add(new NumberCard(color, color.getDarkCounterpart(), type));
                    }
                }
            }

            // Reverse Card
            if (type.isActionCard()) {
                switch (type){
                    case DRAW_ONE -> {
                        for (Color color : Color.values()) {
                            if (color.isLight()) {
                                deck.add(new DrawXCard(color, color.getDarkCounterpart()));
                                deck.add(new DrawXCard(color, color.getDarkCounterpart()));
                            }
                        }
                    }

                    case LIGHT_REVERSE -> {
                        for (Color color : Color.values()) {
                            if (color.isLight()) {
                                deck.add(new ReverseCard(color, color.getDarkCounterpart()));
                                deck.add(new ReverseCard(color, color.getDarkCounterpart()));
                            }
                        }
                    }

                    case SKIP -> {
                        for (Color color : Color.values()) {
                            if (color.isLight()) {
                                deck.add(new SkipCard(color, color.getDarkCounterpart()));
                                deck.add(new SkipCard(color, color.getDarkCounterpart()));
                            }
                        }
                    }

                    case FLIP -> {
                        for (Color color : Color.values()) {
                            if (color.isLight()) {
                                deck.add(new FlipCard(color, color.getDarkCounterpart()));
                                deck.add(new FlipCard(color, color.getDarkCounterpart()));
                            }
                        }
                    }

                    case WILD -> {
                        for (int i = 0; i < 4; i++) {
                            deck.add(new WildCard());
                        }
                    }

                    case WILD_DRAW_TWO -> {
                        for (int i = 0; i < 4; i++) {
                            deck.add(new WildDrawCard());
                        }
                    }

                }
            }
        }

        Collections.shuffle(deck);
    }

    public void addCard(Card card) {
        deck.add(card);
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }

    // Basic drawCard method
    public Card drawCard() {
        if (deck.isEmpty()) {
            return null; // or handle reshuffling from discard pile
        }
        return deck.removeLast(); // Draw from top
    }

    public void shuffle() {
        Collections.shuffle(deck);
    }

}