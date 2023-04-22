import java.util.*;
import java.io.*;
import java.math.*;
import java.awt.*;
class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int surfaceN = in.nextInt(); 
        Point[] land = new Point[surfaceN];
        int ground = -1;
        int high = 0;
        for (int i = 0; i < surfaceN; i++) {
            int landX = in.nextInt(); 
            int landY = in.nextInt(); 
            land[i] = new Point(landX,landY);
            high = Math.max(high,landY);
            if (i == 0) continue;
            if (land[i].y == land[i-1].y) ground = i-1;
        }

        int testCase = -1;
        boolean offTheMark = false;

        // game loop
        while (true) {
            int X = in.nextInt();
            int Y = in.nextInt();
            int hSpeed = in.nextInt(); 
            int vSpeed = in.nextInt(); 
            int fuel = in.nextInt(); 
            int rotate = in.nextInt(); 
            int power = in.nextInt(); 

            if (testCase == -1) {
                if (hSpeed == 0) testCase = 1;
                else testCase = 0;
            }

            
            if (testCase == 0) {
                if (Y - land[ground].y < 800) {
                    if (vSpeed <= -39) System.out.println("0 4");
                    else System.out.println("0 3");
                    continue;
                }
                else if (X <= land[ground+1].x) {System.out.println("-45 4"); continue;}
                else if (vSpeed <= -20) {System.out.println("0 4"); continue;}
                else if (vSpeed <= -12) {System.out.println("0 2"); continue;}
                else {System.out.println("45 4"); continue;}
            }

            
            if (testCase == 1) {
                System.err.println(offTheMark);
                if (vSpeed < -45 || Y <= 1135) {System.out.println("0 4"); continue;}
                else if (X <= land[ground].x) {System.out.println("-32 3"); continue;}
                else if (vSpeed == 0 && Y > high) {System.out.println("0 3"); continue;}
                else if (vSpeed < 0 || Y < high) {System.out.println("0 4"); continue;}
                else if (vSpeed >= 12 || offTheMark) {
                    offTheMark = true;
                    System.out.println("45 4");
                    continue;
                }
                else {System.out.println("0 4"); continue;}
            }
        }
    }
}