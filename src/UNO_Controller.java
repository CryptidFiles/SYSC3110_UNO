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

        // Connect draw button to controller
        view.getDrawButton().addActionListener(this);
        view.getNextPlayerButton().addActionListener(this);

        // Starting a new round
        try {
            model.startNewRound();
        } catch (Exception ignored) {
        }

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
            //handleDrawCard();
            model.drawCard();
        }
        else if (source == view.getNextPlayerButton()) {
            //handleNextPlayer();
            model.moveToNextPlayer();
            updateDrawButtonState();
        }
        // Handle card plays from CardComponents
        else if (source instanceof JButton) {
            JButton button = (JButton) source;

            // Check if this is a card use button by looking at the parent (button is inside this class)
            if (button.getParent() instanceof CardComponent) {
                CardComponent cardComp = (CardComponent) button.getParent();
                handleCardPlay(cardComp.getCardIndex());
            }
        }
    }

    /**
     * Confirms the current player's turn and advances to the next player.
     * Triggered by the "Next Player" button.
     */
    private void handleNextPlayer() {
        try{
            model.moveToNextPlayer();
            view.setNextPlayerButtonEnabled(false); //disable until next valid action
            view.setDrawButtonEnabled(false);

            view.displayMessage("Next Player's turn!");
            view.setHandEnabled(true);

            Player current = model.getCurrentPlayer();
            boolean hasPlayable = model.hasPlayableHand(current);

            view.setDrawButtonEnabled(!hasPlayable);
        } catch (Exception ex) {
            model.notifyMessage("Error moving to next player: " + ex.getMessage());
        }
    }

    /**
     * Handles the "Draw Card" button action.
     * Requests the current player to draw a card from the deck via the model,
     * and lets the model handle any reshuffling or deck-empty scenarios.
     */
    public void handleDrawCard() {
        try {
            Player currentPlayer = model.getCurrentPlayer();
            if (currentPlayer == null) return;

            // Use the existing drawCard() method which already handles drawing for current player
            Card drawnCard = model.drawCard();
            view.setDrawButtonEnabled(false);

            if (drawnCard != null) {
                view.displayMessage(currentPlayer.getName() + " drew a card. Press 'Next Player' to continue.");
                view.setNextPlayerButtonEnabled(true);
                view.setDrawButtonEnabled(false);
                view.setHandEnabled(false);
            } else {
                // Model should handle empty deck scenario through reshuffling
                view.displayMessage("No cards available to draw. ");
            }

        } catch (Exception ex) {
            model.notifyMessage("Error drawing card: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Handles when a player plays a card.
     * This method is called either from a card’s “Use” button
     * or automatically during game actions.
     *
     * @param cardIndex the index of the card being played from the player's hand
     */
    public void handleCardPlay(int cardIndex) {
        try {
            Player currentPlayer = model.getCurrentPlayer();
            if (currentPlayer == null) {
                System.err.println("No current player found.");
                return;
            }

            // Validate and play the card through the model
            boolean success = model.playCard(cardIndex);

            if (!success) {
                System.out.println("Invalid move! This card cannot be played on the current top card.");
            }else{
                if (!model.isWaitingForColorSelection()){
                    view.displayMessage("Card Played successfully! Press 'Next Player' to continue ");
                    view.setHandEnabled(false);
                    view.setDrawButtonEnabled(false);
                    view.setNextPlayerButtonEnabled(true);
                }
            }
            // If successful, model will handle game logic and notify views automatically

        } catch (Exception ex) {
            model.notifyMessage("Error playing card: " + ex.getMessage());
            ex.printStackTrace();
        }

        Player currentPlayer = model.getCurrentPlayer();
        boolean hasPlayable = model.hasPlayableHand(currentPlayer);
        view.setDrawButtonEnabled(!hasPlayable);
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
            model.notifyMessage("Error playing card: " + ex.getMessage());
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
            model.notifyMessage("Error starting new game: " + ex.getMessage());
        }
    }

    private void updateDrawButtonState() {
        Player current = model.getCurrentPlayer();
        boolean hasPlayable = model.hasPlayableHand(current);
        view.setDrawButtonEnabled(!hasPlayable && !current.isPlayerAI());
    }
}