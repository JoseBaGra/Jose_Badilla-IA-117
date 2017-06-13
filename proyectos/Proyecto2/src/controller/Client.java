/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Point;
import java.util.Random;

/**
 *
 * @author joseb
 */
public class Client {
    private Point _actualPosition;
    private final Point _home; 
    private final int _goHome;
    private final Point _job;
    private final int _goJob;
    private boolean _searchingHome;
    private boolean _searchingJob;
    private boolean _isTraveling;
    private char _clientChar;

    public Client(Point pHome, int pGoHome, Point pJob, int pGoJob) {
        _home = pHome;
        _goHome = pGoHome;
        _job = pJob;
        _goJob = pGoJob;
        _actualPosition = (Point)pHome.clone();
        int min = 1;
        int max = 4;
        Random rn = new Random();
        _clientChar = (char) (rn.nextInt(max - min + 1) + min);
    }

    public Point getActualPosition() {return _actualPosition;}
    public void setActualPosition(Point pActualPosition) {_actualPosition = pActualPosition;}

    public boolean isSearchingHome() {return _searchingHome;}
    public boolean isSearchingJob() {return _searchingJob;}
    public boolean isIsTraveling() {return _isTraveling;}
    public void setIsTraveling(boolean pIsTraveling) {_isTraveling = pIsTraveling;}
    public char getClientChar() {return _clientChar;}
    
    
    public void updateStatus(int pHour){
        if(pHour>=_goHome && !_actualPosition.equals(_home)){
            if(_goHome > _goJob){
                if(pHour>_goJob){
                    _searchingHome = true;
                    _searchingJob = false;
                }
                else{
                    _searchingHome = _searchingJob = false;
                }
            }
            else if(pHour < _goJob){
                _searchingHome = true;
                _searchingJob = false;
            }
            else{
                _searchingHome = _searchingJob = false;
            }
            
        }
        else if(pHour>=_goJob && !_actualPosition.equals(_job)){
            if(_goJob > _goHome){
                if(pHour>_goHome){
                    _searchingHome = false;
                    _searchingJob = true;
                }
                else{
                    _searchingHome = _searchingJob = false;
                }
            }
            else if(pHour<_goHome){
                _searchingHome = false;
                _searchingJob = true;
            }
            else{
                _searchingHome = _searchingJob = false;
            }   
        }
        else{
            _searchingHome = _searchingJob = false;
        }
    }
    
    public Point getStart(){
        if(_searchingHome || _searchingJob){
            return _actualPosition;
        }
        return null;
    }
    
    public Point getTarget(){
        if(_searchingHome || _searchingJob){
            if(_searchingHome){
                return _home;
            }
            if(_searchingJob){
                return _job;
            }
        }
        return null;
    }
}
