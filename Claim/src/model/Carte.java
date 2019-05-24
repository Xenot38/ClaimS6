package model;

import java.io.Serializable;

public class Carte implements Serializable{
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
	
	public String affichePropCarte(){               //Renvoie une string contenant les infos de la carte.
                String retour = faction.name();
                retour = retour.concat(" ");
                retour = retour.concat(Integer.toString(force));
                return retour;
        }
        
    public Carte copie(){
        

        Carte cCopie = new Carte(getFaction(), getForce(),getCheminImage());
      
        return cCopie;
    }

    boolean egal(Carte carte) {
        if(carte == null){
            return false;
        }else{
            return this.getFaction() == carte.getFaction() && this.getForce() == carte.getForce();
        }
    }
}
