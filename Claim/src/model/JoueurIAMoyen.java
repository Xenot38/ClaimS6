package model;

import java.util.ArrayList;

public class JoueurIAMoyen extends JoueurIA{
    
    ArrayList<ArrayList<Integer>> grilleLeader;
    ArrayList<ArrayList<Integer>> grille2emeJoueur;

    public JoueurIAMoyen(ArrayList<Carte>main) {
            super(main);
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
