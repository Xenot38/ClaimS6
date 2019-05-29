package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class JoueurIAFacile  extends JoueurIA{
	
    public JoueurIAFacile(ArrayList<Carte> main, boolean isJ1) {
            super(main, isJ1);
            
    }
    
    public String getDifficulte(){
        return "facile";
    }
    
    @Override
    public JoueurIAFacile copie(){
        
        ArrayList<Carte> mainCopie = (ArrayList<Carte>) main.clone();
        ArrayList<Carte> cartesScore = (ArrayList<Carte>) this.cartesScore.clone();
        boolean isJ1 = this.isJ1 == true;
        
        JoueurIAFacile jCopie = new JoueurIAFacile(this.main, this.isJ1);
        jCopie.setMain(mainCopie);
        jCopie.setCartesScore(cartesScore);
        jCopie.setIsJ1(isJ1);
        
        return jCopie;
    }

    @Override
    public Boolean winCard(Plateau p) {
        return p.getCarteEnJeu().getForce() > 4;
    }

    @Override
    public int chooseCardPhase1(Plateau p, Boolean b) {
        
        int indice;
        
        
        //Si on veut la carte
        if (b){
            
            //si on est le joueur 2
            if(!getIsJ1()){
                
                // on récupère notre main
                ArrayList<Carte> main = (ArrayList<Carte>)p.getJ2().getMain().clone();
                
                
                //Si on est le leader
                if (!p.isJ1Courant()){
                    System.out.println("Leader et veut la carte");
                    //on recupere la carte de plus forte puissance
                    //si plusieurs égales, on choisis aléatoirement parmi celles-ci
                    indice = getindex(getCarteMaxForce(main));
                //Si on est le deuxième joueur
                }else{
                    System.out.println("Pas Leader et veut la carte");
                    Carte carteJ1 = p.getCarteJ1();
                    ArrayList<Carte> cartesJouable = p.getJ2().getCartesJouable(carteJ1);
                    ArrayList<Carte> cartesGagnante = p.getJ2().getCartesGagnante(carteJ1);

                    //si on peut gagner
                    if(!cartesGagnante.isEmpty()){
                    System.out.println("carte gagnantes pas empty");
                        indice = getindex(getCarteMinForce(cartesGagnante));
                    //si on ne peut que perdre
                    }else{
                        System.out.println("carte gagnantes empty");
                        indice = getindex(getCarteMinForce(cartesJouable));
                    }
                }
            }else{
                //si on est le joueur 1
                // on récupère notre main
                ArrayList<Carte> main = (ArrayList<Carte>)p.getJ1().getMain().clone();
                //Si on est le leader
                if (p.isJ1Courant()){
                    System.out.println("Leader et veut la carte");
                    //on recupere la carte de plus forte puissance
                    //si plusieurs égales, on choisis aléatoirement parmi celles-ci
                    indice = getindex(getCarteMaxForce(main));
                //Si on est le deuxième joueur
                }else{
                    System.out.println("Pas Leader et veut la carte");
                    Carte carteJ2 = p.getCarteJ2();
                    ArrayList<Carte> cartesJouable = p.getJ1().getCartesJouable(carteJ2);
                    ArrayList<Carte> cartesGagnante = p.getJ1().getCartesGagnante(carteJ2);

                    //si on peut gagner
                    if(!cartesGagnante.isEmpty()){
                    System.out.println("carte gagnantes pas empty");
                        indice = getindex(getRandomCarte(cartesGagnante));
                    //si on ne peut que perdre
                    }else{
                        System.out.println("carte gagnantes empty");
                        indice = getindex(getRandomCarte(cartesJouable));
                    }
                }
            }
        //Si on ne veut pas la carte
        }else{
            //si on est le joueur 2
            if(!getIsJ1()){
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
                        indice =  getindex(getRandomCarte(cartesPerdante));
                    //si on ne peut que gagner
                    }else{
                        indice = getindex(getRandomCarte(cartesJouable));
                    }
                }
            }else{
                //si on est le joueur 1
                //Si on est le leader
                if (p.isJ1Courant()){
                    indice = getindex(getCarteMinForce(main));
                //Si on est le deuxième joueur
                }else{
                    Carte carteJ2 = p.getCarteJ2();
                    ArrayList<Carte> cartesJouable = p.getJ1().getCartesJouable(carteJ2);
                    ArrayList<Carte> cartesPerdante = p.getJ1().getCartesPerdante(carteJ2);

                    //si on peut perdre
                    if(!cartesPerdante.isEmpty()){
                        indice =  getindex(getRandomCarte(cartesPerdante));
                    //si on ne peut que gagner
                    }else{
                        indice = getindex(getRandomCarte(cartesJouable));
                    }
                }
            }
        }
        return indice;
        
    }

    @Override
    public int chooseCardPhase2(Plateau p) {
        
        //si on est le joueur 2
        if(!getIsJ1()){
            
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
                    return getindex(getRandomCarte(cartesGagnante));
                //si on ne peut que perdre
                }else{
                    return getindex(getRandomCarte(cartesJouable));
                }
            }
        }else{
            
            // on récupère notre main
            ArrayList<Carte> main = (ArrayList<Carte>)p.getJ1().getMain().clone();
            
            //si on est joueur 1
            //si on est le leader
            if(p.isJ1Courant()){
                return getindex(getCarteMaxForce(main));

            //si on est deuxième joueur
            }else{

                Carte carteJ2 = p.getCarteJ2();
                ArrayList<Carte> cartesJouable = p.getJ1().getCartesJouable(carteJ2);
                ArrayList<Carte> cartesGagnante = p.getJ1().getCartesGagnante(carteJ2);

                //si on peut gagner
                if(!cartesGagnante.isEmpty()){
                    return getindex(getRandomCarte(cartesGagnante));
                //si on ne peut que perdre
                }else{
                    return getindex(getRandomCarte(cartesJouable));
                }
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
    
    @Override
    public boolean egal(Joueur j) {
        boolean mainEgale = true;
        boolean pileScoreEgale = true;
        //main
        int i = 0;
        Iterator<Carte> it = getMain().iterator();
        while (it.hasNext() && mainEgale) {
            Carte c = it.next();
            if(c.getFaction() != j.getMain().get(i).getFaction() || c.getForce()!= j.getMain().get(i).getForce()){
               mainEgale = false;
            }
            i++;
        }
        //score
        if(mainEgale){
            Iterator<Carte> it2 = getCartesScore().iterator();
            while (it.hasNext() && mainEgale) {
                Carte c = it.next();
                if(c.getFaction() != j.getMain().get(i).getFaction() || c.getForce()!= j.getCartesScore().get(i).getForce()){
                   pileScoreEgale = false;
                }
                i++;
            }
            if(pileScoreEgale){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
}
