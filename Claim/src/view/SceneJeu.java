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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
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
   Stage primary;
   CarteView carteJouerJoueur1 = new CarteView("ressources/images/CarteJouerJ1.png");
   CarteView carteJouerJoueur2 = new CarteView("ressources/images/CarteJouerJ2.png");
   CarteView centreCarteAGagner = null;
   ArrayList<CarteView> arMain1 = new ArrayList<CarteView>();
   ArrayList<CarteView> arMain2 = new ArrayList<CarteView>();
   int[] refMain1 = {0,1,2,3,4,5,6,7,8,9,10,11,12};
   int[] refMain2 = {0,1,2,3,4,5,6,7,8,9,10,11,12};
   HBox Main1;
   HBox Main2;
   
   
   
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
       HBox.setHgrow(panevideJ2G, Priority.ALWAYS);
       cartesJ2.getChildren().add(panevideJ2G);
       //cartes//       
       HBox MainJ2 = getHBMain(p.getJ2().getMain(),0);
       cartesJ2.getChildren().add(MainJ2);
       //espace du coter droit des cartes du joueur 2 //
       Canvas canvideJ2D = new Canvas();
       Pane panevideJ2D = new Pane(canvideJ2D);
       cartesJ2.getChildren().add(panevideJ2D);
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
       //cartes du Joueur1//
       HBox mainJ1 = getHBMain(p.getJ1().getMain(),1);
       cartesJ1.getChildren().add(mainJ1);
       //espace du coter droit des cartes du joueur 1//
       Canvas canvideJ1D = new Canvas();
       Pane panevideJ1D = new Pane(canvideJ1D);
       cartesJ1.getChildren().add(panevideJ1G);
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
       Pane paneCentre8 = new Pane(canCentre7);
       HBox.setHgrow(paneCentre8, Priority.ALWAYS);   
       Canvas canCentre9 = new Canvas();
       Pane paneCentre9 = new Pane(canCentre9);
       HBox.setHgrow(paneCentre9, Priority.ALWAYS);   
       
       partieCentrale.getChildren().add(paneCentre4);   
       partieCentrale.getChildren().add(paneCentre8);
       partieCentrale.getChildren().add(defausse);
       partieCentrale.getChildren().add(paneCentre6);
       partieCentrale.getChildren().add(carteJouer);
       partieCentrale.getChildren().add(paneCentre7);
       partieCentrale.getChildren().add(carteAGagner);
       partieCentrale.getChildren().add(paneCentre9);
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
       HBox jeu = new HBox();
       jeu.getChildren().add(partieC);
       ////////////////////////
       Scene s = new Scene(jeu, x , y);
       return s;
   }
   
   
   public HBox getHBMain(ArrayList<Carte> ar, int a){
       HBox mainJoueur = new HBox();
       for(int i = 0; i<ar.size();i++){
           final int test = i;
           CarteView cr = new CarteView(ar.get(i).getCheminImage());
           if (a ==0){
               arMain2.add(cr);
           }else{
               arMain1.add(cr);
               if(p.isJ1Courant()){
                 cr.getPane().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
       @Override 
        public void handle(MouseEvent e) { 
           
               for(int i = test+1;i<13; i++){
                   refMain1[i]=refMain1[i]-1;
               }
               
                   System.out.println(refMain1[test]);
               p.setCarteJ1(p.getJ1().choisirCarte(refMain1[test]));
               carteJouerJoueur1.getPane().getChildren().clear();
               carteJouerJoueur1.SetImage((ImageView) arMain1.get(test).getPane().getChildren().get(0));
               coupIAJoue1();
               
               p.calculPli();
           
        } 
    });
           }else{
                coupIAJoue1();
                cr.getPane().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
       @Override 
        public void handle(MouseEvent e) { 
           
               for(int i = test+1;i<13; i++){
                   refMain1[i]=refMain1[i]-1;
               }
               
                   System.out.println(refMain1[test]);
               p.setCarteJ1(p.getJ1().choisirCarte(refMain1[test]));
               carteJouerJoueur1.getPane().getChildren().clear();
               carteJouerJoueur1.SetImage((ImageView) arMain1.get(test).getPane().getChildren().get(0));
               coupIAJoue1();
               
               p.calculPli();
           
        } 
    });
               }
           }
          // AnchorPane pane = cr.getPane();
        
           mainJoueur.getChildren().add(cr.getPane());
       }
       return mainJoueur;
    }
   
   
    public void coupIAJoue2(){
                System.out.println("////////////////////");
                System.out.println(p.getJ2().joue(p));
                int a = p.getJ2().joue(p);
                System.out.println(p.getJ2().choisirCarte(a).getForce());
                p.setCarteJ2(p.getJ2().choisirCarte(a));
               carteJouerJoueur2.getPane().getChildren().clear();
               carteJouerJoueur2.SetImage((ImageView) arMain2.get(refMain2[a]).getPane().getChildren().get(0));
               for (int i = a; i< 13;i++){
                   refMain2[i] = refMain2[i]+1;
               }
               if(!p.isJ1Courant()){
                   coupIAJoue1();
               }
            }
    
    
    public void coupIAJoue1(){
        int a = p.getJ2().joue(p);
                System.out.println(p.getJ2().choisirCarte(a).getForce());
                p.setCarteJ2(p.getJ2().choisirCarte(a));
               carteJouerJoueur2.getPane().getChildren().clear();
               carteJouerJoueur2.SetImage((ImageView) arMain2.get(refMain2[a]).getPane().getChildren().get(0));
               for (int i = a; i< 13;i++){
                   refMain2[i] = refMain2[i]+1;
               }
    }
                
   
   
       
      EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() { 
         @Override 
         public void handle(MouseEvent e) { 
             p.getJ1().getMain().remove(0);
             System.out.println(p.getJ1().getMain().get(0).getCheminImage());
            try{ primary.setScene(creerjeu(1920,1020));}catch(FileNotFoundException ex){}
             
         }
            }; 
       
       
   public VBox centreDeffause(){
       Canvas canpousse = new Canvas(0,70);
       Pane panepousse = new Pane(canpousse);
       VBox carte = new VBox();
       AnchorPane pane = new AnchorPane();
       Image image1=null;
       try{image1 = new Image(new File("ressources/images/Ch2.png").toURI().toString(), 200, 175, true, true);}catch(Exception e){}
       ImageView imageSelected = new ImageView();
       imageSelected.setImage(image1);
       pane.getChildren().add(imageSelected);
       carte.getChildren().add(panepousse);
       carte.getChildren().add(pane);
       return carte;
    }
   
   
   public VBox centreCarteAGagner(){
       Canvas canpousse = new Canvas(0,70);
       Pane panepousse = new Pane(canpousse);
       VBox carte = new VBox();
       /*AnchorPane pane = new AnchorPane();
       Image image1=null;
       try{image1 = new Image(new File(p.getCarteEnJeu().getCheminImage()).toURI().toString(), 200, 175, true, true);}catch(Exception e){}
       ImageView imageSelected = new ImageView();
       imageSelected.setImage(image1);
       pane.getChildren().add(imageSelected);*/
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
