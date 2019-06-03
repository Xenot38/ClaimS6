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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import model.Carte;
import model.Faction;
import model.Plateau;
import view.CarteView;
import view.Règle;
import view.SceneCharger;
import view.SceneFinDePartie;
import view.SceneJeu;
import view.SceneMenu;
import view.SceneOptionPartie;

public class ControllerEnver {

    public static double largeurScore = 40.0;
    public static double hauteurScore = 300.0;
    public Plateau p;
    public SceneJeu jeu;
    public SceneCharger charger;
    public Règle regle;
    public SceneMenu menu;
    public SceneOptionPartie option;
    int choixScene = 1;
    boolean J1joue = true;
    boolean partieEncour = false;
    public Scene scene;
    public static int tailleCarteX = 200;
    public static int tailleCarteY = 175;
    public int tailleFenetreJeuX = 1900;
    public int tailleFenetreJeuY = 1000;
    public int partie = -2;
    public String fins = "ressources/images/victoire.png";
    public SceneFinDePartie fin;
    Stage stage;

    public ControllerEnver(Stage s) {
        stage = s;
        menu = new SceneMenu();
        option = new SceneOptionPartie();
        charger = new SceneCharger();
        regle = new Règle();
        fin = new SceneFinDePartie();
    }

    public void afficher() throws FileNotFoundException {
        System.out.println("+++++++++++++++++++++++++" + choixScene + "++++++++++++++++++++++++++");
        switch (choixScene) {
            case 1:
                setupMenu();
                scene = menu.creerMenu();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
                break;
            case 2:
                setupOption();
                scene = option.creerOptionPartie(600, 250);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
                break;
            case 3:
                scene = menu.creerMenu();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
                break;
            case 4:
                setupJeu();
                scene = jeu.creerjeu(tailleFenetreJeuX, tailleFenetreJeuY);
                scene.getStylesheets().add("view/css/Jeu.css");
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
                break;
            case 5:
                setupRegle();
                scene = regle.creerSceneRegle();
                stage.setScene(scene);
                stage.show();
                break;
            case 6:
                setupFin();
                scene = fin.SceneFinParti(fins);
                stage.setScene(scene);
                stage.show();
                break;
        }
    }

