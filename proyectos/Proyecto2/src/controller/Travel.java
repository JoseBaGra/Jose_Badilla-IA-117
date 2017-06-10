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
        Point taxiLocation = pTaxiSimulator.getTaxiLocation();
        String[] map = pTaxiSimulator.getPlottableMap();
        
        map[_client.getStart().x] = Utils.changeCharInPosition(_client.getStart().y, Utils.navigableSpace, map[_client.getStart().x]);
        
        ArrayList<Client> clients = pTaxiSimulator.getMap().getClients();
        clients.remove(_client);
        pTaxiSimulator.getMap().setClients(clients);
        
        char move = ' ';
        if(taxiLocation.x > getMovement().x){move = Utils.BusyTaxiUp;}
        else if(taxiLocation.x < getMovement().x){move = Utils.BusyTaxiDown;}
        else if(taxiLocation.y > getMovement().y){move = Utils.BusyTaxiLeft;}
        else if(taxiLocation.y < getMovement().y){move = Utils.BusyTaxiRight;}
        pTaxiSimulator.setTaxiLocation(getMovement());
        
        pTaxiSimulator.addTrailPoint(taxiLocation);
        pTaxiSimulator.removeTravelPoint(taxiLocation);
        
        map[getMovement().x] = Utils.changeCharInPosition(getMovement().y, move, map[getMovement().x]);
    }
    
}
