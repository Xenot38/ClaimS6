package model;

import model.Plateau;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class JoueurIAMoyen  extends JoueurIA{
    
    private ArrayList<Carte> cartes;
    private ArrayList<ArrayList<Integer>> grilleMatchUp;
	
    public JoueurIAMoyen(ArrayList<Carte> main, ArrayList<Carte> pioche) {
            super(main);
            cartes = pioche;
            initGrille();
            creerGrille(cartes);
            afficheGrille();
    }
    
    @Override
        public int joue(Plateau p) {
            updateGrille(p);
            if(p.getPhase()==1){
            //Phase1
                Boolean winCard = winCard(p);
                return chooseCardPhase1(p, winCard);           
            }else{
            //Phase2
                return chooseCardPhase2(p);   
            }        
        }

    @Override
    public Boolean winCard(Plateau p) {
        return p.getCarteEnJeu().getForce() > 4;
    }

    @Override
    public int chooseCardPhase1(Plateau p, Boolean b) {
        
        int indice;
        
        // on récupère notre main
        ArrayList<Carte> main = (ArrayList<Carte>)p.getJ2().getMain().clone();
                
        //Si on veut la carte
        if (b){
            
            //Si on est le leader
            if (!p.isJ1Courant()){
                
                //on recupere la carte de plus forte puissance
                //si plusieurs égales, on choisis aléatoirement parmi celles-ci
                indice = getindex(getCarteMaxForce(main));
                
            //Si on est le deuxième joueur
            }else{
                Carte carteJ1 = p.getCarteJ1();
                ArrayList<Carte> cartesJouable = p.getJ2().getCartesJouable(carteJ1);
                ArrayList<Carte> cartesGagnante = p.getJ2().getCartesGagnante(carteJ1);
                
                //si on peut gagner
                if(!cartesGagnante.isEmpty()){
                    indice = getindex(getCarteMinForce(cartesGagnante));
                //si on ne peut que perdre
                }else{
                    indice = getindex(getCarteMinForce(cartesJouable));
                }
            }
            
        //Si on ne veut pas la carte
        }else{
            //Si on est le leader
            if (!p.isJ1Courant()){
                indice = getindex(getCarteMinForce(main));
            //Si on est le deuxième joueur
            }else{
                Carte carteJ1 = p.getCarteJ1();
                ArrayList<Carte> cartesJouable = p.getJ2().getCartesJouable(carteJ1);
                ArrayList<Carte> cartesPerdante = p.getJ2().getCartesPerdante(carteJ1);
            
                //si on peut perdre
                if(!cartesPerdante.isEmpty()){
                    indice =  getindex(getCarteMinForce(cartesPerdante));
                //si on ne peut que gagner
                }else{
                    indice = getindex(getCarteMinForce(cartesJouable));
                }
            }
        }
        return indice;
        
    }

    @Override
    public int chooseCardPhase2(Plateau p) {
        
        // on récupère notre main
        ArrayList<Carte> main = (ArrayList<Carte>)p.getJ2().getMain().clone();
         
        //si on est le leader
        if(!p.isJ1Courant()){
            return getindex(getCarteMaxForce(main));
            
        //si on est deuxième joueur
        }else{
            
            Carte carteJ1 = p.getCarteJ1();
            ArrayList<Carte> cartesJouable = p.getJ2().getCartesJouable(carteJ1);
            ArrayList<Carte> cartesGagnante = p.getJ2().getCartesGagnante(carteJ1);
                            
            //si on peut gagner
            if(!cartesGagnante.isEmpty()){
                return getindex(getCarteMinForce(cartesGagnante));
            //si on ne peut que perdre
            }else{
                return getindex(getCarteMinForce(cartesJouable));
            }
        }
    }
    
    public int getindex(Carte carteChoisis){
        int indice = -1;
        int i = 0;
        Iterator<Carte> it = getMain().iterator();
        while(indice == -1 && it.hasNext()){
            Carte c = it.next();
            if(c.getFaction() == carteChoisis.getFaction() && c.getForce() == carteChoisis.getForce()){
                indice = i;
            }
            i++;
        }
        
        return indice;
    }
    
    public Carte getCarteMaxForce(ArrayList<Carte> main){
        Iterator<Carte> it = main.iterator();
        // on créé l'array pour garder la ou les cartes maxs
        ArrayList<Carte> maxs = new ArrayList();
        maxs.add(main.get(0));
        it.next();

        // on parcours toutes les cartes pour trouver celles de plus grandes valeurs
        while(it.hasNext()){
            Carte c = it.next();
            if(c.getForce() > maxs.get(0).getForce()){
                maxs.clear();
                maxs.add(c);
            }else if (c.getForce() == maxs.get(0).getForce()){
                maxs.add(c);
            }
        }
        Random r = new Random();
        int i = r.nextInt(maxs.size());
        return maxs.get(i); 
    }       

    public Carte getRandomCarte(ArrayList<Carte> cartes) {
        Random r = new Random();
        int i = r.nextInt(cartes.size());
        return cartes.get(i);
    }

    public Carte getCarteMinForce(ArrayList<Carte> main) {
        Iterator it = main.iterator();
        // on créé l'array pour garder la ou les cartes maxs
        ArrayList<Carte> maxs = new ArrayList();
        maxs.add(main.get(0));
        it.next();

        // on parcours toutes les cartes pour trouver celles de plus grandes valeurs
        while(it.hasNext()){
            Carte c = (Carte) it.next();
            if(c.getForce() < maxs.get(0).getForce()){
                maxs.clear();
                maxs.add(c);
            }else if (c.getForce() == maxs.get(0).getForce()){
                maxs.add(c);
            }
        }
        Random r = new Random();
        int i = r.nextInt(maxs.size());
        return maxs.get(i); 
    }
    
    private void updateGrille(Plateau p) {
        
    }

    private void creerGrille(ArrayList<Carte> cartes) {
        for (int i = 0; i < cartes.size(); i++) {
            for (int j = 0; j < cartes.size(); j++) {
                grilleMatchUp.get(i).add(gagnePli(cartes.get(i),cartes.get(j)));
            }
        }
    }

    private Integer gagnePli(Carte c1, Carte c2) {
        if (c1.getFaction() == c2.getFaction() || c2.getFaction() == Faction.Doppelgangers) {
                if (c1.getForce() >= c2.getForce()) {
                        return 1;
                } else {
                        return 0;
                }
        } else {
                if (c1.getFaction() == Faction.Gobelins && c2.getFaction() == Faction.Chevaliers) {
                        return 0;
                } else {
                        return 1;
                }
        }
    }

    private void initGrille() {
        grilleMatchUp = new ArrayList();
        for (int i = 0; i < cartes.size(); i++) {
            grilleMatchUp.add(new ArrayList<Integer>());
        }
    }       

    private void afficheGrille() {
        for (int i = 0; i < cartes.size(); i++) {
           for (int j = 0; j < cartes.size(); j++) {
                System.out.print(grilleMatchUp.get(i).get(j) + " ");
            } 
            System.out.println("\n");
        }
    }
}
