package model;

import model.Plateau;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

public class JoueurIAMoyen extends JoueurIA {

    private ArrayList<Carte> cartes;
    private ArrayList<Carte> cartesRetirees;
    //private ArrayList<Boolean> J1HasFactions;
    private ArrayList<ArrayList<Integer>> grilleMatchUp;
    private ArrayList<Integer> coefsFactions;
    /*
    Nains : 0 
    Chevaliers : 1
    Doppelgangers : 2
    Gobelins : 3
    MortsVivants : 4
    */

    public JoueurIAMoyen(ArrayList<Carte> main, boolean isJ1, ArrayList<Carte> pioche) {
        super(main, isJ1);
        cartes = pioche;
        initGrille();
        cartesRetirees = new ArrayList();
        creerGrille(cartes);
        coefsFactions = new ArrayList(Arrays.asList(1,1,1,1,1));

        //afficheGrille();
        /*J1HasFactions = new ArrayList();
        for (int i = 0; i < 5; i++) {
            J1HasFactions.add(true);
        } */
    }

    @Override
    public int joue(Plateau p) {

        ArrayList<Integer> arr = new ArrayList();
        arr.add(1);
        arr.add(1);
        arr.add(1);

        int indice;
        if (p.getPhase() == 1) {
            //afficherCartes();
            updateGrille(p);
            //afficheGrille();
            //Phase1
            Boolean winCard = winCard(p);
            if (winCard) {
                System.out.println("je veux la carte");
            } else {
                System.out.println("je ne veux pas la carte");
            }
            indice = chooseCardPhase1(p, winCard);
            System.out.println("Indice: " + indice);
            return indice;
        } else {
            //Phase2
            indice = chooseCardPhase2(p);
            System.out.println("Indice: " + indice);
            return indice;
        }
    }

    @Override
    public Boolean winCard(Plateau p) {
        //calcul win rate de la carte en jeu;
        Carte c = p.getCarteEnJeu();
        int score = getScore(c);
        System.out.println("Score Carte en jeu: " + score);
        boolean w = wantCardMediane(score);
        if (c.getFaction() != Faction.Nains) {
            return w;
        } else {
            return !w;
        }
    }

    @Override
    public int chooseCardPhase1(Plateau p, Boolean b) {

        int indice;

        //Si on veut la carte
        if (b) {

            //si on est le joueur 2
            if (!getIsJ1()) {
                // on récupère notre main
                ArrayList<Carte> main = (ArrayList<Carte>) p.getJ2().getMain().clone();
                //Si on est le leader
                if (!p.isJ1Courant()) {

                    indice = getMaxUndead(main);
                    if (indice == -1) {
                        indice = getIndexMaxScore(main);
                    }

                    //Si on est le deuxième joueur
                } else {
                    Carte carteJ1 = p.getCarteJ1();
                    ArrayList<Carte> cartesJouable = p.getJ2().getCartesJouable(carteJ1);
                    ArrayList<Carte> cartesGagnante = p.getJ2().getCartesGagnante(carteJ1);

                    //si on peut gagner
                    if (!cartesGagnante.isEmpty()) {
                        System.out.println("test");
                        indice = getindex(getCarteMinForce(cartesGagnante));
                        //si on ne peut que perdre
                    } else {
                        System.out.println("test2");
                        indice = getindex(getCarteMinForce(cartesJouable));
                    }
                }
            } else {
                //si on est le joueur 1
                // on récupère notre main
                ArrayList<Carte> main = (ArrayList<Carte>) p.getJ1().getMain().clone();
                //Si on est le leader
                if (p.isJ1Courant()) {

                    indice = getMaxUndead(main);
                    if (indice == -1) {
                        indice = getIndexMaxScore(main);
                    }

                    //Si on est le deuxième joueur
                } else {
                    Carte carteJ2 = p.getCarteJ2();
                    ArrayList<Carte> cartesJouable = p.getJ1().getCartesJouable(carteJ2);
                    ArrayList<Carte> cartesGagnante = p.getJ1().getCartesGagnante(carteJ2);

                    //si on peut gagner
                    if (!cartesGagnante.isEmpty()) {
                        System.out.println("test");
                        indice = getindex(getCarteMinForce(cartesGagnante));
                        //si on ne peut que perdre
                    } else {
                        System.out.println("test2");
                        indice = getindex(getCarteMinForce(cartesJouable));
                    }
                }
            }

            //Si on ne veut pas la carte
        } else {
            //si on est le joueur 2
            if (!getIsJ1()) {
                //Si on est le leader
                if (!p.isJ1Courant()) {

                    indice = getIndexMinScoreUndead(main);

                    //Si on est le deuxième joueur
                } else {
                    Carte carteJ1 = p.getCarteJ1();
                    ArrayList<Carte> cartesJouable = p.getJ2().getCartesJouable(carteJ1);
                    ArrayList<Carte> cartesPerdante = p.getJ2().getCartesPerdante(carteJ1);

                    //si on peut perdre
                    if (!cartesPerdante.isEmpty()) {
                        System.out.println("test3");

                        indice = getindex(getCarteMinForce(cartesPerdante));
                        //si on ne peut que gagner
                    } else {
                        System.out.println("test4");
                        indice = getindex(getCarteMinForce(cartesJouable));
                    }
                }
            } else {
                //Si on est le joueur 1
                //Si on est le leader
                if (p.isJ1Courant()) {

                    indice = getIndexMinScoreUndead(main);

                    //Si on est le deuxième joueur
                } else {
                    Carte carteJ2 = p.getCarteJ2();
                    ArrayList<Carte> cartesJouable = p.getJ1().getCartesJouable(carteJ2);
                    ArrayList<Carte> cartesPerdante = p.getJ1().getCartesPerdante(carteJ2);

                    //si on peut perdre
                    if (!cartesPerdante.isEmpty()) {
                        System.out.println("test3");

                        indice = getindex(getCarteMinForce(cartesPerdante));
                        //si on ne peut que gagner
                    } else {
                        System.out.println("test4");
                        indice = getindex(getCarteMinForce(cartesJouable));
                    }
                }
            }
        }
        return indice;

    }

