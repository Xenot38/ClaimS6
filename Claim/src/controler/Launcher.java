package controler;


import java.io.FileNotFoundException;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Plateau;
import view.SceneJeu;
import view.SceneMenu;
import view.SceneOptionPartie;


public class Launcher extends Application {
	public static int hauteur = 300;
	public static int largeur = 300;
        public Plateau p = new Plateau("facile");
	
	public static void main(String[] args) {
		Launcher.launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws FileNotFoundException {
            primaryStage.setTitle("Claim");
            ControllerEnver con= new ControllerEnver(primaryStage);
            con.afficher();
		}
}
