/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.FSM.FSM;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import view.TaxiFrame;

/**
 *
 * @author joseb
 */
public class TaxiSimulator extends Thread{
    private final Map _map;
    private final String _name;
    private final String[] _navigableMap;
    private Point _taxiLocation;
    private Queue<Action> _actionsQueue;
    private boolean _showTrail = false;
    private ArrayList<Point> _trailPoints;
    private boolean _showTravel = false;
    private ArrayList<Point> _travelPoints;
    private boolean _runing = true;
    private boolean _paused = false;
    private boolean _continue = false;
    private int _sleep = 0;
    private char _taxiChar = Utils.TaxiUp;
    private FSM _fsm;

    public TaxiSimulator(Map pMap, Point pLocation, String pName){
        _map = pMap;
        _name = pName;
        _taxiLocation = pLocation;
        _navigableMap = pMap.getNavigableMap();
        _trailPoints = new ArrayList<>();
        _travelPoints = new ArrayList<>();
        _actionsQueue = new LinkedList<>();
        createFSM();
    }

    private void createFSM(){
        controller.FSM.GoTo sGoTo = new controller.FSM.GoTo();
        controller.FSM.Park sPark = new controller.FSM.Park();
        controller.FSM.SearchClients sSearchClients = new controller.FSM.SearchClients();
        controller.FSM.TakeARide sTakeARide = new controller.FSM.TakeARide();
        controller.FSM.Wait sWait = new controller.FSM.Wait();
        
        controller.FSM.State[] states = {sWait,sGoTo,sPark,sSearchClients,sTakeARide};
        _fsm = new FSM(this, states);
        
        _fsm.addTransition(sGoTo,Utils.GoTo, sGoTo);
        _fsm.addTransition(sPark,Utils.GoTo, sGoTo);
        _fsm.addTransition(sSearchClients,Utils.GoTo, sGoTo);
        _fsm.addTransition(sTakeARide,Utils.GoTo, sGoTo);
        _fsm.addTransition(sWait,Utils.GoTo, sGoTo);
        
        _fsm.addTransition(sGoTo,Utils.ParkIn, sPark);
        _fsm.addTransition(sPark,Utils.ParkIn, sPark);
        _fsm.addTransition(sSearchClients,Utils.ParkIn, sPark);
        _fsm.addTransition(sTakeARide,Utils.ParkIn, sPark);
        _fsm.addTransition(sWait,Utils.ParkIn, sPark);
        
        _fsm.addTransition(sGoTo,Utils.SearchClients, sSearchClients);
        _fsm.addTransition(sPark,Utils.SearchClients, sSearchClients);
        _fsm.addTransition(sSearchClients,Utils.SearchClients, sSearchClients);
        _fsm.addTransition(sTakeARide,Utils.SearchClients, sSearchClients);
        _fsm.addTransition(sWait,Utils.SearchClients, sSearchClients);
        
        _fsm.addTransition(sGoTo,Utils.TakeARide, sTakeARide);
        _fsm.addTransition(sPark,Utils.TakeARide, sTakeARide);
        _fsm.addTransition(sSearchClients,Utils.TakeARide, sTakeARide);
        _fsm.addTransition(sTakeARide,Utils.TakeARide, sTakeARide);
        _fsm.addTransition(sWait,Utils.TakeARide, sTakeARide);
        
        _fsm.addTransition(sGoTo,Utils.Wait, sWait);
        _fsm.addTransition(sPark,Utils.Wait, sWait);
        _fsm.addTransition(sSearchClients,Utils.Wait, sWait);
        _fsm.addTransition(sTakeARide,Utils.Wait, sWait);
        _fsm.addTransition(sWait,Utils.Wait, sWait);
    }
    
    public int changeState(String pKey){
        return _fsm.changeState(pKey);
    }
    
    public void setPaused(boolean _paused) {this._paused = _paused;}
    
    public Map getMap() {return _map;}
    public String getTaxiName() {return _name;}
    public String[] getNavigableMap() {return _navigableMap;}

    public int getSleep() {return _sleep;}
    public void setSleep(int _sleep) {this._sleep = _sleep; setContinue(true);}

    public void setContinue(boolean _continue) {this._continue = _continue;}

