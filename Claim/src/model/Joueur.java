package model;

import java.util.ArrayList;

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
}
