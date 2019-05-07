package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;

public class Plateau {

        boolean j1Courant;
        private Joueur j1;
        private Joueur j2;
        private ArrayList<Coup> historique;
        private Carte carteJ1;
        private Carte carteJ2;
        private Carte carteEnJeu;
        private Stack<Carte> pioche;
        private ArrayList<Carte> defausse;
        private ArrayList<Integer> score;
        private int phase;
        private boolean fini;

        public Plateau(String difficulte) {
                phase = 1;
                j1Courant = true;
                fini = false;
                historique = new ArrayList<Coup>();
                defausse = new ArrayList<Carte>();
                score = new ArrayList<Integer>();
                for (int i = 0; i < 5; i++) {
                        score.add(0);
                }
                carteJ1 = null;
                carteJ2 = null;
                carteEnJeu = null;
                pioche = genereCartes();
                Collections.shuffle(pioche);
                ArrayList<Carte> mainTemp = new ArrayList<Carte>();
                for (int i = 0; i < 13; i++) {
                        mainTemp.add(pioche.pop());
                }
                j1 = new JoueurHumain((ArrayList<Carte>) mainTemp.clone());
                mainTemp.clear();
                for (int i = 0; i < 13; i++) {
                        mainTemp.add(pioche.pop());
                }
                switch (difficulte) {
                        case "facile":
                                j2 = new JoueurHumain(mainTemp);
                                break;
                        case "moyen":
                                j2 = new JoueurIAMoyen(mainTemp);
                                break;
                        case "difficile":
                                j2 = new JoueurIADifficile(mainTemp);
                                break;
                }
        }

        public Plateau(boolean j1Courant, Joueur j1, Joueur j2, ArrayList<Coup> historique, Carte carteJ1,
                Carte carteJ2, Carte carteEnJeu, Stack<Carte> pioche, ArrayList<Carte> defausse, ArrayList<Integer> score, int phase, boolean fini) {
                super();
                this.phase = phase;
                this.j1Courant = j1Courant;
                this.j1 = j1;
                this.j2 = j2;
                this.historique = historique;
                this.carteJ1 = carteJ1;
                this.carteJ2 = carteJ2;
                this.carteEnJeu = carteEnJeu;
                this.pioche = pioche;
                this.defausse = defausse;
                this.score = score;
                this.fini = fini;
        }

        public void calculPli()/*Calcule le pli en cours, prenant en compte les spécificités des factions. Appelle gagnePli, qui gère la fin du pli*/ {
                Carte c1;
                Carte c2;
                if (j1Courant) {
                        c1 = carteJ1;
                        c2 = carteJ2;
                } else {
                        c1 = carteJ2;
                        c2 = carteJ1;
                }
                if (c1.getFaction() == c2.getFaction() || c2.getFaction() == Faction.Doppelgangers) {
                        if (c1.getForce() >= c2.getForce()) {
                                gagnePli(c1, c2);
                        } else {
                                gagnePli(c2, c1);
                        }
                } else {
                        if (c1.getFaction() == Faction.Gobelins && c2.getFaction() == Faction.Chevaliers) {
                                gagnePli(c2, c1);
                        } else {
                                gagnePli(c1, c2);
                        }
                }
        }

