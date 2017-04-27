/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Point;

/**
 *
 * @author joseb
 */
public class Action {
    private Point _movement;
    public Action(Point pMovement) {
        _movement = pMovement;
    }
    public void execute(TaxiSimulator pTaxiSimulator){
        // TODO code application logic here
    }
    
    public void fixMap(TaxiSimulator pTaxiSimulator){
        for(Client client : pTaxiSimulator.getClients()){
            String[] map = pTaxiSimulator.getPlottableMap();
            map[client.getStart().x] = Utils.changeCharInPosition(client.getStart().y, Utils.client, map[client.getStart().x]);
            pTaxiSimulator.setPlottableMap(map);
        }
    }
            
    public Point getMovement() {return _movement;}  
    
}