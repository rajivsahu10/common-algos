package com.rajiv.algo;

public class CountValley {

    static int countingValleys(int n, String s) {
        int count = 0;
        int level = 0;
        Boolean amIInValley = false;
        char[] rec = s.toCharArray();
        if (rec.length <3 )
            return 0;

        for(int i=0; i < rec.length; i++) {
            String curValue = String.valueOf(rec[i]);
            int dirValue = direction(curValue);
            int newLevel = level + dirValue;
            if(newLevel == -2 && level== -1) {
                if (!amIInValley) {
                    amIInValley = true;
                }
            } else if (newLevel == 0 && level==-1) {
                amIInValley = false;
                count++;
            }
            level=newLevel;

        }
        return count;
    }

    private static int direction(String s) {
        if (s.equalsIgnoreCase("U")) {
            return 1;
        } else if (s.equalsIgnoreCase("D")){
            return -1;
        }
        return 0;
    }

    public static void main(String[] args) {
        String input = "DUDDDUUDUU";
        System.out.println("valley count : " + countingValleys(input.length(), input));
    }

}
