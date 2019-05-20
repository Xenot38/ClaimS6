package model;

import java.util.ArrayList;

public class JoueurIADifficile extends JoueurIA{
	
    public JoueurIADifficile(ArrayList<Carte> main, boolean isJ1) {
            super(main, isJ1);
    }

    @Override
    public Boolean winCard(Plateau p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int chooseCardPhase1(Plateau p, Boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int chooseCardPhase2(Plateau p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
