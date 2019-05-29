/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author kekae
 */
public class CarteView {
    AnchorPane pane;
    
    public CarteView(String s, int x, int y){
        Image image1=null;
        try{image1 = new Image(new File(s).toURI().toString(), x, y, true, true);}catch(Exception e){System.out.println("pas trouver");}
        ImageView imageSelected = new ImageView();
        imageSelected.setImage(image1);
        pane = new AnchorPane();
        pane.getChildren().add(imageSelected);
    }
    
    
    
    public AnchorPane getPane(){
        return this.pane;
    }
    public void SetImage(ImageView im){
           pane.getChildren().add(im);
    }
    
    
    
}
