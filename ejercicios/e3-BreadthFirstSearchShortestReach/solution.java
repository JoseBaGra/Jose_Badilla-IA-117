/*

 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

/**
 *
 * @author joseb
 */
public class BFS {

    // menor a mayor	
	
	public static class Graph{
                private final int cost = 6;
                private final int notFound = -1;
		private ArrayList<ArrayList<Integer>> edges;
		private int qtyEdges;
		private int initalNode;
		
		Graph(int pQtyNodes, int pQtyEdges){
			edges = new ArrayList<>();
			for(int i=0; i < pQtyNodes; i++){
                            edges.add(new ArrayList<Integer>());
			}
			qtyEdges = pQtyEdges;
		}
		
		public void setInitalNode(int pInitialNode){
                    initalNode = pInitialNode - 1;
		}

		public void addEdge(int pNodeA, int pNodeB){
                    edges.get(pNodeA-1).add(pNodeB-1);
                    edges.get(pNodeB-1).add(pNodeA-1);
		}
		
                
                private void getConnections(){
                    ArrayList<Integer> remaining = new ArrayList<>();
                    ArrayList<Integer> remainingAux = new ArrayList<>();
                    ArrayList<Integer> visited = new ArrayList<>();
                    ArrayList<Integer> found = new ArrayList<>();
                    HashMap<Integer,Integer> nodes = new HashMap<>();
                    int wheight = cost;
                    remaining.add(initalNode);
                    while(!remaining.isEmpty()){
                        for (int i = 0; i < remaining.size(); i++) {
                            visited.add(remaining.get(i));
                            for(int node : edges.get(remaining.get(i))){
                                if(found.indexOf(node) == -1){
                                    found.add(node);
                                    nodes.put(node, wheight);
                                }
                                if(visited.indexOf(node) == -1 && remainingAux.indexOf(node) == -1){
                                    remainingAux.add(node);
                                }
                            }
                        }
                        remaining = (ArrayList<Integer>)remainingAux.clone();
                        remainingAux.clear();
                        wheight+=cost;
                    }
                    for (int node = 0; node < edges.size(); node++) {
                        if(node==initalNode){continue;}
                        if(nodes.containsKey(node)){System.out.print(nodes.get(node)+" ");}
                        else{System.out.print(notFound+" ");}
                    }
                    System.out.println("");
                }
		
	}
	
	public static void main(String[] args) {
            Scanner in = new Scanner(System.in);
            int quantityProblems = in.nextInt();
            int edges;
            int nodes;
            Graph tempGraph;
            for(;quantityProblems>0;quantityProblems--){
                nodes = in.nextInt();
                edges = in.nextInt();
                tempGraph = new Graph(nodes, edges);
                for (; edges > 0; edges--) {
                    tempGraph.addEdge(in.nextInt(), in.nextInt());
                }
                tempGraph.setInitalNode(in.nextInt());
                tempGraph.getConnections();
                
            }
	}
    
}
