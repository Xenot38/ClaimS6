package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Plateau {

	boolean joueurCourant;
	private Joueur j1;
	private Joueur j2;
	private ArrayList<Coup> historique;
	private Carte carteJ1;
	private Carte carteJ2;
	private Carte carteEnJeu;
	private Stack<Carte> pioche;
	private ArrayList<Carte> defausse;
	private ArrayList<Integer> score;
	


	public Plateau(String difficulte) {
		joueurCourant  = true;
		historique = new ArrayList<Coup>();
		defausse = new ArrayList<Carte>();
		score = new ArrayList<Integer>();
		for(int i = 0;i<5;i++) {
			score.add(0);
		}
		carteJ1 = null;
		carteJ2 = null;
		carteEnJeu = null;
		pioche = genereCartes();
		Collections.shuffle(pioche);
		ArrayList<Carte> mainTemp = new ArrayList<Carte>();
		for (int i = 0;i<13; i++) {
			mainTemp.add(pioche.pop());
		}
		j1 = new JoueurHumain(mainTemp);
		mainTemp.clear();
		for (int i = 0;i<13; i++) {
			mainTemp.add(pioche.pop());
		}
		switch(difficulte){
		case "facile":
			j2 = new JoueurIAFacile(mainTemp);
			break;
		case "moyen":
			j2 = new JoueurIAMoyen(mainTemp);
			break;
		case "difficile":
			j2 = new JoueurIADifficile(mainTemp);
			break;
		}		
	}
	
	public boolean isJoueurCourant() {
		return joueurCourant;
	}

	public void setJoueurCourant(boolean joueurCourant) {
		this.joueurCourant = joueurCourant;
	}
	
	public Joueur getJ1() {
		return j1;
	}
	
	public void setJ1(Joueur j1) {
		this.j1 = j1;
	}
	
	public Joueur getJ2() {
		return j2;
	}
	
	public void setJ2(Joueur j2) {
		this.j2 = j2;
	}
	
	public ArrayList<Coup> getHistorique() {
		return historique;
	}
	
	public void setHistorique(ArrayList<Coup> historique) {
		this.historique = historique;
	}
	
	public Carte getCarteJ1() {
		return carteJ1;
	}
	
	public void setCarteJ1(Carte carteJ1) {
		this.carteJ1 = carteJ1;
	}
	
	public Carte getCarteJ2() {
		return carteJ2;
	}
	
	public void setCarteJ2(Carte carteJ2) {
		this.carteJ2 = carteJ2;
	}
	
	public Carte getCarteEnJeu() {
		return carteEnJeu;
	}
	
	public void setCarteEnJeu(Carte carteEnJeu) {
		this.carteEnJeu = carteEnJeu;
	}
	
	public ArrayList<Integer> getScore() {
		return score;
	}
	public void setScore(ArrayList<Integer> score) {
		this.score = score;
	}

	public Stack<Carte> getPioche() {
		return pioche;
	}

	public void setPioche(Stack<Carte> pioche) {
		this.pioche = pioche;
	}

	public ArrayList<Carte> getDefausse() {
		return defausse;
	}

	public void setDefausse(ArrayList<Carte> defausse) {
		this.defausse = defausse;
	}
	
	public Stack<Carte> genereCartes(){
		Stack<Carte> cartes = new Stack<Carte>();
		return cartes;
	}
	
}
