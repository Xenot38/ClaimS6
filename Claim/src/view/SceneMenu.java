/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import static controler.Launcher.hauteur;
import static controler.Launcher.largeur;
import java.io.File;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author kekae
 */
public class SceneMenu {
    public SceneMenu(){
       
       
    }
    
    
    public Scene creerMenu(){
        BackgroundImage myBI = null;
       try{  myBI= new BackgroundImage(new Image(new File("ressources/images/Claim.png").toURI().toString(), largeur, hauteur, false, true),
        BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
        BackgroundSize.DEFAULT);}
       catch(Exception e){
           System.out.println("lol ça n'a pas charger mdr");
       }
        //then you set to your node
        
        
        
        Canvas can_thon = new Canvas();
        Pane esp1 = new Pane(can_thon);

        Canvas can_art = new Canvas();
        Pane esp2 = new Pane(can_art);

        Canvas can_ail = new Canvas();
        Pane esp3 = new Pane(can_ail);

        Canvas can_net = new Canvas();
        Pane esp4 = new Pane(can_net);

        // Une boite horizontale avec 3 bouts de texte
        VBox Titre = new VBox();
        Label label = new Label("Claim");
        Titre.getChildren().add(label);
        // Le texte est centré dans l'espace qu'on lui alloue
        label.setAlignment(Pos.CENTER);
        Titre.setAlignment(Pos.BASELINE_CENTER);
        // S'il y a de la place, on donne tout au label
        VBox.setVgrow(label, Priority.ALWAYS);
        label.setMaxWidth(Double.MAX_VALUE);


        //menu bouton
        VBox Button_Menu = new VBox();
        Button Jouer = new Button("Jouer");
        Button Charger = new Button("Charger");
        Button Option  = new Button("Option");
        Button Quitter = new Button("Quitter");
        Button_Menu.getChildren().add(Jouer);
        Button_Menu.getChildren().add(Charger);
        Button_Menu.getChildren().add(Option);
        Button_Menu.getChildren().add(Quitter);
        Button_Menu.setAlignment(Pos.BASELINE_CENTER);

        HBox Regles_Droite = new HBox();
        Button Regles = new Button("Règles");
        Regles_Droite.getChildren().add(esp4);
        Regles_Droite.getChildren().add(Regles);
        Regles.setAlignment(Pos.BOTTOM_RIGHT);
        HBox.setHgrow(esp4, Priority.ALWAYS);

        // Une boite verticale pour contenir toute la scène
        VBox boiteScene = new VBox();

        boiteScene.getChildren().add(esp1);
        boiteScene.getChildren().add(Titre);
        boiteScene.getChildren().add(esp2);
        boiteScene.getChildren().add(Button_Menu);
        boiteScene.getChildren().add(esp3);
        boiteScene.getChildren().add(Regles_Droite);
        VBox.setVgrow(esp1, Priority.ALWAYS);
        VBox.setVgrow(esp2, Priority.ALWAYS);
        VBox.setVgrow(esp3, Priority.ALWAYS);

        boiteScene.setBackground(new Background(myBI));
        // Contenu de la fenêtre
        Scene s = new Scene(boiteScene, largeur, hauteur);
        
        return s;
    }
    
    
    
    
    
    
    
}