    public void setupFin() {
        fin.Quitter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.exit(0);
            }
        });
        fin.Menu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                choixScene = 1;
                try {
                    afficher();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ControllerEnver.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        fin.Recommencer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                p = new Plateau(p.getJ2().getDifficulte());
                p.setCarteEnJeu(p.getPioche().pop());
                p.setCarteEnJeuPerdant(p.getPioche().pop());
                jeu = new SceneJeu(p);

                choixScene = 4;
                try {
                    afficher();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ControllerEnver.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public void setupMenu() {
        menu.Jouer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                choixScene = 2;
                try {
                    afficher();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ControllerEnver.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        menu.Quitter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.exit(0);
            }
        });
        
        
       menu.Charger.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                p = Charger();
                choixScene = 4;
                jeu = new SceneJeu(p);
                try {
                    afficher();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ControllerEnver.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        menu.Regles.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                choixScene = 5;
                try {
                    afficher();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ControllerEnver.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public void setupJeu() {
        jeu.Main1 = getHBMain(p.getJ1().getMain(), 1);
        jeu.Main2 = getHBMainIA(p.getJ2().getMain(), 0);
        jeu.sauver.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Sauvegarder(p);
            }
        });
        jeu.recommencer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                p = new Plateau(p.getJ2().getDifficulte());
                p.setCarteEnJeu(p.getPioche().pop());
                p.setCarteEnJeuPerdant(p.getPioche().pop());
                jeu = new SceneJeu(p);

                choixScene = 4;
                try {
                    afficher();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ControllerEnver.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        jeu.menu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                choixScene = 1;
                try {
                    afficher();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ControllerEnver.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        jeu.Quitter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.exit(0);
            }
        });
        updateScore();

    }

    public void setupRegle() {
        regle.retour.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                choixScene = 1;
                try {
                    afficher();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ControllerEnver.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public void setupOption() {
        option.facile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                option.difficulteIa = "facile";
                option.facile.getStyleClass().clear();
                option.facile.getStyleClass().add("buttonChoisi");
                option.moyen.getStyleClass().clear();
                option.moyen.getStyleClass().add("button");
                option.difficile.getStyleClass().clear();
                option.difficile.getStyleClass().add("button");
            }
        });
        option.moyen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                option.difficulteIa = "moyenne";
                option.facile.getStyleClass().clear();
                option.facile.getStyleClass().add("button");
                option.moyen.getStyleClass().clear();
                option.moyen.getStyleClass().add("buttonChoisi");
                option.difficile.getStyleClass().clear();
                option.difficile.getStyleClass().add("button");
            }
        });
        option.difficile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                option.difficulteIa = "difficile";
                option.difficile.getStyleClass().clear();
                option.difficile.getStyleClass().add("buttonChoisi");
                option.moyen.getStyleClass().clear();
                option.moyen.getStyleClass().add("button");
                option.facile.getStyleClass().clear();
                option.facile.getStyleClass().add("button");
            }
        });
        option.resolution1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                option.resolution1.getStyleClass().clear();
                option.resolution1.getStyleClass().add("buttonChoisi");
                option.resolution2.getStyleClass().clear();
                option.resolution2.getStyleClass().add("button");
                option.resolution3.getStyleClass().clear();
                option.resolution3.getStyleClass().add("button");
                tailleCarteX = 170;
                tailleCarteY = 150;
                tailleFenetreJeuX = 1600;
                tailleFenetreJeuY = 980;
                largeurScore = 35.0;
                hauteurScore = 275.0;

            }
        });
        option.resolution2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                option.resolution1.getStyleClass().clear();
                option.resolution1.getStyleClass().add("button");
                option.resolution2.getStyleClass().clear();
                option.resolution2.getStyleClass().add("buttonChoisi");
                option.resolution3.getStyleClass().clear();
                option.resolution3.getStyleClass().add("button");
                tailleCarteX = 200;
                tailleCarteY = 175;
                tailleFenetreJeuX = 1900;
                tailleFenetreJeuY = 1000;
                largeurScore = 40.0;
                hauteurScore = 300.0;

            }
        });
        option.resolution3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                option.resolution1.getStyleClass().clear();
                option.resolution1.getStyleClass().add("button");
                option.resolution2.getStyleClass().clear();
                option.resolution2.getStyleClass().add("button");
                option.resolution3.getStyleClass().clear();
                option.resolution3.getStyleClass().add("buttonChoisi");
                tailleCarteX = 275;
                tailleCarteY = 235;
                tailleFenetreJeuX = 2500;
                tailleFenetreJeuY = 1400;
                largeurScore = 50.0;
                hauteurScore = 350.0;
            }
        });

        option.retour.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                choixScene = 1;
                try {
                    afficher();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ControllerEnver.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        option.lancer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                choixScene = 4;
                p = new Plateau(option.difficulteIa);
                p.setCarteEnJeu(p.getPioche().pop());
                p.setCarteEnJeuPerdant(p.getPioche().pop());
                jeu = new SceneJeu(p);
                try {
                    System.out.println("looooollllooll");
                    afficher();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ControllerEnver.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public void setupCharger() {
    }

    public HBox getHBMain(ArrayList<Carte> ar, int a) {
        HBox mainJoueur = new HBox();
        for (int i = 0; i < ar.size(); i++) {
            final int test = i;

            CarteView cr = new CarteView(ar.get(i).getCheminImage(), tailleCarteX, tailleCarteY);
            if (a == 0) {
                jeu.arMain2.add(cr);
            } else {
                jeu.arMain1.add(cr);
                cr.getPane().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {

                        if (J1joue && p.containsCard(p.getJ1().getCartesJouable(p.getCarteJ2()), p.getJ1().getMain().get(jeu.refMain1[test]))) {
                            for (int i = 0; i < p.getJ1().getMain().size(); i++) {
                                jeu.arMain1.get(i).getPane().getStyleClass().clear();
                            }
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
                                            Thread.sleep(500);
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

    public HBox getHBMainIA(ArrayList<Carte> ar, int a) {
        HBox mainJoueur = new HBox();
        for (int i = 0; i < ar.size(); i++) {
            CarteView cr = new CarteView(p.getJ2().getMain().get(i).getCheminImage(), tailleCarteX, tailleCarteY);
            jeu.arMain2.add(cr);
            mainJoueur.getChildren().add(cr.getPane());
        }
        return mainJoueur;
    }

    public void CoupIA() {
        int a = p.getJ2().joue(p);
        ImageView im = creerImageView(p.getJ2().getMain().get(a).getCheminImage(), tailleCarteX, tailleCarteY);
        p.setCarteJ2(p.getJ2().choisirCarte(a));
        jeu.carteJouerJoueur2.getPane().getChildren().clear();
        jeu.carteJouerJoueur2.SetImage(im);
        jeu.arMain2.get(jeu.refMain2[a]).getPane().getChildren().clear();

        for (int j = a; j < jeu.refMain2.length - 1; j++) {
            jeu.refMain2[j] = jeu.refMain2[j + 1];
        }

        if (p.isJ1Courant()) {
            Task<Void> reset = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    try {
                        Thread.sleep(500);
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
                    Thread.sleep(500);
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
            jeu.carteJouerJoueur2.getPane().getStyleClass().clear();
            jeu.carteJouerJoueur1.getPane().getStyleClass().clear();
            jeu.carteJouerJoueur1.getPane().getStyleClass().add("carte-jouable");
            if (p.getCarteJ1() != null) {
                new Thread(iaJoue).start();
            } else {
                setJ1joue(true);
            }
        } else {
            jeu.carteJouerJoueur2.getPane().getStyleClass().clear();
            jeu.carteJouerJoueur1.getPane().getStyleClass().clear();
            jeu.carteJouerJoueur2.getPane().getStyleClass().add("carte-jouableIA");
            if (p.getCarteJ2() != null) {
                for (int i = 0; i < jeu.arMain1.size(); i++) {
                    jeu.arMain1.get(i).getPane().getChildren().clear();
                }
                for (int i = 0; i < jeu.arMain2.size(); i++) {
                    jeu.arMain2.get(i).getPane().getChildren().clear();
                }
                setupJeuPhase1();
                ArrayList<Carte> arcarte = p.getJ1().getCartesJouable(p.getCarteJ2());
                for (int i = 0; i < p.getJ1().getMain().size(); i++) {
                    if (p.getJ1().isCarteJouable(arcarte, p.getJ1().getMain().get(jeu.refMain1[i]))) {
                        jeu.arMain1.get(i).getPane().getStyleClass().clear();
                        jeu.arMain1.get(i).getPane().getStyleClass().add("carte-jouable");
                    }
                }
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
                if (getPartie() != -2) {

                    if (getPartie() == 1) {
                        fins = "ressources/images/victoire.png";
                    }
                    if (getPartie() == 0) {
                        fins = "ressources/images/defaite.png";
                    }
                    if (getPartie() == -1) {
                        fins = "ressources/images/egalite.jpg";
                    }
                    choixScene = 6;
                    try {
                        afficher();
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(ControllerEnver.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else {
                    if (p.getPhase() == 1) {
                        if (p.getCarteJ2().getFaction() == Faction.MortsVivants) {
                            if (p.getCarteJ1().getFaction() == Faction.MortsVivants) {

                            } else {
                                jeu.Defausse.getPane().getChildren().clear();
                                jeu.Defausse.SetImage((ImageView) jeu.carteJouerJoueur1.getPane().getChildren().get(0));
                            }
                        } else {
                            jeu.Defausse.getPane().getChildren().clear();
                            jeu.Defausse.SetImage((ImageView) jeu.carteJouerJoueur2.getPane().getChildren().get(0));

                        }

                    }

                    jeu.carteJouerJoueur1.getPane().getChildren().clear();
                    ImageView imageSelected1 = creerImageView("ressources/images/CarteJouerJ1.png", tailleCarteX, tailleCarteY);
                    jeu.carteJouerJoueur1.SetImage(imageSelected1);

                    jeu.carteJouerJoueur2.getPane().getChildren().clear();
                    ImageView imageSelected2 = creerImageView("ressources/images/CarteJouerJ2.png", tailleCarteX, tailleCarteY);
                    jeu.carteJouerJoueur2.SetImage(imageSelected2);

                    if (p.getPhase() == 1) {
                        ImageView im1 = creerImageView(p.getCarteEnJeu().getCheminImage(), tailleCarteX, tailleCarteY);
                        ImageView im2 = creerImageView(p.getCarteEnJeuPerdant().getCheminImage(), tailleCarteX, tailleCarteY);
                        ImageView im3 = creerImageView("ressources/images/Dos.png", tailleCarteX, tailleCarteY);
                        setPartie(p.calculPli());
                        System.out.println(getPartie());
                        jeu.PartisanJ1.getPane().getChildren().clear();
                        jeu.PartisanJ2.getPane().getChildren().clear();
                        if (p.getPhase() == 2) {
                            setupJeuPhase2();
                            System.out.println(" passage phase 2");
                        } else {
                            if (p.isJ1Courant()) {
                                jeu.PartisanJ1.SetImage(im1);
                                jeu.PartisanJ2.SetImage(im3);
                            } else {
                                jeu.PartisanJ2.SetImage(im3);
                                jeu.PartisanJ1.SetImage(im2);
                            }
                            p.setCarteEnJeu(p.getPioche().pop());
                            p.setCarteEnJeuPerdant(p.getPioche().pop());
                            ImageView imageSelected3 = creerImageView(p.getCarteEnJeu().getCheminImage(), tailleCarteX, tailleCarteY);
                            jeu.centreCarteAGagner.getPane().getChildren().clear();
                            jeu.centreCarteAGagner.SetImage(imageSelected3);

                        }
                        gestionTour();
                    } else {
                        setPartie(p.calculPli());
                        if (getPartie() != -2) {

                            if (getPartie() == 1) {
                                fins = "ressources/images/victoire.png";
                            }
                            if (getPartie() == 0) {
                                fins = "ressources/images/defaite.png";
                            }
                            if (getPartie() == -1) {
                                fins = "ressources/images/egalite.jpg";
                            }
                            choixScene = 6;
                            try {
                                afficher();
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(ControllerEnver.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            gestionTour();
                        }
                    }
                }
            }
        });
        new Thread(sleeper).start();

    }

    public void setJ1joue(boolean b) {
        J1joue = b;
    }

    public static ImageView creerImageView(String s, int a, int b) {
        Image im = null;
        try {
            im = new Image(new File(s).toURI().toString(), a, b, true, true);
        } catch (Exception e) {
            System.out.println("pas trouver");
        }
        ImageView imageSelected = new ImageView();
        imageSelected.setImage(im);
        return imageSelected;
    }

    public void setupJeuPhase1() {
        for (int i = 0; i < p.getJ1().getMain().size(); i++) {
            jeu.arMain1.get(i).SetImage(creerImageView(p.getJ1().getMain().get(i).getCheminImage(), tailleCarteX, tailleCarteY));
            
            jeu.refMain1[i] = i;
            
            
            
        }
        for (int i = 0; i < p.getJ2().getMain().size(); i++) {
            jeu.arMain2.get(i).SetImage(creerImageView(p.getJ2().getMain().get(i).getCheminImage(), tailleCarteX, tailleCarteY));
            jeu.refMain2[i] = i;
        }
    }

    public void setupJeuPhase2() {
        for (int i = 0; i < p.getJ1().getMain().size(); i++) {
            jeu.centreCarteAGagner.getPane().getChildren().clear();
            jeu.arMain1.get(i).SetImage(creerImageView(p.getJ1().getMain().get(i).getCheminImage(), tailleCarteX, tailleCarteY));
            jeu.refMain1[i] = i;
        }
        for (int i = 0; i < p.getJ2().getMain().size(); i++) {
            jeu.arMain2.get(i).SetImage(creerImageView(p.getJ2().getMain().get(i).getCheminImage(), tailleCarteX, tailleCarteY));
            jeu.refMain2[i] = i;
        }
    }

    public void updateScore() {
        int chevalier = 8;
        int doppleGanger = 10;
        int nain = 10;
        int gobelin = 14;

        Iterator<Carte> it = p.getDefausse().iterator();

        while (it.hasNext()) {
            switch (it.next().getFaction()) {
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

            int chevalierJ2 = p.getNBCartesScore(false, Faction.Chevaliers);
            int doppleGangerJ2 = p.getNBCartesScore(false, Faction.Doppelgangers);
            int nainJ2 = p.getNBCartesScore(false, Faction.Nains);
            int gobelinJ2 = p.getNBCartesScore(false, Faction.Gobelins);
            int mvJ2 = p.getNBCartesScore(false, Faction.MortsVivants);

            int chevalierJ1 = p.getNBCartesScore(true, Faction.Chevaliers);
            int doppleGangerJ1 = p.getNBCartesScore(true, Faction.Doppelgangers);
            int nainJ1 = p.getNBCartesScore(true, Faction.Nains);
            int gobelinJ1 = p.getNBCartesScore(true, Faction.Gobelins);
            int mvJ1 = p.getNBCartesScore(true, Faction.MortsVivants);

            int chevalierB = chevalier - (chevalierJ2 + chevalierJ1);
            int doppleGangerB = doppleGanger - (doppleGangerJ2 + doppleGangerJ1);
            int nainB = nain - (nainJ2 + nainJ1);
            int gobelinB = gobelin - (gobelinJ2 + gobelinJ1);
            int mvB = 10 - (mvJ2 + mvJ1);

            jeu.score.getChildren().clear();

            ///////CHEVALIER/////////
            VBox vbCheval = new VBox();
            Canvas cancheval = new Canvas();
            Pane panecheval = new Pane(cancheval);
            panecheval.getChildren().add(ControllerEnver.creerImageView("ressources/images/IconeCh.png", 40, 40));
            vbCheval.getChildren().add(panecheval);
            double cubeCheval = hauteurScore / chevalier;

            for (int i = 0; i < chevalierJ2; i++) {
                Polygon poly = new Polygon();
                poly.getPoints().addAll(new Double[]{
                    0.0, 0.0,
                    0.0, cubeCheval,
                    largeurScore, cubeCheval,
                    largeurScore, 0.0});
                poly.setFill(Color.RED);
                poly.setStroke(Color.BLACK);
                vbCheval.getChildren().add(poly);
            }
            for (int i = chevalierJ2; i < chevalierJ2 + chevalierB; i++) {
                Polygon poly = new Polygon();
                poly.getPoints().addAll(new Double[]{
                    0.0, 0.0,
                    0.0, cubeCheval,
                    largeurScore, cubeCheval,
                    largeurScore, 0.0});
                poly.setFill(Color.WHITE);
                poly.setStroke(Color.BLACK);
                vbCheval.getChildren().add(poly);
            }
            for (int i = chevalierJ2 + chevalierB; i < chevalierJ2 + chevalierB + chevalierJ1; i++) {
                Polygon poly = new Polygon();
                poly.getPoints().addAll(new Double[]{
                    0.0, 0.0,
                    0.0, cubeCheval,
                    largeurScore, cubeCheval,
                    largeurScore, 0.0});
                poly.setFill(Color.GREEN);
                poly.setStroke(Color.BLACK);
                vbCheval.getChildren().add(poly);
            }
            ////////////

            ////////MortVivant/////////
            VBox vbMv = new VBox();
            Canvas canmv = new Canvas();
            Pane panemv = new Pane(canmv);
            panemv.getChildren().add(ControllerEnver.creerImageView("ressources/images/IconeMv.png", 40, 40));
            vbMv.getChildren().add(panemv);
            double cubeMv = hauteurScore / 10;
            for (int i = 0; i < mvJ2; i++) {
                Polygon poly = new Polygon();
                poly.getPoints().addAll(new Double[]{
                    0.0, 0.0,
                    0.0, cubeMv,
                    largeurScore, cubeMv,
                    largeurScore, 0.0});
                poly.setFill(Color.RED);
                poly.setStroke(Color.BLACK);
                vbMv.getChildren().add(poly);
            }
            for (int i = mvJ2; i < mvJ2 + mvB; i++) {
                Polygon poly = new Polygon();
                poly.getPoints().addAll(new Double[]{
                    0.0, 0.0,
                    0.0, cubeMv,
                    largeurScore, cubeMv,
                    largeurScore, 0.0});
                poly.setFill(Color.WHITE);
                poly.setStroke(Color.BLACK);
                vbMv.getChildren().add(poly);
            }
            for (int i = mvJ2 + mvB; i < mvJ2 + mvB + mvJ1; i++) {
                Polygon poly = new Polygon();
                poly.getPoints().addAll(new Double[]{
                    0.0, 0.0,
                    0.0, cubeMv,
                    largeurScore, cubeMv,
                    largeurScore, 0.0});
                poly.setFill(Color.GREEN);
                poly.setStroke(Color.BLACK);
                vbMv.getChildren().add(poly);
            }
            ///////////////////////

            //////////DoppleGanger///////
            VBox vbDopple = new VBox();
            Canvas candp = new Canvas();
            Pane panedp = new Pane(candp);
            panedp.getChildren().add(ControllerEnver.creerImageView("ressources/images/IconeDp.png", 40, 40));
            vbDopple.getChildren().add(panedp);
            double cubeDopple = hauteurScore / doppleGanger;
            for (int i = 0; i < doppleGangerJ2; i++) {
                Polygon poly = new Polygon();
                poly.getPoints().addAll(new Double[]{
                    0.0, 0.0,
                    0.0, cubeDopple,
                    largeurScore, cubeDopple,
                    largeurScore, 0.0});
                poly.setFill(Color.RED);
                poly.setStroke(Color.BLACK);
                vbDopple.getChildren().add(poly);
            }
            for (int i = doppleGangerJ2; i < doppleGangerJ2 + doppleGangerB; i++) {
                Polygon poly = new Polygon();
                poly.getPoints().addAll(new Double[]{
                    0.0, 0.0,
                    0.0, cubeDopple,
                    largeurScore, cubeDopple,
                    largeurScore, 0.0});
                poly.setFill(Color.WHITE);
                poly.setStroke(Color.BLACK);
                vbDopple.getChildren().add(poly);
            }
            for (int i = doppleGangerJ2 + doppleGangerB; i < doppleGangerJ2 + doppleGangerB + doppleGangerJ1; i++) {
                Polygon poly = new Polygon();
                poly.getPoints().addAll(new Double[]{
                    0.0, 0.0,
                    0.0, cubeDopple,
                    largeurScore, cubeDopple,
                    largeurScore, 0.0});
                poly.setFill(Color.GREEN);
                poly.setStroke(Color.BLACK);
                vbDopple.getChildren().add(poly);
            }
            /////////////////////

            //////////Nain////////////
            VBox vbNain = new VBox();
            Canvas cannain = new Canvas();
            Pane panenain = new Pane(cannain);
            panenain.getChildren().add(ControllerEnver.creerImageView("ressources/images/IconeN.png", 40, 40));
            vbNain.getChildren().add(panenain);
            double cubeNain = hauteurScore / nain;
            for (int i = 0; i < nainJ2; i++) {
                Polygon poly = new Polygon();
                poly.getPoints().addAll(new Double[]{
                    0.0, 0.0,
                    0.0, cubeNain,
                    largeurScore, cubeNain,
                    largeurScore, 0.0});
                poly.setFill(Color.RED);
                poly.setStroke(Color.BLACK);
                vbNain.getChildren().add(poly);
            }
            for (int i = nainJ2; i < nainJ2 + nainB; i++) {
                Polygon poly = new Polygon();
                poly.getPoints().addAll(new Double[]{
                    0.0, 0.0,
                    0.0, cubeNain,
                    largeurScore, cubeNain,
                    largeurScore, 0.0});
                poly.setFill(Color.WHITE);
                poly.setStroke(Color.BLACK);
                vbNain.getChildren().add(poly);
            }
            for (int i = nainJ2 + nainB; i < nainJ2 + nainB + nainJ1; i++) {
                Polygon poly = new Polygon();
                poly.getPoints().addAll(new Double[]{
                    0.0, 0.0,
                    0.0, cubeNain,
                    largeurScore, cubeNain,
                    largeurScore, 0.0});
                poly.setFill(Color.GREEN);
                poly.setStroke(Color.BLACK);
                vbNain.getChildren().add(poly);
            }
            //////////////////////

            ////////////Gobelin///////////
            VBox vbGoblin = new VBox();
            Canvas cangb = new Canvas();
            Pane panegb = new Pane(cangb);
            panegb.getChildren().add(ControllerEnver.creerImageView("ressources/images/IconeGb.png", 40, 40));
            vbGoblin.getChildren().add(panegb);
            double cubeGoblin = hauteurScore / gobelin;
            for (int i = 0; i < gobelinJ2; i++) {
                Polygon poly = new Polygon();
                poly.getPoints().addAll(new Double[]{
                    0.0, 0.0,
                    0.0, cubeGoblin,
                    largeurScore, cubeGoblin,
                    largeurScore, 0.0});
                poly.setFill(Color.RED);
                poly.setStroke(Color.BLACK);
                vbGoblin.getChildren().add(poly);
            }
            for (int i = gobelinJ2; i < gobelinJ2 + gobelinB; i++) {
                Polygon poly = new Polygon();
                poly.getPoints().addAll(new Double[]{
                    0.0, 0.0,
                    0.0, cubeGoblin,
                    largeurScore, cubeGoblin,
                    largeurScore, 0.0});
                poly.setFill(Color.WHITE);
                poly.setStroke(Color.BLACK);
                vbGoblin.getChildren().add(poly);
            }
            for (int i = gobelinJ2 + gobelinB; i < gobelinJ2 + gobelinB + gobelinJ1; i++) {
                Polygon poly = new Polygon();
                poly.getPoints().addAll(new Double[]{
                    0.0, 0.0,
                    0.0, cubeGoblin,
                    largeurScore, cubeGoblin,
                    largeurScore, 0.0});
                poly.setFill(Color.GREEN);
                poly.setStroke(Color.BLACK);
                vbGoblin.getChildren().add(poly);
            }
            ///////////////

            jeu.score.add(vbCheval, 0, 0);
            jeu.score.add(vbMv, 1, 0);
            jeu.score.add(vbDopple, 2, 0);
            jeu.score.add(vbNain, 3, 0);
            jeu.score.add(vbGoblin, 4, 0);

        }

    }

    public void Sauvegarder(Plateau p) {
        String sep = java.io.File.separator;
                String home = System.getProperty("user.home");
                String chemin = home + sep + "SauvegardesClaim";
                if (!Files.exists(Paths.get(chemin))) {
                        new File(chemin).mkdirs();
                        System.out.println("Le dossier " + chemin + " a été créé");   
                }


                String nomFichier = "autoSave";
                chemin = chemin + sep + nomFichier;
                File file = new File(chemin);
                try {
                        file.createNewFile();
                        FileOutputStream fos = new FileOutputStream(chemin);
                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                        oos.writeObject(p);
                        oos.close();
                }
                catch(Exception e) {
                        System.out.println("Impossible de créer ce fichier");
                }
    }

    public Plateau Charger() {
        String sep = java.io.File.separator;
        String home = System.getProperty("user.home");
        String chemin = home + sep + "SauvegardesClaim";
       
        String nomFichier ="autoSave";
        chemin = chemin + sep + nomFichier;
        System.out.println("Le fichier a pour chemin " + chemin);
        Plateau plateauCharge = null;
        try {
            FileInputStream fin = new FileInputStream(chemin);
            ObjectInputStream ois = new ObjectInputStream(fin);
            plateauCharge = (Plateau) ois.readObject();
            ois.close();
        } catch (Exception e) {
            System.out.println("lul");
        }

        return plateauCharge;
    }

    public int getPartie() {
        return partie;
    }

    public void setPartie(int i) {
        this.partie = i;
    }

}
