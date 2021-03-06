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
public class Travel extends Action{
    Client _client;
    
    public Travel(Point pMovement, Client pClient) {
        super(pMovement);
        _client = pClient;
    }
    
    @Override
    public void execute(TaxiSimulator pTaxiSimulator){
        if(getMovement() != null){
            Point taxiLocation = pTaxiSimulator.getTaxiLocation();

            char move = ' ';
            if(taxiLocation.x > getMovement().x){move = Utils.BusyTaxiUp;}
            else if(taxiLocation.x < getMovement().x){move = Utils.BusyTaxiDown;}
            else if(taxiLocation.y > getMovement().y){move = Utils.BusyTaxiLeft;}
            else if(taxiLocation.y < getMovement().y){move = Utils.BusyTaxiRight;}
            pTaxiSimulator.setTaxiLocation(getMovement());

            pTaxiSimulator.addTrailPoint(taxiLocation);
            pTaxiSimulator.removeTravelPoint(taxiLocation);

            pTaxiSimulator.setTaxiChar(move);
            _client.setActualPosition(getMovement());
        }
        else{
            _client.setIsTraveling(false);
        }
    }
    
}
