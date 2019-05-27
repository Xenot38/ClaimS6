package model;

import model.Plateau;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;

public class JoueurIADifficile extends JoueurIA {

    private int pronfondeurMax;
    private int nbTours;
    private ArrayList<Carte> cartes;
    private ArrayList<Carte> cartesRetirees;
    //private ArrayList<Boolean> J1HasFactions;
    private ArrayList<ArrayList<Integer>> grilleMatchUp;
    private ArrayList<Integer> coefsFactions;
    public HashMap<String, Integer> configsPoids;
    public ArrayList<Plateau> configs;
    public ArrayList<Integer> poids;

    /*
    Nains : 0 
    Chevaliers : 1
    Doppelgangers : 2
    Gobelins : 3
    MortsVivants : 4
     */
    public JoueurIADifficile(ArrayList<Carte> main, boolean isJ1, ArrayList<Carte> pioche) {
        super(main, isJ1);
        cartes = pioche;
        initGrille();
        cartesRetirees = new ArrayList();
        creerGrille(cartes);
        coefsFactions = new ArrayList(Arrays.asList(1, 1, 1, 1, 1));
        configsPoids = new HashMap();
        configs = new ArrayList();
        poids = new ArrayList();
        setPronfondeurMax(4);
        /*
        //Affichage des scores de chaques cartes au début de la partie
        Iterator<Carte> it = cartes.iterator();
        while (it.hasNext()) {
            Carte c = it.next();
            System.out.println(c.getFaction() + " " + c.getForce() + "=>" + getScoreCarteMain(c));
        }*/
        //afficheGrille();
    }

    @Override
    public int joue(Plateau p) {

        int indice;
        if (p.getPhase() == 1) {
            //afficherCartes();
            updateGrille(p);
            updateCoefFaction(p);
            //afficheGrille();
            //Phase1
            Boolean winCard = winCard(p);
            /*if (winCard) {
                System.out.println("je veux la carte");
            } else {
                System.out.println("je ne veux pas la carte");
            }*/
            indice = chooseCardPhase1(p, winCard);
            //System.out.println("Indice: " + indice);
            return indice;
        } else {
            indice = chooseCardPhase2(p);
            return indice;
        }
    }

