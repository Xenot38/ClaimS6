package model;

import java.util.ArrayList;

public class JoueurIADifficile extends JoueurIA{
	
    public JoueurIADifficile(ArrayList<Carte> main) {
            super(main);
    }

    @Override
    public Carte joue(Plateau p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean winCard(Plateau p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Carte chooseCardPhase1(Plateau p, Boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Carte chooseCardPhase2(Plateau p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
