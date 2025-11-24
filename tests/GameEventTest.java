import org.junit.*;
import static org.junit.Assert.*;

/**
 * Unit tests for the GameEvent class.
 */
public class GameEventTest {

    private GameEvent event;
    private Player currentPlayer;
    private Player winningPlayer;
    private Card mockCard;

    /**
     * Simple mock card for independent testing.
     */
    private static class MockCard extends Card {
        public MockCard() {
            this.isLightSideActive = true;
            this.lightColor = CardColor.RED;
            this.darkColor = CardColor.BLUE;
            this.lightType = CardType.ONE;
            this.darkType = CardType.SKIP;
        }

        @Override
        public boolean action(UNO_Model model, Player player) {
            return false;
        }

        @Override
        public boolean playableOnTop(Card otherCard) {
            return false;
        }
    }

    /**
     * Setup before each test to create default mock objects.
     */
    @Before
    public void setUp() {
        currentPlayer = new Player("John", false);
        winningPlayer = new Player("Cena", false);
        mockCard = new MockCard();

        event = new GameEvent(
                GameEvent.EventType.CARD_PLAYED,
                currentPlayer,
                winningPlayer,
                mockCard,
                "Sample message",
                Direction.CLOCKWISE,
                true,
                false,
                CardColor.GREEN
        );
    }

    /**
     * Tests that the event type is returned correctly.
     */
    @Test
    public void testGetType() {
        assertEquals(GameEvent.EventType.CARD_PLAYED, event.getType());
    }

    /**
     * Tests that the current player field is stored correctly.
     */
    @Test
    public void testGetCurrentPlayer() {
        assertEquals(currentPlayer, event.getCurrentPlayer());
        assertEquals("John", event.getCurrentPlayer().getName());
    }

    /**
     * Tests that the winning player field is stored correctly.
     */
    @Test
    public void testGetWinningPlayer() {
        assertEquals(winningPlayer, event.getWinningPlayer());
        assertEquals("Cena", event.getWinningPlayer().getName());
    }

    /**
     * Tests that the card reference is stored correctly.
     */
    @Test
    public void testGetCard() {
        assertEquals(mockCard, event.getCard());
    }

    /**
     * Tests that the event message is returned correctly.
     */
    @Test
    public void testGetMessage() {
        assertEquals("Sample message", event.getMessage());
    }

    /**
     * Tests that the direction field is stored correctly.
     */
    @Test
    public void testGetDirection() {
        assertEquals(Direction.CLOCKWISE, event.getDirection());
    }

    /**
     * Tests that the enableNextPlayer flag is stored and returned correctly.
     */
    @Test
    public void testIsEnableNextPlayer() {
        assertTrue(event.isEnableNextPlayer());
    }

    /**
     * Tests that the enableDrawButton flag is stored and returned correctly.
     */
    @Test
    public void testIsEnableDrawButton() {
        assertFalse(event.isEnableDrawButton());
    }

    /**
     * Tests that the stored wild color choice is returned correctly.
     */
    @Test
    public void testGetWildColorChoice() {
        assertEquals(CardColor.GREEN, event.getWildColorChoice());
    }

    /**
     * Tests that additional data can be stored and retrieved correctly.
     */
    @Test
    public void testSetAndGetData() {
        event.setData("points", 100);
        assertEquals(100, event.getData("points"));
    }

    /**
     * Tests that boolean data retrieval returns correct boolean values.
     */
    @Test
    public void testGetBooleanData() {
        event.setData("flag", true);
        assertTrue(event.getBooleanData("flag"));

        event.setData("flag", false);
        assertFalse(event.getBooleanData("flag"));

        assertFalse(event.getBooleanData("unknown"));
    }
}