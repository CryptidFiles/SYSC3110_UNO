import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UNO_Controller implements ActionListener {
    private UNO_Game model;
    private UNO_Frame view; // Changed to concrete type for button access

    public UNO_Controller(UNO_Game model, UNO_Frame view) {
        this.model = model;
        this.view = view;

        // Connect draw button to controller
        view.getDrawButton().addActionListener(this);

        // Starting a new round
        try {
            model.startNewRound();
        } catch (Exception ignored) {
        }

        // Initial view update
        view.updateGameState();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        // Handle Draw Card button
        if (source == view.getDrawButton()) {
            handleDrawCard();
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
     * Handles when a player wants to draw a card
     */
    public void handleDrawCard() {
        try {
            Player currentPlayer = model.getCurrentPlayer();
            if (currentPlayer == null) return;

            // Use the existing drawCard() method which already handles drawing for current player
            Card drawnCard = model.drawCard();

            if (drawnCard != null) {
                // Model will automatically notify views through observer pattern
                // The card is already added to player's hand in the model
                System.out.println(currentPlayer.getName() + " drew a card.");
            } else {
                // Model should handle empty deck scenario through reshuffling
                System.out.println("No cards available to draw.");
            }

        } catch (Exception ex) {
            model.notifyMessage("Error drawing card: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Handles when a player wants to play a card
     */
    public void handleCardPlay(int cardIndex) {
        try {
            Player currentPlayer = model.getCurrentPlayer();
            if (currentPlayer == null) {
                System.err.println("No current player found.");
                return;
            }

            System.out.println("Attempting to play card index: " + cardIndex + " for player: " + currentPlayer.getName());

            // Validate and play the card through the model
            boolean success = model.playCard(cardIndex);

            if (!success) {
                System.out.println("Invalid move! This card cannot be played on the current top card.");
            }
            // If successful, model will handle game logic and notify views automatically

        } catch (Exception ex) {
            model.notifyMessage("Error playing card: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

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


    public void startNewGame() {
        try {
            model.startNewRound();
            // Model will notify views automatically through observer pattern
        } catch (Exception ex) {
            model.notifyMessage("Error starting new game: " + ex.getMessage());
        }
    }
}