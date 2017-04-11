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
public class TaxiSimulator {
    private final String[] _map;
    private Point _taxiLocation;

    public TaxiSimulator(){
        this._map = Reader.readFileWithoutTaxi();
        this._taxiLocation = Reader.getTaxiLocation();
    }
    
}
