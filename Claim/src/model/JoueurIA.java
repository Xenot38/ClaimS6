package model;

import java.util.ArrayList;

public abstract class JoueurIA extends Joueur{

	public JoueurIA(ArrayList<Carte> mainDebut) {
		super(mainDebut);
	}
        

        public Carte joue(Plateau p) {
            if(p.getPhase()==1){
            //Phase1
                Boolean winCard = winCard(p);
                return chooseCardPhase1(p, winCard);           
            }else{
            //Phase2
                return chooseCardPhase2(p);   
            }        
        }
	
        public abstract Boolean winCard(Plateau p);
                
        public abstract Carte chooseCardPhase1(Plateau p, Boolean b);
        
        public abstract Carte chooseCardPhase2(Plateau p);
        

}
