import org.junit.*;
import static org.junit.Assert.*;

/**
 * Unit tests for the Color enum.
 * This class tests helper methods like isLight(), isDark(), getDarkCounterpart(), and getLightCounterpart().
 */
public class CardColorTest {

    @BeforeClass
    public static void setUpBeforeClass() {
        System.out.println("Starting Color tests...");
    }

    @AfterClass
    public static void tearDownAfterClass() {
        System.out.println("Finished Color tests.");
    }

    @Before
    public void setUp() {
        System.out.println("Starting a test...");
    }

    @After
    public void tearDown() {
        System.out.println("Test finished.");
    }

    @Test
    public void testIsLight() {
        assertTrue(CardColor.RED.isLight());
        assertTrue(CardColor.BLUE.isLight());
        assertTrue(CardColor.YELLOW.isLight());
        assertTrue(CardColor.GREEN.isLight());

        assertFalse(CardColor.PINK.isLight());
        assertFalse(CardColor.TEAL.isLight());
        assertFalse(CardColor.ORANGE.isLight());
        assertFalse(CardColor.PURPLE.isLight());
        assertFalse(CardColor.WILD.isLight());
    }

    @Test
    public void testIsDark() {
        assertTrue(CardColor.PINK.isDark());
        assertTrue(CardColor.TEAL.isDark());
        assertTrue(CardColor.ORANGE.isDark());
        assertTrue(CardColor.PURPLE.isDark());

        assertFalse(CardColor.RED.isDark());
        assertFalse(CardColor.BLUE.isDark());
        assertFalse(CardColor.YELLOW.isDark());
        assertFalse(CardColor.GREEN.isDark());
        assertFalse(CardColor.WILD.isDark());
    }

    @Test
    public void testGetDarkCounterpart() {
        assertEquals(CardColor.PINK, CardColor.RED.getDarkCounterpart());
        assertEquals(CardColor.TEAL, CardColor.BLUE.getDarkCounterpart());
        assertEquals(CardColor.ORANGE, CardColor.YELLOW.getDarkCounterpart());
        assertEquals(CardColor.PURPLE, CardColor.GREEN.getDarkCounterpart());

        // Dark or wild colors return themselves
        assertEquals(CardColor.PINK, CardColor.PINK.getDarkCounterpart());
        assertEquals(CardColor.WILD, CardColor.WILD.getDarkCounterpart());
    }

    @Test
    public void testGetLightCounterpart() {
        assertEquals(CardColor.RED, CardColor.PINK.getLightCounterpart());
        assertEquals(CardColor.BLUE, CardColor.TEAL.getLightCounterpart());
        assertEquals(CardColor.YELLOW, CardColor.ORANGE.getLightCounterpart());
        assertEquals(CardColor.GREEN, CardColor.PURPLE.getLightCounterpart());

        // Light or wild colors return themselves
        assertEquals(CardColor.RED, CardColor.RED.getLightCounterpart());
        assertEquals(CardColor.WILD, CardColor.WILD.getLightCounterpart());
    }
}