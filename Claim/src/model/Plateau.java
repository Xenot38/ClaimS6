package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;

public class Plateau implements Serializable{

        boolean j1Courant;
        private Joueur j1;
        private Joueur j2;
        private ArrayList<Coup> historique;
        private Stack<Coup> contreHistorique;
        private Carte carteJ1;
        private Carte carteJ2;
        private Carte carteEnJeu;
        private Carte carteEnJeuPerdant;
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
                contreHistorique = new Stack<Coup>();
                defausse = new ArrayList<Carte>();
                score = new ArrayList<Integer>();
                for (int i = 0; i < 5; i++) {
                        score.add(0);
                }
                carteJ1 = null;
                carteJ2 = null;
                carteEnJeu = null;
                pioche = genereCartes();
                
                //Pour l'IA Moyenne
                ArrayList<Carte> piocheTemp = genereCartesArrayList();
                
                
                
                Collections.shuffle(pioche);
                ArrayList<Carte> mainTemp = new ArrayList<Carte>();
                for (int i = 0; i < 13; i++) {
                        mainTemp.add(pioche.pop());
                }
                j1 = new JoueurHumain((ArrayList<Carte>) mainTemp.clone(), true);
                j1.rangerMain();
                mainTemp.clear();
                for (int i = 0; i < 13; i++) {
                        mainTemp.add(pioche.pop());
                }
                switch (difficulte) {
                        case "facile":
                                j2 = new JoueurIAFacile(mainTemp, false);
                                break;
                        case "moyen":
                                j2 = new JoueurIAMoyen(mainTemp, false, piocheTemp);
                                break;
                        case "difficile":
                                j2 = new JoueurIADifficile(mainTemp, false, piocheTemp);
                                break;
                }
                j2.rangerMain();
        }
        
        public Plateau(String difficulte, boolean IAvsIA) {
                phase = 1;
                j1Courant = true;
                fini = false;
                historique = new ArrayList<Coup>();
                contreHistorique = new Stack<Coup>();
                defausse = new ArrayList<Carte>();
                score = new ArrayList<Integer>();
                for (int i = 0; i < 5; i++) {
                        score.add(0);
                }
                carteJ1 = null;
                carteJ2 = null;
                carteEnJeu = null;
                pioche = genereCartes();
                
                //Pour l'IA Moyenne
                ArrayList<Carte> piocheTemp = genereCartesArrayList();
                
                
                
                Collections.shuffle(pioche);
                ArrayList<Carte> mainTemp = new ArrayList<Carte>();
                for (int i = 0; i < 13; i++) {
                        mainTemp.add(pioche.pop());
                }
                j1 = new JoueurIADifficile((ArrayList<Carte>) mainTemp.clone(), true, piocheTemp);
                j1.rangerMain();
                mainTemp.clear();
                for (int i = 0; i < 13; i++) {
                        mainTemp.add(pioche.pop());
                }
                switch (difficulte) {
                        case "facile":
                                j2 = new JoueurIAFacile(mainTemp, false);
                                break;
                        case "moyen":
                                j2 = new JoueurIAMoyen(mainTemp, false, piocheTemp);
                                break;
                        case "difficile":
                                j2 = new JoueurIADifficile(mainTemp, false, piocheTemp);
                                break;
                }
                j2.rangerMain();
        }
        
        

        public Plateau(boolean j1Courant, Joueur j1, Joueur j2, ArrayList<Coup> historique, Stack<Coup> contreHistorique, Carte carteJ1, Carte carteJ2, Carte carteEnJeu, Carte carteEnJeuPerdant, Stack<Carte> pioche, ArrayList<Carte> defausse, ArrayList<Integer> score, int phase, boolean fini) {
                this.j1Courant = j1Courant;
                this.j1 = j1;
                this.j2 = j2;
                this.historique = historique;
                this.contreHistorique = contreHistorique;
                this.carteJ1 = carteJ1;
                this.carteJ2 = carteJ2;
                this.carteEnJeu = carteEnJeu;
                this.carteEnJeuPerdant = carteEnJeuPerdant;
                this.pioche = pioche;
                this.defausse = defausse;
                this.score = score;
                this.phase = phase;
                this.fini = fini;
        }



        public int calculPli()/*Calcule le pli en cours, prenant en compte les spécificités des factions. Appelle gagnePli, qui gère la fin du pli*/ {
                int res = -2;
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
                                res = gagnePli(c1, c2);
                        } else {
                                res = gagnePli(c2, c1);
                        }
                } else {
                        if (c1.getFaction() == Faction.Gobelins && c2.getFaction() == Faction.Chevaliers) {
                                res = gagnePli(c2, c1);
                        } else {
                                res = gagnePli(c1, c2);
                        }
                }
                return res;
        }

        public int gagnePli(Carte cG, Carte cP)/*gère les fins de plis. cG = carte gagnante, cP = carte perdante*/ {
                int res = -2;
                boolean prioCoup = j1Courant;
                if (cG == carteJ1) {//victoire du J1
                        if (phase == 1) {                                       //Pendant la phase 1, les morts vivants vont directement dans le score du gagnant et non dans la défausse
                                j1.gagnerPartisan(carteEnJeu);
                                j2.gagnerPartisan(carteEnJeuPerdant);
                                j1Courant = true;
                                if (cG.getFaction() == Faction.MortsVivants) {
                                        j1.gagnerCarte(cG);
                                }else{
                                        defausse.add(cG);
                                }
                                if (cP.getFaction() == Faction.MortsVivants) {
                                        j1.gagnerCarte(cP);
                                }else{
                                        defausse.add(cP);
                                }
                        } else {                                                //Pendant la phase 2, les Nains vont dans le score du perdant et non du gagnant.
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
                        if (phase == 1) {                                       //comme ci-dessus
                                j2.gagnerPartisan(carteEnJeu);
                                j1.gagnerPartisan(carteEnJeuPerdant);
                                j1Courant = true;                              
                                if (cG.getFaction() == Faction.MortsVivants) {
                                        j2.gagnerCarte(cG);
                                }else{
                                        defausse.add(cG);
                                }
                                if (cP.getFaction() == Faction.MortsVivants) {
                                        j2.gagnerCarte(cP);
                                }else{
                                        defausse.add(cP);                                        
                                }
                        } else {                                                //comme ci-dessus
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
                //A la fin de chaque pli, on reset les cartes jouées et les cartes en jeu, no enregistre le pli dans l'historique et l'on vérifie que la phase n'est pas finie
                if (j1.getMain().isEmpty() && phase == 1) {             //Si la phase 1 est finie, la liste des partisans est transférée dans la main du joueur.
                        j1.setMain(j1.getCartesPartisans());
                        j1.setCartesPartisans(null);
                        j1.rangerMain();
                        j2.setMain(j2.getCartesPartisans());
                        j2.setCartesPartisans(null);
                        j2.rangerMain();
                        phase = 2;
                }if(j1.getMain().isEmpty() && phase == 2){              //Si la phase 2 est finie, on calcule le score des 2 joueurs pour déterminer un gagnant.
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
                                System.out.println("Victoire J1");
                                res = 1;
                        }else if(scoreJ1 == scoreJ2){
                                System.out.println("Egalité !");
                                res = -1;
                        }else{
                                System.out.println("Victoire J2");
                                res = 0;
                        }
                       
                }
                contreHistorique.clear();       //On vide le contre historique car il n'est plus synchronisé avec le reste du jeu
                stockagePliHistorique(prioCoup);
                carteJ1 = null;
                carteJ2 = null;
                carteEnJeu = null;
                carteEnJeuPerdant = null;
                
                return res;
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
                if(cartesJ1.size() > cartesJ2.size()){                          //Si le J1 a plus de cartes de la faction, il la remporte.
                        return 1;
                }else if(cartesJ1.size()==cartesJ2.size()){                     //
                        int forceMaxJ1 = -1;                                    //
                        Iterator<Carte> iterJ1 = cartesJ1.iterator();           //
                        while(iterJ1.hasNext()){                                //
                                Carte carteCourante = iterJ1.next();            //
                                if(carteCourante.getForce() > forceMaxJ1){      //
                                        forceMaxJ1 = carteCourante.getForce();  //
                                }                                               //Si les deux joueurs ont le même nombre de cartes d'une faction, on cherche la carte la plus haute des deux mains
                        }                                                       //
                        int forceMaxJ2 = -1;                                    //
                        Iterator<Carte> iterJ2 = cartesJ2.iterator();           //
                        while(iterJ2.hasNext()){                                //
                                Carte carteCourante = iterJ2.next();            //
                                if(carteCourante.getForce() > forceMaxJ2){      //
                                        forceMaxJ2 = carteCourante.getForce();  //
                                }
                        }
                        if(forceMaxJ1 == forceMaxJ2){                           //Si la force J1 et J2 est la même, on se trouve soit dans le cas ou aucune carte de cette faction n'été jouée, ou les deux joueurs avaient pour plus haute carte de gobelins un 0. Dans ces cas, la faction ne revient a personne.
                                return -1;
                        }
                        else if(forceMaxJ1>forceMaxJ2){                         //Si la carte la plus haute des deux mains appartient a J1, J1 remporte la faction.
                                return 1;
                        }else{
                                return 0;                                       //Si la carte la plus haute des deux mains appartient a J2, J2 remporte la faction.
                        }
                }else{                                                          //Si J2 a plus de cartes de la faction, il remporte la faction.
                        return 0;
                }
        } 
        
        public void afficherScore(ArrayList<Integer> score){                    //Affiche l'affiliation de toutes les factions
                Iterator<Integer> iterScore = score.iterator();
                afficherFaction(iterScore.next(), "Chevaliers");
                afficherFaction(iterScore.next(), "Doppelgangers");
                afficherFaction(iterScore.next(), "Gobelins");
                afficherFaction(iterScore.next(), "Morts-Vivants");
                afficherFaction(iterScore.next(), "Nains");                
        }
        
        public void afficherFaction(int i, String faction){                     //Affiche le joueur qui a remporté la faction fournie.
                if(i==1){
                        System.out.println("la faction "+ faction + " a été remportée par le joueur 1.");
                }else if(i==0){
                        System.out.println("la faction "+ faction + " a été remportée par le joueur 2.");                        
                }else{
                        System.out.println("la faction "+ faction + "n'a été remportée par aucune équipe.");
                }
        }
        
        public void stockagePliHistorique(boolean prioCoup) {                                   //gestion de l'historique
                Coup co;
                if (carteEnJeu != null) {
                        co = new Coup(carteJ1, carteJ2, carteEnJeu, carteEnJeuPerdant, j1Courant, prioCoup);
                } else {
                        co = new Coup(carteJ1, carteJ2, j1Courant, prioCoup);
                }
                historique.add(co);
        }

        public void annuler(){
                if (historique.size()<1){
                                System.out.println("Il n'y pas de coups dans l'historique, on ne peut pas remonter plus haut.");
                }else{
                        Coup coupPrecedent = historique.get(historique.size()-1);
                        if(phase == 1){
                                j1.getMain().add(coupPrecedent.getCarteJ1());
                                defausse.remove(coupPrecedent.getCarteJ1());
                                j2.getMain().add(coupPrecedent.getCarteJ2());
                                defausse.remove(coupPrecedent.getCarteJ2());
                                carteEnJeu = coupPrecedent.getCarteEnJeu();
                                carteEnJeuPerdant = coupPrecedent.getCarteEnJeuPerdant();
                                if(coupPrecedent.isVictoireJ1()){
                                        j1.getCartesPartisans().remove(coupPrecedent.getCarteEnJeu());
                                        j2.getCartesPartisans().remove(coupPrecedent.getCarteEnJeuPerdant());
                                }else{
                                        j2.getCartesPartisans().remove(coupPrecedent.getCarteEnJeu());
                                        j1.getCartesPartisans().remove(coupPrecedent.getCarteEnJeuPerdant());
                                }
                        }else{
                                if (j1.getMain().size()==13){           //Si l'on doit revenir a la phase 1
                                        phase  = 1;
                                        j1.setCartesPartisans((ArrayList<Carte>)j1.getMain().clone());
                                        j1.getMain().clear();
                                        j1.getMain().add(coupPrecedent.getCarteJ1());
                                        j2.setCartesPartisans((ArrayList<Carte>)j2.getMain().clone());
                                        j2.getMain().clear();
                                        j2.getMain().add(coupPrecedent.getCarteJ2());
                                        if(coupPrecedent.isVictoireJ1()){
                                                j1.getCartesPartisans().remove(coupPrecedent.getCarteEnJeu());
                                                j2.getCartesPartisans().remove(coupPrecedent.getCarteEnJeuPerdant());
                                        }else{
                                                j2.getCartesPartisans().remove(coupPrecedent.getCarteEnJeu());
                                                j1.getCartesPartisans().remove(coupPrecedent.getCarteEnJeuPerdant());
                                        }                                 
                                }else{                                  //Si l'on reste dans la phase 2
                                        j1.getCartesScore().remove(coupPrecedent.getCarteJ1());
                                        j1.getMain().add(coupPrecedent.getCarteJ1());
                                        j2.getCartesScore().remove(coupPrecedent.getCarteJ2());
                                        j2.getMain().add(coupPrecedent.getCarteJ2());
                                        
                                }
                        }
                        if(coupPrecedent.getCarteJ1().getFaction() == Faction.MortsVivants){
                                j1.getCartesScore().remove(coupPrecedent.getCarteJ1());
                                j2.getCartesScore().remove(coupPrecedent.getCarteJ1());                                
                        }
                        if(coupPrecedent.getCarteJ2().getFaction() == Faction.MortsVivants){
                                j1.getCartesScore().remove(coupPrecedent.getCarteJ2());
                                j2.getCartesScore().remove(coupPrecedent.getCarteJ2());                                
                        }
                        contreHistorique.add(coupPrecedent);
                        historique.remove(coupPrecedent);
                        j1Courant = coupPrecedent.isPrioJ1();
                        j1.rangerMain();
                        j2.rangerMain();
                }
        }
        
        public void refaire(){
                if(contreHistorique.size()>0){
                        Coup coupRefait = contreHistorique.pop();
                        if(phase == 1){
                                j1.getMain().remove(coupRefait.getCarteJ1());
                                if(coupRefait.getCarteJ1().getFaction()==Faction.MortsVivants){         //Gestion de la mise dans la pile score des morts vivants (carteJ1)
                                        if(coupRefait.isVictoireJ1()){
                                                j1.getCartesScore().add(coupRefait.getCarteJ1());
                                        }else{
                                                j2.getCartesScore().add(coupRefait.getCarteJ1());
                                        }
                                }else{
                                        defausse.add(coupRefait.getCarteJ1());                                                
                                }
                                j2.getMain().remove(coupRefait.getCarteJ2());
                                if(coupRefait.getCarteJ2().getFaction()==Faction.MortsVivants){         //Gestion de la mise dans la pile sore des morts vivants (carteJ2)
                                        if(coupRefait.isVictoireJ1()){
                                                j1.getCartesScore().add(coupRefait.getCarteJ2());
                                        }else{
                                                j2.getCartesScore().add(coupRefait.getCarteJ2());
                                        }
                                }else{
                                        defausse.add(coupRefait.getCarteJ2());                                                
                                }
                                if(coupRefait.isVictoireJ1()){
                                        j1.getCartesPartisans().add(coupRefait.getCarteEnJeu());
                                        j2.getCartesPartisans().add(coupRefait.getCarteEnJeuPerdant());
                                }else{
                                        j2.getCartesPartisans().add(coupRefait.getCarteEnJeu());
                                        j1.getCartesPartisans().add(coupRefait.getCarteEnJeuPerdant());
                                }
                                if(j1.getMain().size()==0){
                                        
                                        j1.setMain((ArrayList<Carte>)j1.getCartesPartisans().clone());
                                        j1.getCartesPartisans().clear();
                                        j2.setMain((ArrayList<Carte>)j2.getCartesPartisans().clone());
                                        j2.getCartesPartisans().clear();
                                }
                        }else{
                                j1.getMain().remove(coupRefait.getCarteJ1());
                                j2.getMain().remove(coupRefait.getCarteJ2());
                                if(coupRefait.isVictoireJ1()){
                                        if(coupRefait.getCarteJ1().getFaction()==Faction.Nains){
                                                j2.getCartesScore().add(coupRefait.getCarteJ1());
                                        }else{
                                                j1.getCartesScore().add(coupRefait.getCarteJ1());
                                        }
                                        if(coupRefait.getCarteJ2().getFaction()==Faction.Nains){
                                                j2.getCartesScore().add(coupRefait.getCarteJ2());
                                        }else{
                                                j1.getCartesScore().add(coupRefait.getCarteJ2());
                                        }
                                }else{
                                        if(coupRefait.getCarteJ1().getFaction()==Faction.Nains){
                                                j1.getCartesScore().add(coupRefait.getCarteJ1());
                                        }else{
                                                j2.getCartesScore().add(coupRefait.getCarteJ1());
                                        }
                                        if(coupRefait.getCarteJ1().getFaction()==Faction.Nains){
                                                j1.getCartesScore().add(coupRefait.getCarteJ2());
                                        }else{
                                                j2.getCartesScore().add(coupRefait.getCarteJ2());
                                        }
                                }
                        }
                        j1Courant = coupRefait.isVictoireJ1();
                        j1.rangerMain();
                        j2.rangerMain();
                        if(contreHistorique.size()>0){
                                carteEnJeu = contreHistorique.peek().getCarteEnJeu();
                                carteEnJeuPerdant = contreHistorique.peek().getCarteEnJeuPerdant();
                        }
                        historique.add(coupRefait);                        
                }else{
                        System.out.println("Le contre historique est vide, on ne peut pas rejouer de coup.");
                }
        }
        
        public Stack<Carte> genereCartes() {                                    //Génère toutes les cartes du jeu
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

        public Stack<Coup> getContreHistorique() {
            return contreHistorique;
        }

        public void setContreHistorique(Stack<Coup> contreHistorique) {
            this.contreHistorique = contreHistorique;
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

        public Carte getCarteEnJeuPerdant() {
                return carteEnJeuPerdant;
        }

        public void setCarteEnJeuPerdant(Carte carteEnJeuPerdant) {
                this.carteEnJeuPerdant = carteEnJeuPerdant;
        }
        
    private ArrayList<Carte> genereCartesArrayList() {
        ArrayList<Carte> cartes = new ArrayList();
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
    
    public boolean containsCard(ArrayList<Carte> cartes, Carte carte){
        boolean b =false;
        Iterator<Carte> it = cartes.iterator();
        while (it.hasNext()) {
            Carte c = it.next();
            if(c.getFaction() == carte.getFaction() && c.getForce() == carte.getForce()){
                b = true;
            }
        }
        return b;
    }
    
    public int getNBCartesScore(boolean isJ1 ,Faction f){
        int nbCartes = 0;
        
        ArrayList<Carte> cartesScores = new ArrayList();
        
        if(isJ1){
            cartesScores = getJ1().getCartesScore();
        }else{
            cartesScores = getJ2().getCartesScore();
        }
        
        Iterator<Carte> it = cartesScores.iterator();
        while (it.hasNext()) {
            Carte c = it.next();
            if(c.getFaction() == f){
                nbCartes++;
            }
        }
        return nbCartes;
    }
    
    public int getMaxCartesScore(boolean isJ1 ,Faction f){
        int maxCartes = 0;
        
        ArrayList<Carte> cartesScores = new ArrayList();
        
        if(isJ1){
            cartesScores = getJ1().getCartesScore();
        }else{
            cartesScores = getJ2().getCartesScore();
        }
        
        Iterator<Carte> it = cartesScores.iterator();
        while (it.hasNext()) {
            Carte c = it.next();
            if(c.getFaction() == f && c.getForce() > maxCartes){
                maxCartes = c.getForce();
            }
        }
        return maxCartes;
    }

    boolean egal(Plateau pNewConfig) {
        if(this.j1Courant == pNewConfig.isJ1Courant() && this.j1.egal(pNewConfig.getJ1()) && this.j2.egal(pNewConfig.getJ2())){
            if((this.carteJ1 == null && pNewConfig.getCarteJ1() == null)){
                if((this.carteJ2 == null && pNewConfig.getCarteJ2() == null)){
                    return true;
                }else{
                    if(this.carteJ2.egal(pNewConfig.getCarteJ2())){
                        return true;
                    }else{
                        return false;
                    }
                }
            }else{
                if(this.carteJ1.egal(pNewConfig.getCarteJ1())){
                    return true;
                }else{
                    return false;
                }
            }
            
            
        }else{
            return false;
        }
    }

    String hashString() {
        //les 2 joueurs(les 2 piles scores / les 2 mains) / et les 2 cartes au milieux
        String hashcode = "";
        
        //Main J1
        hashcode+="MainJ1[";
        Iterator<Carte> it = getJ1().getMain().iterator();
        while (it.hasNext()) {
            Carte c = it.next();
            hashcode+=c.getFaction().toString() + c.getForce();
            hashcode+=" ";
        }
        hashcode+="]";
        
        //Main J2
        hashcode+="MainJ2[";
        it = getJ2().getMain().iterator();
        while (it.hasNext()) {
            Carte c = it.next();
            hashcode+=c.getFaction().toString() + c.getForce();
            hashcode+=" ";
        }
        hashcode+="]";
        
        //Score J1
        hashcode+="ScoreJ1[";
        it = getJ1().getCartesScore().iterator();
        while (it.hasNext()) {
            Carte c = it.next();
            hashcode+=c.getFaction().toString() + c.getForce();
            hashcode+=" ";
        }
        hashcode+="]";
        
        //Score J2
        hashcode+="ScoreJ2[";
        it = getJ2().getCartesScore().iterator();
        while (it.hasNext()) {
            Carte c = it.next();
            hashcode+=c.getFaction().toString() + c.getForce();
            hashcode+=" ";
        }
        hashcode+="]";
        
        //Carte J1
        hashcode+="CarteJ1[";
        if(getCarteJ1()==null){
            hashcode+="null";
        }else{
            hashcode+=getCarteJ1().getFaction().toString() + getCarteJ1().getForce();
        }
        hashcode+="]";
        
        //Carte J2
        hashcode+="CarteJ2[";
        if(getCarteJ2()==null){
            hashcode+="null";
        }else{
            hashcode+=getCarteJ2().getFaction().toString() + getCarteJ2().getForce();
        }
        hashcode+="]";
        
        return hashcode;
    }

   
}