        public void gagnePli(Carte cG, Carte cP)/*gère les fins de plis. cG = carte gagnante, cP = carte perdante*/ {
                if (cG == carteJ1) {//victoire du J1
                        if (phase == 1) {
                                j1.gagnerPartisan(carteEnJeu);
                                j2.gagnerPartisan(pioche.pop());
                                j1Courant = true;
                                if (cG.getFaction() == Faction.MortsVivants) {
                                        j1.gagnerCarte(cG);
                                }
                                if (cP.getFaction() == Faction.MortsVivants) {
                                        j1.gagnerCarte(cP);
                                }
                        } else {
                                if (cG.getFaction() == Faction.Nains) {
                                        j2.gagnerCarte(cG);
                                } else {
                                        j1.gagnerCarte(cG);
                                }
                                if (cP.getFaction() == Faction.Nains) {
                                        j2.gagnerCarte(cP);
                                } else {
                                        j1.gagnerCarte(cP);
                                }
                        }
                        j1Courant = true;
                } else {//victoire du J2
                        if (phase == 1) {
                                j2.gagnerPartisan(carteEnJeu);
                                j1.gagnerPartisan(pioche.pop());
                                j1Courant = true;
                                if (cG.getFaction() == Faction.MortsVivants) {
                                        j2.gagnerCarte(cG);
                                }
                                if (cP.getFaction() == Faction.MortsVivants) {
                                        j2.gagnerCarte(cP);
                                }
                        } else {
                                if (cG.getFaction() == Faction.Nains) {
                                        j1.gagnerCarte(cG);
                                } else {
                                        j2.gagnerCarte(cG);
                                }
                                if (cP.getFaction() == Faction.Nains) {
                                        j1.gagnerCarte(cP);
                                } else {
                                        j2.gagnerCarte(cP);
                                }
                        }
                        j1Courant = false;

                }
                stockagePliHistorique();
                carteJ1 = null;
                carteJ2 = null;
                if (j1.getMain().isEmpty() && phase == 1) {
                        j1.setMain(j1.getCartesPartisans());
                        j1.setCartesPartisans(null);
                        j2.setMain(j2.getCartesPartisans());
                        j2.setCartesPartisans(null);
                        phase = 2;
                }if(j1.getMain().isEmpty() && phase == 2){
                        fini = true;
                        System.out.println("/////////////////FIN DE LA PARTIE///////////////////");
                        ArrayList<Integer> score = calculerScore();
                        afficherScore(score);
                        int scoreJ1 = 0;
                        int scoreJ2 = 0;
                        Iterator<Integer> iterScore = score.iterator();
                        while(iterScore.hasNext()){
                                int scoreCourant = iterScore.next();
                                switch (scoreCourant){
                                        case(1):
                                                scoreJ1++;
                                        break;
                                        case(0):
                                                scoreJ2++;
                                        break;
                                }   
                        }
                        if(scoreJ1>scoreJ2){
                                System.out.println("Vous avez gagné !");
                        }else if(scoreJ1 == scoreJ2){
                                System.out.println("Egalité !");
                        }else{
                                System.out.println("Vous avez perdu !");
                        }
                       
                }
        }

