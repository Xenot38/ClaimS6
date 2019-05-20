package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class JoueurHumain extends Joueur{	
	
	public JoueurHumain(ArrayList<Carte> premiereMain, boolean  isJ1){
		super(premiereMain, isJ1);		
	}
        @Override
        public int joue(Plateau p){
                int str = -1;
                Scanner sc = new Scanner(System.in);
                while(str==-1){
                        System.out.println("Veuillez saisir un nombre :");
                        int temp = sc.nextInt();
                        if(temp>=this.getMain().size()||temp<0){
                                System.out.println(temp + " n'est pas une valeur possible, veuillez choisir entre 0 et " + (this.getMain().size()-1));
                        }else{ 
                                if(p.getCarteJ2()==null){
                                        str = temp;
                                }else{
                                        if(this.getCartesJouable(p.getCarteJ2()).contains(this.getMain().get(temp))){
                                                str = temp;
                                        }else{
                                                System.out.println("Cette carte ne peut pas etre jouée, choisissez parmis les suivantes :");
                                                Iterator<Carte> ite = this.getCartesJouable(p.getCarteJ2()).iterator();
                                                while(ite.hasNext()){
                                                        System.out.println(ite.next().affichePropCarte());
                                                }
                                        }
                                }
                        }
                }
                return str;
        }
	
	
}
