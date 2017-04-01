/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package npuzzle;

import java.awt.Point;
import java.util.*;

/**
 *
 * @author joseb
 */
public class NPuzzle {
    
    public static class Node{
        private Node parent;
        private int[][] puzzle;
        private int H;
        private int F;
        private String move;
        
        public Node(Node pParent, int[][] pPuzzle){
            parent = pParent;
            puzzle = pPuzzle;
            CalculateHeuristic();
        }
        
        public Node(Node pParent, int pF, String pMove, int[][] pPuzzle){
            parent = pParent;
            puzzle = pPuzzle;
            F = pF;
            move = pMove;
            CalculateHeuristic();
        }
        
        private void CalculateHeuristic(){
            int correctValue = 0;
            for (int colunm = 0; colunm < puzzle.length; colunm++) {
                for (int row = 0; row < puzzle.length; row++) {
                    H += H(new Point(colunm, row),correctValue,puzzle);
                    correctValue++;
                }
            }
        }
        
        public void setParent(Node pParent) {parent = pParent;}
        public Node getParent() {return parent;}
        public void setF(int F) {this.F = F;}
        public int getF() {return F;}
        public int getH() {return H;}
        public int getG(){return (F+H);}
        public String getMove(){return move;}
        public int[][] getPuzzle(){return puzzle;}
        
        public void PrintSolution(){
            Node aux = this;
            int moves = 0;
            String path = "";
            while(aux.getParent()!=null){
                moves++;
                path = aux.getMove() + "\n" + path;
                aux = aux.getParent();
            }
            System.out.print(moves+"\n"+path);
        }	
    }
    
    public static int H(Point pLocation, int pWanted, int[][] pPuzzle){
        for (int colunm = 0; colunm < pPuzzle.length; colunm++) {
            for (int row = 0; row < pPuzzle.length; row++) {
                if(pPuzzle[colunm][row] == pWanted){
                    return (Math.abs(colunm - pLocation.x) + Math.abs(row - pLocation.y));
                }
            }
        }
        // error
        return -1;
    }
    
    public static void SolvePuzzle(int[][] pPuzzle){
        ArrayList<Node> openList = new ArrayList<>();
        ArrayList<Node> posibleList = new ArrayList<>();
        ArrayList<Node> closeList = new ArrayList<>();
        Node initial = new Node(null, pPuzzle);
        boolean found = false;
        closeList.add(initial);
        addRoutes(initial, openList);
        openList.add(initial);
        while(!found){
            int minG = Integer.MAX_VALUE;
            Node minNode = null;
            for(Node node : openList){
                if(minG > node.getG()){
                    minG = node.getG();
                    minNode = node;
                }
            }
            
            //System.out.println("OPEN "+openList.size());
            //System.out.println("CLOSE "+closeList.size()+"\n");
            
            closeList.add(minNode);
            openList.remove(minNode);
            addRoutes(minNode,posibleList);
            deleteBFromA(posibleList, openList);
            deleteBFromA(posibleList, closeList);
            openList.addAll(posibleList);
            posibleList.clear();

            for(Node node : openList){
                if(node.getH()==0){
                    found = true;
                    node.PrintSolution();
                    break;
                }
            }
        }
    }
    
    
    //gives a copy of an int matrix
    public static int[][] copy(int[][] source) {
        int[][] target = source.clone();
        for (int i = 0; i < source.length; i++) {
            target[i] = source[i].clone();
        }
        return target;
    }
    
    //movable piece
    public static final int moveablePiece = 0;
    public static void addRoutes(Node pStart, ArrayList<Node> pOpenList){
        if(pStart==null){return;}
        
        int pieceX = 0;
        int pieceY = 0;
        
        foundPiece:
        for (int colunm = 0; colunm < pStart.getPuzzle().length; colunm++) {
            for (int row = 0; row < pStart.getPuzzle().length; row++) {
                if(pStart.getPuzzle()[colunm][row] == moveablePiece){
                    pieceX = colunm;
                    pieceY = row;
                    break foundPiece;
                }
            }
        }
       
        if(pieceX + 1 < pStart.getPuzzle().length){
            int[][] pivotPuzzle = copy(pStart.getPuzzle());
            pivotPuzzle[pieceX][pieceY] =  pivotPuzzle[pieceX+1][pieceY];
            pivotPuzzle[pieceX+1][pieceY] = 0;
            pOpenList.add(new Node(pStart,pStart.getF()+1,"DOWN",pivotPuzzle));
            
        }
        if(pieceY + 1 < pStart.getPuzzle().length){
            int[][] pivotPuzzle = copy(pStart.getPuzzle());
            pivotPuzzle[pieceX][pieceY] =  pivotPuzzle[pieceX][pieceY+1];
            pivotPuzzle[pieceX][pieceY+1] = 0;
            pOpenList.add(new Node(pStart,pStart.getF()+1,"RIGHT",pivotPuzzle));
        }
        if(pieceX - 1 >= 0){
            int[][] pivotPuzzle = copy(pStart.getPuzzle());
            pivotPuzzle[pieceX][pieceY] =  pivotPuzzle[pieceX-1][pieceY];
            pivotPuzzle[pieceX-1][pieceY] = 0;
            pOpenList.add(new Node(pStart,pStart.getF()+1,"UP",pivotPuzzle));
        }
        if(pieceY - 1 >= 0){
            int[][] pivotPuzzle = copy(pStart.getPuzzle());
            pivotPuzzle[pieceX][pieceY] =  pivotPuzzle[pieceX][pieceY-1];
            pivotPuzzle[pieceX][pieceY-1] = 0;
            pOpenList.add(new Node(pStart,pStart.getF()+1,"LEFT",pivotPuzzle));
        }
    }

    public static void deleteBFromA(ArrayList<Node> pA, ArrayList<Node> pB){
        for(Node b : pB){
            for (int i = 0; i < pA.size(); i++) {
                if(AEquealsB(b.getPuzzle(), pA.get(i).getPuzzle())){
                    pA.remove(i);
                }							
            }
        }
    }
    
    public static boolean AEquealsB(int[][] pA, int[][]pB){
        boolean resp = true;
        notEquals:
        for (int colunm = 0; colunm < pA.length; colunm++) {
            for (int row = 0; row < pA.length; row++) {
                if(pA[colunm][row] != pB[colunm][row]){
                    resp = false;
                    break notEquals;
                }
            }
        }
        return resp;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int size = scanner.nextInt();
        int[][] puzzle = new int[size][size];
        scanner.nextLine();
        for (int colunm = 0; colunm < size; colunm++) {
            for (int row = 0; row < size; row++) {
                puzzle[colunm][row] = scanner.nextInt();
            }
        }
        SolvePuzzle(puzzle);
    }
    
}