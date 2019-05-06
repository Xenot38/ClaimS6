package controler;


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
import view.SceneMenu;


public class Launcher extends Application {
	public static int hauteur = 400;
	public static int largeur = 600;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Launcher.launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Claim");
                SceneMenu Menu = new SceneMenu();
                Scene s = Menu.creerMenu();
                
		
		
		primaryStage.setScene(s);
		// On affiche la fenêtre (donne leur taille aux objets graphiques)
		primaryStage.show();
		}
}
