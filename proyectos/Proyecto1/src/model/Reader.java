/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;


import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joseb
 */
public  class Reader {
    public static final String FILENAME = "map.txt";
    public static final Character TAXI = 't';
    public static final Character EMPTY = ' ';
    
    public static String[] readFile(){
        ArrayList<String> returnArrayList = new ArrayList<>();
        String[] returnArray;
        try {
            BufferedReader br = new BufferedReader (new FileReader(new File(FILENAME)));
            for(String line; (line = br.readLine()) != null; ) {
                returnArrayList.add(line);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
        returnArray = new String[returnArrayList.size()];
        returnArrayList.toArray(returnArray);
        return returnArray;
    }
    
    public static String[] readFileWithoutTaxi(){
        ArrayList<String> returnArrayList = new ArrayList<>();
        String[] returnArray;
        try {
            BufferedReader br = new BufferedReader (new FileReader(new File(FILENAME)));
            for(String line; (line = br.readLine()) != null; ) {
                returnArrayList.add(line.replace(TAXI, EMPTY));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
        returnArray = new String[returnArrayList.size()];
        returnArrayList.toArray(returnArray);
        return returnArray;
    }
    
    public static Point getTaxiLocation(){
        Point returnPoint = new Point(0,0);
        try {
            BufferedReader br = new BufferedReader (new FileReader(new File(FILENAME)));
            int lineNumber = 0;
            for(String line; (line = br.readLine()) != null; ) {
                if(line.indexOf(TAXI) != -1){
                    returnPoint.setLocation(lineNumber, line.indexOf(TAXI));
                    return returnPoint;
                }
                lineNumber++;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnPoint;
    }
    
}
