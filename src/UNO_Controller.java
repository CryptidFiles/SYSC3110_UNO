import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The UNO_Controller class serves as the main controller in the MVC pattern
 * for the UNO game
 * It handles all user interactions from the view, and coordinates updates
 * between the {@link UNO_Model} model and the {@link UNO_Frame} view.
 *The controller listens to button events such as "Draw Card" or "Use"
 * on each {@link CardComponent}, processes them, and delegates the logic
 * to the model while ensuring proper game flow and state synchronization
 *
 * @author Ahmad El-Jabi 101303269
 * @author Atik Mahmud 101318070
 * @author Aryan Singh 101299776
 * @author Jonathan Gitej 101294584
 *
 * @version 2.0, November 10, 2025
 */
public class UNO_Controller implements ActionListener {
    private UNO_Model model;
    private UNO_Frame view; // Changed to concrete type for button access

    /**
     * Constructs a new UNO_Controller that connects the game model and view.
     * It attaches listeners to buttons and triggers the start of a new game round.
     *
     * @param model the {@link UNO_Model} model that handles game state and logic
     * @param view  the {@link UNO_Frame} view that provides the visual interface
     */
    public UNO_Controller(UNO_Model model, UNO_Frame view) {
        this.model = model;
        this.view = view;

        // Starting a new round
        try {
            model.startNewRound();
        } catch (Exception ignored) {
        }

        view.setNextPlayerButtonEnabled(false);
        view.setDrawButtonEnabled(false);

        Player current = model.getCurrentPlayer();
        boolean hasPlayable = model.hasPlayableHand(current);
        view.setDrawButtonEnabled(!hasPlayable);
    }

    /**
     * Responds to all button click events in the view.
     * Determines which button was pressed and delegates
     * the action to the appropriate handler.
     *
     * @param e the {@link ActionEvent} triggered by a button press
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        // Handle Draw Card button
        if (source == view.getDrawButton()) {
            model.drawCard();
            // Disable draw button after drawing
            view.setDrawButtonEnabled(false);
        }
        else if (source == view.getNextPlayerButton()) {
            model.moveToNextPlayer();
            // Disable next player button after use
            view.setNextPlayerButtonEnabled(false);
        }
        // Handle card plays from CardComponents
        else if (source instanceof JButton) {
            JButton button = (JButton) source;

            if (button.getParent() instanceof CardComponent) {
                CardComponent cardComp = (CardComponent) button.getParent();
                int cardIndex = cardComp.getCardIndex();
                model.playCard(cardIndex);
            }
        }
        // Update button states
        updateButtonStates();
    }

    private void updateButtonStates() {
        Player current = model.getCurrentPlayer();
        boolean hasPlayable = model.hasPlayableHand(current);

        // Only enable draw button if player has no playable cards AND it's not AI
        view.setDrawButtonEnabled(!hasPlayable && !current.isPlayerAI());

        // NOTE FOR OURSELVES: Next player button should only be enabled by model events
    }


    /**
     * Handles color selection for Wild and Wild Draw cards.
     * Once a color is chosen by the player, this method applies the selected color
     * to the top card and executes any special effects
     *
     * @param chosenColor the {@link CardColor} selected by the player
    */
    public void handleWildColorSelection(CardColor chosenColor) {
        try {
            if (!model.isWaitingForColorSelection()) {
                return; // Not in color selection state
            }

            // Get the top card (which should be the wild card that was just played)
            Card topCard = model.topCard();

            if (topCard instanceof WildCard) {
                WildCard wildCard = (WildCard) topCard;
                wildCard.applyChosenColor(chosenColor, model.topCard().isLightSideActive);
                model.completeColorSelection();
                //model.moveToNextPlayer(); // Normal turn progression after wild card

            } else if (topCard instanceof WildDrawCard) {
                WildDrawCard wildDrawCard = (WildDrawCard) topCard;
                Player currentPlayer = model.getCurrentPlayer();
                wildDrawCard.executeDrawAction(chosenColor, model.topCard().isLightSideActive, model, currentPlayer);
                model.completeColorSelection();
                // moveToNextPlayer is handled by addSkip() in the card logic
            }
        } catch (Exception ex) {
            model.prepareEvent(GameEvent.EventType.MESSAGE, "Error playing card: " + ex.getMessage());
        }
    }


    /**
     * Starts a new game round.
     * This method resets the deck, players, and board state
     */
    public void handleNewRound() {
        try {
            model.startNewRound();
            // Model will notify views automatically through observer pattern
        } catch (Exception ex) {
            model.prepareEvent(GameEvent.EventType.MESSAGE, "Error starting new game: " + ex.getMessage());
            model.notifyViews();
        }
    }

    private void updateDrawButtonState() {
        Player current = model.getCurrentPlayer();
        boolean hasPlayable = model.hasPlayableHand(current);
        view.setDrawButtonEnabled(!hasPlayable && !current.isPlayerAI());
    }
}