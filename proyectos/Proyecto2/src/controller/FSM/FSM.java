/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.FSM;

import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author joseb
 */
public class FSM {
    private final Object _owner;
    private final State[] _states;
    private State _actualState;
    private Map<Transition, State> _transitions;

    public FSM(Object pOwner, State[] pStates, Map<Transition, State> pTransitions) {
        _owner = pOwner;
        _states = pStates;
        _actualState = _states[0];
        _transitions = pTransitions;
    }
    
    public FSM(Object pOwner, State[] pStates){
        _owner = pOwner;
        _states = pStates;
        _actualState = _states[0];
        _transitions = new HashMap<>();
    }

    public Object getOwner() {
        return _owner;
    }
    
    public void addTransition(State pInitial, String pKey, State pFinal){
        _transitions.put(new Transition(pInitial, pKey), pFinal);
    }

    public int changeState(String pKey){
        State newState = _transitions.get(new Transition(_actualState, pKey));
        if(newState != null){
            _actualState.onExit(this,pKey);
            _actualState = newState;
            int resp = newState.onEnter(this,pKey);
            return resp;
        }
        return -1;
    }
}
