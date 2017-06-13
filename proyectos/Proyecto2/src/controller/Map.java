/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Point;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Reader;
import view.TaxiOptions;

/**
 *
 * @author joseb
 */
public class Map extends Observable {
    private final String[] _map;
    private final String[] _navigableMap;
    private String[] _traficMap;
    private String[] _plottableMap;
    private ArrayList<Client> _clients;
    private ArrayList<TaxiSimulator> _taxis;
    private int _taxiMS = 500;
    private ArrayList<TaxiOptions> _taxisOptions;
    private boolean _runing = true;
    private boolean _paused = false;
    private int _hour = 0;
    private static Map INSTANCE = null;
    
    private Map(){
        _map = Reader.readFileWithoutTaxi();
        _navigableMap = Utils.makeNavigableMap(_map);
        _traficMap = _navigableMap.clone();
        for (int i = 0; i < _traficMap.length; i++) {
            _traficMap[i] = _traficMap[i].replace(' ', '0');
        }
        _plottableMap = _map.clone();
        _clients = new ArrayList<>();
        _taxis = new ArrayList<>();
        _taxisOptions = new ArrayList<>();
    }
    
    private synchronized static void createInstance() {
        if (INSTANCE == null) { 
            INSTANCE = new Map();
        }
    }

    public static Map getInstance() {
        if (INSTANCE == null){createInstance();}
        return INSTANCE;
    }

