/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controler;

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
        
        public void lancerJeu(){
       
                while(!plateauJeu.isFini()){
                        System.out.println("----------------------------------------------------");
                        int i =0;
                        
                        System.out.println("[Main joueur 1]");
                        Iterator<Carte> iter = plateauJeu.getJ1().getMain().iterator();
                        int nbCarte = 0;
                        while(iter.hasNext()){
                                Carte maCarte = iter.next();
                                System.out.println(nbCarte + ": " + maCarte.affichePropCarte());
                                nbCarte++;
                        }
                        
                        System.out.println("\n\n[Main joueur 2]");
                        iter = plateauJeu.getJ2().getMain().iterator();
                        nbCarte = 0;
                        while(iter.hasNext()){
                                Carte maCarte = iter.next();
                                System.out.println(nbCarte + ": " + maCarte.affichePropCarte());
                                nbCarte++;
                        }
                        if(plateauJeu.getPhase() == 1){
                                plateauJeu.setCarteEnJeu(plateauJeu.getPioche().pop());
                                System.out.println("La carte en jeu est : " + plateauJeu.getCarteEnJeu().affichePropCarte());
                        }
                        //System.out.println("entrez l'index de la carte a jouer (0-indexé)");
                        if(plateauJeu.isJ1Courant()){           //Si le J1 a la main, il joue en premier et les choix du J2 sont restreints en conséquence.
                                Scanner sc = new Scanner(System.in);
                                System.out.println("Veuillez saisir un nombre :");
                                int str = sc.nextInt();
                                plateauJeu.setCarteJ1(plateauJeu.getJ1().choisirCarte(str));
                                System.out.println("Vous avez joué " + plateauJeu.getCarteJ1().affichePropCarte());
                                plateauJeu.setCarteJ2(plateauJeu.getJ2().choisirCarte(plateauJeu.getJ2().joue(plateauJeu),plateauJeu.getCarteJ1()));
                                System.out.println("L'adversaire a joué " + plateauJeu.getCarteJ2().affichePropCarte());

                        }else{
                                plateauJeu.setCarteJ2(plateauJeu.getJ2().choisirCarte(plateauJeu.getJ2().joue(plateauJeu)));
                                System.out.println("L'adversaire a joué " + plateauJeu.getCarteJ2().affichePropCarte());
                                while(plateauJeu.getCarteJ1()==null){//Si le J2 a la main, il joue en premier et les choix du J1 tant que l'utilisateur n'a pas choisi une carte viable, on lui redemande de choisir une carte.
                                        Scanner sc = new Scanner(System.in);
                                        System.out.println("Veuillez saisir un nombre :");
                                        int str = sc.nextInt();
                                        plateauJeu.setCarteJ1(plateauJeu.getJ1().choisirCarte(str,plateauJeu.getCarteJ2()));
                                        if(plateauJeu.getCarteJ1() == null){
                                                System.out.println("Cette carte n'est pas jouable dans cette situation, choisissez en une autre.");
                                        }
                                }
                                System.out.println("Vous avez joué " + plateauJeu.getCarteJ1().affichePropCarte());
                        }
                        plateauJeu.calculPli();
                }
        }      
}
