/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Point;
import java.util.Collection;
import java.util.ArrayList;

/**
 *
 * @author joseb
 */
public final class Utils {
    
    public static void printMap(String[] pMap){
        if(pMap == null){return;}
        for (int i = 0; i < pMap.length; i++) {
            System.out.println(pMap[i]);
        }
    }
    
    public static ArrayList<Integer> getLocationsOfChar(String pString, char pChar){
        ArrayList<Integer> returnList = new ArrayList<Integer>();
        for(int i = 0; i < pString.length(); i++){
            if(pString.charAt(i) == pChar){
               returnList.add(i);
            }
        }
        return returnList;
    }
    
    public static final char moveableTaxi = 't';
    public static final char recorredByTaxi = '-';
    public static final char navigableSpace = ' ';
}
