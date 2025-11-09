import org.junit.*;
import static org.junit.Assert.*;

/**
 * Unit tests for the abstract Card class.
 * Tests methods like getType(), getColor(), flip(), and getActiveSide().
 */
public class CardTest {

    private static Card testCard;

    // Concrete subclass to test the abstract Card
    private static class TestCard extends Card {
        public TestCard(CardColor lightColor, CardColor darkColor, CardType lightType, CardType darkType) {
            this.lightColor = lightColor;
            this.darkColor = darkColor;
            this.lightType = lightType;
            this.darkType = darkType;
            this.isLightSideActive = true; // default to light side
        }

        @Override
        public boolean action(UNO_Game model, Player player) {
            return true; // simple stub
        }

        @Override
        public boolean playableOnTop(Card otherCard) {
            return true; // simple stub
        }
    }

    @BeforeClass
    public static void setUpBeforeClass() {
        System.out.println("Starting CardTest...");
        testCard = new TestCard(Color.RED, Color.BLUE, CardType.ONE, CardType.SKIP);
    }

    @AfterClass
    public static void tearDownAfterClass() {
        System.out.println("Finished CardTest.");
        testCard = null;
    }

    @Before
    public void setUp() {
        // Reset to light side before each test
        testCard.isLightSideActive = true;
    }

    @After
    public void tearDown() {
        // Nothing to clean up for now
    }

    /**
     * Test getType() method returns the correct type depending on active side.
     */
    @Test
    public void testGetType() {
        assertEquals(CardType.ONE, testCard.getType());
        testCard.flip();
        assertEquals(CardType.SKIP, testCard.getType());
    }

    /**
     * Test getColor() method returns the correct color depending on active side.
     */
    @Test
    public void testGetColor() {
        assertEquals(Color.RED, testCard.getColor());
        testCard.flip();
        assertEquals(Color.BLUE, testCard.getColor());
    }

    /**
     * Test flip() method switches the active side.
     */
    @Test
    public void testFlip() {
        assertTrue(testCard.getActiveSide()); // starts as light
        testCard.flip();
        assertFalse(testCard.getActiveSide()); // should now be dark
        testCard.flip();
        assertTrue(testCard.getActiveSide()); // back to light
    }

    /**
     * Test getActiveSide() method returns true for light and false for dark.
     */
    @Test
    public void testGetActiveSide() {
        assertTrue(testCard.getActiveSide());
        testCard.flip();
        assertFalse(testCard.getActiveSide());
    }

    /**
     * Test printCard() method prints the correct representation.
     */
    @Test
    public void testPrintCard() {
        System.out.println("Expected: RED  NUMBER");
        testCard.printCard(); // manually verify console output
        testCard.flip();
        System.out.println("Expected: BLUE  SKIP");
        testCard.printCard(); // manually verify console output
    }
}
