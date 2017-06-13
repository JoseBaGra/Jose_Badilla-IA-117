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
public class GoTo extends State{
    @Override
    public int onEnter(FSM pFSM, String pKey) {
        if(pFSM.getOwner() instanceof TaxiSimulator){
            TaxiSimulator ts = (TaxiSimulator) pFSM.getOwner();
            String[] point = pKey.split(",");
            int resp = ts.goTo(Integer.parseInt(point[1]), Integer.parseInt(point[2]));
            return resp;
        }
        return -1;
    }
}
