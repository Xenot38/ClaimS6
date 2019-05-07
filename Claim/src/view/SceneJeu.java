/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.image.Image ;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
   
   public Scene creerjeu(int x,int y) throws FileNotFoundException{
       
       
       ////////////////////
       //cartes Joueur 2 //
       HBox cartesJ2 = new HBox();
       
       //espace du coter gauche des cartes du joueur 2//
       Canvas canvideJ2G = new Canvas();
       Pane panevideJ2G = new Pane(canvideJ2G);
       cartesJ2.getChildren().add(panevideJ2G);
       //cartes//
       
       
       Carte c1 = p.getJ2().getMain().get(0);
       Carte c2 = p.getJ2().getMain().get(1);
       Carte c3 = p.getJ2().getMain().get(2);
       Carte c4 = p.getJ2().getMain().get(3);
       Carte c5 = p.getJ2().getMain().get(4);
       Carte c6 = p.getJ2().getMain().get(5);
       Carte c7 = p.getJ2().getMain().get(6);
       Carte c8 = p.getJ2().getMain().get(7);
       Carte c9 = p.getJ2().getMain().get(8);
       Carte c10 = p.getJ2().getMain().get(9);
       Carte c11 = p.getJ2().getMain().get(10);
       Carte c12 = p.getJ2().getMain().get(11);
       Carte c13 = p.getJ2().getMain().get(12);
       

       //System.out.println(c.getCheminImage());
       //InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(c.getCheminImage());
       //Image image1 = new Image(new File(c1.getCheminImage()).toURI().toString(), 125, 200, false, false);
       Image image1 = new Image(new FileInputStream(c1.getCheminImage()));
       /*Image image2 = new Image(new FileInputStream(c2.getCheminImage()));
       //Image image2 = new Image(new File(c2.getCheminImage()).toURI().toString(), 100, 100, false, false);
       /*Image image3 = new Image(new File(c3.getCheminImage()).toURI().toString(), 100, 100, false, false);
       Image image4 = new Image(new File(c4.getCheminImage()).toURI().toString(), 100, 100, true, false);
       Image image5 = new Image(new File(c5.getCheminImage()).toURI().toString(), 100, 100, true, false);
       Image image6 = new Image(new File(c6.getCheminImage()).toURI().toString(), 100, 100, true, false);
       Image image7 = new Image(new File(c7.getCheminImage()).toURI().toString(), 100, 100, true, false);
       Image image8 = new Image(new File(c8.getCheminImage()).toURI().toString(), 100, 100, true, false);
       Image image9 = new Image(new File(c9.getCheminImage()).toURI().toString(), 100, 100, true, false);
       Image image10 = new Image(new File(c10.getCheminImage()).toURI().toString(), 100, 100, true, false);
       Image image11 = new Image(new File(c11.getCheminImage()).toURI().toString(), 100, 100, true, false);
       Image image12 = new Image(new File(c12.getCheminImage()).toURI().toString(), 100, 100, true, false);
       Image image13 = new Image(new File(c13.getCheminImage()).toURI().toString(), 100, 100, true, false);*/
       
      /* Canvas cancarte1 = new Canvas();
       GraphicsContext gc = cancarte1.getGraphicsContext2D();
       gc.drawImage(image1, 100, 100);
       gc.setFill(Color.WHITE);
       gc.fillRect(0, 0, cancarte1.getWidth(), cancarte1.getHeight());
       cartesJ2.getChildren().add(cancarte1);*/
       
      /* ImageView selectedImage1 = new ImageView();
       selectedImage1.setImage(image1);
       Pane paneimage1 = new Pane(selectedImage1);
       cartesJ2.getChildren().add(paneimage1);
       Button B = new Button("mom");
       cartesJ2.getChildren().add(B);
       /*ImageView selectedImage2 = new ImageView();
       selectedImage1.setImage(image2);
       Pane paneimage2 = new Pane(selectedImage2);
       cartesJ2.getChildren().add(paneimage2);
      /* ImageView selectedImage3 = new ImageView();
       selectedImage1.setImage(image3);
       cartesJ2.getChildren().add(selectedImage3);
       ImageView selectedImage4 = new ImageView();
       selectedImage1.setImage(image4);
       cartesJ2.getChildren().add(selectedImage4);
       ImageView selectedImage5 = new ImageView();
       selectedImage1.setImage(image5);
       cartesJ2.getChildren().add(selectedImage5);
       ImageView selectedImage6 = new ImageView();
       selectedImage1.setImage(image6);
       cartesJ2.getChildren().add(selectedImage6);
       ImageView selectedImage7 = new ImageView();
       selectedImage1.setImage(image7);
       cartesJ2.getChildren().add(selectedImage7);
       ImageView selectedImage8 = new ImageView();
       selectedImage1.setImage(image8);
       cartesJ2.getChildren().add(selectedImage8);
       ImageView selectedImage9 = new ImageView();
       selectedImage1.setImage(image9);
       cartesJ2.getChildren().add(selectedImage9);
       ImageView selectedImage10 = new ImageView();
       selectedImage1.setImage(image10);
       cartesJ2.getChildren().add(selectedImage10);
       ImageView selectedImage11 = new ImageView();
       selectedImage1.setImage(image11);
       cartesJ2.getChildren().add(selectedImage11);
       ImageView selectedImage12 = new ImageView();
       selectedImage1.setImage(image12);
       cartesJ2.getChildren().add(selectedImage12);
       ImageView selectedImage13 = new ImageView();
       selectedImage1.setImage(image13);
       cartesJ2.getChildren().add(selectedImage13);
       /*Canvas cancarte = new Canvas();
       GraphicsContext gc = cancarte.getGraphicsContext2D();
       gc.setFill(Color.GOLD);
       gc.fillRect(0, 0, 100, 100);
       gc.drawImage(image1, 0, 100);*/
       
       
       /*ArrayList<Carte> carteJ2 = p.getJ2().getMain();
       ArrayList<Canvas> paneJ2 = new ArrayList();
       
       Iterator MainJ2 = carteJ2.iterator();
       while (MainJ2.hasNext()){
           Carte i = (Carte)MainJ2.next();
           InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(i.getCheminImage());
           Image img = new Image(in);
           Canvas cancarte = new Canvas();
           GraphicsContext gc = cancarte.getGraphicsContext2D();
           gc.setFill(Color.GOLD);
           gc.fillRect(0, 0, 100, 100);
           gc.drawImage(img, 100, 100);
           //Pane pane = new Pane(cancarte);
           paneJ2.add(cancarte);
            }
       
       Iterator AfficherMain = paneJ2.iterator();
       while (AfficherMain.hasNext()){
           cartesJ2.getChildren().add((Canvas)AfficherMain.next());
            }*/
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
