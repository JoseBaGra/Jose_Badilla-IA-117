/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Point;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
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
    private ArrayList<Client> _clients;
    private TaxiFrame _taxiFrame;
    private boolean _showTrail = false;
    private boolean _showTravel = false;
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
        _clients = new ArrayList<>();
    }

    public String[] getMap() {return _map;}
    public String[] getNavigableMap() {return _navigableMap;}
    public String[] getPlottableMap() {return _plottableMap;}
    public void setPlottableMap(String[] pPlottableMap) {_plottableMap = pPlottableMap;}
    public void setTaxiFrame(TaxiFrame pTaxiFrame) {_taxiFrame = pTaxiFrame;}

    public int getSleep() {return _sleep;}
    public void setSleep(int _sleep) {this._sleep = _sleep; setContinue(true);}

    public void setContinue(boolean _continue) {this._continue = _continue;}

    public boolean isShowTrail() {return _showTrail;}
    public void setShowTrail(boolean _showTrail) {
        this._showTrail = _showTrail;
        if(!_showTrail){clearPlottableMap();}
    }
    
    public boolean isShowTravel() {return _showTravel;}
    public void setShowTravel(boolean _showTravel) {
        this._showTravel = _showTravel;
        if(!_showTravel){clearPlottableMap();}
    }
    
    public ArrayList<Client> getClients() {return _clients;}
    public void setClients(ArrayList<Client> _clients) {this._clients = _clients;}
    
    
    public Point getTaxiLocation() {return _taxiLocation;}
    public void setTaxiLocation(Point _taxiLocation) {this._taxiLocation = _taxiLocation;}

    public Queue<Action> getActionsQueue() {return _actionsQueue;}
    public void setActionsQueue(Queue<Action> _actionsQueue) {this._actionsQueue = _actionsQueue;}
    
    
    
    private void clearPlottableMap(){
        for (int line = 0; line < _plottableMap.length; line++) {
            _plottableMap[line] = _plottableMap[line].replace(Utils.smoke, Utils.navigableSpace);
            _plottableMap[line] = _plottableMap[line].replace(Utils.route, Utils.navigableSpace);
            _plottableMap[line] = _plottableMap[line].replace(Utils.BusyTaxiDown, Utils.TaxiDown);
            _plottableMap[line] = _plottableMap[line].replace(Utils.BusyTaxiLeft, Utils.TaxiLeft);
            _plottableMap[line] = _plottableMap[line].replace(Utils.BusyTaxiRight, Utils.TaxiRight);
            _plottableMap[line] = _plottableMap[line].replace(Utils.BusyTaxiUp, Utils.TaxiUp);
        }
        _taxiFrame.refeshLabels();
    }
    
    public void addClients(int pCuantity){
        while(pCuantity != 0){
            Random rand = new Random();
            ArrayList<Integer> indexes;
            int  StartX = 0, StartY = 0;
            boolean isValid = false;
            while(!isValid){
                StartX = (int)(Math.random() * (_navigableMap.length-2) + 1);
                indexes = Utils.getLocationsOfChar(_navigableMap[StartX], Utils.navigableSpace);
                while(indexes.size()<=0){
                    StartX = (int)(Math.random() * (_navigableMap.length-2) + 1);
                    indexes = Utils.getLocationsOfChar(_navigableMap[StartX], Utils.navigableSpace);
                }
                StartY = indexes.get(rand.nextInt(indexes.size()));
                
                isValid = true;
                for(Client client : _clients){
                    if(client.getStart().x == StartX && client.getStart().y == StartY){
                        isValid = false;
                        break;
                    }
                }
                
            }
            
            
            int  TargetX = (int)(Math.random() * (_navigableMap.length-2) + 1);
            indexes = Utils.getLocationsOfChar(_navigableMap[TargetX], Utils.navigableSpace);
            int  TargetY = indexes.get(rand.nextInt(indexes.size()));
            while(StartX == TargetX || StartY == TargetY){
                TargetX = (int)(Math.random() * (_navigableMap.length-2) + 1);
                indexes = Utils.getLocationsOfChar(_navigableMap[TargetX], Utils.navigableSpace);
                TargetY = indexes.get(rand.nextInt(indexes.size()));
            }
            
            
            _clients.add(new Client(new Point(StartX, StartY), new Point(TargetX, TargetY)));
            _plottableMap[StartX] = Utils.changeCharInPosition(StartY, Utils.client, _plottableMap[StartX]);
            pCuantity--;
        }
        _taxiFrame.refeshLabels();
    }
    
    // 1=good, -1=colision, -2 invalid
    public int addClient(int pStartX, int pStartY){
        for(Client client : _clients){
            if(client.getStart().x == pStartX && client.getStart().y == pStartY){
                return -1;
            }
        }
        
        if(pStartX >= _map.length){return -2;}
        if(pStartY >= _map[pStartX].length()){return -2;}
        
        if(_navigableMap[pStartX].charAt(pStartY) != Utils.navigableSpace){
            return -2;
        }
        
        Random rand = new Random();
        int  TargetX = (int)(Math.random() * (_navigableMap.length-2) + 1);
        ArrayList<Integer> indexes = Utils.getLocationsOfChar(_navigableMap[TargetX], Utils.navigableSpace);
        int TargetY = indexes.get(rand.nextInt(indexes.size()));
        while(pStartX == TargetX || pStartY == TargetY){
            TargetX = (int)(Math.random() * (_navigableMap.length-2) + 1);
            indexes = Utils.getLocationsOfChar(_navigableMap[TargetX], Utils.navigableSpace);
            TargetY = indexes.get(rand.nextInt(indexes.size()));
        }
        _clients.add(new Client(new Point(pStartX, pStartY), new Point(TargetX, TargetY)));
        _plottableMap[pStartX] = Utils.changeCharInPosition(pStartY, Utils.client, _plottableMap[pStartX]);
        _taxiFrame.refeshLabels();
        return 1;
    }
    
    // 1=good, -1=not found
    public int parkIn(String pLocation){
        boolean found = false;
        int x = 0, y = 0;
        for (x = 0; x < _map.length; x++) {
            if(_map[x].contains(pLocation)){
                found = true;
                y = _map[x].indexOf(pLocation);
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
            if(minValue > _taxiLocation.distance(new Point(x-2,y+2)) && x-2 > 0 && y+2 < _map[x].length()){
                minValue = _taxiLocation.distance(new Point(x-2,y+2));
                minPoint = new Point(x-2,y+2);
            }
            if(minValue > _taxiLocation.distance(new Point(x+2,y-2)) && x+2 < _map.length && y-2 > 0){
                minValue = _taxiLocation.distance(new Point(x+2,y-2));
                minPoint = new Point(x+2,y-2);
            }
            if(minValue > _taxiLocation.distance(new Point(x+2,y+2)) && x+2 < _map.length && y+2 < _map[x].length()){
                minValue = _taxiLocation.distance(new Point(x+2,y+2));
                minPoint = new Point(x+2,y+2);
            }
            
            Point taxiPivot = (Point)_taxiLocation.clone();
            String[] navigableMap = _navigableMap.clone();
            navigableMap[taxiPivot.x] = Utils.changeCharInPosition(taxiPivot.y, Utils.moveableTaxi, navigableMap[taxiPivot.x]);
            ArrayList<Point> moves = Utils.AStar(navigableMap, taxiPivot, minPoint);
            while(!moves.isEmpty()){
                Point move = moves.get(0);
                moves.remove(move);
                _actionsQueue.add(new Park(move, (ArrayList<Point>) moves.clone()));
            }
            
            return 1;
        }
        else{
            return -1;
        }
    }
    
    // 1=good, -1=invalid
    public int goTo(int pTargetX, int pTargetY){
        if(pTargetX >= _map.length){return -1;}
        if(pTargetY >= _map[pTargetX].length()){return -1;}
        if(_navigableMap[pTargetX].charAt(pTargetY) != Utils.navigableSpace){return -1;}
        
        Point taxiPivot = (Point)_taxiLocation.clone();
        String[] navigableMap = _navigableMap.clone();
        navigableMap[taxiPivot.x] = Utils.changeCharInPosition(taxiPivot.y, Utils.moveableTaxi, navigableMap[taxiPivot.x]);
        ArrayList<Point> moves = Utils.AStar(navigableMap, taxiPivot, new Point(pTargetX, pTargetY));
        while(!moves.isEmpty()){
            Point move = moves.get(0);
            moves.remove(move);
            _actionsQueue.add(new Park(move, (ArrayList<Point>) moves.clone()));
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
            
            
            for(Client client : _clients){
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
                    _actionsQueue.add(new Travel(move, minClient, (ArrayList<Point>) moves.clone()));
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
            }
            
            while(_sleep == 0){
                while(!_continue){
                    //stops doing anything
                    //here waits to next move
                    try {sleep(1);} catch (InterruptedException ex) {Logger.getLogger(TaxiSimulator.class.getName()).log(Level.SEVERE, null, ex);}
                }
                _continue = false;
                break;
            }
            if(!_actionsQueue.isEmpty()){
                if(!lastAction.equals(_actionsQueue.peek().getClass().getSimpleName())){
                    lastAction = _actionsQueue.peek().getClass().getSimpleName();
                    clearPlottableMap();
                    new Action(_taxiLocation).fixMap(this);
                }
                _actionsQueue.poll().execute(this);
                if(_taxiFrame != null){
                    _taxiFrame.refeshLabels();
                }
                if (_sleep>0) {
                    try {sleep(_sleep);} catch (InterruptedException ex) {Logger.getLogger(TaxiSimulator.class.getName()).log(Level.SEVERE, null, ex);}
                }
            }
            else{
                clearPlottableMap();
                new Action(_taxiLocation).fixMap(this);
                try {sleep(1);} catch (InterruptedException ex) {Logger.getLogger(TaxiSimulator.class.getName()).log(Level.SEVERE, null, ex);}
            }
        }
    }
}
