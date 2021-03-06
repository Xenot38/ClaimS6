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
public class SceneOptionPartie {
    
    public Button facile = new Button("Facile");
    public Button moyen = new Button("Moyen");
    public Button difficile = new Button("Difficile");
    public Button resolution1 = new Button("1680X1050");
    public Button resolution2 = new Button("1920X1080");
    public Button resolution3 = new Button("2560x1440");
    public Button retour = new Button("Retour");
    public Button lancer = new Button("Lancer");
    public String difficulteIa = "facile";
    public String resolutionecran = "1920X1080";

    
    public SceneOptionPartie(){
        
    }
    
    public Scene creerOptionPartie(int x, int y){
        VBox boiteScene = new VBox();
        Scene s = new Scene(boiteScene, x, y);
        s.getStylesheets().add("view/css/Option.css");
        facile.getStyleClass().add("buttonChoisi");
        difficile.getStyleClass().add("button");
        moyen.getStyleClass().add("button");
        resolution1.getStyleClass().add("button");
        resolution2.getStyleClass().add("buttonChoisi");
        resolution3.getStyleClass().add("button");
         lancer.getStyleClass().add("button");
        retour.getStyleClass().add("button");
       
        Canvas can_thon = new Canvas();
        Pane esp1 = new Pane(can_thon);

        Canvas can_art = new Canvas();
        Pane esp2 = new Pane(can_art);

        Canvas can_ail = new Canvas();
        Pane esp3 = new Pane(can_ail);
        Canvas can4 = new Canvas();
        Pane esp4 = new Pane(can4);
        
        Canvas can5 = new Canvas();
        Pane esp5 = new Pane(can5);
        Canvas can6 = new Canvas();
        Pane esp6 = new Pane(can6);
        Canvas can7 = new Canvas();
        Pane esp7 = new Pane(can7);
        
        Canvas can8 = new Canvas(10,10);
        Canvas can9 = new Canvas(10,10);
        
        
        
        BackgroundImage myBI = null;
       try{  myBI= new BackgroundImage(new Image(new File("ressources/images/bg2.png").toURI().toString(), 600, 250, false, true),
        BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
        BackgroundSize.DEFAULT);}
       catch(Exception e){
       }
        
        HBox textIa = new HBox();
        Label titreIa = new Label("Veuillez sélectionner une difficulté pour votre adversaire");
        titreIa.getStyleClass().add("label");
        textIa.getChildren().add(titreIa);
        textIa.setAlignment(Pos.BASELINE_CENTER);
        //////////////////////////////////////////////
        //Button de selection de difficulter de l'IA//
        HBox choixIa = new HBox();
        
        
        //choixIa.getChildren().add(esp4);
        choixIa.getChildren().add(facile);
        choixIa.getChildren().add(moyen);
        choixIa.getChildren().add(difficile);
        choixIa.setAlignment(Pos.BASELINE_CENTER);
        ///////////////////////////////////////////////
        
        HBox textResolution = new HBox();
        Label titreResolution = new Label("Veuillez sélectionner la dimension d'écran qui vous convient");
        titreResolution.getStyleClass().add("label");
        textResolution.getChildren().add(titreResolution);
        textResolution.setAlignment(Pos.BASELINE_CENTER);
        //Choix de la reolution de l'ecran//
        HBox choixResolution = new HBox();
        choixResolution.getChildren().add(resolution1);
        choixResolution.getChildren().add(resolution2);
        choixResolution.getChildren().add(resolution3);
        choixResolution.setAlignment(Pos.BASELINE_CENTER);
        /////////////////////////////////////////////
        //Button de retour - lancement de la partie//
        HBox retourJouer = new HBox();
        
        
        retourJouer.getChildren().add(esp5);
        retourJouer.getChildren().add(retour);
        retourJouer.getChildren().add(esp6);
        retourJouer.getChildren().add(lancer);
        retourJouer.getChildren().add(esp7);
        
        HBox.setHgrow(esp5, Priority.ALWAYS);
        HBox.setHgrow(esp6, Priority.ALWAYS);
        HBox.setHgrow(esp7, Priority.ALWAYS);
        
        //////////////////////////////////////////////
        VBox.setVgrow(esp4, Priority.ALWAYS);
        
        
        
        boiteScene.getChildren().add(esp1);
        boiteScene.getChildren().add(textIa);
        boiteScene.getChildren().add(can8);
        boiteScene.getChildren().add(choixIa);
        boiteScene.getChildren().add(esp2);
        boiteScene.getChildren().add(textResolution);
        boiteScene.getChildren().add(can9);
        boiteScene.getChildren().add(choixResolution);
        boiteScene.getChildren().add(esp4);
        boiteScene.getChildren().add(retourJouer);
        boiteScene.getChildren().add(esp3);
        boiteScene.setBackground(new Background(myBI));
        
        VBox.setVgrow(esp1, Priority.ALWAYS);
        VBox.setVgrow(esp2, Priority.ALWAYS);
        VBox.setVgrow(esp3, Priority.ALWAYS);
        

        
        return s;
    }
    
}
