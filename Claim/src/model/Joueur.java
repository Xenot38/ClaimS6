package model;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class Joueur {
	
	ArrayList<Carte> main;
	ArrayList<Carte> cartesScore;
	ArrayList<Carte> cartesPartisans;
	
	public ArrayList<Carte> getMain() {
		return main;
	}
	public void setMain(ArrayList<Carte> main) {
		this.main = main;
	}
	public ArrayList<Carte> getCartesScore() {
		return cartesScore;
	}
	public void setCartesScore(ArrayList<Carte> cartesScore) {
		this.cartesScore = cartesScore;
	}
	public ArrayList<Carte> getCartesPartisans() {
		return cartesPartisans;
	}
	public void setCartesPartisans(ArrayList<Carte> cartesPartisans) {
		this.cartesPartisans = cartesPartisans;
	}
	
	public Joueur(ArrayList<Carte> mainDebut){// Constructeur en début de partie
		main = mainDebut;
		cartesScore = new ArrayList<Carte>();
		cartesPartisans = new ArrayList<Carte>();
	}
	
	public void gagnerPartisan(Carte c) {
		cartesPartisans.add(c);
	}
	
	public void gagnerCarte(Carte c) {
		cartesScore.add(c);
	}
        
        public Carte choisirCarte(int index){// Si le joueur a gagné il peut choisir n'importe quelle carte
                return main.remove(index);
        }
        
        public Carte choisirCarte(int index, Carte cAdversaire){// Si le joueur a perdu au tour précédent, il doit jouer de la même faction que son adversaire si il le peut, a moins de jouer doppelganger. Si il n'a aucune carte de la faction adverse, alors il peut jouer n'importe quelle carte.
                Carte cChoisie = main.get(index);
                if(cChoisie.getFaction() == cAdversaire.getFaction()||cChoisie.getFaction() == Faction.Doppelgangers){
                        main.remove(index);
                        return cChoisie;
                }else{
                Iterator<Carte> iter = main.iterator(); 
                boolean aFaction = false;
                        while (iter.hasNext()) { 
                                if(iter.next().getFaction() == cAdversaire.getFaction()){
                                        aFaction = true;
                                }
                        } 
                        if(aFaction){
                                return null;
                        }else{
                                main.remove(index);
                                return cChoisie;
                        }
                }
        }
}