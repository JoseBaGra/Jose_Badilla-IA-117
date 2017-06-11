/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.FSM;

/**
 *
 * @author joseb
 */
public class State{
    
    public int onEnter(FSM pFSM, String pKey) {return 1;}
    
    public int onUpdate(FSM pFSM, String pKey) {return 1;}
    
    public int onExit(FSM pFSM, String pKey) {return 1;}
    
}
