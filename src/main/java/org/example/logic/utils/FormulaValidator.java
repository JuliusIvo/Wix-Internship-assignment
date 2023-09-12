package org.example.logic.utils;

import java.util.List;

public class FormulaValidator {

    public static String findFormulaType(Object cell) {
        return Formula.match(cell.toString()) != null ?
                Formula.match(cell.toString()).name() : "";
    }

    public static boolean containsFormula(List<String> operations){
        return operations.stream().anyMatch(operation -> !findFormulaType(operation).equals(""));
    }

    public static boolean isID(String input){
        if(input.length()<2){
            return false;
        }
        return Character.isDigit(input.charAt(1)) &&
                Character.isLetter(input.charAt(0)) &&
                Character.isUpperCase(input.charAt(0));
    }
}
