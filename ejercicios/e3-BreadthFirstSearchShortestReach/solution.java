import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {
	// menor a mayor	
	
	public static class Graph{
		int[][] AdjacencyMatrix;
		int edges;
		int initalNode;
		
		Graph(int pNodes, int pEdges){
			AdjacencyMatrix = new int[pNodes][pNodes];
			for(int i=0; i < pNodes; i++){
				for(int j=0; j < pNodes; j++){
					AdjacencyMatrix[i][j] = -1;
				}
			}
			edges = pEdges;
		}
		
		public void setInitalNode(int pInitialNode){
			initalNode = pInitialNode - 1;
		}

		public void addEdge(int pNodeA, int pNodeB){
			AdjacencyMatrix[pNodeA-1][pNodeB-1] = 6;
			AdjacencyMatrix[pNodeB-1][pNodeA-1] = 6;
		}
		
		public void getConnections(){
			for (int node=0; node < AdjacencyMatrix.length; node++) {
				if(node != initalNode){
					getConnection(initalNode,node);
				}
			}
			System.out.print("\n");
		}

		void printMatrix(){
			for(int i=0; i < AdjacencyMatrix.length; i++){
				for(int j=0; j < AdjacencyMatrix.length; j++){
					System.out.print(AdjacencyMatrix[i][j]+"\t");
				}
				System.out.println(" ");
			}
		}

				
		private final int found  = -1;
		private void getConnection(int pNodeA, int pNodeB){
			ArrayList<Integer> visited = new ArrayList<Integer>();
			ArrayList<Integer> posibilities = new ArrayList<Integer>();
			visited.add(pNodeB);
			if(AdjacencyMatrix[pNodeB][pNodeA] == 6){
				visited.add(found);
				System.out.print("6 ");
			}
			else{
				for (int node = 0; node < AdjacencyMatrix.length; node ++) {
					if(AdjacencyMatrix[pNodeB][node]==6){
						posibilities.add(node);
					}
				}
				
				if(posibilities.size() == 0){
					System.out.print("-1 ");
				}
				else{
					ArrayList<Integer> returnValue;
					ArrayList<Integer> pivot;

					returnValue = getConnectionAux(pNodeA,posibilities.get(0),(ArrayList<Integer>)visited.clone());
					for (int posibility = 1; posibility < posibilities.size(); posibility++) {
						pivot = getConnectionAux(pNodeA,posibilities.get(posibility),(ArrayList<Integer>)visited.clone());
						if(pivot==null){continue;}
						else if(pivot.size() < returnValue.size() && pivot.get(pivot.size()-1) == found){
							returnValue = pivot;
						}
					}
					if(returnValue != null){System.out.print(((returnValue.size()-1)*6)+" ");}
					else{System.out.print("-1 ");}
				}
			}

		}

		private ArrayList<Integer> getConnectionAux(int pNodeA, int pNodeB, ArrayList<Integer> pVisited){
			ArrayList<Integer> posibilities = new ArrayList<Integer>();
			pVisited.add(pNodeB);
			if(AdjacencyMatrix[pNodeB][pNodeA] == 6){
				pVisited.add(found);
				return pVisited;
			}
			else{
				for (int node = 0; node < AdjacencyMatrix.length; node ++) {
					if(AdjacencyMatrix[pNodeB][node]==6 && pVisited.indexOf(node) == -1){
						posibilities.add(node);
					}
				}
			}

			if(posibilities.size() == 0){
				return null;
			}
			else{
				ArrayList<Integer> returnValue;
				ArrayList<Integer> pivot;
				returnValue = getConnectionAux(pNodeA,posibilities.get(0),(ArrayList<Integer>)pVisited.clone());
				for (int posibility = 1; posibility < posibilities.size(); posibility++) {
					pivot = getConnectionAux(pNodeA,posibilities.get(posibility),(ArrayList<Integer>)pVisited.clone());
					if(pivot==null){continue;}
					else if(pivot.size() < returnValue.size() && pivot.get(pivot.size()-1) == found){
						returnValue = pivot;
					}
				}
				return returnValue;
			}
		}
	}
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int quantityProblems = Integer.parseInt(in.nextLine());
		ArrayList problems = new ArrayList();
		String inputLine;
		String[] numbers;
		ArrayList<Graph> graphs = new ArrayList<Graph>();
		int graphNumber = 0;
		while(quantityProblems>0){
			int iteration = 0;
			while(true){
				inputLine = in.nextLine();
				numbers = inputLine.split(" ");
				if(numbers.length>1){
					if(!(iteration==0)){
						graphs.get(graphNumber).addEdge(Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1]));
					}
					else{
						graphs.add(new Graph(Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1])));
						iteration++;
					}
						
				}
				else{
					graphs.get(graphNumber).setInitalNode(Integer.parseInt(numbers[0]));
					quantityProblems--;
					graphNumber++;
					break;
				}
			}
		}
		for ( Graph graph : graphs) {
			graph.getConnections();
			
		}
	}
}