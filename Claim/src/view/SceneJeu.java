/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controler.ControllerEnver;
import static controler.Launcher.hauteur;
import static controler.Launcher.largeur;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.image.Image ;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.BLUE;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Box;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import model.Carte;
import model.Plateau;
import view.CarteView;
/**
 *
 * @author kekae
 */
public class SceneJeu {
    
   public Plateau p;
   public CarteView carteJouerJoueur1 = new CarteView("ressources/images/CarteJouerJ1.png");
   public CarteView carteJouerJoueur2 = new CarteView("ressources/images/CarteJouerJ2.png");
   public CarteView centreCarteAGagner = null;
   public CarteView Defausse = new CarteView("ressources/images/Defausse.png");
   public CarteView PartisanJ1 = new CarteView("ressources/images/PartisantJ1.png");
   public CarteView PartisanJ2 = new CarteView("ressources/images/PartisantJ2.png");
   public ArrayList<CarteView> arMain1 = new ArrayList<CarteView>();
   public ArrayList<CarteView> arMain2 = new ArrayList<CarteView>();
   public int[] refMain1 = {0,1,2,3,4,5,6,7,8,9,10,11,12};
   public int[] refMain2 = {0,1,2,3,4,5,6,7,8,9,10,11,12};
   public  HBox Main1;
   public  HBox Main2;
   public GridPane score = new GridPane();
   public GridPane chevalierGrid = new GridPane();
   public Label chevalierScore = new Label("0");
   public GridPane mortVivantGrid = new GridPane();
   public Label mortVivantScore = new Label("0");
   public GridPane nainGrid = new GridPane();
   public Label nainScore = new Label("0");
   public GridPane doppelGangerGrid = new GridPane();
   public Label doppelGangerScore = new Label("0");
   public GridPane gobelinGrid = new GridPane();
   public Label gobelinScore = new Label("0");
   
   
   public SceneJeu(Plateau plateau){
       this.p = plateau;
       
   }
   
