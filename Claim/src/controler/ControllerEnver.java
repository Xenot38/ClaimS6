/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controler;

/**
 *
 * @author kekae
 */




import java.io.FileNotFoundException;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Carte;
import model.Plateau;
import view.CarteView;
import view.SceneCharger;
import view.SceneJeu;
import view.SceneMenu;
import view.SceneOptionPartie;


public class ControllerEnver{
    public Plateau p;
    public SceneJeu jeu;
    public SceneCharger charger;
    public SceneMenu menu;
    public SceneOptionPartie option;
    int choixScene = 1;
    public Scene scene;
    Stage stage;
    
    public ControllerEnver(Stage s){
        p= new Plateau("facile");
        p.setCarteEnJeu(p.getPioche().pop());
        stage = s;
        menu = new SceneMenu();
        option = new SceneOptionPartie();               
        jeu = new SceneJeu(p);
        charger = new SceneCharger();
    }
    
    public void afficher() throws FileNotFoundException{
        switch (choixScene){
            case 1:
                scene = menu.creerMenu();
                stage.setScene(scene);
                stage.show();
            break;
            case 2:
                scene = menu.creerMenu();
                stage.setScene(scene);
                stage.show();
            break;
            case 3:
                scene = menu.creerMenu();
                stage.setScene(scene);
                stage.show();
            break;
            case 4:
                scene = jeu.creerjeu(1900,1000);
                stage.setScene(scene);
                stage.show();
            break;
        }
    }
    
    
    public void setupJeu(){
        
        
        
    }
    public void setupMenu(){
    }
    public void setupOption(){
    }
    public void setupCharger(){
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
                 cr.getPane().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandlerEventHandler<MouseEventMouseEvent>() {
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
                cr.getPane().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandlerEventHandler<MouseEventMouseEvent>() {
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
    
    
}
