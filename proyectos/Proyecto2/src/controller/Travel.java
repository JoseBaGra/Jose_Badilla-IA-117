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
    ArrayList<Point> _route;
    Client _client;
    
    public Travel(Point pMovement, Client pClient, ArrayList<Point> pRoute) {
        super(pMovement);
        _route = pRoute;
        _client = pClient;
    }
    
    @Override
    public void execute(TaxiSimulator pTaxiSimulator){
        Point taxiLocation = pTaxiSimulator.getTaxiLocation();
        String[] map = pTaxiSimulator.getPlottableMap();
        
        map[_client.getStart().x] = Utils.changeCharInPosition(_client.getStart().y, Utils.navigableSpace, map[_client.getStart().x]);
        
        ArrayList<Client> clients = pTaxiSimulator.getClients();
        clients.remove(_client);
        pTaxiSimulator.setClients(clients);
        
        if(pTaxiSimulator.isShowTravel()){
            for(Point point : _route ){
                map[point.x] = Utils.changeCharInPosition(point.y, Utils.route, map[point.x]);
            }
        }
        
        char move = ' ';
        if(taxiLocation.x > getMovement().x){move = Utils.BusyTaxiUp;}
        else if(taxiLocation.x < getMovement().x){move = Utils.BusyTaxiDown;}
        else if(taxiLocation.y > getMovement().y){move = Utils.BusyTaxiLeft;}
        else if(taxiLocation.y < getMovement().y){move = Utils.BusyTaxiRight;}
        pTaxiSimulator.setTaxiLocation(getMovement());
        
        if(pTaxiSimulator.isShowTrail()){map[taxiLocation.x] = Utils.changeCharInPosition(taxiLocation.y, Utils.smoke, map[taxiLocation.x]);}
        else{map[taxiLocation.x] = Utils.changeCharInPosition(taxiLocation.y, Utils.navigableSpace, map[taxiLocation.x]);}
        
        map[getMovement().x] = Utils.changeCharInPosition(getMovement().y, move, map[getMovement().x]);
    }
    
}
