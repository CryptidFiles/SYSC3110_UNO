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


    private void handleCardPlay(int cardIndex) {
        // Validate move, update model, refresh view
    }

    private void handleDrawCard() {
        // Process draw card action
    }
}
