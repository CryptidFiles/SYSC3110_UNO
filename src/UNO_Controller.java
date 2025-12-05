import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.nio.file.Path;

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
 * @version 3.0, November 24, 2025
 */
public class UNO_Controller implements ActionListener, Serializable {
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
            view.setNextPlayerButtonEnabled(false);

        } else if (source == view.getUndoButton()) {
            model.undo();
        } else if (source == view.getRedoButton()) {
            model.redo();
        } else if (source instanceof JButton) {
            // Handle card plays from CardComponents
            JButton button = (JButton) source;

            if (button.getParent() instanceof CardComponent) {
                CardComponent cardComp = (CardComponent) button.getParent();
                Card cardToPlay = cardComp.getCard();

                // Find the current index of this card in the player's hand
                Player currentPlayer = model.getCurrentPlayer();
                int cardIndex = findCardIndex(currentPlayer, cardToPlay);

                if (cardIndex != -1) {
                    model.playCard(cardIndex);
                    view.setDrawButtonEnabled(false);
                }
            }
        }
    }


    /**
     * Searches the current player's hand to locate the index of the specified card.
     * This method compares card references to ensure the exact card instance is matched.
     *
     * @param player the player whose hand is being searched
     * @param card   the card instance to locate in the player's hand
     * @return the index of the card if found, or -1 if the card is not present
     */
    private int findCardIndex(Player player, Card card) {
        ArrayList<Card> hand = player.getHand();
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i) == card) { // Reference comparison for exact match
                return i + 1; // Return 1-based index for model
            }
        }
        return -1; // Card not found
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

            } else if (topCard instanceof WildDrawCard) {
                WildDrawCard wildDrawCard = (WildDrawCard) topCard;
                Player currentPlayer = model.getCurrentPlayer();
                model.completeColorSelection();
                wildDrawCard.executeDrawAction(chosenColor, model.topCard().isLightSideActive, model, currentPlayer);

            }
        } catch (Exception ex) {
            model.prepareEvent(GameEvent.EventType.MESSAGE, "Error playing card: " + ex.getMessage());
            model.notifyViews();
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

    /**
     * Starts a new game.
     * This method resets everything except the players.
     */
    public void handleNewGame() {
        try {
            model.startNewGame();
        } catch (Exception ex) {
            model.prepareEvent(GameEvent.EventType.MESSAGE,"Error starting new game: " + ex.getMessage());
            model.notifyViews();
        }
    }


    /**
     * Initiates the save game feature
     */
    public void saveGame() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save UNO Game");

        int result = chooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            try {
                model.saveGame(file.getAbsolutePath());
                JOptionPane.showMessageDialog(null, "Game saved successfully.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Error saving game: " + e.getMessage(),
                        "Save Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Initiates the load game feature
     */
    public void loadGame() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Load UNO Game");

        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            try {
                UNO_Model loadedModel = UNO_Model.loadGame(file.getAbsolutePath());
                view.setModel(loadedModel);

                // Reassign new model to the controller
                this.model = loadedModel;

                // Clear the collection of views already saved if full
                model.clearUnoViews();

                // Then add the current frame(s) into the model
                model.addUnoView(this.view);

                if (loadedModel != null) {
                    System.out.println("Model loaded successfully.");
                    this.model = loadedModel;     // Replace current model
                    view.refreshGameState(model); // Ask View to update UI
                    JOptionPane.showMessageDialog(null, "Game loaded successfully.");
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Error loading game: " + e.getMessage(),
                        "Load Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}