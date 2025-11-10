import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UNO_Controller implements ActionListener {
    private UNO_Game model;
    private UNO_View view;

    public UNO_Controller(UNO_Game model, UNO_View view) {
        this.model = model;
        this.view = view;

        // Starting a new round
        try {
            model.startNewRound();
        } catch (Exception ignored) {
        }

        // Initial view
        view.updateGameState();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();

//        if (source == view.getDrawButton(){
//            handleDrawCard();
//            return;
//        }
//
//        // This mean they are playing a card because we only have two buttons (draw and card)
//        int index = view.getCardIndex(); //TODO: implement this in view
//        handleCardPlay(index);
    }

    private void handleDrawCard() {
        try {
            if (model == null || view == null) return;

            Card drawnCard = model.drawCard();

            if (drawnCard == null) {
                view.displayMessage("No cards could be drawn (deck empty).");
            }

            // Update view after drawing
            view.updateGameState();

            if (model.isGameOver()) {
                Player gameWinner = model.getGameWinningPlayer();
                view.displayMessage("Game Over! " + gameWinner.getName() + " has won the game!");
            }
        } catch (Exception ex) {
            view.displayMessage("Error playing card: " + ex.getMessage());
        }
    }


    public void handleCardPlay(int cardIndex) {
        // Validate move, update model, refresh view
        try {
            Player currentPlayer = model.getCurrentPlayer();

            if (model.hasPlayableHand(currentPlayer)) {
                boolean success = model.playCard(cardIndex);

                if (success) {
                    view.showCardPlayed(currentPlayer.playCard(cardIndex));

                    if (model.isRoundOver()) {
                        view.showRoundWinner(model.getRoundWinningPlayer());
                    } else if (model.isGameOver()) {
                        view.showWinner(model.getGameWinningPlayer());
                    } else {
                        view.updateGameState();
                    }
                } else {
                    view.displayMessage("Invalid move! Please try a different card.");
                }
            } else {
                view.displayMessage("No playable cards in hand. You must draw a card.");
            }
        } catch (Exception ex) {
            view.displayMessage("Error playing card: " + ex.getMessage());
        }
    }
}
