package model;

public class Carte {
	private Faction faction;
	private int force;
	private String cheminImage;
	
	public Carte(Faction fac,int f,String chemin){
		faction = fac;
		force = f;
		cheminImage = chemin; 
	}

	public Faction getFaction() {
		return faction;
	}

	public void setFaction(Faction faction) {
		this.faction = faction;
	}

	public int getForce() {
		return force;
	}

	public void setForce(int force) {
		this.force = force;
	}

	public String getCheminImage() {
		return cheminImage;
	}

	public void setCheminImage(String cheminImage) {
		this.cheminImage = cheminImage;
	}
	
	
}