        public ArrayList<Integer> calculerScore(){//renvoie un tableau de booleains représentant l'affiliation des différentes factions dans l'ordre Chevaliers-Doppelgangers-Gobelins-Morts_Vivants-Nains
                ArrayList scoreJ1 = (ArrayList<Carte>) j1.getCartesScore();
                Iterator<Carte> iterJ1 = scoreJ1.iterator();
                ArrayList<Carte> cartesChJ1 = new ArrayList<>();
                ArrayList<Carte> cartesDpJ1 = new ArrayList<>();
                ArrayList<Carte> cartesGbJ1 = new ArrayList<>();
                ArrayList<Carte> cartesMvJ1 = new ArrayList<>();
                ArrayList<Carte> cartesNaJ1 = new ArrayList<>();
                while (iterJ1.hasNext()){
                        Carte cCourante = iterJ1.next();
                        switch(cCourante.getFaction()){
                                case Chevaliers:
                                        cartesChJ1.add(cCourante);
                                break;
                                case Doppelgangers:
                                        cartesDpJ1.add(cCourante);
                                break;
                                case Gobelins:
                                        cartesGbJ1.add(cCourante);                    
                                break;
                                case MortsVivants:
                                        cartesMvJ1.add(cCourante);                          
                                break;
                                case Nains:
                                        cartesNaJ1.add(cCourante);        
                                break;   
                        }
                }
                ArrayList scoreJ2 = (ArrayList<Carte>) j2.getCartesScore();
                Iterator<Carte> iterJ2 = scoreJ2.iterator();
                ArrayList<Carte> cartesChJ2 = new ArrayList<>();
                ArrayList<Carte> cartesDpJ2 = new ArrayList<>();
                ArrayList<Carte> cartesGbJ2 = new ArrayList<>();
                ArrayList<Carte> cartesMvJ2 = new ArrayList<>();
                ArrayList<Carte> cartesNaJ2 = new ArrayList<>();
                while (iterJ2.hasNext()){
                        Carte cCourante = iterJ2.next();
                        switch(cCourante.getFaction()){
                                case Chevaliers:
                                        cartesChJ2.add(cCourante);
                                break;
                                case Doppelgangers:
                                        cartesDpJ2.add(cCourante);
                                break;
                                case Gobelins:
                                        cartesGbJ2.add(cCourante);                    
                                break;
                                case MortsVivants:
                                        cartesMvJ2.add(cCourante);                          
                                break;
                                case Nains:
                                        cartesNaJ2.add(cCourante);        
                                break;   
                        }
                }
                ArrayList<Integer> affiliationFactions = new ArrayList<>();
                affiliationFactions.add(calculerFaction(cartesChJ1, cartesChJ2));
                affiliationFactions.add(calculerFaction(cartesDpJ1, cartesDpJ2));
                affiliationFactions.add(calculerFaction(cartesGbJ1, cartesGbJ2));
                affiliationFactions.add(calculerFaction(cartesMvJ1, cartesMvJ2));
                affiliationFactions.add(calculerFaction(cartesNaJ1, cartesNaJ2));  
                return affiliationFactions;
        }
        
        public Integer calculerFaction(ArrayList<Carte> cartesJ1, ArrayList<Carte> cartesJ2){//Calcule l'affiliation d'une faction en fonction des cartes de cette faction dans les mains des 2 joueurs.
                if(cartesJ1.size() > cartesJ2.size()){
                        return 1;
                }else if(cartesJ1.size()==cartesJ2.size()){
                        int forceMaxJ1 = -1;
                        Iterator<Carte> iterJ1 = cartesJ1.iterator();
                        while(iterJ1.hasNext()){
                                Carte carteCourante = iterJ1.next();
                                if(carteCourante.getForce() > forceMaxJ1){
                                        forceMaxJ1 = carteCourante.getForce();
                                }
                        }
                        int forceMaxJ2 = -1;
                        Iterator<Carte> iterJ2 = cartesJ2.iterator();
                        while(iterJ2.hasNext()){
                                Carte carteCourante = iterJ2.next();
                                if(carteCourante.getForce() > forceMaxJ2){
                                        forceMaxJ2 = carteCourante.getForce();
                                }
                        }
                        if(forceMaxJ1 == forceMaxJ2){
                                return -1;
                        }
                        else if(forceMaxJ1>forceMaxJ2){
                                return 1;
                        }else{
                                return 0;
                        }
                }else{
                        return 0;
                }
        } 
        
        public void afficherScore(ArrayList<Integer> score){
                Iterator<Integer> iterScore = score.iterator();
                afficherFaction(iterScore.next(), "Chevaliers");
                afficherFaction(iterScore.next(), "Doppelgangers");
                afficherFaction(iterScore.next(), "Gobelins");
                afficherFaction(iterScore.next(), "Morts-Vivants");
                afficherFaction(iterScore.next(), "Nains");                
        }
        
        public void afficherFaction(int i, String faction){
                if(i==1){
                        System.out.println("la faction "+ faction + " a été remportée par le joueur 1.");
                }else if(i==0){
                        System.out.println("la faction "+ faction + " a été remportée par le joueur 2.");                        
                }else{
                        System.out.println("la faction "+ faction + "n'a été remportée par aucune équipe.");
                }
        }
        
