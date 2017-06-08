/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

/**
 *
 * @author joseb
 */
class Transition {
    private State _start;
    private String _key;

    public Transition(State pStart, String pKey) {
        _start = pStart;
        _key = pKey;
    }
    
    
}
