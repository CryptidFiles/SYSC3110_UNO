import org.junit.*;
import static org.junit.Assert.*;
import java.util.ArrayList;

/**
 * Unit tests for the Player class.
 */
public class PlayerTest {

    private Player player;

    /**
     * Simple mock card for isolated testing.
     */
    private static class MockCard extends Card {
        public MockCard() {
            this.isLightSideActive = true;
            this.lightColor = CardColor.RED;
            this.darkColor = CardColor.BLUE;
            this.lightType = CardType.ONE;
            this.darkType = CardType.TWO;
        }

        @Override
        // No logic implemented here
        public boolean action(UNO_Model model, Player player) { return false; }

        @Override
        // No logic implemented here
        public boolean playableOnTop(Card otherCard) { return false; }
    }

    /**
     * Simple mock AI strategy for isolated testing.
     */
    private static class MockStrategy implements AIStrategy {

        private int delay;

        @Override
        // No logic implemented here
        public int chooseCard(Player player, Card topCard, UNO_Model game) {
            return 1;
        }

        @Override
        // // No logic implemented here. Always picks RED.
        public CardColor chooseWildColor(Player aiPlayer, boolean isLightSide) {
            return CardColor.RED;
        }

        @Override
        public int getDelayMilliseconds() {
            return delay;
        }

        @Override
        public void setDelayMilliseconds(int delayMilliseconds) {
            this.delay = delayMilliseconds;
        }
    }

    /**
     * Runs before each test to create a fresh player.
     */
    @Before
    public void setUp() {
        player = new Player("John", false);
        System.out.println("Starting new Player test...");
    }

    /**
     * Runs after each test.
     */
    @After
    public void tearDown() {
        System.out.println("Player test completed.");
    }

    /**
     * Tests that getName returns the correct value.
     */
    @Test
    public void testGetName() {
        assertEquals("John", player.getName());
    }

    /**
     * Tests that drawCardToHand adds a card to the hand.
     */
    @Test
    public void testDrawCardToHand() {
        Card c = new MockCard();
        player.drawCardToHand(c);

        assertEquals(1, player.handSize());
        assertEquals(c, player.getCardInHand(1));
    }

    /**
     * Tests retrieving a card from hand by index.
     */
    @Test
    public void testGetCardInHand() {
        Card c = new MockCard();
        player.drawCardToHand(c);

        Card retrieved = player.getCardInHand(1);
        assertEquals(c, retrieved);
    }

    /**
     * Tests removing a card from the hand.
     */
    @Test
    public void testRemoveCard() {
        Card c1 = new MockCard();
        Card c2 = new MockCard();

        player.drawCardToHand(c1);
        player.drawCardToHand(c2);

        player.removeCard(1);

        assertEquals(1, player.handSize());
        assertEquals(c2, player.getCardInHand(1));
    }

    /**
     * Tests that clearHand empties the hand.
     */
    @Test
    public void testClearHand() {
        player.drawCardToHand(new MockCard());
        player.drawCardToHand(new MockCard());

        player.clearHand();

        assertEquals(0, player.handSize());
    }

    /**
     * Tests that getHand returns the actual hand list.
     */
    @Test
    public void testGetHand() {
        player.drawCardToHand(new MockCard());

        ArrayList<Card> hand = player.getHand();
        assertEquals(1, hand.size());
    }

    /**
     * Tests that handSize returns the correct count.
     */
    @Test
    public void testHandSize() {
        assertEquals(0, player.handSize());

        player.drawCardToHand(new MockCard());
        assertEquals(1, player.handSize());
    }

    /**
     * Tests that addScore increments the score correctly.
     */
    @Test
    public void testAddScore() {
        player.addScore(10);
        player.addScore(20);

        assertEquals(30, player.getScore());
    }

    /**
     * Tests that getScore returns the correct total.
     */
    @Test
    public void testGetScore() {
        assertEquals(0, player.getScore());

        player.addScore(7);
        assertEquals(7, player.getScore());
    }

    /**
     * Tests AI flag behavior and setAI.
     */
    @Test
    public void testIsPlayerAI() {
        assertFalse(player.isPlayerAI());

        player.setAI(true);
        assertTrue(player.isPlayerAI());
    }

    /**
     * Tests setting and retrieving AIStrategy.
     */
    @Test
    public void testSetAndGetAIStrategy() {
        MockStrategy strategy = new MockStrategy();
        player.setAiStrategy(strategy);

        assertEquals(strategy, player.getAIStrategy());
    }

    /**
     * Tests second constructor that includes an AI strategy.
     */
    @Test
    public void testConstructorWithStrategy() {
        MockStrategy strategy = new MockStrategy();
        Player p = new Player("Ronaldo", true, strategy);

        assertEquals("Ronaldo", p.getName());
        assertTrue(p.isPlayerAI());
        assertEquals(strategy, p.getAIStrategy());
        assertEquals(0, p.handSize());
        assertEquals(0, p.getScore());
    }
}