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
public class Park extends State{
    @Override
    public int onEnter(FSM pFSM, String pKey) {
        if(pFSM.getOwner() instanceof TaxiSimulator){
            TaxiSimulator ts = (TaxiSimulator) pFSM.getOwner();
            int resp = ts.parkIn(pKey.split(",")[1]);
            return resp;
        }
        return -1;
    }
}
