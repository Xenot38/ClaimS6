package model;

public class Coup {

	private Carte carteJ1;
	private Carte carteJ2;
	private Carte carteEnJeu;
	private boolean victoireJ1;
	
	public Coup(Carte c1,Carte c2,boolean vic) {
		carteJ1 = c1;
		carteJ2 = c2;
		carteEnJeu = null;
		victoireJ1 = vic;
	}
	
	public Coup(Carte c1,Carte c2, Carte cej, boolean vic) {
		carteJ1 = c1;
		carteJ2 = c2;
		carteEnJeu = cej;
		victoireJ1 = vic;
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

	public boolean isVictoireJ1() {
		return victoireJ1;
	}

	public void setVictoireJ1(boolean victoireJ1) {
		this.victoireJ1 = victoireJ1;
	}
	
	
}

