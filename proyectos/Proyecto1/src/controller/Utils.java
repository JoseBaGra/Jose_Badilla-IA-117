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
    
    public static String[] makeNavigableMap(String[] pMap){
        String[] pivotMap = pMap.clone();
        for (int line = 0; line < pivotMap.length; line++) {
            pivotMap[line] = pivotMap[line].replace("---", "***");
            pivotMap[line] = pivotMap[line].replaceAll("-[a-zA-Z| ]-", "***");
        }
        return pivotMap;
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
    public static final char route = '+';
    public static final char smoke = '.';
    public static final char client = ',';
    public static final char TaxiDown='↓';
    public static final char TaxiLeft='←';
    public static final char TaxiRight='→';
    public static final char TaxiUp='↑';
    public static final char BusyTaxiDown='⇓';
    public static final char BusyTaxiLeft='⇐';
    public static final char BusyTaxiRight='⇒';
    public static final char BusyTaxiUp='⇑';
    
    
    public static ArrayList<Point> AStar(String[] pMap, Point pStart, Point pTarget){
        PriorityQueue<AStarNode> openList = new PriorityQueue<AStarNode>();
        HashSet<AStarNode> posibleList = new HashSet<>();
        HashSet<AStarNode> closeList = new HashSet<>();
        AStarNode initial = new AStarNode(null,H(pStart,pTarget),0,"",pMap);
        ArrayList<Point> moves = null;
        openList.add(initial);
        boolean found = false;
        while(!found){
            AStarNode minAStarNode = openList.poll();
            if(minAStarNode.getH()==0){
                found = true;
                moves = minAStarNode.getSolution();
                break;
            }
            closeList.add(minAStarNode);
            addRoutes(minAStarNode,posibleList,pTarget);
            posibleList.removeAll(openList);
            posibleList.removeAll(closeList);
            openList.addAll(posibleList);
            posibleList.clear();
        }
        return moves;
    }
    
    public static int H(Point pStart, Point pTarget){
        return (Math.abs(pStart.x - pTarget.x) + Math.abs(pStart.y - pTarget.y));
    }
    
    public static Point getTaxiLocation(String[] pMap){
        int taxiX = 0;
        int taxiY = 0;
        
        for (int line = 0; line < pMap.length; line++) {
            if(pMap[line].indexOf(moveableTaxi) != -1){
                taxiX = line;
                taxiY = pMap[line].indexOf(moveableTaxi);
                break;
            }
        }
        return (new Point(taxiX, taxiY));
    }
    
    public static void addRoutes(AStarNode pStart, Collection<AStarNode> pOpenList, Point pTarget){
        if(pStart==null){return;}
        
        String[] map = pStart.getMap();
        Point taxi = getTaxiLocation(map);
        
        
        if(taxi.x + 1 < map.length && map[taxi.x+1].charAt(taxi.y) != unnavigableSpace){
            String[] pivotMap = map.clone();
            pivotMap[taxi.x] = changeCharInPosition(taxi.y, navigableSpace, pivotMap[taxi.x]);
            pivotMap[taxi.x+1] = changeCharInPosition(taxi.y, moveableTaxi, pivotMap[taxi.x+1]);
            int H = H(new Point(taxi.x+1, taxi.y), pTarget);
            pOpenList.add(new AStarNode(pStart,H,pStart.getG()+1,"DOWN",pivotMap));
            
        }
        if(taxi.y + 1 < map[taxi.x].length() && map[taxi.x].charAt(taxi.y+1) != unnavigableSpace){
            String[] pivotMap = map.clone();
            pivotMap[taxi.x] = changeCharInPosition(taxi.y, navigableSpace, pivotMap[taxi.x]);
            pivotMap[taxi.x] = changeCharInPosition(taxi.y+1, moveableTaxi, pivotMap[taxi.x]);
            int H = H(new Point(taxi.x, taxi.y+1), pTarget);
            pOpenList.add(new AStarNode(pStart,H,pStart.getG()+1,"RIGHT",pivotMap));
        }
        if(taxi.x - 1 >= 0 && map[taxi.x-1].charAt(taxi.y) != unnavigableSpace){
            String[] pivotMap = map.clone();
            pivotMap[taxi.x] = changeCharInPosition(taxi.y, navigableSpace, pivotMap[taxi.x]);
            pivotMap[taxi.x-1] = changeCharInPosition(taxi.y, moveableTaxi, pivotMap[taxi.x-1]);
            int H = H(new Point(taxi.x-1, taxi.y), pTarget);
            pOpenList.add(new AStarNode(pStart,H,pStart.getG()+1,"UP",pivotMap));
        }
        if(taxi.y - 1 >= 0 && map[taxi.x].charAt(taxi.y-1) != unnavigableSpace){
            String[] pivotMap = map.clone();
            pivotMap[taxi.x] = changeCharInPosition(taxi.y, navigableSpace, pivotMap[taxi.x]);
            pivotMap[taxi.x] = changeCharInPosition(taxi.y-1, moveableTaxi, pivotMap[taxi.x]);
            int H = H(new Point(taxi.x, taxi.y-1), pTarget);
            pOpenList.add(new AStarNode(pStart,H,pStart.getG()+1,"LEFT",pivotMap));
        }
    }
    
}