    public boolean isShowTrail() {return _showTrail;}
    public void setShowTrail(boolean pShowTrail) {
        this._showTrail = pShowTrail;
    }

    public ArrayList<Point> getTrailPoints() {
        return _trailPoints;
    }
    public void addTrailPoint(Point p){
        for(Point pAux : _trailPoints){
            if(pAux.equals(p)){return;}
        }
        
        _trailPoints.add(p);
    }
    public void removeTrailPoint(Point p){
        ArrayList<Point> toRemove = new ArrayList<>();
        for(Point pAux : _trailPoints){
            if(pAux.equals(p)){
                toRemove.add(pAux);
            }
        }
        _trailPoints.removeAll(toRemove);
    }
    public void clearTrailPoints(){
        _trailPoints.clear();
    }
    
    public boolean isShowTravel() {return _showTravel;}
    public void setShowTravel(boolean pShowTravel) {
        this._showTravel = pShowTravel;
    }
    
    public ArrayList<Point> getTravelPoints() {
        return _travelPoints;
    }
    public void addTravelPoint(Point p){
        for(Point pAux : _travelPoints){
            if(pAux.equals(p)){return;}
        }
        
        _travelPoints.add(p);
    }
    public void removeTravelPoint(Point p){
        ArrayList<Point> toRemove = new ArrayList<>();
        for(Point pAux : _travelPoints){
            if(pAux.equals(p)){
                toRemove.add(pAux);
            }
        }
        _travelPoints.removeAll(toRemove);
    }
    public void clearTravelPoints(){
        _travelPoints.clear();
    }
    
    public Point getTaxiLocation() {return _taxiLocation;}
    public void setTaxiLocation(Point _taxiLocation) {this._taxiLocation = _taxiLocation;}

    public Queue<Action> getActionsQueue() {return _actionsQueue;}
    public void setActionsQueue(Queue<Action> _actionsQueue) {this._actionsQueue = _actionsQueue;}

    public char getTaxiChar() {return _taxiChar;}
    public void setTaxiChar(char pTaxiChar) {_taxiChar = pTaxiChar;}
    
    
    
    // 1=good, -1=not found
    public int parkIn(String pLocation){
        String[] map = _map.getMap().clone();
        boolean found = false;
        int x = 0, y = 0;
        for (x = 0; x < map.length; x++) {
            if(map[x].contains(pLocation)){
                found = true;
                y = map[x].indexOf(pLocation);
                break;
            }
        }
        
        if(found){
            Point minPoint = null;
            double minValue = Float.MAX_VALUE;
            if(minValue > _taxiLocation.distance(new Point(x-2,y-2)) && x-2 > 0 && y-2 > 0){
                minValue = _taxiLocation.distance(new Point(x-2,y-2));
                minPoint = new Point(x-2,y-2);
            }
            if(minValue > _taxiLocation.distance(new Point(x-2,y+2)) && x-2 > 0 && y+2 < _navigableMap[x].length()){
                minValue = _taxiLocation.distance(new Point(x-2,y+2));
                minPoint = new Point(x-2,y+2);
            }
            if(minValue > _taxiLocation.distance(new Point(x+2,y-2)) && x+2 < _navigableMap.length && y-2 > 0){
                minValue = _taxiLocation.distance(new Point(x+2,y-2));
                minPoint = new Point(x+2,y-2);
            }
            if(minValue > _taxiLocation.distance(new Point(x+2,y+2)) && x+2 < _navigableMap.length && y+2 < _navigableMap[x].length()){
                minValue = _taxiLocation.distance(new Point(x+2,y+2));
                minPoint = new Point(x+2,y+2);
            }
            
            Point taxiPivot = (Point)_taxiLocation.clone();
            String[] navigableMap = _navigableMap.clone();
            navigableMap[taxiPivot.x] = Utils.changeCharInPosition(taxiPivot.y, Utils.moveableTaxi, navigableMap[taxiPivot.x]);
            ArrayList<Point> moves = Utils.AStar(navigableMap, taxiPivot, minPoint);
            while(!moves.isEmpty()){
                Point move = moves.get(0);
                _travelPoints.add(move);
                moves.remove(move);
                _actionsQueue.add(new Park(move));
            }
            
            return 1;
        }
        else{
            return -1;
        }
    }
    
