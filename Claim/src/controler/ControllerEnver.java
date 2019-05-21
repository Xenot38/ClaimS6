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
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Carte;
import model.Plateau;
import view.CarteView;
import view.SceneCharger;
import view.SceneJeu;
import view.SceneMenu;
import view.SceneOptionPartie;

public class ControllerEnver {

    public Plateau p;
    public SceneJeu jeu;
    public SceneCharger charger;
    public SceneMenu menu;
    public SceneOptionPartie option;
    int choixScene = 4;
    boolean J1joue = true;
    public Scene scene;
    Stage stage;

    public ControllerEnver(Stage s) {
        p = new Plateau("facile");
        p.setCarteEnJeu(p.getPioche().pop());
        p.setCarteEnJeuPerdant(p.getPioche().pop());
        stage = s;
        menu = new SceneMenu();
        option = new SceneOptionPartie();
        jeu = new SceneJeu(p);
        charger = new SceneCharger();
    }

    public void afficher() throws FileNotFoundException {
        switch (choixScene) {
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
                setupJeu();
                scene = jeu.creerjeu(1900, 1000);
                stage.setScene(scene);
                stage.show();
                break;
        }
    }

    public void setupJeu() {
        jeu.Main1 = getHBMain(p.getJ1().getMain(), 1);
        jeu.Main2 = getHBMain(p.getJ2().getMain(), 0);

    }

    public void setupMenu() {
    }

    public void setupOption() {
    }

    public void setupCharger() {
    }

    public HBox getHBMain(ArrayList<Carte> ar, int a) {
        HBox mainJoueur = new HBox();
        for (int i = 0; i < ar.size(); i++) {
            final int test = i;

            CarteView cr = new CarteView(ar.get(i).getCheminImage());
            if (a == 0) {
                jeu.arMain2.add(cr);
            } else {
                jeu.arMain1.add(cr);
                cr.getPane().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        
                        
                        if (J1joue && p.containsCard(p.getJ1().getCartesJouable(p.getCarteJ2()),p.getJ1().getMain().get(jeu.refMain1[test]))) {
                            
                            for (int i = test + 1; i < 13; i++) {
                                jeu.refMain1[i] = jeu.refMain1[i] - 1;
                            }
                            p.setCarteJ1(p.getJ1().choisirCarte(jeu.refMain1[test]));
                            jeu.carteJouerJoueur1.getPane().getChildren().clear();
                            jeu.carteJouerJoueur1.SetImage((ImageView) jeu.arMain1.get(test).getPane().getChildren().get(0));
                            J1joue = false;
                            if (p.isJ1Courant()) {
                                gestionTour();
                            } else {
                                Task<Void> sleeper = new Task<Void>() {
                                    @Override
                                    protected Void call() throws Exception {
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                        }
                                        return null;
                                    }
                                };
                                sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                                    @Override
                                    public void handle(WorkerStateEvent event) {
                                        resetTour();
                                    }
                                });
                                new Thread(sleeper).start();
                            }
                        }
                    }
                });
            }
            mainJoueur.getChildren().add(cr.getPane());
        }
        return mainJoueur;
    }

    public void CoupIA() {
        int a = p.getJ2().joue(p);
        p.setCarteJ2(p.getJ2().choisirCarte(a));
        jeu.carteJouerJoueur2.getPane().getChildren().clear();
        jeu.carteJouerJoueur2.SetImage((ImageView) jeu.arMain2.get(jeu.refMain2[a]).getPane().getChildren().get(0));

        for (int j = a; j < jeu.refMain2.length - 1; j++) {
            jeu.refMain2[j] = jeu.refMain2[j + 1];
        }

        if (p.isJ1Courant()) {
            Task<Void> reset = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    return null;
                }
            };
            reset.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    resetTour();
                }
            });
            new Thread(reset).start();
        } else {
            gestionTour();
        }

       // setJ1joue(true);
    }

    public void gestionTour() {

        Task<Void> iaJoue = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                return null;
            }
        };
        iaJoue.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                CoupIA();
            }
        });

        if (p.isJ1Courant()) {
            if (p.getCarteJ1() != null) {
                new Thread(iaJoue).start();
            } else {
                setJ1joue(true);
            }
        } else {
            if (p.getCarteJ2() != null) {
                setJ1joue(true);
            } else {
                new Thread(iaJoue).start();
            }
        }

    }

    public void resetTour() {
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                
                if(p.getPhase()==1){
                    jeu.Defausse.getPane().getChildren().clear();
                    jeu.Defausse.SetImage((ImageView) jeu.carteJouerJoueur2.getPane().getChildren().get(0));
                }
                    
                    
                jeu.carteJouerJoueur1.getPane().getChildren().clear();
                ImageView imageSelected1 = creerImageView("ressources/images/CarteJouerJ1.png");
                jeu.carteJouerJoueur1.SetImage(imageSelected1);

                jeu.carteJouerJoueur2.getPane().getChildren().clear();
                ImageView imageSelected2 = creerImageView("ressources/images/CarteJouerJ2.png");
                jeu.carteJouerJoueur2.SetImage(imageSelected2);
                
                if(p.getPhase()==1){
                    ImageView im1 = creerImageView(p.getCarteEnJeu().getCheminImage());
                    ImageView im2 = creerImageView(p.getCarteEnJeuPerdant().getCheminImage());
                    p.calculPli();
                    jeu.PartisanJ1.getPane().getChildren().clear();
                    jeu.PartisanJ2.getPane().getChildren().clear();
                    if(p.getPhase() == 2){
                        setupJeuPhase2();
                        System.out.println(" passage phase 2");
                    }else{
                        if(p.isJ1Courant()){
                            jeu.PartisanJ1.SetImage(im1);
                            jeu.PartisanJ2.SetImage(im2);
                        }else{
                            jeu.PartisanJ2.SetImage(im1);
                            jeu.PartisanJ1.SetImage(im2);}
                            p.setCarteEnJeu(p.getPioche().pop());
                            p.setCarteEnJeuPerdant(p.getPioche().pop());
                            ImageView imageSelected3 = creerImageView(p.getCarteEnJeu().getCheminImage());                
                            jeu.centreCarteAGagner.getPane().getChildren().clear();
                            jeu.centreCarteAGagner.SetImage(imageSelected3);
                    
                    }
                }else{
                    p.calculPli();
                }
                gestionTour();
            }
        });
        new Thread(sleeper).start();

    }

    public void setJ1joue(boolean b) {
        J1joue = b;
    }
    
    public ImageView creerImageView(String s){
        Image im = null;
        try{
        im = new Image(new File(s).toURI().toString(), 200, 175, true, true);
        } catch (Exception e) {
            System.out.println("pas trouver");
        }
        ImageView imageSelected = new ImageView();
        imageSelected.setImage(im);
        return imageSelected;
    }
    
    
    public void setupJeuPhase2(){
        jeu.arMain1.clear();
        jeu.arMain2.clear();
        jeu.Main1.getChildren().clear();
        jeu.Main1 = getHBMain(p.getJ1().getMain(), 1);
        System.out.println("remplissage main 1");
        jeu.Main2.getChildren().clear();
        jeu.Main2 = getHBMain(p.getJ2().getMain(), 0);
        System.out.println("remplissage main 2");
        for (int i =0; i<13;i++){
            jeu.refMain1[i]=i;
            jeu.refMain2[i]=i;
            
        }
        System.out.println("taille = "+(jeu.refMain1.length -1));
        System.out.println("taille = "+(jeu.refMain2.length -1));
    }

}
