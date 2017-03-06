import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {
	static void displayPathtoPrincess(int n, String [] grid){
		int princess[] = new int[2];
		int hero[] = new int[2];
		for(int line = 0; line < grid.length; line++){
			for(int character = 0; character < grid[line].length(); character++){
				if (grid[line].charAt(character) == 'm'){
					hero[0] = line;hero[1] = character;
				}
				else if (grid[line].charAt(character) == 'p'){
					princess[0] = line;princess[1] = character;
				}
			}
		}
		displayPathtoPrincessAux(hero,princess);
	}
	
	static void displayPathtoPrincessAux(int [] hero, int[] princess){
		if(hero[0] == princess[0] && hero[1] == princess[1]){
			return;
		}
		else{
			if(hero[0]<princess[0]){hero[0]++;System.out.println("DOWN");}
			else if(hero[0]>princess[0]){hero[0]--;System.out.println("UP");}
			if(hero[1]<princess[1]){hero[1]++;System.out.println("RIGHT");}
			else if(hero[1]>princess[1]){hero[1]--;System.out.println("LEFT");}
			displayPathtoPrincessAux(hero,princess);
		}
	}
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int m;
		m = in.nextInt();
		String grid[] = new String[m];
		for(int i = 0; i < m; i++) {
			grid[i] = in.next();
		}

		displayPathtoPrincess(m,grid);
	}
}
