/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author joseb
 */
public class Park extends Action{
    
    public Park(Point pMovement) {
        super(pMovement);
    }
    
    @Override
    public void execute(TaxiSimulator pTaxiSimulator){
        Point taxiLocation = pTaxiSimulator.getTaxiLocation();
        
        char move = ' ';
        if(taxiLocation.x > getMovement().x){move = Utils.TaxiUp;}
        else if(taxiLocation.x < getMovement().x){move = Utils.TaxiDown;}
        else if(taxiLocation.y > getMovement().y){move = Utils.TaxiLeft;}
        else if(taxiLocation.y < getMovement().y){move = Utils.TaxiRight;}
        pTaxiSimulator.setTaxiLocation(getMovement());
        
        pTaxiSimulator.addTrailPoint(taxiLocation);
        pTaxiSimulator.removeTravelPoint(taxiLocation);
        
        pTaxiSimulator.setTaxiChar(move);
        pTaxiSimulator.getMap().refreshPlottableMap();
    }
    
}