   public Scene creerjeu(int x,int y) throws FileNotFoundException{
       ////////////////////////////
       BackgroundImage myBI = null;
       try{  myBI= new BackgroundImage(new Image(new File("ressources/images/bg.png").toURI().toString(), 1900, 1000, false, true),
        BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
        BackgroundSize.DEFAULT);}
       catch(Exception e){
           System.out.println("lol ça n'a pas charger mdr");
       }
       // historique //
       /*Canvas canHistorique = new Canvas(400,900);
       Pane panehi1 = new Pane(canHistorique);*/
       
       ////////////////////
       //cartes Joueur 2 //
       HBox cartesJ2 = new HBox();   
       //espace du coter gauche des cartes du joueur 2//
       Canvas canvideJ2G1 = new Canvas();
       Pane panevideJ2G1 = new Pane(canvideJ2G1);
       HBox.setHgrow(panevideJ2G1, Priority.ALWAYS);
       cartesJ2.getChildren().add(panevideJ2G1);
       //cartes//       
       cartesJ2.getChildren().add(Main2);
       //espace du coter droit des cartes du joueur 2 //
       Canvas canvideJ2D = new Canvas();
       Pane panevideJ2D = new Pane(canvideJ2D);
       HBox.setHgrow(panevideJ2D, Priority.ALWAYS);
       cartesJ2.getChildren().add(panevideJ2D);
       //carte partisan J2//
       cartesJ2.getChildren().add(PartisanJ2.getPane());
       // espacevide //
       Canvas canvideJ2D2 = new Canvas(40,10);
       Pane panevideJ2D2 = new Pane(canvideJ2D2);
       cartesJ2.getChildren().add(panevideJ2D2);
       ////////////////////
       Canvas canCentre = new Canvas();
       Pane paneCentre = new Pane(canCentre);
       VBox.setVgrow(paneCentre, Priority.ALWAYS);
       Canvas canCentre2 = new Canvas();
       Pane paneCentre2 = new Pane(canCentre2);
       VBox.setVgrow(paneCentre2, Priority.ALWAYS);
       ////////////////////
       //cartes Joueur 1 //
       HBox cartesJ1 = new HBox();
       //espace du coter gauche des cartes du joueur 1//
       Canvas canvideJ1G = new Canvas();
       Pane panevideJ1G = new Pane(canvideJ1G);
       HBox.setHgrow(panevideJ1G, Priority.ALWAYS);
        cartesJ1.getChildren().add(panevideJ1G);
       //cartes du Joueur1//
       cartesJ1.getChildren().add(Main1);
       //espace du coter droit des cartes du joueur 1//
       Canvas canvideJ1D = new Canvas();
       Pane panevideJ1D = new Pane(canvideJ1D);
       HBox.setHgrow(panevideJ1D, Priority.ALWAYS);
       cartesJ1.getChildren().add(panevideJ1D);
       //carte partisan J1//
       cartesJ1.getChildren().add(PartisanJ1.getPane());
       // espacevide //
       Canvas canvideJ1D2 = new Canvas(40,10);
       Pane panevideJ1D2 = new Pane(canvideJ1D2);
       cartesJ1.getChildren().add(panevideJ1D2);
       ////////////////////
       ////////////////////////////
       //Partie Centrale du plateau//
       HBox partieCentrale = new HBox();
       VBox defausse = centreDeffause();
       VBox carteJouer = centre2();
       VBox carteAGagner = centreCarteAGagner();       
       
       Canvas canCentre4 = new Canvas();
       Pane paneCentre4 = new Pane(canCentre4);
       HBox.setHgrow(paneCentre4, Priority.ALWAYS);
       
       
       Canvas canCentre5 = new Canvas();
       Pane paneCentre5 = new Pane(canCentre5);
       HBox.setHgrow(paneCentre5, Priority.ALWAYS);    
               
       Canvas canCentre6 = new Canvas();
       Pane paneCentre6 = new Pane(canCentre6);
       HBox.setHgrow(paneCentre6, Priority.ALWAYS);        
       
       Canvas canCentre7 = new Canvas();
       Pane paneCentre7 = new Pane(canCentre7);
       HBox.setHgrow(paneCentre7, Priority.ALWAYS);   
       Canvas canCentre8 = new Canvas();
       Pane paneCentre8 = new Pane(canCentre8);
       HBox.setHgrow(paneCentre8, Priority.ALWAYS);   
       Canvas canCentre9 = new Canvas();
       Pane paneCentre9 = new Pane(canCentre9);
       HBox.setHgrow(paneCentre9, Priority.ALWAYS);   
       
       
       for(int i =0;i<8 ;i ++){
           Polygon p= new Polygon();
           p.getPoints().addAll(new Double[]{
           0.0, 0.0,
           0.0, 20.0,
           20.0, 20.0,
           20.0, 0.0 });
           p.setFill(Color.WHITE);
            score.add(p, 0, i);
        }
       ImageView imChevalier = ControllerEnver.creerImageView("IconeCh.png",20,20);
       /*chevalierGrid.add(imChevalier,0,0);
       chevalierGrid.add(chevalierScore,1,0);*/
       score.add(imChevalier, 0, 8);
       score.add(chevalierScore, 0, 9);
       
       
       for(int i =0;i<10 ;i ++){
           Polygon p= new Polygon();
           p.getPoints().addAll(new Double[]{
           0.0, 0.0,
           0.0, 20.0,
           20.0, 20.0,
           20.0, 0.0 });
           p.setFill(Color.WHITE);
            score.add(p, 1, i);
        }
       
       for(int i =0;i<10 ;i ++){
           Polygon p= new Polygon();
           p.getPoints().addAll(new Double[]{
           0.0, 0.0,
           0.0, 20.0,
           20.0, 20.0,
           20.0, 0.0 });
           p.setFill(Color.WHITE);
            score.add(p, 2, i);
        }
       
       for(int i =0;i<10 ;i ++){
           Polygon p= new Polygon();
           p.getPoints().addAll(new Double[]{
           0.0, 0.0,
           0.0, 20.0,
           20.0, 20.0,
           20.0, 0.0 });
           p.setFill(Color.WHITE);
            score.add(p, 3, i);
        }
       
       for(int i =0;i<14 ;i ++){
           Polygon p= new Polygon();
           p.getPoints().addAll(new Double[]{
           0.0, 0.0,
           0.0, 20.0,
           20.0, 20.0,
           20.0, 0.0 });
           p.setFill(Color.WHITE);
            score.add(p, 4, i);
        }
       
       
       
       
       
       
       
       

       partieCentrale.getChildren().add(paneCentre4);   
       partieCentrale.getChildren().add(paneCentre8);
       partieCentrale.getChildren().add(defausse);
       partieCentrale.getChildren().add(paneCentre6);
       partieCentrale.getChildren().add(carteJouer);
       partieCentrale.getChildren().add(paneCentre7);
       partieCentrale.getChildren().add(carteAGagner);
       partieCentrale.getChildren().add(paneCentre9);
       partieCentrale.getChildren().add(score);
       partieCentrale.getChildren().add(paneCentre5);
       
       
       VBox partieC = new VBox();
       partieC.getChildren().add(cartesJ2);
       partieC.getChildren().add(paneCentre);
       partieC.getChildren().add(partieCentrale);
       partieC.getChildren().add(paneCentre2);
       partieC.getChildren().add(cartesJ1);
       ////////////////////////////
       
       ////////////////////////
       //plateau final de jeu//
      // HBox jeu = new HBox();
       //jeu.getChildren().add(partieC);
       ////////////////////////
       partieC.setBackground(new Background(myBI));
       
       Scene s = new Scene(partieC, x , y);
       
       return s;
   }
                
     
   public VBox centreDeffause(){
       Canvas canpousse = new Canvas(0,70);
       Pane panepousse = new Pane(canpousse);
       VBox carte = new VBox();
       carte.getChildren().add(panepousse);
       carte.getChildren().add(Defausse.getPane());
       return carte;
    }
   
   
   public VBox centreCarteAGagner(){
       Canvas canpousse = new Canvas(0,70);
       Pane panepousse = new Pane(canpousse);
       VBox carte = new VBox();
       centreCarteAGagner = new CarteView(p.getCarteEnJeu().getCheminImage());
       carte.getChildren().add(panepousse);
       carte.getChildren().add(centreCarteAGagner.getPane());
       return carte;
    }
   
   
   
   
   public VBox centre2(){
       VBox cartes = new VBox();
       Canvas canpousse = new Canvas(0,40);
       Pane panepousse = new Pane(canpousse);
       cartes.getChildren().add(carteJouerJoueur2.getPane());
       cartes.getChildren().add(panepousse);
       cartes.getChildren().add(carteJouerJoueur1.getPane());
       
       return cartes;
    }
   
   
   
   
   
   
   
}
