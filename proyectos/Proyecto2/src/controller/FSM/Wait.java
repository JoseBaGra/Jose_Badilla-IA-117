/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.FSM;

import controller.TaxiSimulator;

/**
 *
 * @author joseb
 */
public class Wait extends State{
    public int onEnter(FSM pFSM, String pKey) {
        if(pFSM.getOwner() instanceof TaxiSimulator){
            return 1;
        }
        return -1;
    }
}