    @Override
    public int chooseCardPhase2(Plateau p) {
        int indice;

        //si on est le joueur 2
        if (!getIsJ1()) {
            // on récupère notre main
            ArrayList<Carte> main = (ArrayList<Carte>) p.getJ2().getMain().clone();

            //si on est le leader
            if (!p.isJ1Courant()) {
                Carte carteNainMinJ1 = getCarteNainMin(p.getJ1().getMain());
                Carte carteNainMinJ2 = getCarteNainMin(p.getJ2().getMain());

                if (carteNainMinJ2 != null) {
                    if ((carteNainMinJ1 == null) || (carteNainMinJ1 != null && carteNainMinJ2.getForce() < carteNainMinJ1.getForce())) {
                        indice = getindex(carteNainMinJ2);
                    } else {
                        updateGrillePhase2(p);
                        //indice = getindex(getCarteMaxForce(main));
                        indice = getIndexMaxScore(main);
                    }
                } else {
                    updateGrillePhase2(p);
                    //indice = getindex(getCarteMaxForce(main));
                    indice = getIndexMaxScore(main);
                }

                //si on est deuxième joueur
            } else {

                Carte carteJ1 = p.getCarteJ1();
                ArrayList<Carte> cartesPerdantes = p.getJ2().getCartesPerdante(carteJ1);
                ArrayList<Carte> cartesJouable = p.getJ2().getCartesJouable(carteJ1);
                ArrayList<Carte> cartesGagnante = p.getJ2().getCartesGagnante(carteJ1);

                //si l'adversaire à joué un nain
                if (carteJ1.getFaction() == Faction.Nains && containsDwarf(cartesPerdantes)) {
                    Iterator<Carte> it = cartesPerdantes.iterator();
                    while (it.hasNext()) {
                        Carte c = it.next();
                        if (c.getFaction() != Faction.Nains) {
                            it.remove();
                        }
                    }
                    indice = getindex(getCarteMaxForce(cartesPerdantes));
                } else {
                    //si on peut gagner
                    if (!cartesGagnante.isEmpty()) {
                        indice = getindex(getCarteMinForce(cartesGagnante));
                        //si on ne peut que perdre
                    } else {
                        Carte carteNainMinJ2 = getCarteNainMin(p.getJ2().getMain());
                        if(carteNainMinJ2 != null && p.containsCard(cartesJouable, carteNainMinJ2)){
                            indice = getindex(carteNainMinJ2);
                        }else{
                            indice = getindex(getCarteMinForce(cartesJouable));
                        }
                    }
                }
            }
        } else {
            //si on est joueur 1
            // on récupère notre main
            ArrayList<Carte> main = (ArrayList<Carte>) p.getJ1().getMain().clone();

            //si on est le leader
            if (p.isJ1Courant()) {
                Carte carteNainMinJ1 = getCarteNainMin(p.getJ1().getMain());
                Carte carteNainMinJ2 = getCarteNainMin(p.getJ1().getMain());

                if (carteNainMinJ1 != null) {
                    if ((carteNainMinJ2 == null) || (carteNainMinJ2 != null && carteNainMinJ2.getForce() < carteNainMinJ1.getForce())) {
                        indice = getindex(carteNainMinJ2);
                    } else {
                        updateGrillePhase2(p);
                        //indice = getindex(getCarteMaxForce(main));
                        indice = getIndexMinScoreUndead(main);
                    }
                } else {
                    updateGrillePhase2(p);
                    //indice = getindex(getCarteMaxForce(main));
                    indice = getIndexMinScoreUndead(main);
                }

                //si on est deuxième joueur
            } else {

                Carte carteJ2 = p.getCarteJ2();
                ArrayList<Carte> cartesJouable = p.getJ1().getCartesJouable(carteJ2);
                ArrayList<Carte> cartesGagnante = p.getJ1().getCartesGagnante(carteJ2);
                ArrayList<Carte> cartesPerdantes = p.getJ1().getCartesPerdante(carteJ2);

                //si l'adversaire à joué un nain
                if (carteJ2.getFaction() == Faction.Nains && containsDwarf(cartesPerdantes)) {
                    Iterator<Carte> it = cartesPerdantes.iterator();
                    while (it.hasNext()) {
                        Carte c = it.next();
                        if (c.getFaction() != Faction.Nains) {
                            it.remove();
                        }
                    }
                    indice = getindex(getCarteMaxForce(cartesPerdantes));
                } else {
                    //si on peut gagner
                    if (!cartesGagnante.isEmpty()) {
                        indice = getindex(getCarteMinForce(cartesGagnante));
                        //si on ne peut que perdre
                    } else {
                        Carte carteNainMinJ2 = getCarteNainMin(p.getJ2().getMain());
                        if(carteNainMinJ2 != null && p.containsCard(cartesJouable, carteNainMinJ2)){
                            indice = getindex(carteNainMinJ2);
                        }else{
                            indice = getindex(getCarteMinForce(cartesJouable));
                        }
                    }
                }
            }
        }
        return indice;
    }

