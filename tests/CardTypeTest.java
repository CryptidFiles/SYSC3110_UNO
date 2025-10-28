import org.junit.*;
import static org.junit.Assert.*;

/**
 * Unit tests for the CardType enum.
 * Tests classification methods (isNumberCard, isActionCard, isWildCard) and the point value calculation (getPointValue) for all card types.
 */
public class CardTypeTest {

    /**
     * Runs once before all tests.
     * Used to initialize any global resources if needed.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        System.out.println(">>> Starting CardType tests...");
    }

    /**
     * Runs once after all tests.
     * Used to clean up any global resources if needed.
     */
    @AfterClass
    public static void tearDownAfterClass() {
        System.out.println(">>> Finished all CardType tests.");
    }

    /**
     * Runs before each individual test.
     * Used to set up preconditions for the test.
     */
    @Before
    public void setUp() {
        System.out.println("Starting a new test...");
    }

    /**
     * Runs after each individual test.
     * Used to clean up after each test.
     */
    @After
    public void tearDown() {
        System.out.println("Test completed.\n");
    }

    /**
     * Tests that number cards return true for isNumberCard().
     */
    @Test
    public void testIsNumberCard_TrueForNumbers() {
        assertTrue(CardType.ONE.isNumberCard());
        assertTrue(CardType.NINE.isNumberCard());
    }

    /**
     * Tests that action cards return false for isNumberCard().
     */
    @Test
    public void testIsNumberCard_FalseForActionCards() {
        assertFalse(CardType.DRAW_ONE.isNumberCard());
        assertFalse(CardType.WILD.isNumberCard());
    }

    /**
     * Tests that action cards return true for isActionCard().
     */
    @Test
    public void testIsActionCard_TrueForActions() {
        assertTrue(CardType.DRAW_ONE.isActionCard());
        assertTrue(CardType.DARK_FLIP.isActionCard());
    }

    /**
     * Tests that number cards return false for isActionCard().
     */
    @Test
    public void testIsActionCard_FalseForNumbers() {
        assertFalse(CardType.THREE.isActionCard());
    }

    /**
     * Tests that wild cards return true for isWildCard().
     */
    @Test
    public void testIsWildCard_TrueForWilds() {
        assertTrue(CardType.WILD.isWildCard());
        assertTrue(CardType.WILD_DRAW_TWO.isWildCard());
        assertTrue(CardType.DARK_WILD.isWildCard());
        assertTrue(CardType.WILD_DRAW_COLOR.isWildCard());
    }

    /**
     * Tests that non-wild cards return false for isWildCard().
     */
    @Test
    public void testIsWildCard_FalseForOthers() {
        assertFalse(CardType.ONE.isWildCard());
        assertFalse(CardType.FLIP.isWildCard());
    }

    /**
     * Tests that number cards return the correct point values.
     */
    @Test
    public void testGetPointValue_NumberCards() {
        assertEquals(1, CardType.ONE.getPointValue());
        assertEquals(9, CardType.NINE.getPointValue());
    }

    /**
     * Tests that action and wild cards return the correct point values.
     */
    @Test
    public void testGetPointValue_ActionCards() {
        assertEquals(10, CardType.DRAW_ONE.getPointValue());
        assertEquals(20, CardType.LIGHT_REVERSE.getPointValue());
        assertEquals(20, CardType.DARK_REVERSE.getPointValue());
        assertEquals(20, CardType.SKIP.getPointValue());
        assertEquals(20, CardType.FLIP.getPointValue());
        assertEquals(20, CardType.DARK_FLIP.getPointValue());
        assertEquals(30, CardType.SKIP_EVERYONE.getPointValue());
        assertEquals(50, CardType.WILD_DRAW_TWO.getPointValue());
        assertEquals(60, CardType.WILD_DRAW_COLOR.getPointValue());
        assertEquals(40, CardType.WILD.getPointValue());
        assertEquals(40, CardType.DARK_WILD.getPointValue());
    }

    /**
     * Tests that DRAW_FIVE card returns the correct point value.
     */
    @Test
    public void testGetPointValue_DrawFive() {
        assertEquals(20, CardType.DRAW_FIVE.getPointValue());
    }
}
