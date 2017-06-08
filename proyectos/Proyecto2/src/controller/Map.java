/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Point;
import model.Reader;

/**
 *
 * @author joseb
 */
public class Map {
    private final String[] _map;
    private final String[] _navigableMap;
    private String[] _plottableMap;
    
    public Map(){
        _map = Reader.readFileWithoutTaxi();
        _navigableMap = Utils.makeNavigableMap(_map);
        _plottableMap = _map.clone();
        
        Point _taxiLocation = Reader.getTaxiLocation();
        _plottableMap[_taxiLocation.x] = Utils.changeCharInPosition(_taxiLocation.y, Utils.TaxiUp, _plottableMap[_taxiLocation.x]);
    }
    
    public void refreshPlottableMap(String[] pPlottableMap){
        for (int line = 0; line < pPlottableMap.length; line++) {
            for (int character = 0; character < pPlottableMap[line].length(); character++) {
                if(_plottableMap[line].charAt(character) != pPlottableMap[line].charAt(character)){
                    _plottableMap[line] = Utils.changeCharInPosition(character, pPlottableMap[line].charAt(character), _plottableMap[line]);
                }
            }
        }
    }
}