    @Override
    public Boolean winCard(Plateau p) {
        //calcul win rate de la carte en jeu;
        Carte c = p.getCarteEnJeu();
        int score = getScore(c);
        //System.out.println("Score Carte en jeu: " + score);
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
                        indice = getindex(getCarteMinForce(cartesGagnante));
                        //si on ne peut que perdre
                    } else {
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
                        indice = getindex(getCarteMinForce(cartesGagnante));
                        //si on ne peut que perdre
                    } else {
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

                    indice = getIndexMinScore(main);

                    //Si on est le deuxième joueur
                } else {
                    Carte carteJ1 = p.getCarteJ1();
                    ArrayList<Carte> cartesJouable = p.getJ2().getCartesJouable(carteJ1);
                    ArrayList<Carte> cartesPerdante = p.getJ2().getCartesPerdante(carteJ1);
                    ArrayList<Carte> cartesGagnante = p.getJ2().getCartesGagnante(carteJ1);

                    if (carteJ1.getFaction() == Faction.MortsVivants && !cartesGagnante.isEmpty()) {
                        indice = getindex(getCarteMinForce(cartesGagnante));

                    } else {
                        //si on peut perdre
                        if (!cartesPerdante.isEmpty()) {

                            indice = getindex(getCarteMinForce(cartesPerdante));
                            //si on ne peut que gagner
                        } else {
                            indice = getindex(getCarteMinForce(cartesJouable));
                        }
                    }

                }
            } else {
                //Si on est le joueur 1
                //Si on est le leader
                if (p.isJ1Courant()) {

                    indice = getIndexMinScore(main);

                    //Si on est le deuxième joueur
                } else {
                    Carte carteJ2 = p.getCarteJ2();
                    ArrayList<Carte> cartesJouable = p.getJ1().getCartesJouable(carteJ2);
                    ArrayList<Carte> cartesPerdante = p.getJ1().getCartesPerdante(carteJ2);
                    ArrayList<Carte> cartesGagnante = p.getJ1().getCartesGagnante(carteJ2);

                    if (carteJ2.getFaction() == Faction.MortsVivants && !cartesGagnante.isEmpty()) {
                        indice = getindex(getCarteMinForce(cartesGagnante));

                    } else {
                        //si on peut perdre
                        if (!cartesPerdante.isEmpty()) {

                            indice = getindex(getCarteMinForce(cartesPerdante));
                            //si on ne peut que gagner
                        } else {
                            indice = getindex(getCarteMinForce(cartesJouable));
                        }
                    }
                }
            }
        }
        return indice;

    }

    @Override
    public int chooseCardPhase2(Plateau p) {
        updateGrillePhase2(p);
        int indice = -1;
        if (getNbTours() != 0) {
            if (getNbTours() % 2 == 0) {
                setPronfondeurMax(getPronfondeurMax() + 1);
            }
        }
        //Phase2
        configsPoids = new HashMap();
        creerConfigsPoids(p);
        int i = 0;
        /*configsPoids.entrySet().forEach((en) -> {
                System.out.println("Config: ");
                String key = en.getKey();
                String[] config = key.split("]");
                for (int j = 0; j < config.length; j++) {
                    System.out.println("\t"+config[j]);
                }
                int value = en.getValue();
                //System.out.println(key);
            });*/
        //indice = chooseCardPhase2(p);
        ArrayList<Carte> cartesJouable = new ArrayList();

        if (getIsJ1()) {
            if (p.isJ1Courant()) {
                cartesJouable = p.getJ1().getMain();
            } else {
                cartesJouable = getCartesJouable(p.getCarteJ2());
            }
        } else {
            if (!p.isJ1Courant()) {
                cartesJouable = p.getJ2().getMain();
            } else {
                cartesJouable = getCartesJouable(p.getCarteJ1());
            }
        }
        indice = getIndexMeilleurConfig(p, cartesJouable);
        //System.out.println("Indice: " + indice);
        setNbTours(getNbTours() + 1);
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
        boolean alreadyRetired = false;
        Iterator<Carte> it = cartes.iterator();
        while (it.hasNext() && !alreadyRetired) {
            Carte carte = it.next();
            if (carte.getFaction() == c.getFaction() && carte.getForce() == c.getForce()) {
                it.remove();
                alreadyRetired = true;
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
        int mult1 = 1;
        int mult2 = 1;
        switch (c.getFaction()) {
            case Chevaliers:
                mult1 = 6;
                mult2 = 4;
                break;
            case Doppelgangers:
                mult1 = 2;
                mult2 = 8;
                break;
            case Gobelins:
                mult1 = 8;
                mult2 = 2;
                break;
            case MortsVivants:
                mult1 = 8;
                mult2 = 2;
                break;
            case Nains:
                mult1 = 2;
                mult2 = 8;
                break;
        }
        return ((wins1 + 1) * mult1) * ((wins2 + 1) * mult2) * (1 + c.getForce() / 2) * coefsFactions.get(getIndexfactions(c.getFaction()));
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
                            if (win == 1) {
                                //System.out.println("Victoire 1: " + cartes.get(j).getFaction() + cartes.get(j).getForce());
                                //System.out.println("V1: " + j);
                            }
                            wins1 += win;
                        }
                    }
                    if (j == indexCard) {
                        if (!arrayListContains(arrayIndexGrilleMain, i)) {
                            int win = grilleMatchUp.get(i).get(j);
                            if (win == 0) {
                                //System.out.println("Victoire 2:" + cartes.get(i).getFaction() + cartes.get(i).getForce());
                                //System.out.println("V2: " + i);
                            }
                            wins2 += 1 - win;
                        }
                    }

                }
            }
        }
        int mult1 = 1;
        int mult2 = 1;
        switch (c.getFaction()) {
            case Chevaliers:
                mult1 = 6;
                mult2 = 4;
                break;
            case Doppelgangers:
                mult1 = 2;
                mult2 = 8;
                break;
            case Gobelins:
                mult1 = 8;
                mult2 = 2;
                break;
            case MortsVivants:
                mult1 = 8;
                mult2 = 2;
                break;
            case Nains:
                mult1 = 2;
                mult2 = 8;
                break;
        }
        //System.out.println("Score 1er: " + wins1 + " Score2eme: " + wins2);
        return (wins1 + 1) * mult1 * (wins2 + 1) * mult2 * (1 + c.getForce() / 2) * coefsFactions.get(getIndexfactions(c.getFaction()));
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

        //System.out.println("Score moyen: " + moyenne1 * moyenne2);
        return score > (moyenne1 + 1) * (moyenne2 + 1);

    }

    private boolean wantCardMediane(int score) {
        /*ArrayList<Integer> winsAllCard1 = new ArrayList();
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
        }*/

        ArrayList<Integer> scores = new ArrayList();
        Iterator<Carte> it = cartes.iterator();
        while (it.hasNext()) {
            Carte carte = it.next();
            scores.add(getScoreCarteMain(carte));
        }

        Collections.sort(scores);

        return score > scores.get(grilleMatchUp.size() / 2);

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

    private int getIndexMinScore(ArrayList<Carte> main) {
        //System.out.println(main.size() +"||||||||||||||||||||||||||");
        int scoreMin = getScore(getMain().get(0));
        int i = 0;
        ArrayList<Integer> victoryCardsIndex = new ArrayList();
        victoryCardsIndex.add(0);
        Iterator<Carte> it = main.iterator();
        Carte c = it.next();
        while (it.hasNext()) {
            c = it.next();
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
        //System.out.println(victoryCardsIndex.size() + "////////////");
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

    private int getIndexfactions(Faction faction) {
        int indice = -1;
        switch (faction) {
            case Nains:
                indice = 0;
                break;

            case Chevaliers:
                indice = 1;
                break;

            case Doppelgangers:
                indice = 2;
                break;

            case Gobelins:
                indice = 3;
                break;

            case MortsVivants:
                indice = 4;
                break;
        }
        return indice;
    }

    private Carte carteDoppelMin(ArrayList<Carte> main) {
        int forceMin = 10;
        Carte carteDoppelMin = null;
        ArrayList<Carte> mainAdversaire = (ArrayList<Carte>) main.clone();

        Iterator<Carte> it = mainAdversaire.iterator();
        while (it.hasNext()) {
            Carte c = it.next();
            if (c.getFaction() == Faction.Doppelgangers) {
                if (c.getForce() < forceMin) {
                    carteDoppelMin = c;
                    forceMin = c.getForce();
                }
            }
        }
        return carteDoppelMin;
    }

    private void updateCoefFaction(Plateau p) {
        ArrayList<Carte> pp1 = new ArrayList();//pile partisans joueur 1
        ArrayList<Carte> pp2 = new ArrayList();//pile partisans joueur 2
        ArrayList<Carte> ps1 = new ArrayList();//pile score joueur 1
        ArrayList<Carte> ps2 = new ArrayList();//pile score joueur 2
        ArrayList<Carte> defausse = new ArrayList();//defausse
        ArrayList<Carte> main1 = new ArrayList();
        ArrayList<Carte> main2 = new ArrayList();

        if (p.getPhase() == 1) {
            if (this.getIsJ1()) {
                pp1 = p.getJ1().getCartesPartisans();
            } else {
                pp2 = p.getJ2().getCartesPartisans();
            }
        }

        ps1 = p.getJ1().getCartesScore();
        ps2 = p.getJ2().getCartesScore();
        defausse = p.getDefausse();

        if (this.getIsJ1()) {
            main1 = p.getJ1().getMain();
        } else {
            main2 = p.getJ2().getMain();
        }

    }

    public int chooseCardPhase2MiniMax(Plateau p) {
        ArrayList<Integer> scores = new ArrayList();

        if (getIsJ1()) {
            if (p.isJ1Courant()) {
                scores.add(miniMax(p.getJ1().getMain(), p.getJ2().getMain(), p.getJ1().getCartesScore(), p.getJ2().getCartesScore(), p.isJ1Courant(), null));
            } else {
                scores.add(miniMaxAdversaire(p.getJ1().getMain(), p.getJ2().getMain(), p.getJ1().getCartesScore(), p.getJ2().getCartesScore(), p.isJ1Courant(), null));
            }
        } else {
            if (!p.isJ1Courant()) {
                scores.add(miniMax(p.getJ1().getMain(), p.getJ2().getMain(), p.getJ1().getCartesScore(), p.getJ2().getCartesScore(), p.isJ1Courant(), null));
            } else {
                scores.add(miniMaxAdversaire(p.getJ1().getMain(), p.getJ2().getMain(), p.getJ1().getCartesScore(), p.getJ2().getCartesScore(), p.isJ1Courant(), null));
            }
        }

        if (getIsJ1()) {
            if (p.isJ1Courant()) {
                Iterator<Carte> it = getMain().iterator();
                while (it.hasNext()) {
                    Carte next = it.next();

                }
            }
        }

        int scoreMax = Collections.max(scores);
        int indice = -1;
        for (int i = 0; i < scores.size(); i++) {
            if (scores.get(i) == scoreMax) {
                indice = i;
                i = scores.size();
            }
        }
        return indice;
    }

    private int miniMax(ArrayList<Carte> main1, ArrayList<Carte> main2, ArrayList<Carte> score1, ArrayList<Carte> score2, boolean isJ1Leader, Carte carteJouee) {
        if (main1.size() == 0 && main2.size() == 0) {
            if (getIsJ1()) {
                if (aGagne(score1, score2)) {
                    return 1;
                } else {
                    return 0;
                }
            } else {
                if (aGagne(score2, score1)) {
                    return 1;
                } else {
                    return 0;
                }
            }

        } else {
            ArrayList<Carte> cartesJouable = new ArrayList();
            ArrayList<Integer> scores = new ArrayList();

            if (getIsJ1()) {
                if (isJ1Leader) {
                    cartesJouable = main1;
                } else {
                    cartesJouable = getCartesJouable(carteJouee);
                }
            } else {
                if (!isJ1Leader) {
                    cartesJouable = main2;
                } else {
                    cartesJouable = getCartesJouable(carteJouee);
                }
            }

            for (Carte c : cartesJouable) {

                //si on est le premier a devoir jouer
                if (carteJouee == null) {
                    //modif config
                    ArrayList<Carte> mainCopie = new ArrayList();
                    if (getIsJ1()) {
                        mainCopie = (ArrayList<Carte>) main1.clone();
                    } else {
                        mainCopie = (ArrayList<Carte>) main2.clone();
                    }
                    Iterator<Carte> it2 = mainCopie.iterator();
                    boolean alreadyRemoved = false;
                    while (it2.hasNext() && !alreadyRemoved) {
                        Carte carte = it2.next();
                        if (carte.getFaction() == c.getFaction() && carte.getForce() == c.getForce()) {
                            it2.remove();
                            alreadyRemoved = true;
                        }

                    }
                    if (getIsJ1()) {
                        scores.add(miniMaxAdversaire(mainCopie, main2, score1, score2, isJ1Leader, c));
                    } else {
                        scores.add(miniMaxAdversaire(main1, mainCopie, score1, score2, isJ1Leader, c));
                    }
                } else {

                    //modif config
                    ArrayList<Carte> mainCopie = new ArrayList();
                    ArrayList<Carte> scoreCopie1 = (ArrayList<Carte>) score1.clone();
                    ArrayList<Carte> scoreCopie2 = (ArrayList<Carte>) score2.clone();
                    if (getIsJ1()) {
                        mainCopie = (ArrayList<Carte>) main1.clone();
                    } else {
                        mainCopie = (ArrayList<Carte>) main2.clone();
                    }
                    Iterator<Carte> it2 = mainCopie.iterator();
                    boolean alreadyRemoved = false;
                    while (it2.hasNext() && !alreadyRemoved) {
                        Carte carte = it2.next();
                        if (carte.getFaction() == c.getFaction() && carte.getForce() == c.getForce()) {
                            it2.remove();
                            alreadyRemoved = true;
                        }

                    }

                    //si on gagne le pli avec la carte c
                    if (gagnePli(carteJouee, c) == 1) {
                        if (getIsJ1()) {
                            if (carteJouee.getFaction() == Faction.Nains) {
                                scoreCopie2.add(c);
                            } else {
                                scoreCopie1.add(c);
                            }
                            if (c.getFaction() == Faction.Nains) {
                                scoreCopie2.add(c);
                            } else {
                                scoreCopie1.add(c);
                            }

                        }

                        if (getIsJ1()) {
                            scores.add(miniMax(mainCopie, main2, scoreCopie1, scoreCopie2, true, c));
                        } else {
                            scores.add(miniMax(main1, mainCopie, scoreCopie1, scoreCopie2, false, c));
                        }

                        //si on perd le pli
                    } else {
                        if (getIsJ1()) {
                            if (carteJouee.getFaction() == Faction.Nains) {
                                scoreCopie1.add(c);
                            } else {
                                scoreCopie2.add(c);
                            }
                            if (c.getFaction() == Faction.Nains) {
                                scoreCopie1.add(c);
                            } else {
                                scoreCopie2.add(c);
                            }

                        }

                        if (getIsJ1()) {
                            scores.add(miniMaxAdversaire(mainCopie, main2, scoreCopie1, scoreCopie2, false, c));
                        } else {
                            scores.add(miniMaxAdversaire(main1, mainCopie, scoreCopie1, scoreCopie2, true, c));
                        }
                    }

                }
            }
            System.out.println("[Test minimax ]");
            System.out.println(scores.get(0));
            return Collections.max(scores);
        }

    }

    private int miniMaxAdversaire(ArrayList<Carte> main1, ArrayList<Carte> main2, ArrayList<Carte> score1, ArrayList<Carte> score2, boolean isJ1Leader, Carte carteJouee) {
        if (main1.size() == 0 && main2.size() == 0) {
            if (getIsJ1()) {
                if (aGagne(score1, score2)) {
                    return 100000;
                } else {
                    return -100000;
                }
            } else {
                if (aGagne(score2, score1)) {
                    return 100000;
                } else {
                    return -100000;
                }
            }

        } else {
            ArrayList<Carte> cartesJouable = new ArrayList();
            ArrayList<Integer> scores = new ArrayList();

            if (getIsJ1()) {
                if (isJ1Leader) {
                    cartesJouable = main2;
                } else {
                    cartesJouable = getCartesJouable(carteJouee);
                }
            } else {
                if (!isJ1Leader) {
                    cartesJouable = main1;
                } else {
                    cartesJouable = getCartesJouable(carteJouee);
                }
            }

            Iterator<Carte> it = cartesJouable.iterator();
            while (it.hasNext()) {
                Carte c = it.next();
                //si on est le premier a devoir jouer
                if (carteJouee == null) {
                    //modif config
                    ArrayList<Carte> mainCopie = new ArrayList();
                    if (getIsJ1()) {
                        mainCopie = (ArrayList<Carte>) main1.clone();
                    } else {
                        mainCopie = (ArrayList<Carte>) main2.clone();
                    }
                    Iterator<Carte> it2 = mainCopie.iterator();
                    boolean alreadyRemoved = false;
                    while (it2.hasNext() && !alreadyRemoved) {
                        Carte carte = it2.next();
                        if (carte.getFaction() == c.getFaction() && carte.getForce() == c.getForce()) {
                            it2.remove();
                            alreadyRemoved = true;
                        }

                    }
                    if (getIsJ1()) {
                        scores.add(miniMaxAdversaire(mainCopie, main2, score1, score2, isJ1Leader, c));
                    } else {
                        scores.add(miniMaxAdversaire(main1, mainCopie, score1, score2, isJ1Leader, c));
                    }
                } else {

                    //modif config
                    ArrayList<Carte> mainCopie = new ArrayList();
                    ArrayList<Carte> scoreCopie1 = (ArrayList<Carte>) score1.clone();
                    ArrayList<Carte> scoreCopie2 = (ArrayList<Carte>) score2.clone();
                    if (getIsJ1()) {
                        mainCopie = (ArrayList<Carte>) main1.clone();
                    } else {
                        mainCopie = (ArrayList<Carte>) main2.clone();
                    }
                    Iterator<Carte> it2 = mainCopie.iterator();
                    boolean alreadyRemoved = false;
                    while (it2.hasNext() && !alreadyRemoved) {
                        Carte carte = it2.next();
                        if (carte.getFaction() == c.getFaction() && carte.getForce() == c.getForce()) {
                            it2.remove();
                            alreadyRemoved = true;
                        }

                    }

                    //si on gagne le pli avec la carte c
                    if (gagnePli(carteJouee, c) == 1) {
                        if (getIsJ1()) {
                            if (carteJouee.getFaction() == Faction.Nains) {
                                scoreCopie2.add(c);
                            } else {
                                scoreCopie1.add(c);
                            }
                            if (c.getFaction() == Faction.Nains) {
                                scoreCopie2.add(c);
                            } else {
                                scoreCopie1.add(c);
                            }

                        }

                        if (getIsJ1()) {
                            scores.add(miniMax(mainCopie, main2, scoreCopie1, scoreCopie2, true, c));
                        } else {
                            scores.add(miniMax(main1, mainCopie, scoreCopie1, scoreCopie2, false, c));
                        }

                        //si on perd le pli
                    } else {
                        if (getIsJ1()) {
                            if (carteJouee.getFaction() == Faction.Nains) {
                                scoreCopie1.add(c);
                            } else {
                                scoreCopie2.add(c);
                            }
                            if (c.getFaction() == Faction.Nains) {
                                scoreCopie1.add(c);
                            } else {
                                scoreCopie2.add(c);
                            }

                        }

                        if (getIsJ1()) {
                            scores.add(miniMaxAdversaire(mainCopie, main2, scoreCopie1, scoreCopie2, false, c));
                        } else {
                            scores.add(miniMaxAdversaire(main1, mainCopie, scoreCopie1, scoreCopie2, true, c));
                        }
                    }

                }
            }
            if (scores.size() > 0) {
                return Collections.min(scores);
            } else {
                return 0;
            }
        }
    }

    private boolean aGagne(ArrayList<Carte> score1, ArrayList<Carte> score2) {
        ArrayList<Integer> nbCartesFactions = new ArrayList(Arrays.asList(0, 0, 0, 0, 0));
        ArrayList<Integer> forceMaxCartesFactions1 = new ArrayList(Arrays.asList(-1, -1, -1, -1, -1));
        ArrayList<Integer> forceMaxCartesFactions2 = new ArrayList(Arrays.asList(-1, -1, -1, -1, -1));

        Iterator<Carte> it = score1.iterator();
        while (it.hasNext()) {
            Carte c = it.next();
            int indiceFaction = getIndexfactions(c.getFaction());
            nbCartesFactions.set(indiceFaction, nbCartesFactions.get(indiceFaction) + 1);
            int forceMaxCarteFaction = forceMaxCartesFactions1.get(indiceFaction);
            if (c.getForce() > forceMaxCarteFaction) {
                forceMaxCartesFactions1.set(indiceFaction, c.getForce());
            }
        }

        Iterator<Carte> it2 = score2.iterator();
        while (it2.hasNext()) {
            Carte c = it2.next();
            int indiceFaction = getIndexfactions(c.getFaction());
            nbCartesFactions.set(indiceFaction, nbCartesFactions.get(indiceFaction) - 1);
            int forceMaxCarteFaction = forceMaxCartesFactions2.get(indiceFaction);
            if (c.getForce() > forceMaxCarteFaction) {
                forceMaxCartesFactions2.set(indiceFaction, c.getForce());
            }
        }

        int voteFaction = 0;
        int i = 0;
        Iterator<Integer> it3 = nbCartesFactions.iterator();
        while (it3.hasNext()) {
            Integer nbCarteF = it3.next();
            if (nbCarteF > 0) {
                voteFaction++;
            } else if (nbCarteF < 0) {
                voteFaction--;
            } else {
                if (forceMaxCartesFactions1.get(i) > forceMaxCartesFactions2.get(i)) {
                    voteFaction++;
                } else if (forceMaxCartesFactions1.get(i) < forceMaxCartesFactions2.get(i)) {
                    voteFaction--;
                }
            }

            i++;
        }
        return voteFaction > 0;
    }

    private int getIndexMeilleurConfig(Plateau p, ArrayList<Carte> main) {
        int indice = -1;
        int scoreMax = -10000000;
        Iterator<Carte> it = main.iterator();
        while (it.hasNext()) {
            Carte c = it.next();
            int score = getConfigsPoids(p, c);
            if (score > scoreMax) {
                scoreMax = score;
                indice = getIndexCarteMain(c, getMain());
                ;
            }
        }
        return indice;
    }

    private int getConfigsPoids(Plateau p, Carte c) {
        //Partie création de la nouvelle configuration
        //on joue la carte c

        boolean isJ1C = p.isJ1Courant() == true;
        Joueur j1 = p.getJ1().copie();
        Joueur j2 = p.getJ2().copie();
        Carte c2 = null;
        Carte c1 = null;
        if (p.getCarteJ1() != null) {
            c1 = p.getCarteJ1().copie();
        }
        if (p.getCarteJ2() != null) {
            c2 = p.getCarteJ2().copie();
        }

        Plateau pNewConfig = new Plateau(isJ1C, j1, j2, (ArrayList<Coup>) p.getHistorique().clone(), (Stack<Coup>) p.getContreHistorique().clone(), c1, c2, null, null, (Stack<Carte>) p.getPioche().clone(), (ArrayList<Carte>) p.getDefausse().clone(), (ArrayList<Integer>) p.getScore().clone(), 2, p.isFini() == true);

        boolean premierJoueur;

        if (isJ1) {
            pNewConfig.getJ1().choisirCarte(pNewConfig.getJ1().getIndexCarteMain(c, pNewConfig.getJ1().getMain()));
            pNewConfig.setCarteJ1(c);
            premierJoueur = (pNewConfig.getCarteJ2() == null);
        } else {
            pNewConfig.getJ2().choisirCarte(pNewConfig.getJ2().getIndexCarteMain(c, pNewConfig.getJ2().getMain()));
            pNewConfig.setCarteJ2(c);
            premierJoueur = (pNewConfig.getCarteJ1() == null);
        }

        if (!premierJoueur) {
            pNewConfig.calculPli();
        }

        int i = 0;
        int indice = -1;
        boolean trouve = false;
        Iterator<Plateau> it = configs.iterator();
        while (it.hasNext() && !trouve) {
            Plateau plat = it.next();
            if (plat.egal(pNewConfig)) {
                indice = i;
                trouve = true;
            }
            i++;
        }

        //return poids.get(indice);
        /*if(configsPoids.get(pNewConfig.hashString()) == null){
            return getScoreConf(pNewConfig);
        }else{*/
        return configsPoids.get(pNewConfig.hashString());
        //}
    }

    private void creerConfigsPoids(Plateau p) {
        boolean isJ1C = p.isJ1Courant() == true;
        Joueur j1 = p.getJ1().copie();
        Joueur j2 = p.getJ2().copie();
        Carte c2 = null;
        Carte c1 = null;
        if (p.getCarteJ1() != null) {
            c1 = p.getCarteJ1().copie();
        }
        if (p.getCarteJ2() != null) {
            c2 = p.getCarteJ2().copie();
        }

        Plateau plateau = new Plateau(isJ1C, j1, j2, (ArrayList<Coup>) p.getHistorique().clone(), (Stack<Coup>) p.getContreHistorique().clone(), c1, c2, null, null, (Stack<Carte>) p.getPioche().clone(), (ArrayList<Carte>) p.getDefausse().clone(), (ArrayList<Integer>) p.getScore().clone(), 2, p.isFini() == true);
        //on doit créer pour chaque config possible une entrée dans notre hashmap

        ArrayList<Carte> cartesJouables = new ArrayList();

        //si on est le joueur 1
        if (getIsJ1()) {
            //si on est le premier a devoir jouer
            if (plateau.isJ1Courant()) {
                cartesJouables = plateau.getJ1().getMain();
            } else {
                cartesJouables = getCartesJouable(p.getCarteJ2());
            }
        } else {
            //si on est le premier a devoir jouer
            if (!plateau.isJ1Courant()) {
                cartesJouables = plateau.getJ2().getMain();
            } else {
                cartesJouables = getCartesJouable(p.getCarteJ1());
            }
        }

        Iterator<Carte> it = cartesJouables.iterator();
        while (it.hasNext()) {
            Carte c = it.next();
            System.out.println("" + c.getFaction() + c.getForce());
            workerMiniMax(plateau, getIsJ1(), c, configsPoids, getPronfondeurMax());
        }
    }

    private int workerMiniMax(Plateau p, boolean isJ1, Carte c, HashMap<String, Integer> configsPoids, int ttl) {
        //Partie création de la nouvelle configuration
        //on joue la carte c

        boolean isJ1C = p.isJ1Courant() == true;
        Joueur j1 = p.getJ1().copie();
        Joueur j2 = p.getJ2().copie();
        Carte c2 = null;
        Carte c1 = null;
        if (p.getCarteJ1() != null) {
            c1 = p.getCarteJ1().copie();
        }
        if (p.getCarteJ2() != null) {
            c2 = p.getCarteJ2().copie();
        }

        Plateau pNewConfig = new Plateau(isJ1C, j1, j2, (ArrayList<Coup>) p.getHistorique().clone(), (Stack<Coup>) p.getContreHistorique().clone(), c1, c2, null, null, (Stack<Carte>) p.getPioche().clone(), (ArrayList<Carte>) p.getDefausse().clone(), (ArrayList<Integer>) p.getScore().clone(), 2, p.isFini() == true);

        boolean premierJoueur;

        if (isJ1) {
            pNewConfig.getJ1().choisirCarte(pNewConfig.getJ1().getIndexCarteMain(c, pNewConfig.getJ1().getMain()));
            pNewConfig.setCarteJ1(c);

            premierJoueur = pNewConfig.isJ1Courant();
        } else {
            pNewConfig.getJ2().choisirCarte(pNewConfig.getJ2().getIndexCarteMain(c, pNewConfig.getJ2().getMain()));
            pNewConfig.setCarteJ2(c);
            premierJoueur = !pNewConfig.isJ1Courant();
        }

        if (!premierJoueur) {
            pNewConfig.calculPli();
        }

        //si on est sur une configration terminale
        if (pNewConfig.getJ1().getMain().size() == 0 && pNewConfig.getJ2().getMain().size() == 0) {
            int j1Victory = -1;
            if (aGagne(pNewConfig.getJ1().getCartesScore(), pNewConfig.getJ2().getCartesScore())) {
                j1Victory = 100;
            } else {
                j1Victory = 0;
            }
            if (isJ1) {
                //configs.add(pNewConfig);
                //poids.add(j1Victory);

                configsPoids.put(pNewConfig.hashString(), j1Victory);
                return j1Victory;
            } else {
                //configs.add(pNewConfig);
                //poids.add(1-j1Victory);

                configsPoids.put(pNewConfig.hashString(), 1 - j1Victory);
                return 1 - j1Victory;
            }
            //sinon
        } else {
            if (ttl > 0) {
                boolean aMoiDeJouer = false;
                ArrayList<Carte> cartesJouables = new ArrayList();
                //si on était le premier joueur, c'est à l'adversaire de jouer
                if (premierJoueur) {
                    if (isJ1) {
                        cartesJouables = pNewConfig.getJ2().getCartesJouable(c);
                    } else {
                        cartesJouables = pNewConfig.getJ1().getCartesJouable(c);
                    }
                } else {
                    //si on a gagné le pli , c'est à nous sinon, c'est à l'autre joueur

                    //si le joueur 1 a gagné le pli
                    if (pNewConfig.isJ1Courant()) {
                        if (isJ1) {
                            aMoiDeJouer = true;
                        }

                        cartesJouables = pNewConfig.getJ1().getMain();

                        //si le joueur 2 a gagné le pli
                    } else {
                        if (!isJ1) {
                            aMoiDeJouer = true;
                        }
                        cartesJouables = pNewConfig.getJ2().getMain();

                    }
                }

                //On recupere le score max/min des nouvelle config par rapport a si c'est à nous de jouer ou pas
                int scoreMax = -1;
                int scoreMin = 2;
                //si c'est à nous de jouer
                Iterator<Carte> it = cartesJouables.iterator();
                while (it.hasNext()) {
                    Carte carte = it.next();
                    int score;
                    if (aMoiDeJouer) {
                        score = workerMiniMax(pNewConfig, isJ1, carte, configsPoids, ttl - 1);
                    } else {
                        score = workerMiniMax(pNewConfig, !isJ1, carte, configsPoids, ttl - 1);
                    }
                    if (aMoiDeJouer) {
                        if (score > scoreMax) {
                            scoreMax = score;
                        }
                    } else {
                        if (score < scoreMin) {
                            scoreMin = score;
                        }
                    }
                }
                if (aMoiDeJouer) {
                    /*if(pNewConfig == null){
                        System.out.println("test1");
                    }*/
                    //configs.add(pNewConfig);
                    //poids.add(scoreMax);

                    configsPoids.put(pNewConfig.hashString(), scoreMax);
                    return scoreMax;
                } else {
                    /*if(pNewConfig == null){
                        System.out.println("test2");
                    }*/
                    //configs.add(pNewConfig);
                    //poids.add(scoreMin);

                    configsPoids.put(pNewConfig.hashString(), scoreMin);
                    return scoreMin;
                }
            } else {
                int scoreConf = getScoreConf(pNewConfig);

                configsPoids.put(pNewConfig.hashString(), scoreConf);
                return scoreConf;
            }

        }

    }

    @Override
    public JoueurIADifficile copie() {

        ArrayList<Carte> mainCopie = (ArrayList<Carte>) main.clone();
        ArrayList<Carte> cartesScore = (ArrayList<Carte>) this.cartesScore.clone();
        boolean isJ1 = this.isJ1 == true;
        ArrayList<ArrayList<Integer>> grilleMatchUp = (ArrayList<ArrayList<Integer>>) this.grilleMatchUp.clone();
        ArrayList<Carte> cartes = (ArrayList<Carte>) this.cartes.clone();
        ArrayList<Carte> cartesRetirees = (ArrayList<Carte>) this.cartesRetirees.clone();
        ArrayList<Integer> coefsFactions = (ArrayList<Integer>) this.coefsFactions.clone();
        ArrayList<Plateau> configs = (ArrayList<Plateau>) this.configs.clone();
        ArrayList<Integer> poids = (ArrayList<Integer>) this.poids.clone();

        JoueurIADifficile jCopie = new JoueurIADifficile(this.main, this.isJ1, this.cartes);
        jCopie.setCartes(cartes);
        jCopie.setMain(mainCopie);
        jCopie.setCartesScore(cartesScore);
        jCopie.setIsJ1(isJ1);
        jCopie.setGrilleMatchUp(grilleMatchUp);
        jCopie.setCartesRetirees(cartesRetirees);
        jCopie.setCoefsFactions(coefsFactions);
        jCopie.setConfigs(configs);
        jCopie.setPoids(poids);

        return jCopie;
    }

    public ArrayList<Carte> getCartes() {
        return cartes;
    }

    public void setCartes(ArrayList<Carte> cartes) {
        this.cartes = cartes;
    }

    public ArrayList<Carte> getCartesRetirees() {
        return cartesRetirees;
    }

    public void setCartesRetirees(ArrayList<Carte> cartesRetirees) {
        this.cartesRetirees = cartesRetirees;
    }

    public ArrayList<ArrayList<Integer>> getGrilleMatchUp() {
        return grilleMatchUp;
    }

    public void setGrilleMatchUp(ArrayList<ArrayList<Integer>> grilleMatchUp) {
        this.grilleMatchUp = grilleMatchUp;
    }

    public ArrayList<Integer> getCoefsFactions() {
        return coefsFactions;
    }

    public void setCoefsFactions(ArrayList<Integer> coefsFactions) {
        this.coefsFactions = coefsFactions;
    }

    public HashMap<String, Integer> getConfigsPoids() {
        return configsPoids;
    }

    public void setConfigsPoids(HashMap<String, Integer> configsPoids) {
        this.configsPoids = configsPoids;
    }

    public ArrayList<Plateau> getConfigs() {
        return configs;
    }

    public void setConfigs(ArrayList<Plateau> configs) {
        this.configs = configs;
    }

    public ArrayList<Integer> getPoids() {
        return poids;
    }

    public void setPoids(ArrayList<Integer> poids) {
        this.poids = poids;
    }

    public int getScoreConf(Plateau p) {
        int scoreConf = 0;

        int factionsVotesJ1 = 0;
        int factionsVotesJ2 = 0;

        int scoresMain1 = 0;
        int scoresMain2 = 0;

        ArrayList<Integer> nbCarteFactions1 = new ArrayList(Arrays.asList(0, 0, 0, 0, 0));
        nbCarteFactions1.set(0, p.getNBCartesScore(true, Faction.Nains));
        nbCarteFactions1.set(1, p.getNBCartesScore(true, Faction.Chevaliers));
        nbCarteFactions1.set(2, p.getNBCartesScore(true, Faction.Doppelgangers));
        nbCarteFactions1.set(3, p.getNBCartesScore(true, Faction.Gobelins));
        nbCarteFactions1.set(4, p.getNBCartesScore(true, Faction.MortsVivants));

        ArrayList<Integer> nbCarteFactions2 = new ArrayList(Arrays.asList(0, 0, 0, 0, 0));
        nbCarteFactions1.set(0, p.getNBCartesScore(false, Faction.Nains));
        nbCarteFactions1.set(1, p.getNBCartesScore(false, Faction.Chevaliers));
        nbCarteFactions1.set(2, p.getNBCartesScore(false, Faction.Doppelgangers));
        nbCarteFactions1.set(3, p.getNBCartesScore(false, Faction.Gobelins));
        nbCarteFactions1.set(4, p.getNBCartesScore(false, Faction.MortsVivants));

        ArrayList<Integer> forceCarteFactions1 = new ArrayList(Arrays.asList(0, 0, 0, 0, 0));
        forceCarteFactions1.set(0, p.getMaxCartesScore(true, Faction.Nains));
        forceCarteFactions1.set(1, p.getMaxCartesScore(true, Faction.Chevaliers));
        forceCarteFactions1.set(2, p.getMaxCartesScore(true, Faction.Doppelgangers));
        forceCarteFactions1.set(3, p.getMaxCartesScore(true, Faction.Gobelins));
        forceCarteFactions1.set(4, p.getMaxCartesScore(true, Faction.MortsVivants));

        ArrayList<Integer> forceCarteFactions2 = new ArrayList(Arrays.asList(0, 0, 0, 0, 0));
        forceCarteFactions2.set(0, p.getMaxCartesScore(false, Faction.Nains));
        forceCarteFactions2.set(1, p.getMaxCartesScore(false, Faction.Chevaliers));
        forceCarteFactions2.set(2, p.getMaxCartesScore(false, Faction.Doppelgangers));
        forceCarteFactions2.set(3, p.getMaxCartesScore(false, Faction.Gobelins));
        forceCarteFactions2.set(4, p.getMaxCartesScore(false, Faction.MortsVivants));

        Iterator<Integer> it = nbCarteFactions1.iterator();
        int i = 0;
        while (it.hasNext()) {
            int nbCaFa = it.next();
            if (nbCaFa > nbCarteFactions2.get(i)) {
                factionsVotesJ1++;
            } else if (nbCaFa == nbCarteFactions2.get(i)) {
                if (forceCarteFactions1.get(i) > forceCarteFactions2.get(i)) {
                    factionsVotesJ1++;
                } else if (forceCarteFactions1.get(i) < forceCarteFactions2.get(i)) {
                    factionsVotesJ2++;
                }
            } else {
                factionsVotesJ2++;
            }
            i++;
        }

        //scoreMain
        //J1
        Iterator<Carte> it2 = p.getJ1().getMain().iterator();
        while (it2.hasNext()) {
            Carte c = it2.next();
            scoresMain1 += getScore(c);
        }

        //J2
        it2 = p.getJ2().getMain().iterator();
        while (it2.hasNext()) {
            Carte c = it2.next();
            scoresMain2 += getScore(c);
        }

        //Calcul score conf        
        if (getIsJ1()) {
            scoreConf = (factionsVotesJ1 - factionsVotesJ2) + (scoresMain1 - scoresMain2);
        } else {
            scoreConf = (factionsVotesJ2 - factionsVotesJ1) + (scoresMain2 - scoresMain1);
        }

        return scoreConf;
    }

    public int getPronfondeurMax() {
        return pronfondeurMax;
    }

    public void setPronfondeurMax(int pronfondeurMax) {
        this.pronfondeurMax = pronfondeurMax;
    }

    public int getNbTours() {
        return nbTours;
    }

    public void setNbTours(int nbTours) {
        this.nbTours = nbTours;
    }

}
