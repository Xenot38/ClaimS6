package view;


import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Ihm extends Application {
	public static int hauteur = 400;
	public static int largeur = 600;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("hello world");
		Ihm.launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Sokoban");
        Group root = new Group();
		Scene s = new Scene(root , hauteur,largeur);
		primaryStage.setScene(s);
		primaryStage.show();
		
	}
}
