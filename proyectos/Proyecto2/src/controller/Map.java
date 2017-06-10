/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;
import model.Reader;
import view.TaxiOptions;

/**
 *
 * @author joseb
 */
public class Map extends Observable {
    private final String[] _map;
    private final String[] _navigableMap;
    private String[] _plottableMap;
    private ArrayList<Client> _clients;
    private ArrayList<TaxiSimulator> _taxis;
    
    public Map(){
        _map = Reader.readFileWithoutTaxi();
        _navigableMap = Utils.makeNavigableMap(_map);
        _plottableMap = _map.clone();
        _clients = new ArrayList<>();
        _taxis = new ArrayList<>();
    }

    public String[] getMap() {return _map;}
    public String[] getNavigableMap() {return _navigableMap;}
    public String[] getPlottableMap() {return _plottableMap;}
    public ArrayList<Client> getClients() {return _clients;}
    public void setClients(ArrayList<Client> _clients) {this._clients = _clients;}
    
    
    
    public void refreshPlottableMap(){
        _plottableMap = _map.clone();
        for(TaxiSimulator taxi : _taxis){
            if(taxi.isShowTrail()){
                for(Point trail : taxi.getTrailPoints()){
                    _plottableMap[trail.x] = Utils.changeCharInPosition(trail.y, Utils.smoke,_plottableMap[trail.x]);
                }
            }
            if(taxi.isShowTravel()){
                for(Point travel : taxi.getTravelPoints()){
                    _plottableMap[travel.x] = Utils.changeCharInPosition(travel.y, Utils.route,_plottableMap[travel.x]);
                }
            }
        }
        for(TaxiSimulator taxi : _taxis){
            _plottableMap[taxi.getTaxiLocation().x] = Utils.changeCharInPosition(taxi.getTaxiLocation().y, taxi.getTaxiChar() ,_plottableMap[taxi.getTaxiLocation().x]);
        }
        setChanged();
        notifyObservers();
    }
    
    // 1=good, -1=invalid position, -2=invalid name
    public int addTaxi(int pLocationX , int pLocationY, String pName){
        if(_navigableMap[pLocationX].charAt(pLocationY) != Utils.navigableSpace){
            return -1;
        }
        else{
            for(TaxiSimulator ts : _taxis){
                if(ts.getTaxiName().equals(pName)){
                    return -2;
                }
            }
            
            TaxiSimulator ts = new TaxiSimulator(this, new Point(pLocationX, pLocationY), pName);
            ts.start();
            _taxis.add(ts);
            TaxiOptions to = new TaxiOptions(ts);
            to.setVisible(true);
            refreshPlottableMap();
            return 1;
        }
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
    }
    
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
        return 1;
    }
}