        public void stockagePliHistorique() {	//gestion de l'historique
                Coup co;
                if (carteEnJeu != null) {
                        co = new Coup(carteJ1, carteJ2, j1Courant);
                } else {
                        co = new Coup(carteJ1, carteJ2, carteEnJeu, j1Courant);
                }
                historique.add(co);
        }

        public Stack<Carte> genereCartes() {
                Stack<Carte> cartes = new Stack<Carte>();
                //ajout des chevaliers
                cartes.add(new Carte(Faction.Chevaliers, 2, "ressources/images/Ch2.png"));
                cartes.add(new Carte(Faction.Chevaliers, 3, "ressources/images/Ch3.png"));
                cartes.add(new Carte(Faction.Chevaliers, 4, "ressources/images/Ch4.png"));
                cartes.add(new Carte(Faction.Chevaliers, 5, "ressources/images/Ch5.png"));
                cartes.add(new Carte(Faction.Chevaliers, 6, "ressources/images/Ch6.png"));
                cartes.add(new Carte(Faction.Chevaliers, 7, "ressources/images/Ch7.png"));
                cartes.add(new Carte(Faction.Chevaliers, 8, "ressources/images/Ch8.png"));
                cartes.add(new Carte(Faction.Chevaliers, 9, "ressources/images/Ch9.png"));
                //ajout des doppelgangers
                cartes.add(new Carte(Faction.Doppelgangers, 0, "ressources/images/Dp0.png"));
                cartes.add(new Carte(Faction.Doppelgangers, 1, "ressources/images/Dp1.png"));
                cartes.add(new Carte(Faction.Doppelgangers, 2, "ressources/images/Dp2.png"));
                cartes.add(new Carte(Faction.Doppelgangers, 3, "ressources/images/Dp3.png"));
                cartes.add(new Carte(Faction.Doppelgangers, 4, "ressources/images/Dp4.png"));
                cartes.add(new Carte(Faction.Doppelgangers, 5, "ressources/images/Dp5.png"));
                cartes.add(new Carte(Faction.Doppelgangers, 6, "ressources/images/Dp6.png"));
                cartes.add(new Carte(Faction.Doppelgangers, 7, "ressources/images/Dp7.png"));
                cartes.add(new Carte(Faction.Doppelgangers, 8, "ressources/images/Dp8.png"));
                cartes.add(new Carte(Faction.Doppelgangers, 9, "ressources/images/Dp9.png"));
                //ajout des gobelins
                cartes.add(new Carte(Faction.Gobelins, 0, "ressources/images/Gb0.png"));
                cartes.add(new Carte(Faction.Gobelins, 0, "ressources/images/Gb0.png"));
                cartes.add(new Carte(Faction.Gobelins, 0, "ressources/images/Gb0.png"));
                cartes.add(new Carte(Faction.Gobelins, 0, "ressources/images/Gb0.png"));
                cartes.add(new Carte(Faction.Gobelins, 0, "ressources/images/Gb0.png"));
                cartes.add(new Carte(Faction.Gobelins, 1, "ressources/images/Gb1.png"));
                cartes.add(new Carte(Faction.Gobelins, 2, "ressources/images/Gb2.png"));
                cartes.add(new Carte(Faction.Gobelins, 3, "ressources/images/Gb3.png"));
                cartes.add(new Carte(Faction.Gobelins, 4, "ressources/images/Gb4.png"));
                cartes.add(new Carte(Faction.Gobelins, 5, "ressources/images/Gb5.png"));
                cartes.add(new Carte(Faction.Gobelins, 6, "ressources/images/Gb6.png"));
                cartes.add(new Carte(Faction.Gobelins, 7, "ressources/images/Gb7.png"));
                cartes.add(new Carte(Faction.Gobelins, 8, "ressources/images/Gb8.png"));
                cartes.add(new Carte(Faction.Gobelins, 9, "ressources/images/Gb9.png"));
                //ajout des morts vivants
                cartes.add(new Carte(Faction.MortsVivants, 0, "ressources/images/Mv0.png"));
                cartes.add(new Carte(Faction.MortsVivants, 1, "ressources/images/Mv1.png"));
                cartes.add(new Carte(Faction.MortsVivants, 2, "ressources/images/Mv2.png"));
                cartes.add(new Carte(Faction.MortsVivants, 3, "ressources/images/Mv3.png"));
                cartes.add(new Carte(Faction.MortsVivants, 4, "ressources/images/Mv4.png"));
                cartes.add(new Carte(Faction.MortsVivants, 5, "ressources/images/Mv5.png"));
                cartes.add(new Carte(Faction.MortsVivants, 6, "ressources/images/Mv6.png"));
                cartes.add(new Carte(Faction.MortsVivants, 7, "ressources/images/Mv7.png"));
                cartes.add(new Carte(Faction.MortsVivants, 8, "ressources/images/Mv8.png"));
                cartes.add(new Carte(Faction.MortsVivants, 9, "ressources/images/Mv9.png"));
                //ajout des Nains
                cartes.add(new Carte(Faction.Nains, 0, "ressources/images/N0.png"));
                cartes.add(new Carte(Faction.Nains, 1, "ressources/images/N1.png"));
                cartes.add(new Carte(Faction.Nains, 2, "ressources/images/N2.png"));
                cartes.add(new Carte(Faction.Nains, 3, "ressources/images/N3.png"));
                cartes.add(new Carte(Faction.Nains, 4, "ressources/images/N4.png"));
                cartes.add(new Carte(Faction.Nains, 5, "ressources/images/N5.png"));
                cartes.add(new Carte(Faction.Nains, 6, "ressources/images/N6.png"));
                cartes.add(new Carte(Faction.Nains, 7, "ressources/images/N7.png"));
                cartes.add(new Carte(Faction.Nains, 8, "ressources/images/N8.png"));
                cartes.add(new Carte(Faction.Nains, 9, "ressources/images/N9.png"));

                return cartes;
        }
        
