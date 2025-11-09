import org.junit.*;
import static org.junit.Assert.*;

/**
 * Unit tests for NumberCard in isolation, without other classes.
 */
public class NumberCardTest {

    private NumberCard numberCard;

    @BeforeClass
    public static void setUpBeforeClass() {
        System.out.println("Starting NumberCard tests...");
    }

    @AfterClass
    public static void tearDownAfterClass() {
        System.out.println("Finished NumberCard tests.");
    }

    @Before
    public void setUp() {
        numberCard = new NumberCard(Color.YELLOW, Color.ORANGE, CardType.ONE);
    }

    @After
    public void tearDown() {
        System.out.println("Test finished.");
    }

    /**
     * Tests that the constructor correctly sets light and dark colors and types.
     */
    @Test
    public void testConstructorSetsColorsAndTypes() {
        assertEquals(Color.YELLOW, numberCard.lightColor);
        assertEquals(Color.ORANGE, numberCard.darkColor);
        assertEquals(CardType.ONE, numberCard.lightType);
        assertEquals(CardType.ONE, numberCard.darkType);
        assertTrue(numberCard.isLightSideActive);
    }

    /**
     * Tests that playableOnTop returns true for cards with the same color.
     */
    @Test
    public void testPlayableOnTopSameColor() {
        NumberCard other = new NumberCard(Color.YELLOW, Color.RED, CardType.TWO);
        assertTrue(numberCard.playableOnTop(other));
    }

    /**
     * Tests that playableOnTop returns true for cards with the same type.
     */
    @Test
    public void testPlayableOnTopSameType() {
        NumberCard other = new NumberCard(Color.RED, Color.PURPLE, CardType.ONE);
        assertTrue(numberCard.playableOnTop(other));
    }

    /**
     * Tests that playableOnTop returns false for cards with different color and type.
     */
    @Test
    public void testPlayableOnTopDifferentColorAndType() {
        NumberCard other = new NumberCard(Color.RED, Color.PURPLE, CardType.TWO);
        assertFalse(numberCard.playableOnTop(other));
    }

    /**
     * Tests manual toggling of the light/dark side.
     */
    @Test
    public void testManualLightDarkSideToggle() {
        assertTrue(numberCard.isLightSideActive);
        numberCard.isLightSideActive = false;
        assertFalse(numberCard.isLightSideActive);
        numberCard.isLightSideActive = true;
        assertTrue(numberCard.isLightSideActive);
    }
}
