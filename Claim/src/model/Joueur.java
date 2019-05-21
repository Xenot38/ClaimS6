package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class Joueur implements Serializable {

    ArrayList<Carte> main;
    ArrayList<Carte> cartesScore;
    ArrayList<Carte> cartesPartisans;
    boolean isJ1;

    public Joueur(ArrayList<Carte> mainDebut, boolean isJ1) {// Constructeur en début de partie
        main = mainDebut;
        cartesScore = new ArrayList<Carte>();
        cartesPartisans = new ArrayList<Carte>();
        setIsJ1(isJ1);
    }

    public boolean getIsJ1() {
        return isJ1;
    }

    public void setIsJ1(boolean isJ1) {
        this.isJ1 = isJ1;
    }

    public ArrayList<Carte> getMain() {
        return main;
    }

    public void setMain(ArrayList<Carte> main) {
        this.main = main;
    }

    public ArrayList<Carte> getCartesScore() {
        return cartesScore;
    }

    public void setCartesScore(ArrayList<Carte> cartesScore) {
        this.cartesScore = cartesScore;
    }

    public ArrayList<Carte> getCartesPartisans() {
        return cartesPartisans;
    }

    public void setCartesPartisans(ArrayList<Carte> cartesPartisans) {
        this.cartesPartisans = cartesPartisans;
    }

    public void gagnerPartisan(Carte c) {
        cartesPartisans.add(c);
    }

    public void gagnerCarte(Carte c) {
        cartesScore.add(c);
    }

    public Carte choisirCarte(int index) {// Si le joueur a gagné il peut choisir n'importe quelle carte
        return main.remove(index);
    }

    public Carte choisirCarte(int index, Carte cAdversaire) {// Si le joueur a perdu au tour précédent, il doit jouer de la même faction que son adversaire si il le peut, a moins de jouer doppelganger. Si il n'a aucune carte de la faction adverse, alors il peut jouer n'importe quelle carte.
        Carte cChoisie = main.get(index);
        if (cChoisie.getFaction() == cAdversaire.getFaction() || cChoisie.getFaction() == Faction.Doppelgangers) {
            main.remove(index);
            return cChoisie;
        } else {
            Iterator<Carte> iter = main.iterator();
            boolean aFaction = false;                       //Tant qu'on ne trouve pas de carte de la faction demandée, cette variable reste false. soi on en trouve, alors elle devient true et reestreint le choix des cartes.
            while (iter.hasNext()) {
                if (iter.next().getFaction() == cAdversaire.getFaction()) {
                    aFaction = true;
                }
            }
            if (aFaction) {
                return null;
            } else {
                main.remove(index);
                return cChoisie;
            }
        }
    }

    public ArrayList<Carte> getCartesJouable(Carte carteJ1) {
        if (carteJ1 == null){
            return this.getMain();
        }
        Iterator<Carte> it = getMain().iterator();
        ArrayList<Carte> carteJouables = new ArrayList();
        ArrayList<Carte> carteDoppel = new ArrayList();

        while (it.hasNext()) {
            Carte c = it.next();
            //la carte est jouable si les fac sont compatibles
            if (c.getFaction() == carteJ1.getFaction()) {
                carteJouables.add(c);
            } else if (c.getFaction() == Faction.Doppelgangers) {
                carteDoppel.add(c);
            }
        }
        //on return toutes les cartes de notre main si aucune de nos cartes n'est compatible
        if (carteJouables.isEmpty()) {
            carteJouables.addAll(getMain());
        } else {
            carteJouables.addAll(carteDoppel);
        }
        return carteJouables;
    }

    public ArrayList<Carte> getCartesGagnante(Carte carteJ1) {
        Iterator<Carte> it = getMain().iterator();
        ArrayList<Carte> carteJouables = new ArrayList();
        ArrayList<Carte> autresCartes = new ArrayList();
        ArrayList<Carte> cartesGagnantes = new ArrayList();
        ArrayList<Carte> cartesChevalier = new ArrayList();
        ArrayList<Carte> carteDoppel = new ArrayList();

        while (it.hasNext()) {
            Carte c = it.next();
            //si notre carte est de la même fac ou est un doppel, elle est jouable
            if (c.getFaction() == carteJ1.getFaction()) {
                carteJouables.add(c);
                //si sa force est supérieure , elle est gagnante
                if (c.getForce() > carteJ1.getForce()) {
                    System.out.println("Carte Gagnante: " + c.getFaction() + c.getForce());
                    cartesGagnantes.add(c);
                }
            } else if (c.getFaction() == Faction.Doppelgangers) {
                carteDoppel.add(c);
                //si sa force est supérieure , elle est gagnante
                if (c.getForce() > carteJ1.getForce()) {
                    System.out.println("Carte gagante: " + c.getFaction() + c.getForce());
                    cartesGagnantes.add(c);
                }
            } else {
                //si la fac n'est pas compatible mais que l'adversaire est un gobelin
                // et que l'on a un chevalier, on l'ajoute
                if (carteJ1.getFaction() == Faction.Gobelins && c.getFaction() == Faction.Chevaliers) {
                    System.out.println("Carte Autre: " + c.getFaction() + c.getForce());
                    cartesChevalier.add(c);
                }
                autresCartes.add(c);
            }
        }
        // si on a aucune carte gagnante et/ou jouable, on return les chevaliers
        // ( qui sont soit vide soit pleins si gobelin)
        if (cartesGagnantes.isEmpty()) {
            if (carteJouables.isEmpty()) {
                System.out.println("test5");
                return cartesChevalier;
            } else {
                System.out.println("test6");
                return cartesGagnantes;
            }
        } else {
            //on vérifie si nos cartes gagnantes ne sont que des doppel
            Boolean mainCompleteDoppel = true;
            it = cartesGagnantes.iterator();
            while (mainCompleteDoppel && it.hasNext()) {
                Carte c = it.next();
                if (c.getFaction() != Faction.Doppelgangers) {
                    mainCompleteDoppel = false;
                }
            }
            //si c'est la cas, on ajoute aux cartes gagnantes les cartes chevalier 
            if (mainCompleteDoppel) {
                if (carteJouables.isEmpty()) {
                    System.out.println("test7");
                    cartesGagnantes.addAll(cartesChevalier);
                }
            }
            return cartesGagnantes;
        }
    }

    public ArrayList<Carte> getCartesPerdante(Carte carteJ1) {
        Iterator<Carte> it = getMain().iterator();
        ArrayList<Carte> carteJouables = new ArrayList();
        ArrayList<Carte> autresCartes = new ArrayList();
        ArrayList<Carte> cartesPerdantes = new ArrayList();
        ArrayList<Carte> cartesDoppel = new ArrayList();

        while (it.hasNext()) {
            Carte c = it.next();
            //si notre carte est de la même fac ou est un doppel, elle est jouable
            if (c.getFaction() == carteJ1.getFaction()) {
                carteJouables.add(c);
                //si sa force est inférieure , elle est perdante
                if (c.getForce() <= carteJ1.getForce()) {
                    cartesPerdantes.add(c);
                    System.out.println("Carte perdante: " + c.getFaction() + c.getForce());
                }
            } else if (c.getFaction() == Faction.Doppelgangers) {
                cartesDoppel.add(c);
                //si sa force est inférieure , elle est perdante
                if (c.getForce() <= carteJ1.getForce()) {
                    System.out.println("Carte perdante: " + c.getFaction() + c.getForce());
                    cartesPerdantes.add(c);
                }
            } else {
                //si on est pas dans le cas gobelin(Leader) et chevalier(2eme)
                if (!(carteJ1.getFaction() == Faction.Gobelins && c.getFaction() == Faction.Chevaliers)) {
                    autresCartes.add(c);
                    System.out.println("Carte Autre: " + c.getFaction() + c.getForce());
                }
            }
        }

        // si on a aucune carte perdante et/ou jouable, on return les autresCartes
        // qui sont les cartes non compatibles avec la faction( qui sont donc perdantes )
        if (cartesPerdantes.isEmpty()) {
            if (carteJouables.isEmpty()) {
                System.out.println("test5");
                return this.getMain();
            } else {
                System.out.println("test6");
                return cartesPerdantes;
            }
        } else {
            //on vérifie si nos cartesPerdantes ne sont que des doppel
            Boolean mainCompleteDoppel = true;
            it = cartesPerdantes.iterator();
            while (mainCompleteDoppel && it.hasNext()) {
                Carte c = it.next();
                if (c.getFaction() != Faction.Doppelgangers) {
                    mainCompleteDoppel = false;
                }
            }
            //si oui on ajoute toutes les cartes des autres factions
            if (mainCompleteDoppel) {
                if (carteJouables.isEmpty()) {
                    cartesPerdantes.addAll(autresCartes);
                }
            }
            return cartesPerdantes;
        }
    }

    public int joue(Plateau p) {
        return 0;
    }

    public void rangerMain() {
        ArrayList<Carte> temp = (ArrayList<Carte>) main.clone();
        main.clear();
        for (int i = 0; i < 5; i++) {
            Faction fCourante = Faction.Chevaliers;
            switch (i) {
                case (0):
                    fCourante = Faction.Chevaliers;
                    break;
                case (1):
                    fCourante = Faction.Doppelgangers;
                    break;
                case (2):
                    fCourante = Faction.Gobelins;
                    break;
                case (3):
                    fCourante = Faction.MortsVivants;
                    break;
                case (4):
                    fCourante = Faction.Nains;
                    break;
            }
            Iterator<Carte> ite = temp.iterator();
            ArrayList<Carte> cartesFaction = new ArrayList<>();
            while (ite.hasNext()) {
                Carte cTemp = ite.next();
                if (cTemp.getFaction() == fCourante) {
                    cartesFaction.add(cTemp);
                }
            }
            while (cartesFaction.size() > 0) {
                Iterator<Carte> iteFaction = cartesFaction.iterator();
                Carte carteHaute = new Carte(Faction.Chevaliers, -1, "");
                while (iteFaction.hasNext()) {
                    Carte cTemp = iteFaction.next();
                    if (cTemp.getForce() > carteHaute.getForce()) {
                        carteHaute = cTemp;
                    }
                }
                cartesFaction.remove(carteHaute);
                main.add(carteHaute);
            }
        }
    }
}