        public Joueur getJ1() {
                return j1;
        }

        public void setJ1(Joueur j1) {
                this.j1 = j1;
        }

        public Joueur getJ2() {
                return j2;
        }

        public void setJ2(Joueur j2) {
                this.j2 = j2;
        }

        public ArrayList<Coup> getHistorique() {
                return historique;
        }

        public void setHistorique(ArrayList<Coup> historique) {
                this.historique = historique;
        }

        public Carte getCarteJ1() {
                return carteJ1;
        }

        public void setCarteJ1(Carte carteJ1) {
                this.carteJ1 = carteJ1;
        }

        public Carte getCarteJ2() {
                return carteJ2;
        }

        public void setCarteJ2(Carte carteJ2) {
                this.carteJ2 = carteJ2;
        }

        public Carte getCarteEnJeu() {
                return carteEnJeu;
        }

        public void setCarteEnJeu(Carte carteEnJeu) {
                this.carteEnJeu = carteEnJeu;
        }

        public ArrayList<Integer> getScore() {
                return score;
        }

        public void setScore(ArrayList<Integer> score) {
                this.score = score;
        }

        public Stack<Carte> getPioche() {
                return pioche;
        }

        public void setPioche(Stack<Carte> pioche) {
                this.pioche = pioche;
        }

        public ArrayList<Carte> getDefausse() {
                return defausse;
        }

        public void setDefausse(ArrayList<Carte> defausse) {
                this.defausse = defausse;
        }

        public int getPhase() {
                return phase;
        }

        public void setPhase(int phase) {
                this.phase = phase;
        }
        
        
        public boolean isJ1Courant() {
                return j1Courant;
        }

        public void setJ1Courant(boolean j1Courant) {
                this.j1Courant = j1Courant;
        }

        public boolean isFini() {
                return fini;
        }

        public void setFini(boolean fini) {
                this.fini = fini;
        }


}
