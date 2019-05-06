/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controler;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author baratinm
 */
public class mainTest1 extends Application {

        /**
         * @param args the command line arguments
         */
        public static void main(String[] args) {
                launch(args);
                
        }
        
        @Override
        public void start(Stage stage) {
                
                System.out.println("luuul");
                ControleurJeu monControleur = new ControleurJeu();
                monControleur.lancerJeu();
        }
        
}
