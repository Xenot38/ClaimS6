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
import java.util.Iterator;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import model.Carte;
import model.Faction;
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
        updateScore();
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
                    if(p.getCarteJ2().getFaction() == Faction.MortsVivants ){
                        if (p.getCarteJ1().getFaction() == Faction.MortsVivants){
                            
                        }else{
                            jeu.Defausse.getPane().getChildren().clear();
                            jeu.Defausse.SetImage((ImageView) jeu.carteJouerJoueur1.getPane().getChildren().get(0));}
                    }else {
                            jeu.Defausse.getPane().getChildren().clear();
                            jeu.Defausse.SetImage((ImageView) jeu.carteJouerJoueur2.getPane().getChildren().get(0));
                        
                    }
                    
                }
                    
                    
                jeu.carteJouerJoueur1.getPane().getChildren().clear();
                ImageView imageSelected1 = creerImageView("ressources/images/CarteJouerJ1.png",200,175);
                jeu.carteJouerJoueur1.SetImage(imageSelected1);

                jeu.carteJouerJoueur2.getPane().getChildren().clear();
                ImageView imageSelected2 = creerImageView("ressources/images/CarteJouerJ2.png",200,175);
                jeu.carteJouerJoueur2.SetImage(imageSelected2);
                
                if(p.getPhase()==1){
                    ImageView im1 = creerImageView(p.getCarteEnJeu().getCheminImage(),200,175);
                    ImageView im2 = creerImageView(p.getCarteEnJeuPerdant().getCheminImage(),200,175);
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
                            ImageView imageSelected3 = creerImageView(p.getCarteEnJeu().getCheminImage(),200,175);                
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
    
    public static ImageView creerImageView(String s,int a, int b){
        Image im = null;
        try{
        im = new Image(new File(s).toURI().toString(), a, b, true, true);
        } catch (Exception e) {
            System.out.println("pas trouver");
        }
        ImageView imageSelected = new ImageView();
        imageSelected.setImage(im);
        return imageSelected;
    }
    
    
    public void setupJeuPhase2(){
        for (int i =0; i<13;i++){
            jeu.centreCarteAGagner.getPane().getChildren().clear();
            jeu.arMain1.get(i).SetImage(creerImageView(p.getJ1().getMain().get(i).getCheminImage(),200,175));
            jeu.arMain2.get(i).SetImage(creerImageView(p.getJ2().getMain().get(i).getCheminImage(),200,175));
            jeu.refMain1[i]=i;
            jeu.refMain2[i]=i;
        }
    }
    
    
    public void updateScore(){
        int chevalier = 8;
        int doppleGanger = 10;
        int nain = 10;
        int gobelin = 14;
        
        Iterator<Carte> it = p.getDefausse().iterator();
        
        while( it.hasNext()){
            switch(it.next().getFaction()){
                case Chevaliers:
                    chevalier--;
                    break;
                case Doppelgangers:
                    doppleGanger--;
                    break;
                case Nains:
                    nain--;
                    break;
                case Gobelins:
                    gobelin--;
                    break;
            }
            
        int chevalierJ2 = p.getNBCartesScore(false ,Faction.Chevaliers);
        int doppleGangerJ2 = p.getNBCartesScore(false ,Faction.Doppelgangers);
        int nainJ2 = p.getNBCartesScore(false ,Faction.Nains);
        int gobelinJ2 = p.getNBCartesScore(false ,Faction.Gobelins);
        int mvJ2 = p.getNBCartesScore(false ,Faction.MortsVivants);
        
        int chevalierJ1 = p.getNBCartesScore(true ,Faction.Chevaliers);
        int doppleGangerJ1 = p.getNBCartesScore(true ,Faction.Doppelgangers);
        int nainJ1 = p.getNBCartesScore(true ,Faction.Nains);
        int gobelinJ1 = p.getNBCartesScore(true ,Faction.Gobelins);
        int mvJ1 = p.getNBCartesScore(true ,Faction.MortsVivants);
        
        
        int chevalierB = chevalier-(chevalierJ2+chevalierJ1);
        int doppleGangerB = doppleGanger-(doppleGangerJ2+doppleGangerJ1);
        int nainB = nain-(nainJ2+nainJ1);
        int gobelinB = gobelin-(gobelinJ2+gobelinJ1);
        int mvB = 10-(mvJ2+mvJ1);
        
        jeu.score.getChildren().clear();
        jeu.chevalierGrid.getChildren().clear();
        jeu.mortVivantGrid.getChildren().clear();
        jeu.nainGrid.getChildren().clear();
        jeu.doppelGangerGrid.getChildren().clear();
        jeu.gobelinGrid.getChildren().clear();
        
        ///////CHEVALIER/////////
        
       /* Label chevalLabeler = new Label("chelvalier");
        chevalLabeler.setRotate(-90);
        jeu.score.add(chevalLabeler, 0, 4);*/
        
        
        for(int i =0; i<chevalierJ2 ;i ++){
           Polygon p= new Polygon();
           p.getPoints().addAll(new Double[]{
           0.0, 0.0,
           0.0, 20.0,
           20.0, 20.0,
           20.0, 0.0 });
           p.setFill(Color.RED);
            jeu.score.add(p, 0, i);
        }
        for(int i =chevalierJ2; i<chevalierJ2+chevalierB ;i ++){
           Polygon p= new Polygon();
           p.getPoints().addAll(new Double[]{
           0.0, 0.0,
           0.0, 20.0,
           20.0, 20.0,
           20.0, 0.0 });
           p.setFill(Color.WHITE);
            jeu.score.add(p, 0, i);
        }
        for(int i =chevalierJ2+chevalierB; i<chevalierJ2+chevalierB+chevalierJ1 ;i ++){
           Polygon p= new Polygon();
           p.getPoints().addAll(new Double[]{
           0.0, 0.0,
           0.0, 20.0,
           20.0, 20.0,
           20.0, 0.0 });
           p.setFill(Color.GREEN);
            jeu.score.add(p, 0, i);
        }
        ////////////
        
        ////////MortVivant/////////
        for(int i =0; i<mvJ2 ;i ++){
           Polygon p= new Polygon();
           p.getPoints().addAll(new Double[]{
           0.0, 0.0,
           0.0, 20.0,
           20.0, 20.0,
           20.0, 0.0 });
           p.setFill(Color.RED);
            jeu.score.add(p, 1, i);
        }
        for(int i =mvJ2; i<mvJ2+mvB ;i ++){
           Polygon p= new Polygon();
           p.getPoints().addAll(new Double[]{
           0.0, 0.0,
           0.0, 20.0,
           20.0, 20.0,
           20.0, 0.0 });
           p.setFill(Color.WHITE);
            jeu.score.add(p,1, i);
        }
        for(int i =mvJ2+mvB; i<mvJ2+mvB+mvJ1 ;i ++){
           Polygon p= new Polygon();
           p.getPoints().addAll(new Double[]{
           0.0, 0.0,
           0.0, 20.0,
           20.0, 20.0,
           20.0, 0.0 });
           p.setFill(Color.GREEN);
            jeu.score.add(p, 1, i);
        }
        ///////////////////////
        
        
        
        //////////DoppleGanger///////
        for(int i =0; i<doppleGangerJ2 ;i ++){
           Polygon p= new Polygon();
           p.getPoints().addAll(new Double[]{
           0.0, 0.0,
           0.0, 20.0,
           20.0, 20.0,
           20.0, 0.0 });
           p.setFill(Color.RED);
            jeu.score.add(p, 2, i);
        }
        for(int i =doppleGangerJ2; i<doppleGangerJ2+doppleGangerB ;i ++){
           Polygon p= new Polygon();
           p.getPoints().addAll(new Double[]{
           0.0, 0.0,
           0.0, 20.0,
           20.0, 20.0,
           20.0, 0.0 });
           p.setFill(Color.WHITE);
            jeu.score.add(p, 2, i);
        }
        for(int i =doppleGangerJ2+doppleGangerB; i<doppleGangerJ2+doppleGangerB+doppleGangerJ1 ;i ++){
           Polygon p= new Polygon();
           p.getPoints().addAll(new Double[]{
           0.0, 0.0,
           0.0, 20.0,
           20.0, 20.0,
           20.0, 0.0 });
           p.setFill(Color.GREEN);
            jeu.score.add(p, 2, i);
        }
        /////////////////////
        
        
        //////////Nain////////////
        for(int i =0; i<nainJ2 ;i ++){
           Polygon p= new Polygon();
           p.getPoints().addAll(new Double[]{
           0.0, 0.0,
           0.0, 20.0,
           20.0, 20.0,
           20.0, 0.0 });
           p.setFill(Color.RED);
            jeu.score.add(p,3, i);
        }
        for(int i =nainJ2; i<nainJ2+nainB ;i ++){
           Polygon p= new Polygon();
           p.getPoints().addAll(new Double[]{
           0.0, 0.0,
           0.0, 20.0,
           20.0, 20.0,
           20.0, 0.0 });
           p.setFill(Color.WHITE);
            jeu.score.add(p, 3, i);
        }
        for(int i =nainJ2+nainB; i<nainJ2+nainB+nainJ1 ;i ++){
           Polygon p= new Polygon();
           p.getPoints().addAll(new Double[]{
           0.0, 0.0,
           0.0, 20.0,
           20.0, 20.0,
           20.0, 0.0 });
           p.setFill(Color.GREEN);
            jeu.score.add(p, 3, i);
        }
        //////////////////////
        
        ////////////Gobelin///////////
        for(int i =0; i<gobelinJ2 ;i ++){
           Polygon p= new Polygon();
           p.getPoints().addAll(new Double[]{
           0.0, 0.0,
           0.0, 20.0,
           20.0, 20.0,
           20.0, 0.0 });
           p.setFill(Color.RED);
            jeu.score.add(p, 4, i);
        }
        for(int i =gobelinJ2; i<gobelinJ2+gobelinB ;i ++){
           Polygon p= new Polygon();
           p.getPoints().addAll(new Double[]{
           0.0, 0.0,
           0.0, 20.0,
           20.0, 20.0,
           20.0, 0.0 });
           p.setFill(Color.WHITE);
            jeu.score.add(p, 4, i);
        }
        for(int i =gobelinJ2+gobelinB; i<gobelinJ2+gobelinB+gobelinJ1 ;i ++){
           Polygon p= new Polygon();
           p.getPoints().addAll(new Double[]{
           0.0, 0.0,
           0.0, 20.0,
           20.0, 20.0,
           20.0, 0.0 });
           p.setFill(Color.GREEN);
            jeu.score.add(p, 4, i);
        }
        ///////////////
         
        }
        
    }

}
