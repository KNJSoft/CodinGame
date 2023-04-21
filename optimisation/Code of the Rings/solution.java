import java.util.*;
import java.io.*;
import java.math.*;



class Player {

    static final String alphabet = " ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    static final String PLUS = "+";
    static final String MINUS = "-";
    static final String FORWARD = ">";
    static final String BACKWARD = "<";
    static final String TRIGGER = ".";
    static int position = 0;

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        String magicPhrase = in.nextLine();
        //String magicPhrase = "O OROFARNE LASSEMISTA CARNIMIRIE O ROWAN FAIR UPON YOUR HAIR HOW WHITE THE BLOSSOM LAY";


        String[] fields = new String[30];
        for(int i = 0; i < 30 ; i++){
            fields [i] = " ";
        }

        String shortest = new String();
        for(char c : magicPhrase.toCharArray()) {
            String best = findShortest(alphabet.indexOf(c), fields);


                shortest += best;

                int steps = steps(best);

                if(steps < 0 && position - steps < 0){
                    position = 30 - (steps + position);
                }else if(steps > 0 && position + steps > 29){
                    steps = 30 - position;
                    position = steps;
                }else{
                    position += steps;
                }
                fields[position] = String.valueOf(c);

        }
        System.out.println(shortest);
    }

    public static String findShortest(int magicIndex, String [] fields) {
        int tmp = position;
        String best = new String();
        while(true){
            String result = new String();

            if(tmp == position){
                result = trigger(letterDistance(fields[tmp], magicIndex));
            }else if(position < tmp){
                for(int i = 0; i < tmp-position; i++){
                    result += FORWARD;
                }

                result += trigger(letterDistance(fields[tmp], magicIndex));
            }else{
                for(int i = 0; i < (30-position+tmp); i++){
                    result += FORWARD;
                }
                result += trigger(letterDistance(fields[tmp], magicIndex));
            }


            tmp++;
            if(tmp == 30){
                tmp = 0;
            }
            if(tmp == position){
                break;
            }
            if(best.length() == 0){
                best = result;
            }else if(result.length() <= best.length()){
                best = result;
            }
        }

        tmp = position;
        while(true){
            String result = new String();

            if(tmp == position){
                result = trigger(letterDistance(fields[tmp], magicIndex));
            }else if(tmp < position){
                for(int i = 0; i < position-tmp; i++){
                    result += BACKWARD;
                }

                result += trigger(letterDistance(fields[tmp], magicIndex));
            }else{      
                for(int i = 0; i < 30-position+tmp; i++){
                    result += BACKWARD;
                }
                result += trigger(letterDistance(fields[tmp], magicIndex));
            }


            tmp--;
            if(tmp == -1){
                tmp = 29;
            }
            if(tmp == position){
                break;
            }

            if(best.length() == 0){
                best = result;
            }else if(result.length() <= best.length()){
                best = result;
            }
        }
        return best;
    }
    public static String trigger(int limit){

        String result = new String();
        for (int i = 0; i < Math.abs(limit); i++) {
            if (limit <= 0) {
                result += MINUS;
            } else {
                result += PLUS;
            }
        }
        return  result+TRIGGER;
    }


    public static int letterDistance(String start, int dest){
        int startPosition = alphabet.indexOf(start);

        if(startPosition < dest){
            int spinUp = dest - startPosition;
            int spinDown = 27 - dest + startPosition;

            return spinUp <= spinDown ? spinUp : -spinDown;

        }else if(startPosition > dest){
            int spinUp = 27 - startPosition + dest;
            int spinDown =  startPosition - dest;

            return spinUp <= spinDown ? spinUp : -spinDown;
        }
        return 0;
    }

    public static int steps(String str){
        int counter = 0;
        for(char c : str.toCharArray()){
            if(c == '<'){
                counter--;
            }else if(c == '>'){
                counter++;
            }
        }
        return counter;
    }
}