/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Point;
import java.util.*;

/**
 *
 * @author joseb
 */
public class PacmanAStar {

	/**
	 * @param args the command line arguments
	 */
	
	public static class Node{
		private Node parent;
		private Point location;
		private int H;
		private int F;
		
		public Node(Node pParent, Point pLocation, Point Target){
			parent = pParent;
			location = pLocation;
			H = H(pLocation, Target);
		}
		
		public Node(Node pParent, int pF , Point pLocation, Point Target){
			parent = pParent;
			location = pLocation;
			H = H(pLocation, Target);
			setF(pF);
		}

		public void setParent(Node pParent) {parent = pParent;}
		public Node getParent() {return parent;}
		public int getF() {return F;}
		public void setF(int F) {this.F = F;}
		public int getH() {return H;}
		public int getG(){return (F+H);}
		public Point getLocation() {return location;}
		
		public void PrintSolution(){
			Node aux = this;
			int moves = -1; //-1 porque cuenta el primer estado como movimiento
			String path = "";
			while(aux!=null){
				moves++;
				path = aux.getLocation().x + " " + aux.getLocation().y + "\n" + path;
				aux = aux.getParent();
			}
			System.out.println(moves+"\n"+path);
		}		
	}
	
	static final char wall = '%';
	// http://www.java2s.com/Tutorial/Java/0040__Data-Type/IntegerMAXMINVALUE.htm
	static final int max = 2147483647;
	
	public static int H(Point pLocation, Point pFood){
		return (Math.abs(pFood.x - pLocation.x) + Math.abs(pFood.y - pLocation.y));
	}
	
	public static void AStar(Point pPacman, Point pFood, char[][] pTable){
		ArrayList<Node> openList = new ArrayList<>();
		ArrayList<Node> closeList = new ArrayList<>();
		Node initial = new Node(null, 0, pPacman, pPacman);
		boolean found = false;
		closeList.add(initial);
		addRoutes(initial, pFood, pTable, openList);
		while(!found){
			int minG = max;
			Node minNode = null;
			for(Node node : openList){
				if(minG>node.getG()){
					minG = node.getG();
					minNode = node;
				}
			}

			closeList.add(minNode);
			openList.remove(minNode);
			addRoutes(minNode, pFood, pTable, openList);
			deleteBFromA(openList, closeList);
			
			for(Node node : openList){
				if(node.getLocation().x==pFood.x && node.getLocation().y==pFood.y){
					found = true;
					node.PrintSolution();
					break;
				}
			}
		}
	}
	
	public static void addRoutes(Node pStart, Point pGoal, char[][] pTable, ArrayList<Node> pOpenList){
		if(pStart==null){return;}
		if(pStart.getLocation().x + 1 < pTable.length){
			if(pTable[pStart.getLocation().x+1][pStart.getLocation().y]!=wall){
				pOpenList.add(new Node(pStart, pStart.getF()+1, new Point(pStart.getLocation().x+1,pStart.getLocation().y), pGoal));
			}
		}
		if(pStart.getLocation().y + 1 < pTable[0].length){
			if(pTable[pStart.getLocation().x][pStart.getLocation().y+1]!=wall){
				pOpenList.add(new Node(pStart, pStart.getF()+1, new Point(pStart.getLocation().x,pStart.getLocation().y+1), pGoal));
			}
		}
		if(pStart.getLocation().x - 1 > 0){
			if(pTable[pStart.getLocation().x-1][pStart.getLocation().y]!=wall){
				pOpenList.add(new Node(pStart, pStart.getF()+1, new Point(pStart.getLocation().x-1,pStart.getLocation().y), pGoal));
			}
		}
		if(pStart.getLocation().y - 1 > 0){
			if(pTable[pStart.getLocation().x][pStart.getLocation().y-1]!=wall){
				pOpenList.add(new Node(pStart, pStart.getF()+1, new Point(pStart.getLocation().x,pStart.getLocation().y-1), pGoal));
			}
		}
	}
	
	public static void deleteBFromA(ArrayList<Node> pA, ArrayList<Node> pB){
		for(Node b : pB){
			for (int i = 0; i < pA.size(); i++) {
				if(b.getLocation().x==pA.get(i).getLocation().x && b.getLocation().y==pA.get(i).getLocation().y){
					pA.remove(i);
				}							
			}
		}
	}
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Point pacman = new Point(scanner.nextInt(), scanner.nextInt());
		Point food = new Point(scanner.nextInt(), scanner.nextInt());
		Point size = new Point(scanner.nextInt(), scanner.nextInt());
		char[][] table = new char[size.x][size.y];
		scanner.nextLine();
		for (int line = 0; line < size.x; line++) {
			char [] characters = scanner.nextLine().toCharArray();
			for (int character = 0; character < characters.length; character++) {
				table[line][character] = characters[character];
			}
		}
		AStar(pacman, food, table);
	}
	
}