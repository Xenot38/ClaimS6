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

    public ArrayList<Carte> getCartesJouable(Carte carteJ1) {
        Iterator<Carte> it = getMain().iterator();
        ArrayList<Carte> carteJouables = new ArrayList();

        while(it.hasNext()){
            Carte c = it.next();
            //la carte est jouable si les fac sont compatibles
            if(c.getFaction() == carteJ1.getFaction() || c.getFaction() == Faction.Doppelgangers){
                carteJouables.add(c);
            }
        }
        //on return toutes les cartes de notre main si aucune de nos cartes n'est compatible
        if(carteJouables.isEmpty()){
            return getMain();
        }else{
            return carteJouables;
        }
    }

    public ArrayList<Carte> getCartesGagnante(Carte carteJ1) {
        Iterator<Carte> it = getMain().iterator();
        ArrayList<Carte> carteJouables = new ArrayList();
        ArrayList<Carte> autresCartes = new ArrayList();
        ArrayList<Carte> cartesGagnantes = new ArrayList();
        ArrayList<Carte> cartesChevalier = new ArrayList();
        
        while(it.hasNext()){
            Carte c = it.next();
            //si notre carte est de la même fac ou est un doppel, elle est jouable
            if(c.getFaction() == carteJ1.getFaction() || c.getFaction() == Faction.Doppelgangers){
                carteJouables.add(c);
                //si sa force est supérieure , elle est gagnante
                if(c.getForce() > carteJ1.getForce()){
                    cartesGagnantes.add(c);
                }
            }else{
                //si la fac n'est pas compatible mais que l'adversaire est un gobelin
                // et que l'on a un chevalier, on l'ajoute
                if(carteJ1.getFaction() == Faction.Gobelins && c.getFaction() == Faction.Chevaliers){
                    cartesChevalier.add(c);
                }
                autresCartes.add(c);                
            }
        }
        // si on a aucune carte gagnante et/ou jouable, on return les chevaliers
        // ( qui sont soit vide soit pleins si gobelin)
        if (cartesGagnantes.isEmpty() || carteJouables.isEmpty()) {
             return cartesChevalier;
        }else{
            //on vérifie si nos cartes gagnantes ne sont que des doppel
            Boolean mainCompleteDoppel = true;
            it = cartesGagnantes.iterator();
            while(mainCompleteDoppel && it.hasNext()){
                Carte c = it.next();
                if(c.getFaction() != Faction.Doppelgangers){
                    mainCompleteDoppel = false;
                }
            }
            //si c'est la cas, on ajoute aux cartes gagnantes les cartes chevalier 
            if(mainCompleteDoppel){
                cartesGagnantes.addAll(cartesChevalier);
            }
            return cartesGagnantes;
        }
    }

    public ArrayList<Carte> getCartesPerdante(Carte carteJ1) {
         Iterator<Carte> it = getMain().iterator();
        ArrayList<Carte> carteJouables = new ArrayList();
        ArrayList<Carte> autresCartes = new ArrayList();
        ArrayList<Carte> cartesPerdantes = new ArrayList();
        
        while(it.hasNext()){
            Carte c = it.next();
            //si notre carte est de la même fac ou est un doppel, elle est jouable
            if(c.getFaction() == carteJ1.getFaction() || c.getFaction() == Faction.Doppelgangers){
                carteJouables.add(c);
                //si sa force est inférieure , elle est perdante
                if(c.getForce() < carteJ1.getForce()){
                    cartesPerdantes.add(c);
                }
            }else{
                //si on est pas dans le cas gobelin(Leader) et chevalier(2eme)
                if(!(carteJ1.getFaction() == Faction.Gobelins && c.getFaction() == Faction.Chevaliers)){
                    autresCartes.add(c);                
                }
            }
        }
        
        
        // si on a aucune carte perdante et/ou jouable, on return les autresCartes
        // qui sont les cartes non compatibles avec la faction( qui sont donc perdante )
        if (cartesPerdantes.isEmpty() || carteJouables.isEmpty() ) {
            return autresCartes;
        }else{
            //on vérifie si nos cartesPerdantes ne sont que des doppel
            Boolean mainCompleteDoppel = true;
            it = cartesPerdantes.iterator();
            while(mainCompleteDoppel && it.hasNext()){
                Carte c = it.next();
                if(c.getFaction() != Faction.Doppelgangers){
                    mainCompleteDoppel = false;
                }
            }
            //si oui on ajoute toutes les cartes des autres factions
            if(mainCompleteDoppel){
                cartesPerdantes.addAll(autresCartes);
            }
            return cartesPerdantes;
        }
    }
    
    public int joue(Plateau p) {
        return 0;
    }

}
