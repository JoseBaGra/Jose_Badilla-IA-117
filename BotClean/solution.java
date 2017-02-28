import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;
import java.lang.*;

public class Solution {
    // http://docs.oracle.com/javase/8/docs/api/java/lang/Float.html#MAX_VALUE
    static float infinity = 2139095039;
    
    static void next_move(int[] bot, String[] board){
        int[] dirt = new int[2];
        float distance = infinity;
        float distancePrime;
        for(int line = 0; line < board.length; line++){
            for(int character = 0; character < board[line].length(); character++){
                if (board[line].charAt(character) == 'd'){
                    //System.out.println(((Math.abs(bot[0]-line) + Math.abs(bot[1]-character)) < (dirt[0]+dirt[1])));
                    distancePrime = calculateDistance(bot[0],bot[1],line,character);
                    if(distance == infinity){
                        distance = distancePrime;
                        dirt[0]=line;dirt[1]=character;
                    }
                    else if(distance > distancePrime){
                        dirt[0]=line;dirt[1]=character;
                        distance = distancePrime;
                        //System.out.println(dirt[0]+" "+dirt[1]);
                    }
                    
                }
            }
        }
        
        if(bot[0] != dirt[0] || bot[1] != dirt[1]){
            if(bot[0]<dirt[0]){bot[0]++;System.out.println("DOWN");}
            else if(bot[0]>dirt[0]){bot[0]--;System.out.println("UP");}
            else if(bot[1]<dirt[1]){bot[1]++;System.out.println("RIGHT");}
            else if(bot[1]>dirt[1]){bot[1]--;System.out.println("LEFT");}
        }
        else {System.out.println("CLEAN");}
        
    }
    
    // http://d1hyf4ir1gqw6c.cloudfront.net//wp-content/uploads/dist_formula.png this formula calculates distance between two points
    static float calculateDistance(float botX, float botY, float dirtX, float dirtY){
        float x = (botX-dirtX)*(botX-dirtX);
        float y = (botY-dirtY)*(botY-dirtY);
        return (float)(Math.sqrt(Math.sqrt(x-y)));
    }
        
    static void next_move_aux(int[] bot, ArrayList<int[]> dirt){
        if(dirt.size()==0){return;}
        int[] actualDirt = dirt.get(0);
        dirt.remove(0);
        
        next_move_aux(bot, dirt);
    }
    
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int [] pos = new int[2];
        String board[] = new String[5];
        for(int i=0;i<2;i++) pos[i] = in.nextInt();
        for(int i=0;i<5;i++) board[i] = in.next();
        next_move(pos, board);
    }
}
