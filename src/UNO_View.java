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
 * @version 4.0, December 05, 2025
 */
public interface UNO_View {
    /**
     * Single method to handle all game state updates
     * @param event the game event containing what changed
     */
    void handleGameEvent(GameEvent event);

    /**
     * Returns the "Draw Card" button so that the controller
     * can attach listeners and enable/disable it as needed.
     *
     * @return the draw button component
     */

    JButton getDrawButton();

    /**
     * Returns the "Next Player" button so that the controller
     * can manage turn progression logic and UI updates.
     *
     * @return the next player button component
     */
    JButton getNextPlayerButton();

    /**
     * Forces the view to refresh its full game state,
     * typically used after loading a saved game or replacing the model.
     *
     * @param model the updated {@link UNO_Model} instance to display
     */
    void refreshGameState(UNO_Model model);

    /**
     * Assigns a new {@link UNO_Model} to the view.
     * Used when loading a saved game to rebind MVC connections.
     *
     * @param loadedModel the newly loaded game model
     */
    void setModel(UNO_Model loadedModel);
}
