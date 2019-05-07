/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import javafx.scene.image.Image ;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.Carte;
import model.Plateau;

/**
 *
 * @author kekae
 */
public class SceneJeu {
    
   public Plateau p;
   public SceneJeu(Plateau plateau){
       this.p = plateau;
   }
   
   public Scene creerjeu(int x,int y){
       
       
       ////////////////////
       //cartes Joueur 2 //
       HBox cartesJ2 = new HBox();
       
       //espace du coter gauche des cartes du joueur 2//
       Canvas canvideJ2G = new Canvas();
       Pane panevideJ2G = new Pane(canvideJ2G);
       cartesJ2.getChildren().add(panevideJ2G);
       //cartes//
       ArrayList<Carte> carteJ2 = p.getJ2().getMain();
       ArrayList<Pane> paneJ2 = new ArrayList();
       
       Iterator MainJ2 = carteJ2.iterator();
       while (MainJ2.hasNext()){
           Carte i = (Carte)MainJ2.next();
           InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(i.getCheminImage());
           Image img = new Image(in);
           Canvas cancarte = new Canvas();
           GraphicsContext gc = cancarte.getGraphicsContext2D();
           gc.drawImage(img, 100, 100);
           Pane pane = new Pane(cancarte);
           paneJ2.add(pane);
            }
       
       Iterator AfficherMain = paneJ2.iterator();
       while (AfficherMain.hasNext()){
           cartesJ2.getChildren().add((Pane)AfficherMain.next());
            }
       //espace du coter droit des cartes du joueur 2 //
       Canvas canvideJ2D = new Canvas();
       Pane panevideJ2D = new Pane(canvideJ2D);
       cartesJ2.getChildren().add(panevideJ2D);
       ////////////////////
       
       
       ////////////////////
       //cartes Joueur 1 //
       HBox cartesJ1 = new HBox();
       
       //espace du coter gauche des cartes du joueur 1//
       Canvas canvideJ1G = new Canvas();
       Pane panevideJ1G = new Pane(canvideJ1G);
       
       
       
       
       //espace du coter droit des cartes du joueur 1//
       Canvas canvideJ1D = new Canvas();
       Pane panevideJ1D = new Pane(canvideJ1D);
       cartesJ1.getChildren().add(panevideJ1G);
       
       
       ////////////////////
       
       
       
       
       ////////////////////////////
       //Partie Gauche du plateau//
       VBox partieG = new VBox();
       partieG.getChildren().add(cartesJ2);
       
       partieG.getChildren().add(cartesJ1);
       ////////////////////////////
       
       ////////////////////////
       //plateau final de jeu//
       HBox jeu = new HBox();
       jeu.getChildren().add(partieG);
       ////////////////////////
       Scene s = new Scene(jeu, x , y);
       return s;
   }
   
   
   
    
    
    
}
