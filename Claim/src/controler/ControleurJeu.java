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
                        Iterator<Carte> iter = plateauJeu.getJ1().getMain().iterator();
                        int nbCarte = 0;
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
                        if(plateauJeu.isJ1Courant()){
                                Scanner sc = new Scanner(System.in);
                                System.out.println("Veuillez saisir un nombre :");
                                int str = sc.nextInt();
                                plateauJeu.setCarteJ1(plateauJeu.getJ1().choisirCarte(str));
                                //plateauJeu.setCarteJ1(plateauJeu.getJ1().choisirCarte(0));
                                int index = 0;
                                System.out.println("Vous avez joué " + plateauJeu.getCarteJ1().affichePropCarte());
                                while(plateauJeu.getCarteJ2()==null){
                                        plateauJeu.setCarteJ2(plateauJeu.getJ2().choisirCarte(index,plateauJeu.getCarteJ1()));
                                        index++;
                                        /*if(plateauJeu.getCarteJ2() == null){
                                                System.out.println("Cette carte n'est pas jouable dans cette situation, choisissez en une autre.");
                                        }*/
                                }
                                System.out.println("L'adversaire a joué " + plateauJeu.getCarteJ2().affichePropCarte());

                        }else{
                                plateauJeu.setCarteJ2(plateauJeu.getJ2().choisirCarte(0));
                                System.out.println("L'adversaire a joué " + plateauJeu.getCarteJ2().affichePropCarte());
                                int index = 0;
                                while(plateauJeu.getCarteJ1()==null){
                                        Scanner sc = new Scanner(System.in);
                                        System.out.println("Veuillez saisir un nombre :");
                                        int str = sc.nextInt();
                                        plateauJeu.setCarteJ1(plateauJeu.getJ1().choisirCarte(str,plateauJeu.getCarteJ2()));
                                        //plateauJeu.setCarteJ1(plateauJeu.getJ1().choisirCarte(index,plateauJeu.getCarteJ2()));
                                        index++;
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
