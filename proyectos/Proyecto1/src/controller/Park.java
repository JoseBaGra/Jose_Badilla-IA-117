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
    ArrayList<Point> _route;
    
    public Park(Point pMovement, ArrayList<Point> pRoute) {
        super(pMovement);
        _route = pRoute;
    }
    
    @Override
    public void execute(TaxiSimulator pTaxiSimulator){
        Point taxiLocation = pTaxiSimulator.getTaxiLocation();
        String[] map = pTaxiSimulator.getPlottableMap();
        
        
        if(pTaxiSimulator.isShowTravel()){
            for(Point point : _route ){
                map[point.x] = Utils.changeCharInPosition(point.y, Utils.route, map[point.x]);
            }
        }
        
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
