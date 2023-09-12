package org.example.logic.utils;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class IdInterpreter {
    public static int getRowById(String input){
        if(matchesRegexPattern(input) && Integer.parseInt(input.substring(1))!=0) {
            return Integer.parseInt(input.substring(1)) - 1;
        }
       return -1;
    }

    public static int getColumnById(String input){
        if(matchesRegexPattern(input) && Integer.parseInt(input.substring(1))!=0) {
            return letterValue(input.toCharArray()[0]);
        }
        return -1;
    }
    public static boolean matchesRegexPattern(String input){
        if(input.matches("[A-Z][0-9]+")){
            return true;
        }
        return false;
    }
    private static int letterValue(char letter){
        return letter - 'A';
    }

}