    public int getindex(Carte carteChoisis) {
        int indice = -1;
        int i = 0;
        Iterator<Carte> it = getMain().iterator();
        while (indice == -1 && it.hasNext()) {
            Carte c = it.next();
            if (c.getFaction() == carteChoisis.getFaction() && c.getForce() == carteChoisis.getForce()) {
                indice = i;
            }
            i++;
        }

        return indice;
    }

    public Carte getCarteMaxForce(ArrayList<Carte> main) {
        Iterator<Carte> it = main.iterator();
        // on créé l'array pour garder la ou les cartes maxs
        ArrayList<Carte> maxs = new ArrayList();
        maxs.add(main.get(0));
        it.next();

        // on parcours toutes les cartes pour trouver celles de plus grandes valeurs
        while (it.hasNext()) {
            Carte c = it.next();
            if (c.getForce() > maxs.get(0).getForce()) {
                maxs.clear();
                maxs.add(c);
            } else if (c.getForce() == maxs.get(0).getForce()) {
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
        ArrayList<Carte> mins = new ArrayList();
        mins.add(main.get(0));
        it.next();

        // on parcours toutes les cartes pour trouver celles de plus grandes valeurs
        while (it.hasNext()) {
            Carte c = (Carte) it.next();
            if (c.getForce() < mins.get(0).getForce()) {
                mins.clear();
                mins.add(c);
            } else if (c.getForce() == mins.get(0).getForce()) {
                mins.add(c);
            }
        }
        Random r = new Random();
        int i = r.nextInt(mins.size());
        return mins.get(i);
    }

    private void updateGrille(Plateau p) {
        //on enlève toutes les cartes de la défausse, des piles de scores et de notre pile partisans
        //System.out.println("_______DebutUpdateGrille________");

        if (getIsJ1()) {
            ArrayList<Carte> defausse = (ArrayList<Carte>) p.getDefausse().clone();
            ArrayList<Carte> pilePartisans = (ArrayList<Carte>) p.getJ1().getCartesPartisans().clone();
            ArrayList<Carte> pileScoreJ1 = (ArrayList<Carte>) p.getJ2().getCartesScore().clone();
            ArrayList<Carte> pileScoreJ2 = (ArrayList<Carte>) p.getJ1().getCartesScore().clone();

            defausse.addAll(pilePartisans);
            defausse.addAll(pileScoreJ1);
            defausse.addAll(pileScoreJ2);

            Iterator<Carte> it = defausse.iterator();
            while (it.hasNext()) {
                Carte c = it.next();
                if (!estRetiree(c)) {
                    removeCarte(cartes, c);
                    cartesRetirees.add(c);
                    //System.out.println("La carte " + c.getFaction() + " " +c.getForce() + " est retirée");
                }
            }
        } else {
            ArrayList<Carte> defausse = (ArrayList<Carte>) p.getDefausse().clone();
            ArrayList<Carte> pilePartisans = (ArrayList<Carte>) p.getJ2().getCartesPartisans().clone();
            ArrayList<Carte> pileScoreJ1 = (ArrayList<Carte>) p.getJ1().getCartesScore().clone();
            ArrayList<Carte> pileScoreJ2 = (ArrayList<Carte>) p.getJ2().getCartesScore().clone();

            defausse.addAll(pilePartisans);
            defausse.addAll(pileScoreJ1);
            defausse.addAll(pileScoreJ2);

            Iterator<Carte> it = defausse.iterator();
            while (it.hasNext()) {
                Carte c = it.next();
                if (!estRetiree(c)) {
                    removeCarte(cartes, c);
                    cartesRetirees.add(c);
                    //System.out.println("La carte " + c.getFaction() + " " +c.getForce() + " est retirée");
                }
            }
        }

        grilleMatchUp = new ArrayList();
        initGrille();
        creerGrille(cartes);
        //System.out.println("_______FinUpdateGrille________");
    }

    private void creerGrille(ArrayList<Carte> cartes) {
        for (int i = 0; i < cartes.size(); i++) {
            for (int j = 0; j < cartes.size(); j++) {
                grilleMatchUp.get(i).add(gagnePli(cartes.get(i), cartes.get(j)));
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
        for (int i = 0; i < grilleMatchUp.size(); i++) {
            for (int j = 0; j < grilleMatchUp.size(); j++) {
                System.out.print(grilleMatchUp.get(i).get(j) + " ");
            }
            System.out.println("\n");
        }
    }

    private boolean estRetiree(Carte c) {
        boolean b = false;
        Iterator<Carte> it = cartesRetirees.iterator();
        while (it.hasNext()) {
            Carte carte = it.next();
            if (carte.getFaction() == c.getFaction() && carte.getForce() == c.getForce()) {
                b = true;
                break;
            }

        }
        return b;
    }

    private void removeCarte(ArrayList<Carte> cartes, Carte c) {
        Iterator<Carte> it = cartes.iterator();
        while (it.hasNext()) {
            Carte carte = it.next();
            if (carte.getFaction() == c.getFaction() && carte.getForce() == c.getForce()) {
                it.remove();
            }
        }
    }

    private int getScoreCarteMain(Carte c) {
        int indexCard = getIndexGrille(c);

        ArrayList<Integer> arrayIndexGrilleMain = new ArrayList();

        Iterator<Carte> it = this.getMain().iterator();
        while (it.hasNext()) {
            Carte carte = it.next();
            arrayIndexGrilleMain.add(getIndexGrille(carte));
        }

        int wins1 = 0;
        int wins2 = 0;

        for (int i = 0; i < cartes.size(); i++) {
            for (int j = 0; j < cartes.size(); j++) {

                //pas de matchup avec les même cartes(deux cartes gobelin 0 ne sont pas considérés comme les "même" cartes
                if (i != j) {

                    if (i == indexCard) {
                        //if((arrayListContains(arrayIndexGrilleMain, i) && !arrayListContains(arrayIndexGrilleMain, j)) || (!arrayListContains(arrayIndexGrilleMain, i) && arrayListContains(arrayIndexGrilleMain, j))){
                        if (!arrayListContains(arrayIndexGrilleMain, j)) {
                            int win = grilleMatchUp.get(i).get(j);
                            if (win == 1) {
                            }
                            wins1 += win;
                        }
                    }
                    if (j == indexCard) {
                        if (!arrayListContains(arrayIndexGrilleMain, i)) {
                            int win = grilleMatchUp.get(i).get(j);
                            if (win == 1) {
                            }
                            wins2 += 1 - win;
                        }
                    }

                }
            }
        }
        return ((wins1 + 1)) * ((wins2 + 1));
    }

    private int getScore(Carte c) {
        int indexCard = getIndexGrille(c);

        ArrayList<Integer> arrayIndexGrilleMain = new ArrayList();

        Iterator<Carte> it = this.getMain().iterator();
        while (it.hasNext()) {
            Carte carte = it.next();
            arrayIndexGrilleMain.add(getIndexGrille(carte));
        }

        int wins1 = 0;
        int wins2 = 0;

        for (int i = 0; i < cartes.size(); i++) {
            for (int j = 0; j < cartes.size(); j++) {
                //pas de matchup avec les même cartes(deux cartes gobelin 0 ne sont pas considérés comme les "même" cartes
                if (i != j) {

                    if (i == indexCard) {
                        //if((arrayListContains(arrayIndexGrilleMain, i) && !arrayListContains(arrayIndexGrilleMain, j)) || (!arrayListContains(arrayIndexGrilleMain, i) && arrayListContains(arrayIndexGrilleMain, j))){
                        if (!arrayListContains(arrayIndexGrilleMain, j)) {
                            int win = grilleMatchUp.get(i).get(j);
                            /*if(win == 1){
                                //System.out.println("Victoire 1: " + cartes.get(j).getFaction() + cartes.get(j).getForce());
                                System.out.println("V1: " + j);
                            }*/
                            wins1 += win;
                        }
                    }
                    if (j == indexCard) {
                        if (!arrayListContains(arrayIndexGrilleMain, i)) {
                            int win = grilleMatchUp.get(i).get(j);
                            /*if(win == 0){
                                //System.out.println("Victoire 2:" + cartes.get(i).getFaction() + cartes.get(i).getForce());
                                System.out.println("V2: " + i);
                            }*/
                            wins2 += 1 - win;
                        }
                    }

                }
            }
        }
        System.out.println("Score 1er: " + wins1 + " Score2eme: " + wins2);
        return (wins1 + 1) * (wins2 + 1);
    }

    private boolean wantCard(int score) {
        ArrayList<Integer> winsAllCard1 = new ArrayList();
        ArrayList<Integer> winsAllCard2 = new ArrayList();

        ArrayList<Integer> arrayIndexGrilleMain = new ArrayList();

        Iterator<Carte> it = this.getMain().iterator();
        while (it.hasNext()) {
            Carte carte = it.next();
            arrayIndexGrilleMain.add(getIndexGrille(carte));
        }

        for (int i = 0; i < cartes.size(); i++) {
            int winCard = 0;
            for (int j = 0; j < cartes.size(); j++) {
                if (i != j) {
                    if (!arrayListContains(arrayIndexGrilleMain, j)) {
                        winCard += grilleMatchUp.get(i).get(j);
                    }
                }
            }
            winsAllCard1.add(winCard);
        }

        for (int j = 0; j < cartes.size(); j++) {
            int winCard = 0;
            for (int i = 0; i < cartes.size(); i++) {
                if (i != j) {
                    if (!arrayListContains(arrayIndexGrilleMain, i)) {
                        winCard += 1 - grilleMatchUp.get(i).get(j);
                    }
                }
            }
            winsAllCard2.add(winCard);
        }

        int somme = 0;

        Iterator<Integer> it2 = winsAllCard1.iterator();
        while (it2.hasNext()) {
            Integer winCurrent = it2.next();
            somme += winCurrent;
        }

        float moyenne1 = somme / winsAllCard1.size();

        somme = 0;

        it2 = winsAllCard2.iterator();
        while (it2.hasNext()) {
            Integer winCurrent = it2.next();
            somme += winCurrent;
        }

        float moyenne2 = somme / winsAllCard2.size();

        System.out.println("Score moyen: " + moyenne1 * moyenne2);
        return score > (moyenne1 + 1) * (moyenne2 + 1);

    }

    private boolean wantCardMediane(int score) {
        ArrayList<Integer> winsAllCard1 = new ArrayList();
        ArrayList<Integer> winsAllCard2 = new ArrayList();

        ArrayList<Integer> arrayIndexGrilleMain = new ArrayList();

        Iterator<Carte> it = this.getMain().iterator();
        while (it.hasNext()) {
            Carte carte = it.next();
            arrayIndexGrilleMain.add(getIndexGrille(carte));
        }

        for (int i = 0; i < cartes.size(); i++) {
            int winCard = 0;
            for (int j = 0; j < cartes.size(); j++) {
                if (i != j) {
                    if (!arrayListContains(arrayIndexGrilleMain, j)) {
                        winCard += grilleMatchUp.get(i).get(j);
                    }
                }
            }
            winsAllCard1.add(winCard);
        }

        for (int j = 0; j < cartes.size(); j++) {
            int winCard = 0;
            for (int i = 0; i < cartes.size(); i++) {
                if (i != j) {
                    if (!arrayListContains(arrayIndexGrilleMain, i)) {
                        winCard += 1 - grilleMatchUp.get(i).get(j);
                    }
                }
            }
            winsAllCard2.add(winCard);
        }

        int somme = 0;

        ArrayList<Integer> scores = new ArrayList();
        int index = 0;
        Iterator<Integer> it2 = winsAllCard1.iterator();
        while (it2.hasNext()) {
            int score1 = it2.next();
            int score2 = winsAllCard2.get(index);
            scores.add(score1 * score2);
            index ++;
        }

        Collections.sort(scores);
       
        
        return score > scores.get(grilleMatchUp.size()/2);

    }

    private int getIndexGrille(Carte c) {
        int i = 0;
        boolean carteTrouve = false;
        Iterator<Carte> it = cartes.iterator();
        while (it.hasNext()) {
            Carte carte = it.next();
            if (carte.getFaction() == c.getFaction() && carte.getForce() == c.getForce()) {
                carteTrouve = true;
                break;
            }
            if (!carteTrouve) {
                i++;
            }
        }
        return i;
    }

    private int getMaxUndead(ArrayList<Carte> main) {
        int maxForceUndead = getMaxForceUndead(grilleMatchUp);
        int indice = -1;
        int i = 0;
        Iterator<Carte> it = main.iterator();
        while (it.hasNext()) {
            Carte c = it.next();
            if (c.getFaction() == Faction.MortsVivants && c.getForce() == maxForceUndead) {
                indice = i;
            }
            i++;
        }
        return indice;
    }

    private int getMaxForceUndead(ArrayList<ArrayList<Integer>> grilleMatchUp) {
        Iterator<Carte> it = cartes.iterator();
        int maxForceUndead = 0;
        while (it.hasNext()) {
            Carte c = it.next();
            if (c.getFaction() == Faction.MortsVivants && c.getForce() > maxForceUndead) {
                maxForceUndead = c.getForce();
            }
        }
        return maxForceUndead;
    }

    private int getIndexMaxScore(ArrayList<Carte> main) {
        int scoreMax = 0;
        int i = 0;
        ArrayList<Integer> victoryCardsIndex = new ArrayList();
        Iterator<Carte> it = main.iterator();
        while (it.hasNext()) {
            Carte c = it.next();
            int score = getScoreCarteMain(c);
            if (score > scoreMax) {
                victoryCardsIndex.clear();
                scoreMax = score;
                victoryCardsIndex.add(i);
            } else if (score == scoreMax) {
                victoryCardsIndex.add(i);
            }
            i++;
        }
        Random r = new Random();
        int indice = r.nextInt(victoryCardsIndex.size());
        return victoryCardsIndex.get(indice);
    }

    private boolean arrayListContains(ArrayList ar, int i) {
        boolean contains = false;
        Iterator<Integer> it = ar.iterator();
        while (it.hasNext()) {
            Integer in = it.next();
            if (in == i) {
                contains = true;
            }
        }
        return contains;
    }

    private void afficherCartes() {
        int i = 0;
        Iterator<Carte> it = cartes.iterator();
        while (it.hasNext()) {
            Carte c = it.next();
            System.out.println("Indice " + i + ": " + c.getFaction() + c.getForce());
            i++;
        }
    }

    private int getIndexMinScoreUndead(ArrayList<Carte> main) {
        int scoreMin = getScore(getMain().get(0));
        int i = 0;
        ArrayList<Integer> victoryCardsIndex = new ArrayList();
        Iterator<Carte> it = main.iterator();

        while (it.hasNext()) {
            Carte c = it.next();
            int score = getScoreCarteMain(c);
            if (score < scoreMin) {
                victoryCardsIndex.clear();
                scoreMin = score;
                victoryCardsIndex.add(i);
            } else if (score == scoreMin) {
                victoryCardsIndex.add(i);
            }
            i++;
        }
        Random r = new Random();
        System.out.println(victoryCardsIndex.size() + "////////////");
        int indice = r.nextInt(victoryCardsIndex.size());
        return victoryCardsIndex.get(indice);
    }

    private void updateGrillePhase2(Plateau p) {
        ArrayList<Carte> cartesJ1 = (ArrayList<Carte>) p.getJ1().getMain().clone();
        ArrayList<Carte> cartesJ2 = (ArrayList<Carte>) p.getJ2().getMain().clone();

        cartes = new ArrayList();
        cartes.addAll(cartesJ1);
        cartes.addAll(cartesJ2);

        if (getIsJ1()) {
            ArrayList<Carte> defausse = (ArrayList<Carte>) p.getDefausse().clone();
            ArrayList<Carte> pileScoreJ1 = (ArrayList<Carte>) p.getJ2().getCartesScore().clone();
            ArrayList<Carte> pileScoreJ2 = (ArrayList<Carte>) p.getJ1().getCartesScore().clone();

            defausse.addAll(pileScoreJ1);
            defausse.addAll(pileScoreJ2);

            Iterator<Carte> it = defausse.iterator();
            while (it.hasNext()) {
                Carte c = it.next();
                if (!estRetiree(c)) {
                    removeCarte(cartes, c);
                    cartesRetirees.add(c);
                    //System.out.println("La carte " + c.getFaction() + " " +c.getForce() + " est retirée");
                }
            }
        } else {
            ArrayList<Carte> defausse = (ArrayList<Carte>) p.getDefausse().clone();
            ArrayList<Carte> pileScoreJ1 = (ArrayList<Carte>) p.getJ1().getCartesScore().clone();
            ArrayList<Carte> pileScoreJ2 = (ArrayList<Carte>) p.getJ2().getCartesScore().clone();

            defausse.addAll(pileScoreJ1);
            defausse.addAll(pileScoreJ2);

            Iterator<Carte> it = defausse.iterator();
            while (it.hasNext()) {
                Carte c = it.next();
                if (!estRetiree(c)) {
                    removeCarte(cartes, c);
                    cartesRetirees.add(c);
                    //System.out.println("La carte " + c.getFaction() + " " +c.getForce() + " est retirée");
                }
            }
        }

        grilleMatchUp = new ArrayList();
        initGrille();
        creerGrille(cartes);

    }

    /*private Carte getCarteMinScore(ArrayList<Carte> cartes) {
        Iterator it = cartes.iterator();
        // on créé l'array pour garder la ou les cartes maxs
        ArrayList<Carte> mins = new ArrayList();
        mins.add(main.get(0));
        it.next();

        // on parcours toutes les cartes pour trouver celles de plus grandes valeurs
        while (it.hasNext()) {
            Carte c = (Carte) it.next();
            if (getScore(c) < getScore(mins.get(0))) {
                mins.clear();
                mins.add(c);
            } else if (getScore(c) == getScore(mins.get(0))) {
                mins.add(c);
            }
        }
        Random r = new Random();
        int i = r.nextInt(mins.size());
        return mins.get(i);
    }*/
    private boolean containsDwarf(ArrayList<Carte> cartesPerdantes) {
        boolean b = false;
        Iterator<Carte> it = cartesPerdantes.iterator();
        while (it.hasNext()) {
            Carte c = it.next();
            if (c.getFaction() == Faction.Nains) {
                b = true;
            }
        }
        return b;
    }

    private Carte getCarteNainMin(ArrayList<Carte> main) {
        int forceMin = 10;
        Carte carteNainMin = null;
        ArrayList<Carte> mainAdversaire = (ArrayList<Carte>) main.clone();

        Iterator<Carte> it = mainAdversaire.iterator();
        while (it.hasNext()) {
            Carte c = it.next();
            if (c.getFaction() == Faction.Nains) {
                if (c.getForce() < forceMin) {
                    carteNainMin = c;
                    forceMin = c.getForce();
                }
            }
        }
        return carteNainMin;
    }
    
    private int getIndexfactions(Faction faction){
        int indice = -1;
        switch(faction){
            case Nains:indice = 0; 
            break;
            
            case Chevaliers:indice = 1;
            break;
            
            case Doppelgangers:indice = 2;
            break;
            
            case Gobelins:indice = 3;
            break;
            
            case MortsVivants:indice = 4;
            break;
        }
        return indice;
    }

}
