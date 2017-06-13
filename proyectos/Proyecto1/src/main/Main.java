package main;

import controller.TaxiSimulator;
import controller.Utils;
import java.util.ArrayList;
import view.TaxiFrame;
import view.TaxiOptions;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author joseb
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        // TODO code application logic here
        /*
        TaxiSimulator tx = new TaxiSimulator();
        TaxiFrame tf = new TaxiFrame(tx);
        tf.setVisible(true);
        
        TaxiOptions to = new TaxiOptions(tx);
        to.setVisible(true);
        
        tx.setTaxiFrame(tf);
        tx.start();
        */
        char[][] x = {  {'1','1','0','2'},
                        {'0','1','2','0'},
                        {'0','2','1','0'},
                        {'2','1','2','1'}};
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[i].length; j++) {
                System.out.print(x[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println("\n");
        for (int i = 0; i < x[0].length; i++) {
            for (int j = 0; j < x.length; j++) {
                System.out.print(x[j][i] + " ");
            }
            System.out.println("");
        }
        System.out.println("\n");
        
        ArrayList<String> diagonals = new ArrayList<>();
        for (int i = 0; i <= x.length+2; i++) {
            diagonals.add("");
        }
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[i].length; j++) {
                diagonals.set(i+j, diagonals.get(i+j) + x[i][j] + " ");
            }
        }
        for (int i = 0; i < diagonals.size(); i++) {
            System.out.println(diagonals.get(i));
        }
        
        
        System.out.println("");
        
        ArrayList<String> diagonals2 = new ArrayList<>();
        for (int i = 0; i <= x.length+2; i++) {
            diagonals2.add("");
        }
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[i].length; j++) {
                if(i-j<0){
                    diagonals2.set(i-j+x.length+3, diagonals2.get(i-j+x.length+3) + x[i][j] + " ");
                }
                else{
                    diagonals2.set(i-j, diagonals2.get(i-j) + x[i][j] + " ");
                }
            }
        }
        for (int i = 0; i < diagonals2.size(); i++) {
            System.out.println(diagonals2.get(i));
        }
        
    }
    
}
