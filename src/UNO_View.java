import javax.swing.*;

/**
 * The UNO_View interface defines the contract for the visual
 * representation (View) in the UNO game’s (MVC) architecture.
 * It specifies the methods that any concrete view (such as {@link UNO_Frame})
 * must implement to display game state updates, handle user interactions,
 * and reflect changes triggered by the {@link UNO_Game} model.
 *
 * @author Ahmad El-Jabi 101303269
 * @author Atik Mahmud 101318070
 * @author Aryan Singh 101299776
 * @author Jonathan Gitej 101294584
 *
 * @version 2.0, November 10, 2025
 */
public interface UNO_View {
    void updateGameState();
    void displayPlayerHand(Player player);
    void highlightCurrentPlayer();
    void showCardPlayed(Card card);
    void showWildColorSelection();
    void displayMessage(String message);
    void updateScores();
    void showWinner(Player player);
    void showRoundWinner(Player player);
    void initiateNewRound();
    JButton getDrawButton();
    JButton getNextPlayerButton();
    void setEnabled(boolean enabled);
    void setNextPlayerButtonEnabled(boolean enabled);
    void setDrawButtonEnabled(boolean enabled);
}
