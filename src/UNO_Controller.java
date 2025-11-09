import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UNO_Controller implements ActionListener {
    private UNO_Game model;
    private UNO_View view;

    public UNO_Controller(UNO_Game model, UNO_View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        //

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

    public void handleDrawCard() {
        // Process draw card action
    }
}
