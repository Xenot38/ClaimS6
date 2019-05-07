package view;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Principale {

    @FXML
    private AnchorPane principe;

    @FXML
    private Button game;

    @FXML
    private Button règles;

    @FXML
    void game(ActionEvent event) throws IOException {
           Stage st = (Stage)game.getScene().getWindow();
           Parent root = FXMLLoader.load(getClass().getResource("graphic.fxml"));
           Scene scene = new Scene(root);
           st.setScene(scene);
           st.show();
    }

    @FXML
    void règles(ActionEvent event) {

    }

}
