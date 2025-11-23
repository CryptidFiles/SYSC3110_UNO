import org.junit.*;
import static org.junit.Assert.*;
import java.util.ArrayList;

/**
 * Unit tests for Player class.
 */
public class PlayerTest {

    private Player player;
    private Card dummyCard;

    @BeforeClass
    public static void setUpBeforeClass() {
        System.out.println("Starting Player tests...");
    }

    @AfterClass
    public static void tearDownAfterClass() {
        System.out.println("Finished Player tests.");
    }

    @Before
    public void setUp() {
        player = new Player("Alice", false);
        // Minimal dummy card for testing
        dummyCard = new Card() {
            @Override
            public boolean action(UNO_Model model, Player player) { return true; }
            @Override
            public boolean playableOnTop(Card otherCard) { return true; }
        };
    }

    @After
    public void tearDown() {
        System.out.println("Test finished.");
    }

    /**
     * Tests getName() method.
     */
    @Test
    public void testGetName() {
        assertEquals("Alice", player.getName());
    }

    /**
     * Tests drawCard() adds a card to the player's hand.
     */
    @Test
    public void testDrawCard() {
        player.drawCardToHand(dummyCard);
        assertEquals(1, player.handSize());
        assertTrue(player.getHand().contains(dummyCard));
    }

    /**
     * Tests playCard() returns the correct card.
     */
    @Test
    public void testGetCardInHand() {
        player.drawCardToHand(dummyCard);
        Card played = player.getCardInHand(1);
        assertEquals(dummyCard, played);
    }

    /**
     * Tests removeCard() removes the correct card.
     */
    @Test
    public void testRemoveCard() {
        player.drawCardToHand(dummyCard);
        player.removeCard(1);
        assertEquals(0, player.handSize());
    }

    /**
     * Tests clearHand() empties the player's hand.
     */
    @Test
    public void testClearHand() {
        player.drawCardToHand(dummyCard);
        player.clearHand();
        assertEquals(0, player.handSize());
    }

    /**
     * Tests getHand() returns the correct hand.
     */
    @Test
    public void testGetHand() {
        player.drawCardToHand(dummyCard);
        ArrayList<Card> hand = player.getHand();
        assertEquals(1, hand.size());
        assertTrue(hand.contains(dummyCard));
    }

    /**
     * Tests handSize() returns the correct number of cards.
     */
    @Test
    public void testHandSize() {
        assertEquals(0, player.handSize());
        player.drawCardToHand(dummyCard);
        assertEquals(1, player.handSize());
    }

    /**
     * Tests addScore() correctly increments the score.
     */
    @Test
    public void testAddScore() {
        assertEquals(0, player.getScore());
        player.addScore(5);
        assertEquals(5, player.getScore());
        player.addScore(3);
        assertEquals(8, player.getScore());
    }

    /**
     * Tests getScore() returns the correct score.
     */
    @Test
    public void testGetScore() {
        player.addScore(10);
        assertEquals(10, player.getScore());
    }
}
