import java.util.*;
import java.io.*;
import java.math.*;
 
/** Class representing a motorbike. A motorbike has 3 properties :
 * <ul>
 * <li> Its X and Y coordinates on the road
 * <li> A boolean A true if and only if the motorbike is activated
 * </ul>
 */
class Moto {
    int X;
    int Y;
    boolean A;
    
    public Moto(int X, int Y, boolean A){
        this.X = X;
        this.Y = Y;
        this.A = A;
    }
    
    public Moto(Moto m){
        this.X = m.X;
        this.Y = m.Y;
        this.A = m.A;
    }
}
 
/** This class represents a step of the run, ie the positions of the
 * motorbikes, and their speed
 */
class State {
    Moto[] motos;
    int s;
    
    public State(Moto[] motos, int s) {
        this.motos = motos;
        this.s = s;
    }
    
    public State(State s) {
        this.motos = new Moto[s.motos.length];
        for (int i = 0; i < s.motos.length; i++)
            this.motos[i] = new Moto(s.motos[i]);
        this.s = s.s;
    }
}
 
class Player {
 
    /* Returns true if there is a hole between the moto m
     * and its next position on the lane
     */
    static public boolean willFall (Moto m, int s, String lane){
        // The area to examine go from the moto to
        // either the end of the road, or the next position
        for (int x = m.X+1; x <= Math.min(m.X+s, lane.length()-1); x++)
            if (lane.charAt(x) == '0')
                return true;
        return false;
    }
    
    /* Put the moto on its next position
     * or disactivates it if it is going to fall in a hole
     */
    static public void move (Moto m, int s, String lane) {
        if (willFall(m, s, lane))
            m.A = false;
        else
            m.X += s;
    }
 
    /** This inner abstract class represents an action (slow, speed, etc).
     * the name should be the string to print to perform this action.
     * the function 'apply' applies the action to a state and returns the new state.
     * it should also returns null if the action can't be performed, or will lead
     * to a useless, or already encoutered state, for performance purposes
     */
    static abstract class Action {
        protected String name;
    
        /** Applies the action to state 's' and returns the new state*/
        abstract public State apply(State s);
    }
 
    static class SPEED extends Action {
    
        public SPEED(){
            this.name = "SPEED";
        }
 
        public State apply(State s){
            State s2 = new State(s);
            // increases speed by one, and moves each bike
            s2.s++;
            for (Moto m : s2.motos) 
                if (m.A)
                    move(m, s2.s, lanes[m.Y]);
            return s2;
        }
    }
 
    static class SLOW extends Action {
 
        public SLOW(){
            this.name = "SLOW";
        }
 
        public State apply(State s){
            if (s.s < 2)
                return null;
            else {
                State s2 = new State(s);
                // decreases speed by one and moves each bike
                s2.s--;
                for (Moto m : s2.motos)
                    if (m.A)
                        move(m, s2.s, lanes[m.Y]);
                return s2;
            }
        }
    }
 
    static class UP extends Action {
        
        public UP(){
            this.name = "UP";
        }
 
        public State apply(State s){
            if (s.s == 0)
                return null;
 
            // if moving UP is not possible
            for (Moto m : s.motos)
                if (m.A && m.Y == 0)
                    return null;
                    
            State s2 = new State(s);
            // for each bike, if there is a hole on the concerned area of the current lane
            // disactives the bike, else, moves it up, and moves it forward
            for (Moto m : s2.motos)
                if (m.A)
                    if (willFall(m, s2.s-1, lanes[m.Y]))
                        m.A = false;
                    else {
                        m.Y--;
                        move(m, s2.s, lanes[m.Y]);
                    }
            return s2;
        } 
    }
 
    static class DOWN extends Action {
 
        public DOWN(){
            this.name = "DOWN";
        }
        
        public State apply(State s){
            
            if (s.s == 0)
                return null;
            // if moving DOWN isnt possible
            for (Moto m : s.motos)
                if (m.A && (m.Y == 3))
                    return null;
                    
            State s2 = new State(s);
            // for each bike, if there is a hole on the concerned area of the current lane
            // disactives the bike, else, moves it down, and moves it forward
            for (int i = 0 ; i < s2.motos.length ; i++){
                Moto m = s2.motos[i];
                if (m.A)
                    if (willFall(m, s2.s-1, lanes[m.Y]))
                        m.A = false;
                    else
                    {
                        m.Y++;
                        move(m, s2.s, lanes[m.Y]);
                    }
            }
            return s2;
        }
    }
 
    static class JUMP extends Action {
        
        public JUMP(){
            this.name = "JUMP";
        }
        
        public State apply(State s){
            if (s.s == 0)
                return null;
                
            State s2 = new State(s);
            // for each bike, if there is a hole at the landing spot, disactives it
            // else, places it on the landing spot
            for (Moto m : s2.motos)
                if (m.A)
                    if (m.X+s2.s < lanes[m.Y].length() && lanes[m.Y].charAt(m.X+s2.s) == '0')
                        m.A = false;
                    else
                        m.X += s2.s;
            return s2;
        }
    }
 
    /** array of possible actions */
    private static final Action[] instructions = { new SPEED(), new JUMP(), 
        new UP(), new DOWN(), new SLOW() };
 
    /** lanes of the road*/
    private static String lanes[] = new String[4];
        
    /** If it is possible to win from state 's', this functions returns the
     * string to print to do so. Else, it returns null.
     * Winning means leading a minimum amount of 'min' bikes to the end */
    public static String winningMove(State s, int min){
        /* If the number of active motorbikes is 0, this state is lost, returns null
         if the x of an active motorbike is greater than the length of the lane,
         this state is won, returns "WAIT" */
        int nbOfA = 0;
        int x = 0;
        for (Moto m : s.motos)
            if (m.A) {
                nbOfA++;
                x = m.X;
            }
        if (nbOfA < min)
            return null;
        if (x >= lanes[0].length())
            return "WAIT";
 
        // tries each possible instruction, calling recursively winningMove
        // on all the next states.
        // If one of the calls returns a winning instruction, returns it,
        // else returns null         
        for (Action a : instructions) {
            State s2 = a.apply(s);
            if (s2 != null && winningMove(s2, min) != null)
                return a.name;
        }
        return null;
    }
 
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        // amount of motorbikes, and minimum amount of survivors
        int M = in.nextInt();
        int V = in.nextInt();
        in.nextLine();
 
        // Initializes lanes of the road
        for (int i = 0; i < 4; i++)
            lanes[i] = in.nextLine();
        int speed;
        Moto[] motos = new Moto[M];
        State s = null;
        
        while(true){
            // creates the current state
            speed = in.nextInt();
            in.nextLine();            
            for (int i = 0; i < M; i++) {
                motos[i] = new Moto(in.nextInt(), in.nextInt(), (in.nextInt() == 1));
                in.nextLine();
            }
            s = new State(motos, speed);
            
            /* Search for a move saving all the bikes (for the 3rd achievement)
            and if it is not possible, for a move saving the minimum amount of bikes*/
            String str = winningMove(s, M);
            if (str != null)
                System.out.println(str);
            else
                System.out.println(winningMove(s, V));
        }
    }
}