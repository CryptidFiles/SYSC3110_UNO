import javax.swing.*;

/**
 * The UNO_View interface defines the contract for the visual
 * representation (View) in the UNO game’s (MVC) architecture.
 * It specifies the methods that any concrete view (such as {@link UNO_Frame})
 * must implement to display game state updates, handle user interactions,
 * and reflect changes triggered by the {@link UNO_Model} model.
 *
 * @author Ahmad El-Jabi 101303269
 * @author Atik Mahmud 101318070
 * @author Aryan Singh 101299776
 * @author Jonathan Gitej 101294584
 *
 * @version 3.0, November 24, 2025
 */
public interface UNO_View {
    /**
     * Single method to handle all game state updates
     * @param event the game event containing what changed
     */
    void handleGameEvent(GameEvent event);

    // Only keep the methods that are truly needed for controller interaction
    JButton getDrawButton();
    JButton getNextPlayerButton();

    void refreshGameState(UNO_Model model);

    void setModel(UNO_Model loadedModel);
}
