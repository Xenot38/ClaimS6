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
                        while(iter.hasNext()){
                                Carte maCarte = iter.next();
                                System.out.println(maCarte.affichePropCarte());
                        }
                        if(plateauJeu.getPhase() == 1){
                                plateauJeu.setCarteEnJeu(plateauJeu.getPioche().pop());
                                System.out.println("La carte en jeu est : " + plateauJeu.getCarteEnJeu().affichePropCarte());
                        }
                        //System.out.println("entrez l'index de la carte a jouer (0-indexé)");
                        plateauJeu.setCarteJ1(plateauJeu.getJ1().choisirCarte(0));
                        plateauJeu.setCarteJ2(plateauJeu.getJ2().choisirCarte(0));
                        System.out.println("Vous avez joué " + plateauJeu.getCarteJ1().affichePropCarte());
                        System.out.println("L'adversaire a joué " + plateauJeu.getCarteJ2().affichePropCarte());
                        plateauJeu.calculPli();
                }
        }      
}
