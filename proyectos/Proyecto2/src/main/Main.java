package main;

import controller.Map;
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
        Map map = Map.getInstance();
        TaxiFrame tf = new TaxiFrame(map);
        map.addObserver(tf);
        new Thread(() -> map.run()).start();
        new Thread(() -> map.startTimer(1.5)).start();
        tf.setVisible(true);
    }
    
}
