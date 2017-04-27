/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Point;

/**
 *
 * @author joseb
 */
public class Client {
    private Point _start; 
    private Point _target;

    public Client(Point pStart, Point pTarget) {
        this._start = pStart;
        this._target = pTarget;
    }

    public Point getStart() {return _start;}
    public void setStart(Point _start) {this._start = _start;}

    public Point getTarget() {return _target;}
    public void setTarget(Point _target) {this._target = _target;}
    
    
    
}