    // 1=good, -1=invalid
    public int goTo(int pTargetX, int pTargetY){
        if(pTargetX >= _navigableMap.length){return -1;}
        if(pTargetY >= _navigableMap[pTargetX].length()){return -1;}
        if(_navigableMap[pTargetX].charAt(pTargetY) != Utils.navigableSpace){return -1;}
        
        Point taxiPivot = (Point)_taxiLocation.clone();
        String[] navigableMap = _navigableMap.clone();
        navigableMap[taxiPivot.x] = Utils.changeCharInPosition(taxiPivot.y, Utils.moveableTaxi, navigableMap[taxiPivot.x]);
        ArrayList<Point> moves = Utils.AStar(navigableMap, taxiPivot, new Point(pTargetX, pTargetY));
        while(!moves.isEmpty()){
            Point move = moves.get(0);
            _travelPoints.add(move);
            moves.remove(move);
            _actionsQueue.add(new Park(move));
        }
        
        return 1;
    }
    
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
            double minValue = Float.MAX_VALUE;
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
    
    public void searchClients(){
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
        boolean findClient = false;
        while(!navigableSpaces.isEmpty() && !findClient) {            
            double minValue = Integer.MAX_VALUE;
            Point minPoint = null;
            Client minClient = null;
            for(Point point : navigableSpaces){
                if(minValue > point.distance(taxiPivot)){
                    minValue = point.distance(taxiPivot);
                    minPoint = point;
                }
            }
            
            navigableMap[taxiPivot.x] = Utils.changeCharInPosition(taxiPivot.y, Utils.moveableTaxi, navigableMap[taxiPivot.x]);
            
            ArrayList<Client> clients = _map.getClients();
            for(Client client : clients){
                if(client.getStart().x == minPoint.x && client.getStart().y == minPoint.y){
                    findClient = true;
                    minClient = client;
                    break;
                }
            }
            
            
            moves = Utils.AStar(navigableMap, taxiPivot, minPoint);
            for(Point move : moves){
                _actionsQueue.add(new SearchClient(move));
            }
            
            navigableMap[taxiPivot.x] = Utils.changeCharInPosition(taxiPivot.y, Utils.navigableSpace, navigableMap[taxiPivot.x]);
            taxiPivot.setLocation(minPoint);
            navigableMap[taxiPivot.x] = Utils.changeCharInPosition(taxiPivot.y, Utils.moveableTaxi, navigableMap[taxiPivot.x]);
            navigableSpaces.remove(minPoint);
            
            if(findClient){
                //System.out.println("N:" +navigableMap + " T:" + taxiPivot + " P:" + minClient.getTarget() + "\n");
                moves = Utils.AStar(navigableMap, taxiPivot, minClient.getTarget());
                while(!moves.isEmpty()){
                    Point move = moves.get(0);
                    moves.remove(move);
                    _actionsQueue.add(new Travel(move, minClient));
                    addTravelPoint(move);
                }
            }
        }
    }
    
    @Override
    public void run(){
        String lastAction = "";
        while(_runing){
            while(_paused){
                //stops doing anything
                try {sleep(1);} catch (InterruptedException ex) {Logger.getLogger(TaxiSimulator.class.getName()).log(Level.SEVERE, null, ex);}
            }
            
            if (_sleep>0) {
                try {sleep(_sleep);} catch (InterruptedException ex) {Logger.getLogger(TaxiSimulator.class.getName()).log(Level.SEVERE, null, ex);}
            }
            
            while(_sleep == 0){
                while(!_continue){
                    //stops doing anything, here waits to next move
                    try {sleep(1);} catch (InterruptedException ex) {Logger.getLogger(TaxiSimulator.class.getName()).log(Level.SEVERE, null, ex);}
                }
                _continue = false;
                break;
            }
            if(!_actionsQueue.isEmpty()){
                if(!lastAction.equals(_actionsQueue.peek().getClass().getSimpleName())){
                    lastAction = _actionsQueue.peek().getClass().getSimpleName();
                    _trailPoints.clear();
                }
                _actionsQueue.poll().execute(this);
                
            }
            else{
                _trailPoints.clear();
                try {sleep(1);} catch (InterruptedException ex) {Logger.getLogger(TaxiSimulator.class.getName()).log(Level.SEVERE, null, ex);}
            }
        }
    }
}
