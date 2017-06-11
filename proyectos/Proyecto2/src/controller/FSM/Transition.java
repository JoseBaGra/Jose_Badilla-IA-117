/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.FSM;

import controller.FSM.State;

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
    
    @Override
    public int hashCode(){
        String hash = _start.getClass().getSimpleName() + _key.split(",")[0];
        return hash.hashCode();
    }
    
    @Override
    public boolean equals(Object o){
        if(o instanceof Transition){
            return (o.hashCode() == this.hashCode());
        }
        else{
            return false;
        }
    }

}
