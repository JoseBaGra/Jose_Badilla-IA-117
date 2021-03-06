/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author joseb
 */
public class AStarNode implements Comparator<AStarNode>, Comparable<AStarNode>{
    private AStarNode parent;
    private String[] map;
    private int H;
    private int G;
    private String move;

    public AStarNode(AStarNode pParent, String[] pMap){
        parent = pParent;
        map = pMap;
    }

    public AStarNode(AStarNode pParent, int pH, int pG, String pMove, String[] pMap){
        parent = pParent;
        map = pMap;
        H = pH;
        G = pG;
        move = pMove;
    }

    public void setParent(AStarNode pParent) {parent = pParent;}
    public AStarNode getParent() {return parent;}
    public int getG() {return G;}
    public int getH() {return H;}
    public int getF(){return (G+H);}
    public String getMove(){return move;}
    public String[] getMap(){return map;}

    public void printSolution(){
        AStarNode aux = this;
        int moves = 0;
        String path = "";
        while(aux.getParent()!=null){
            moves++;
            path = aux.getMove() + "\n" + path;
            aux = aux.getParent();
        }
        System.out.print(moves+"\n"+path);
    }
    
    public ArrayList<Point> getSolution(){
        ArrayList<Point> returnValue = new ArrayList<>();
        AStarNode aux = this;
        while(aux.getParent()!=null){
            returnValue.add(0,Utils.getTaxiLocation(aux.getMap()));
            aux = aux.getParent();
        }
        return returnValue;
    }

    @Override
    public int compare(AStarNode o1, AStarNode o2) {
        return o1.getF() - o2.getF();
    }

    @Override
    public int compareTo(AStarNode o) {
        return this.getF() - o.getF();
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof AStarNode){
            boolean resp = true;
            notEquals:
            for (int line = 0; line < ((AStarNode) o).getMap().length; line++) {
                if(!map[line].equals(((AStarNode) o).getMap()[line])){
                    resp = false;
                    break notEquals;
                }
            }
            return resp;
        }
        else{
            return false;
        }
    }

    @Override
    public int hashCode(){
        String hash = "";
        for(String line : map){
            hash += line;
        }
        return hash.hashCode();
    }
}
