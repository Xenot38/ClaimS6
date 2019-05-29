/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controler.ControllerEnver;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author kekae
 */
public class Règle {
    public Button retour = new Button("retour");
    public Règle(){
        
    }
    public Scene creerSceneRegle(){
        VBox vbScene = new VBox();
        
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        ImageView im = ControllerEnver.creerImageView("ressources/images/reglesFr.png",1000,3000);
        scrollPane.setContent(im);
        HBox buttonHB = new HBox();
        Canvas can1 = new Canvas();
        Pane pane1 = new Pane(can1);
        HBox.setHgrow(pane1, Priority.ALWAYS); 
        Canvas can2 = new Canvas();
        Pane pane2 = new Pane(can2);
        HBox.setHgrow(pane2, Priority.ALWAYS); 
        buttonHB.getChildren().add(pane1);
        buttonHB.getChildren().add(retour);
        buttonHB.getChildren().add(pane2);
        vbScene.getChildren().add(scrollPane);
        vbScene.getChildren().add(buttonHB);
        
        Scene s = new Scene(vbScene, 1000, 950);
        return s;
    }
    
    
    
}
