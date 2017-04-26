package main;

import controller.TaxiSimulator;
import controller.Utils;
import view.TaxiFrame;

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
        TaxiSimulator tx = new TaxiSimulator();
        TaxiFrame tf = new TaxiFrame(tx);
        tx.setTaxiFrame(tf);
        tf.setVisible(true);
        tx.start();
        tx.setSleep(500);
        tx.takeARide();
    }
    
}
