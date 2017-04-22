/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Queue;
import model.Reader;

/**
 *
 * @author joseb
 */
public class TaxiSimulator {
    private final String[] _map;
    private Point _taxiLocation;
    private Queue<Action> _actionsQueue;

    public TaxiSimulator(){
        this._map = Reader.readFileWithoutTaxi();
        this._taxiLocation = Reader.getTaxiLocation();
    }
    
    public void takeARide(){
        String[] pivotMap = _map.clone();
        for (int line = 0; line < pivotMap.length; line++) {
            pivotMap[line] = pivotMap[line].replace("---", "***");
            pivotMap[line] = pivotMap[line].replaceAll("-[a-zA-Z| ]-", "***");
        }
        ArrayList<Point> navigableSpaces = new ArrayList<>();
        
        // x and y variables are for the point so they're not insignificant
        for (int x = 0; x < pivotMap.length; x++) {
            ArrayList<Integer> indexes = Utils.getLocationsOfChar(pivotMap[x], Utils.navigableSpace);
            for (Integer y : indexes) {
                if(!(_taxiLocation.x == x && _taxiLocation.y == (int)y)){
                    navigableSpaces.add(new Point(x, y));
                }
            }
        }
        System.out.println(_taxiLocation.x + " " + _taxiLocation.y);
        
        pivotMap[_taxiLocation.x] = Utils.changeCharInPosition(_taxiLocation.y, Utils.moveableTaxi, pivotMap[_taxiLocation.x]);
        Utils.AStar(pivotMap, _taxiLocation, new Point(1, 1));
        
        while(!navigableSpaces.isEmpty()) {            
            double minValue = Integer.MAX_VALUE;
            Point minPoint = null;
            for(Point point : navigableSpaces){
                if(minValue > point.distance(_taxiLocation)){
                    minValue = point.distance(_taxiLocation);
                    minPoint = point;
                }
            }
            _taxiLocation.setLocation(minPoint);
            System.out.println(_taxiLocation.x + " " + _taxiLocation.y);
            navigableSpaces.remove(minPoint);
        }
        
        Utils.printMap(pivotMap);
        
    }
}
