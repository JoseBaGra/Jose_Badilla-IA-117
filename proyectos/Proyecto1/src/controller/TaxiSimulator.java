/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Reader;
import view.TaxiFrame;

/**
 *
 * @author joseb
 */
public class TaxiSimulator extends Thread{
    private final String[] _map;
    private final String[] _navigableMap;
    private String[] _plottableMap;
    private Point _taxiLocation;
    private Queue<Action> _actionsQueue;
    private TaxiFrame _taxiFrame;
    private boolean _runing = true;
    private boolean _paused = false;
    private boolean _continue = false;
    private int _sleep = 0;

    public TaxiSimulator(){
        _map = Reader.readFileWithoutTaxi();
        _taxiLocation = Reader.getTaxiLocation();
        _navigableMap = Utils.makeNavigableMap(_map);
        _plottableMap = _map.clone();
        _plottableMap[_taxiLocation.x] = Utils.changeCharInPosition(_taxiLocation.y, Utils.TaxiUp, _plottableMap[_taxiLocation.x]);
        _actionsQueue = new LinkedList<>();
    }

    public String[] getMap() {return _map;}
    public String[] getNavigableMap() {return _navigableMap;}
    public String[] getPlottableMap() {return _plottableMap;}
    public void setPlottableMap(String[] pPlottableMap) {_plottableMap = pPlottableMap;}
    public void setTaxiFrame(TaxiFrame pTaxiFrame) {_taxiFrame = pTaxiFrame;}
    public void setSleep(int _sleep) {this._sleep = _sleep;}
    
    public Point getTaxiLocation() {return _taxiLocation;}
    public void setTaxiLocation(Point _taxiLocation) {this._taxiLocation = _taxiLocation;}
    
    
    public void takeARide(){
        ArrayList<Point> navigableSpaces = new ArrayList<>();
        
        // x and y variables are for the point so they're not insignificant
        for (int x = 0; x < _navigableMap.length; x++) {
            ArrayList<Integer> indexes = Utils.getLocationsOfChar(_navigableMap[x], Utils.navigableSpace);
            for (Integer y : indexes) {
                if(!(_taxiLocation.x == x && _taxiLocation.y == (int)y)){
                    navigableSpaces.add(new Point(x, y));
                }
            }
        }
        
        Point taxiPivot = (Point)_taxiLocation.clone();
        ArrayList<Point> moves = null;
        String[] navigableMap = _navigableMap.clone();
        while(!navigableSpaces.isEmpty()) {            
            double minValue = Integer.MAX_VALUE;
            Point minPoint = null;
            for(Point point : navigableSpaces){
                if(minValue > point.distance(taxiPivot)){
                    minValue = point.distance(taxiPivot);
                    minPoint = point;
                }
            }
            
            navigableMap[taxiPivot.x] = Utils.changeCharInPosition(taxiPivot.y, Utils.moveableTaxi, navigableMap[taxiPivot.x]);
            
            moves = Utils.AStar(navigableMap, taxiPivot, minPoint);
            for(Point move : moves){
                _actionsQueue.add(new TakeARide(move));
            }
            navigableMap[taxiPivot.x] = Utils.changeCharInPosition(taxiPivot.y, Utils.navigableSpace, navigableMap[taxiPivot.x]);
            taxiPivot.setLocation(minPoint);
            navigableMap[taxiPivot.x] = Utils.changeCharInPosition(taxiPivot.y, Utils.moveableTaxi, navigableMap[taxiPivot.x]);
            navigableSpaces.remove(minPoint);
        }
    }
    
    @Override
    public void run(){
        while(_runing){
            while(_paused){
                //stops doing anything
            }
            
            while(_sleep == 0){
                while(!_continue){
                    //stops doing anything
                    //here waits to next move
                }
                _continue = false;
            }
            if(!_actionsQueue.isEmpty()){
                _actionsQueue.poll().execute(this);
                if(_taxiFrame != null){
                    _taxiFrame.refeshLabels();
                }
                try {sleep(_sleep);} catch (InterruptedException ex) {Logger.getLogger(TaxiSimulator.class.getName()).log(Level.SEVERE, null, ex);}
            }
        }
    }
}
