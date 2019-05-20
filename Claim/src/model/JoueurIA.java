package model;

import java.util.ArrayList;

public abstract class JoueurIA extends Joueur{

	public JoueurIA(ArrayList<Carte> mainDebut,boolean isJ1) {
		super(mainDebut, isJ1);
	}
        
        @Override
        public int joue(Plateau p) {
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
                
        public abstract int chooseCardPhase1(Plateau p, Boolean b);
        
        public abstract int chooseCardPhase2(Plateau p);
        

}