    public String[] getMap() {return _map;}
    public String[] getNavigableMap() {return _navigableMap;}
    public String[] getTraficMap() {return _traficMap;}    
    public String[] getPlottableMap() {return _plottableMap;}
    public ArrayList<TaxiSimulator> getTaxis() {return _taxis;}
    public ArrayList<Client> getClients() {return _clients;}
    public void setClients(ArrayList<Client> _clients) {this._clients = _clients;}
    public int getHour(){return _hour;}
    public boolean isPaused() {return _paused;}
    
    
    public void refreshPlottableMap(){
        for(TaxiOptions to : _taxisOptions){
            to.setValues();
        }
        _plottableMap = _map.clone();
        for(TaxiSimulator taxi : _taxis){
            if(taxi.isShowTrail() || taxi.isShowTravel()){
                if(taxi.isShowTrail()){
                    ArrayList<Point> trails = (ArrayList<Point>)taxi.getTrailPoints().clone();
                    for(Point trail : trails){
                        _plottableMap[trail.x] = Utils.changeCharInPosition(trail.y, Utils.smoke,_plottableMap[trail.x]);
                    }
                }
                if(taxi.isShowTravel()){
                    if(taxi.getActionsQueue().peek() instanceof TakeARide || taxi.getActionsQueue().peek() instanceof SearchClient){
                        continue;
                    }
                    ArrayList<Point> travels = (ArrayList<Point>)taxi.getTravelPoints().clone();
                    for(Point travel : travels){
                        _plottableMap[travel.x] = Utils.changeCharInPosition(travel.y, Utils.route,_plottableMap[travel.x]);
                    }
                }
            }
        }
        for (Client client : _clients) {
            if(client.isSearchingHome() || client.isSearchingJob()){
                _plottableMap[client.getActualPosition().x] = Utils.changeCharInPosition(client.getActualPosition().y, client.getClientChar() ,_plottableMap[client.getActualPosition().x]);
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
            if(true){                
                TaxiSimulator ts = new TaxiSimulator(this, new Point(pLocationX, pLocationY), pName);
                ts.start();
                ts.setSleep(_taxiMS);
                _taxis.add(ts);
                ts.setPaused(_paused);
                TaxiOptions to = new TaxiOptions(ts);
                to.setVisible(true);
                _taxisOptions.add(to);
            }
            return 1;
        }
    }
     
    // 1=all good, -1=home out of bounds, -2=job out of bounds, -3=0<goHome>24, -4=0<goJob>24, -5 goHome = goJob
    public int addClient(int pHomeX, int pHomeY, int pGoHome, int pJobX, int pJobY, int pGoJob){
        if(pHomeX >= _map.length){return -1;}
        if(pHomeY >= _map[pHomeX].length()){return -1;}
        if(_navigableMap[pHomeX].charAt(pHomeY) != Utils.navigableSpace){return -1;}

        if(pJobX >= _map.length){return -2;}
        if(pJobY >= _map[pJobX].length()){return -2;}
        if(_navigableMap[pJobX].charAt(pJobY) != Utils.navigableSpace){return -2;}
        
        if(pGoHome<0 || pGoHome>23){return -3;}
        if(pGoJob<0 || pGoJob>23){return -4;}
        if(pGoHome == pGoJob){return pGoHome-5;}
        
        Client client = new Client(new Point(pHomeX, pHomeY),pGoHome,new Point(pJobX, pJobY),pGoJob);
        _clients.add(client);
        return 1;
    }
    
    public void run(){
        int fps = 60;
        int ms_wait = 1000/fps;
        while(_runing){
            /*
            while(_paused){
                //stops doing anything
                try {sleep(1);} catch (InterruptedException ex) {Logger.getLogger(TaxiSimulator.class.getName()).log(Level.SEVERE, null, ex);}
            }
            */
            
            refreshPlottableMap();
            try {sleep(ms_wait);} catch (InterruptedException ex) {Logger.getLogger(TaxiSimulator.class.getName()).log(Level.SEVERE, null, ex);}
        }
    }
    
    public void startTimer(double pHourDuration) {
        int ms_wait = (int)(pHourDuration * 1000);
        while (_runing) {            
            while(_paused){
                //stops doing anything
                try {sleep(1);} catch (InterruptedException ex) {Logger.getLogger(TaxiSimulator.class.getName()).log(Level.SEVERE, null, ex);}
            }
            _hour++;
            _hour%=24;
            for (Client client : _clients) {
                client.updateStatus(_hour);
            }
            
            
            
            try {sleep(ms_wait);} catch (InterruptedException ex) {Logger.getLogger(TaxiSimulator.class.getName()).log(Level.SEVERE, null, ex);}
        }
    }
    
    public void traficUpdater(){
        new Thread(() -> traficUpdaterAux()).start();
        while (_runing) {            
            while(_paused){
                //stops doing anything
                try {sleep(1);} catch (InterruptedException ex) {Logger.getLogger(TaxiSimulator.class.getName()).log(Level.SEVERE, null, ex);}
            }
            
            ArrayList<Point> taxisLocation = new ArrayList<>();
            for (TaxiSimulator taxi : _taxis) {
                taxisLocation.add(taxi.getTaxiLocation());
                taxi.setPaused(true);
            }
            //System.out.println(taxisLocation);
            
            if(!taxisLocation.isEmpty()){
                HashMap<Point,Integer> trafic = new HashMap<>();
                int times = 0;
                Point actualPoint = taxisLocation.get(0);
                for(Point taxPoint : taxisLocation){
                    if(taxPoint.equals(actualPoint)){
                        times++;
                    }
                    else{
                        trafic.put(actualPoint, times%10);
                        times = 1;
                        actualPoint = taxPoint;
                    }
                }
                trafic.put(actualPoint, times%10);
                
                //System.out.println(trafic);
                
                trafic.forEach((point,value) -> {
                    if(value>Character.getNumericValue(_traficMap[point.x].charAt(point.y))){
                        _traficMap[point.x] = Utils.changeCharInPosition(point.y, Character.forDigit(value, 10), _traficMap[point.x]);
                    }
                    else if(value == Character.getNumericValue(_traficMap[point.x].charAt(point.y)) && value<=1){
                        _traficMap[point.x] = Utils.changeCharInPosition(point.y, Character.forDigit((value*2)%10, 10), _traficMap[point.x]);
                    }
                });
            }
            
            for (TaxiSimulator taxi : _taxis) {taxi.setPaused(false);}
            try {sleep(1);} catch (InterruptedException ex) {Logger.getLogger(TaxiSimulator.class.getName()).log(Level.SEVERE, null, ex);}
        }
    }
    
    private void traficUpdaterAux(){
        while (_runing) {
            while(_paused){
                try {sleep(1);} catch (InterruptedException ex) {Logger.getLogger(TaxiSimulator.class.getName()).log(Level.SEVERE, null, ex);}
            }
            ArrayList<Point> taxisLocation = new ArrayList<>();
            for (int i = 0; i < _traficMap.length; i++) {
                for (int j = 0; j < _traficMap[i].length(); j++) {
                    if(_traficMap[i].charAt(j)!= '0' && _traficMap[i].charAt(j)!= '*'){
                        if(!taxisLocation.contains(new Point(i, j))){
                            int x = Character.getNumericValue(_traficMap[i].charAt(j));
                            x--;
                            if(x<0){x=0;}
                            _traficMap[i] = Utils.changeCharInPosition(j, Character.forDigit(x, 10),  _traficMap[i]);
                        }
                    }
                }
            }
            try {sleep(_taxiMS*2);} catch (InterruptedException ex) {Logger.getLogger(TaxiSimulator.class.getName()).log(Level.SEVERE, null, ex);}
        }
    }
    
    public void stop(){
        _runing = false;
    }
    
    public void switchPause(){
        if(_paused){
            _paused=false;
            for(TaxiSimulator taxi : _taxis){
                taxi.setPaused(_paused);
            }
        }
        else{
            _paused=true;
            for(TaxiSimulator taxi : _taxis){
                taxi.setPaused(_paused);
            }
        }
    }
    
    public void setMS(int pMS){
        _taxiMS = pMS;
        for(TaxiSimulator taxi : _taxis){
            taxi.setSleep(pMS);
        }
        for(TaxiOptions to : _taxisOptions){
            to.setValues();
        }
    }
}
