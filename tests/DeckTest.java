import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

/**
 * Unit tests for the Deck class.
 * Tests methods like drawCard(), addCard(Card), getDeck(), setDeck(ArrayList), and shuffle().
 */
public class DeckTest {

    private Deck deck;

    @BeforeClass
    public static void setUpBeforeClass() {
        System.out.println("Starting Deck tests...");
    }

    @AfterClass
    public static void tearDownAfterClass() {
        System.out.println("Finished Deck tests.");
    }

    @Before
    public void setUp() {
        deck = new Deck();
        System.out.println("Starting a test...");
    }

    @After
    public void tearDown() {
        System.out.println("Test finished.");
    }

    @Test
    public void testDeckInitialization() {
        assertNotNull(deck.getDeck());
        // Assuming full deck has cards, cannot test exact number without CardType knowledge
        assertTrue(deck.getDeck().size() > 0);
    }

    @Test
    public void testDrawCard() {
        int initialSize = deck.getDeck().size();
        Card drawn = deck.drawCardFromDeck();
        assertNotNull(drawn);
        assertEquals(initialSize - 1, deck.getDeck().size());

        // Draw all remaining cards
        while (!deck.getDeck().isEmpty()) {
            deck.drawCardFromDeck();
        }
        assertNull(deck.drawCardFromDeck()); // Should return null when empty
    }

    @Test
    public void testAddCard() {
        int initialSize = deck.getDeck().size();
        Card card = new WildCard();
        deck.addCard(card);
        assertEquals(initialSize + 1, deck.getDeck().size());
        assertTrue(deck.getDeck().contains(card));
    }

    @Test
    public void testSetAndGetDeck() {
        ArrayList<Card> newDeck = new ArrayList<>();
        newDeck.add(new WildCard());
        deck.setDeck(newDeck);
        assertEquals(newDeck, deck.getDeck());
    }

    @Test
    public void testShuffle() {
        ArrayList<Card> copyBeforeShuffle = new ArrayList<>(deck.getDeck());
        deck.shuffle();
        ArrayList<Card> copyAfterShuffle = deck.getDeck();

        // Same cards remain, but order may change
        assertEquals(copyBeforeShuffle.size(), copyAfterShuffle.size());
        assertTrue(copyAfterShuffle.containsAll(copyBeforeShuffle));
    }
}
