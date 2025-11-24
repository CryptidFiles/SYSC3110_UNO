import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
/**
 * Confirms the AIStrategy interface contract through a fake strategy.
 */
public class AIStrategyTest {

    private AIStrategy fake;
    private Player player;

    @Before
    public void setUp() {
        fake = new TestAIStrategy();

        ArrayList<String> names = new ArrayList<>(Arrays.asList("Bot"));
        ArrayList<Boolean> aiList = new ArrayList<>(Arrays.asList(true));
        UNO_Model model = new UNO_Model(1, names, aiList);
        player = model.getPlayers().get(0);
    }

    /**
     * Ensures the fake strategy returns the predetermined card index.
     */
    @Test
    public void testFakeChooseCard() {
        int result = fake.chooseCard(player, null, null);
        assertEquals(3, result); // predetermined
    }

    /**
     * Ensures the fake color choice matches expected value.
     */
    @Test
    public void testFakeChooseWildColor() {
        CardColor color = fake.chooseWildColor(player, true);
        assertEquals(CardColor.BLUE, color);
    }

    /**
     * Ensures delay getters and setters work correctly.
     */
    @Test
    public void testFakeDelayGetterSetter() {
        fake.setDelayMilliseconds(1234);
        assertEquals(1234, fake.getDelayMilliseconds());
    }

    /**
     * Fake predictable AI for testing the AIStrategy contract.
     */
    private static class TestAIStrategy implements AIStrategy {

        private int delay = 0;

        @Override
        public int chooseCard(Player p, Card top, UNO_Model model) {
            return 3;
        }

        @Override
        public CardColor chooseWildColor(Player p, boolean isLightSide) {
            return CardColor.BLUE;
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
}