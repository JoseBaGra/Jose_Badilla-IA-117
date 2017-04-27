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
public class TakeARide extends Action {
    
    public TakeARide(Point pMovement) {
        super(pMovement);
    }
    
    @Override
    public void execute(TaxiSimulator pTaxiSimulator){
        Point taxiLocation = pTaxiSimulator.getTaxiLocation();
        String[] map = pTaxiSimulator.getPlottableMap();
        char move = ' ';
        if(taxiLocation.x > getMovement().x){move = Utils.TaxiUp;}
        else if(taxiLocation.x < getMovement().x){move = Utils.TaxiDown;}
        else if(taxiLocation.y > getMovement().y){move = Utils.TaxiLeft;}
        else if(taxiLocation.y < getMovement().y){move = Utils.TaxiRight;}
        pTaxiSimulator.setTaxiLocation(getMovement());
        
        if(pTaxiSimulator.isShowTrail()){map[taxiLocation.x] = Utils.changeCharInPosition(taxiLocation.y, Utils.smoke, map[taxiLocation.x]);}
        else{map[taxiLocation.x] = Utils.changeCharInPosition(taxiLocation.y, Utils.navigableSpace, map[taxiLocation.x]);}
        
        map[getMovement().x] = Utils.changeCharInPosition(getMovement().y, move, map[getMovement().x]);
    }
}
