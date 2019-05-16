/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import model.*;
/**
 *
 * @author baratinm
 */
public class ControleurJeu {
        Plateau plateauJeu;
        
        public ControleurJeu() {
                this.plateauJeu = new Plateau("facile");
        }
        
        public void lancerJeu() {
       
                while(!plateauJeu.isFini()){
                        System.out.println("----------------------------------------------------");
                        //int i =0;
                        System.out.println("[Main joueur 1]");
                        Iterator<Carte> iter = plateauJeu.getJ1().getMain().iterator();
                        int nbCarte = 0;
                        while(iter.hasNext()){
                                Carte maCarte = iter.next();
                                System.out.println(nbCarte + ": " + maCarte.affichePropCarte());
                                nbCarte++;
                        } 
                        /*System.out.println("\n\n[Main joueur 2]");
                        iter = plateauJeu.getJ2().getMain().iterator();
                        nbCarte = 0;
                        while(iter.hasNext()){
                                Carte maCarte = iter.next();
                                System.out.println(nbCarte + ": " + maCarte.affichePropCarte());
                                nbCarte++;
                        }*/
                        if(plateauJeu.getPhase() == 1){
                            System.out.println("\n\n[Pile Partisans]");
                            iter = plateauJeu.getJ1().getCartesPartisans().iterator();
                            while(iter.hasNext()){
                                    Carte maCarte = iter.next();
                                    System.out.println(": " + maCarte.affichePropCarte());
                            }
                        }
                        
                        System.out.println("\n\n[Pile Score Joueur 1]");
                        iter = plateauJeu.getJ1().getCartesScore().iterator();
                        while(iter.hasNext()){
                                Carte maCarte = iter.next();
                                System.out.println(": " + maCarte.affichePropCarte());
                        }
                        
                        System.out.println("\n\n[Pile Score Joueur 2(IA)]");
                        iter = plateauJeu.getJ2().getCartesScore().iterator();
                        while(iter.hasNext()){
                                Carte maCarte = iter.next();
                                System.out.println(": " + maCarte.affichePropCarte());
                        }
                        System.out.println("\n\n");
                        
                        System.out.println("Annuler le coup : A | Refaire le dernier coup : R | Sauvegarder : S | Charger : C | Autre : rien");
                        Scanner scanTemp = new Scanner(System.in);
                        String yn = scanTemp.nextLine();
                        //System.out.println(yn);
                        if(yn.equals("A")){
                                plateauJeu.annuler();
                        }else if(yn.equals("R")){
                                plateauJeu.refaire();
                        }else if(yn.equals("S")){
                                Sauvegarder(plateauJeu);
                        }else if(yn.equals("C")){
                                plateauJeu = Charger();
                        }else{
                                if(plateauJeu.getPhase() == 1){
                                        if(plateauJeu.getCarteEnJeu()==null){
                                                plateauJeu.setCarteEnJeu(plateauJeu.getPioche().pop());
                                                plateauJeu.setCarteEnJeuPerdant(plateauJeu.getPioche().pop());
                                        }
                                        System.out.println("La carte en jeu est : " + plateauJeu.getCarteEnJeu().affichePropCarte());
                                        System.out.println("La carte pour le perdant est : " + plateauJeu.getCarteEnJeuPerdant().affichePropCarte());
                                }
                                //System.out.println("entrez l'index de la carte a jouer (0-indexé)");
                                if(plateauJeu.isJ1Courant()){           //Si le J1 a la main, il joue en premier et les choix du J2 sont restreints en conséquence.
                                        plateauJeu.setCarteJ1(plateauJeu.getJ1().choisirCarte(plateauJeu.getJ1().joue(plateauJeu)));
                                        System.out.println("Vous avez joué " + plateauJeu.getCarteJ1().affichePropCarte());
                                        plateauJeu.setCarteJ2(plateauJeu.getJ2().choisirCarte(plateauJeu.getJ2().joue(plateauJeu),plateauJeu.getCarteJ1()));
                                        System.out.println("L'adversaire a joué " + plateauJeu.getCarteJ2().affichePropCarte());

                                }else{
                                        plateauJeu.setCarteJ2(plateauJeu.getJ2().choisirCarte(plateauJeu.getJ2().joue(plateauJeu)));
                                        System.out.println("L'adversaire a joué " + plateauJeu.getCarteJ2().affichePropCarte());
                                        plateauJeu.setCarteJ1(plateauJeu.getJ1().choisirCarte(plateauJeu.getJ1().joue(plateauJeu)));
                                        System.out.println("Vous avez joué " + plateauJeu.getCarteJ1().affichePropCarte());
                                }
                                plateauJeu.calculPli();
                        }
                }
        } 
        public void Sauvegarder(Plateau p){
                String sep = java.io.File.separator;
                String home = System.getProperty("user.home");
                String chemin = home + sep + "SauvegardesClaim";
                if (!Files.exists(Paths.get(chemin))) {
                        new File(chemin).mkdirs();
                        System.out.println("Le dossier " + chemin + " a été créé");   
                }

                Scanner sc = new Scanner(System.in);
                System.out.println("Choisissez le nom du fichier sauvegardé");
                String nomFichier = sc.nextLine();
                chemin = chemin + sep + nomFichier;
                System.out.println("Le fichier a pour chemin " + chemin);
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
        
        public Plateau Charger(){
                String sep = java.io.File.separator;
                String home = System.getProperty("user.home");
                String chemin = home + sep + "SauvegardesClaim";
                Scanner sc = new Scanner(System.in);
                System.out.println("Choisissez le nom du fichier a charger");
                String nomFichier = sc.nextLine();
                chemin = chemin + sep + nomFichier;
                System.out.println("Le fichier a pour chemin " + chemin);
                Plateau plateauCharge = null;
                try{
                        FileInputStream fin = new FileInputStream(chemin);
                        ObjectInputStream ois = new ObjectInputStream(fin);
                        plateauCharge = (Plateau) ois.readObject();
                        ois.close();
                }
                catch(Exception e){
                        System.out.println("lul");
                }
                
                return plateauCharge;
        }
}
