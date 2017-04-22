/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Point;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;

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
    
    public static String changeCharInPosition(int position, char ch, String str){
        char[] charArray = str.toCharArray();
        charArray[position] = ch;
        return new String(charArray);
    }
    
    public static final char moveableTaxi = 't';
    public static final char recorredByTaxi = '-';
    public static final char navigableSpace = ' ';
    public static final char unnavigableSpace = '*';
    
    public static void AStar(String[] pMap, Point pStart, Point pTarget){
        PriorityQueue<AStarNode> openList = new PriorityQueue<AStarNode>();
        HashSet<AStarNode> posibleList = new HashSet<>();
        HashSet<AStarNode> closeList = new HashSet<>();
        AStarNode initial = new AStarNode(null,H(pStart,pTarget),0,"",pMap);
        boolean found = false;
        openList.add(initial);
        while(!found){
            AStarNode minAStarNode = openList.poll();
//            System.out.println(minAStarNode.getF());
//            minAStarNode.PrintSolution();
//            System.out.println("");
            if(minAStarNode.getH()==0){
                found = true;
                minAStarNode.PrintSolution();
                break;
            }
            closeList.add(minAStarNode);
            addRoutes(minAStarNode,posibleList,pTarget);
            posibleList.removeAll(openList);
            posibleList.removeAll(closeList);
            openList.addAll(posibleList);
            posibleList.clear();
        }
    }
    
    public static int H(Point pStart, Point pTarget){
        //System.out.println(pStart+"\t"+pTarget + "\t="+(Math.abs(pStart.x - pTarget.x) + Math.abs(pStart.y - pTarget.y)));
        return (Math.abs(pStart.x - pTarget.x) + Math.abs(pStart.y - pTarget.y));
    }
    
    public static void addRoutes(AStarNode pStart, Collection<AStarNode> pOpenList, Point pTarget){
        if(pStart==null){return;}
        
        int pieceX = 0;
        int pieceY = 0;
        
        String[] map = pStart.getMap();
        for (int line = 0; line < map.length; line++) {
            if(map[line].indexOf(moveableTaxi) != -1){
                pieceX = line;
                pieceY = map[line].indexOf(moveableTaxi);
                break;
            }
        }
        if(pieceX + 1 < map.length && map[pieceX+1].charAt(pieceY) != unnavigableSpace){
            String[] pivotMap = map.clone();
            pivotMap[pieceX] = changeCharInPosition(pieceY, navigableSpace, pivotMap[pieceX]);
            pivotMap[pieceX+1] = changeCharInPosition(pieceY, moveableTaxi, pivotMap[pieceX+1]);
            int H = H(new Point(pieceX+1, pieceY), pTarget);
            pOpenList.add(new AStarNode(pStart,H,pStart.getG()+1,"DOWN",pivotMap));
            
        }
        if(pieceY + 1 < map[pieceX].length() && map[pieceX].charAt(pieceY+1) != unnavigableSpace){
            String[] pivotMap = map.clone();
            pivotMap[pieceX] = changeCharInPosition(pieceY, navigableSpace, pivotMap[pieceX]);
            pivotMap[pieceX] = changeCharInPosition(pieceY+1, moveableTaxi, pivotMap[pieceX]);
            int H = H(new Point(pieceX, pieceY+1), pTarget);
            pOpenList.add(new AStarNode(pStart,H,pStart.getG()+1,"RIGHT",pivotMap));
        }
        if(pieceX - 1 >= 0 && map[pieceX-1].charAt(pieceY) != unnavigableSpace){
            String[] pivotMap = map.clone();
            pivotMap[pieceX] = changeCharInPosition(pieceY, navigableSpace, pivotMap[pieceX]);
            pivotMap[pieceX-1] = changeCharInPosition(pieceY, moveableTaxi, pivotMap[pieceX-1]);
            int H = H(new Point(pieceX-1, pieceY), pTarget);
            pOpenList.add(new AStarNode(pStart,H,pStart.getG()+1,"UP",pivotMap));
        }
        if(pieceY - 1 >= 0 && map[pieceX].charAt(pieceY-1) != unnavigableSpace){
            String[] pivotMap = map.clone();
            pivotMap[pieceX] = changeCharInPosition(pieceY, navigableSpace, pivotMap[pieceX]);
            pivotMap[pieceX] = changeCharInPosition(pieceY-1, moveableTaxi, pivotMap[pieceX]);
            int H = H(new Point(pieceX, pieceY-1), pTarget);
            pOpenList.add(new AStarNode(pStart,H,pStart.getG()+1,"LEFT",pivotMap));
        }
    }
    
}
