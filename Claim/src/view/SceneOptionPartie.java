/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import static controler.Launcher.hauteur;
import static controler.Launcher.largeur;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author kekae
 */
public class SceneOptionPartie {
    public SceneOptionPartie(){
        
    }
    
    public Scene creerOptionPartie(int x, int y){
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
        
        
        HBox textIa = new HBox();
        Label titreIa = new Label("Veuillez selectionner une diffculter pour votre adversaire");
        textIa.getChildren().add(titreIa);
        textIa.setAlignment(Pos.BASELINE_CENTER);
        //////////////////////////////////////////////
        //Button de selection de difficulter de l'IA//
        HBox choixIa = new HBox();
        Button facile = new Button("Facile");
        Button moyen = new Button("Moyen");
        Button difficile = new Button("Difficile");
        
        //choixIa.getChildren().add(esp4);
        choixIa.getChildren().add(facile);
        choixIa.getChildren().add(moyen);
        choixIa.getChildren().add(difficile);
        choixIa.setAlignment(Pos.BASELINE_CENTER);
        ///////////////////////////////////////////////
        
        /////////////////////////////////////////////
        //Button de retour - lancement de la partie//
        HBox retourJouer = new HBox();
        Button retour = new Button("Retour");
        Button lancer = new Button("Lancer");
        
        retourJouer.getChildren().add(esp5);
        retourJouer.getChildren().add(retour);
        retourJouer.getChildren().add(esp6);
        retourJouer.getChildren().add(lancer);
        retourJouer.getChildren().add(esp7);
        
        HBox.setHgrow(esp5, Priority.ALWAYS);
        HBox.setHgrow(esp6, Priority.ALWAYS);
        HBox.setHgrow(esp7, Priority.ALWAYS);
        
        //////////////////////////////////////////////
        
        
        
        VBox boiteScene = new VBox();
        
        boiteScene.getChildren().add(esp1);
        boiteScene.getChildren().add(textIa);
        boiteScene.getChildren().add(choixIa);
        boiteScene.getChildren().add(esp2);
        boiteScene.getChildren().add(retourJouer);
        boiteScene.getChildren().add(esp3);
        
        VBox.setVgrow(esp1, Priority.ALWAYS);
        VBox.setVgrow(esp2, Priority.ALWAYS);
        VBox.setVgrow(esp3, Priority.ALWAYS);
        

        Scene s = new Scene(boiteScene, x, y);
        return s;
    }
    
}
