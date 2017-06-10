package main;

import controller.Map;
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
        Map map = new Map();
        TaxiFrame tf = new TaxiFrame(map);
        map.addObserver(tf);
        tf.setVisible(true);
        /*
        
        TaxiOptions to = new TaxiOptions(tx);
        to.setVisible(true);
        
        tx.setTaxiFrame(tf);
        tx.start();
        */
    }
    
}
