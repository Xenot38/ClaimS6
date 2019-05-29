/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controler.ControllerEnver;
import static controler.ControllerEnver.creerImageView;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author kekae
 */
public class SceneFinDePartie {
    public Button Quitter = new Button("Quitter");
    public Button Menu = new Button("Menu");
    public Button Recommencer = new Button("Recommencer");
    public SceneFinDePartie(){
        
    }
    
    
    public Scene SceneFinParti(String s ){
        VBox vb = new VBox();
        
        Canvas can = new Canvas();
        Pane pane = new Pane(can);
        ImageView im = ControllerEnver.creerImageView(s,400,300);
        pane.getChildren().add(im);
        HBox hb = new HBox();
        Canvas can1 = new Canvas();
        Pane pane1 = new Pane(can1);
        HBox.setHgrow(pane1, Priority.ALWAYS); 
        
        Canvas can2 = new Canvas();
        Pane pane2 = new Pane(can2);
        HBox.setHgrow(pane2, Priority.ALWAYS); 
         
        Canvas can3 = new Canvas();
        Pane pane3 = new Pane(can3);
        HBox.setHgrow(pane3, Priority.ALWAYS); 

        Canvas can4 = new Canvas();
        Pane pane4 = new Pane(can4);
        HBox.setHgrow(pane4, Priority.ALWAYS); 
        hb.getChildren().add(pane1);
        hb.getChildren().add(Recommencer);
        hb.getChildren().add(pane2);
        hb.getChildren().add(Menu);
        hb.getChildren().add(pane3);
        hb.getChildren().add(Quitter);
        hb.getChildren().add(pane4);
        
        Canvas can5 = new Canvas();
        Pane pane5 = new Pane(can5);
        VBox.setVgrow(pane5, Priority.ALWAYS); 
        Canvas can6 = new Canvas();
        Pane pane6 = new Pane(can6);
        VBox.setVgrow(pane6, Priority.ALWAYS); 

        vb.getChildren().add(pane);
        vb.getChildren().add(can5);
        vb.getChildren().add(hb);
        vb.getChildren().add(can6);
        Scene scene = new Scene(vb, 400, 350);
        return scene;
    }
}
