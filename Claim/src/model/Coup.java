package model;

public class Coup {

	private Carte carteJ1;
	private Carte carteJ2;
	private Carte carteEnJeu;
        private Carte carteEnJeuPerdant;
	private boolean victoireJ1;
        private boolean prioJ1;
	
	public Coup(Carte c1,Carte c2,boolean vic, boolean pJ1) {                    //Durant la phase 2, il n'y a pas de carte a gagner, il faut donc un constructeur sans carteEnJeu
		carteJ1 = c1;
		carteJ2 = c2;
		carteEnJeu = null;
                carteEnJeuPerdant = null;
		victoireJ1 = vic;
                prioJ1 = pJ1;
	}
	
	public Coup(Carte c1,Carte c2, Carte cej,Carte cejp, boolean vic, boolean pJ1) {
		carteJ1 = c1;
		carteJ1 = c1;
		carteJ2 = c2;
		carteEnJeu = cej;
                carteEnJeuPerdant = cejp;
		victoireJ1 = vic;
                prioJ1 = pJ1;
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

        public Carte getCarteEnJeuPerdant() {
                return carteEnJeuPerdant;
        }

        public void setCarteEnJeuPerdant(Carte carteEnJeuPerdant) {
                this.carteEnJeuPerdant = carteEnJeuPerdant;
        }

        public boolean isPrioJ1() {
                return prioJ1;
        }

        public void setPrioJ1(boolean prioJ1) {
                this.prioJ1 = prioJ1;
        }
        
	public boolean isVictoireJ1() {
		return victoireJ1;
	}

	public void setVictoireJ1(boolean victoireJ1) {
		this.victoireJ1 = victoireJ1;
	}
		
}

