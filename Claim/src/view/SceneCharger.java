/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author kekae
 */
public class SceneCharger {
    public Button retour = new Button("retour");
    public Button supprimer = new Button("supprimer");
    public Button charger = new Button("chager");
    
    public SceneCharger(){
    }
    
    public Scene creerSceneCharger(int x, int y){
        Canvas can_thon = new Canvas();
        Pane esp1 = new Pane(can_thon);

        Canvas can_art = new Canvas();
        Pane esp2 = new Pane(can_art);

        Canvas can_ail = new Canvas();
        Pane esp3 = new Pane(can_ail);
        Canvas can4 = new Canvas();
        Pane esp4 = new Pane(can4);
        
        
        HBox hbScene = new HBox();
        VBox charge = new VBox();
        VBox buttonVb = new VBox();
        
        
        
        
        Scene s = new Scene (hbScene, x, y);
        return s;
    }
    
    
    
    
    
}
