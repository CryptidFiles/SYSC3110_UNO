import org.junit.*;
import static org.junit.Assert.*;

public class WildCardTest {

    private WildCard wildCard;
    private Card dummyCard;

    @BeforeClass
    public static void setUpBeforeClass() {
        System.out.println("Starting WildCard tests...");
    }

    @AfterClass
    public static void tearDownAfterClass() {
        System.out.println("Finished WildCard tests.");
    }

    @Before
    public void setUp() {
        wildCard = new WildCard();
        dummyCard = new NumberCard(CardColor.RED, CardColor.BLUE, CardType.ONE);
        System.out.println("Test setup completed.");
    }

    @After
    public void tearDown() {
        wildCard = null;
        dummyCard = null;
        System.out.println("Test teardown completed.");
    }

    /**
     * Unit test for playableOnTop method in isolation.
     * WildCard can always be played on top of any card.
     */
    @Test
    public void testPlayableOnTop() {
        assertTrue(wildCard.playableOnTop(dummyCard));
        System.out.println("testPlayableOnTop passed.");
    }
}
