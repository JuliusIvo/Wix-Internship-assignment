package org.example.logic.utils;

import org.example.model.Sheet;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.example.logic.utils.FormulaValidator.isID;
import static org.example.logic.utils.IdInterpreter.getColumnById;
import static org.example.logic.utils.IdInterpreter.getRowById;

public class FormulaValueParser {

    private static List<Object> formulaActions(String cellValue){
        return List.of(cellValue.substring(0, cellValue.length()-1).substring(cellValue.indexOf("(")+1).replace(" ", "").split(","));
    }

    public static Object findValueByCellId(String cellAsString, Sheet sheet){
        if(cellAsString.contains("=")){
            cellAsString = cellAsString.replace("=", "");
        }
        Object result = sheet.getData().get(getRowById(cellAsString)).get(getColumnById(cellAsString));
        if(result.toString().matches("=+[A-Z]+[0-9]+")){
            result = findValueByCellId(result.toString().replace("=",""), sheet);
        }
        return result;
    }

    public static boolean isNumber(String input){
        try{
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    public static Number parseNumber(String input){
        try{
            return Integer.parseInt(input);
        } catch (NumberFormatException notInteger){
            try{
                return Double.parseDouble(input);
            } catch (NumberFormatException notDouble){
                try{
                    return Long.parseLong(input);
                } catch (NumberFormatException notLong){
                    throw new RuntimeException("not supported number format");
                }
            }
        }
    }

    public static List<Object> findAllFormulaValues(Object cell, Sheet sheet){
        List<Object> operations = new ArrayList<>(formulaActions(cell.toString().replace("=", "")));
        List<Object> values = new ArrayList<>();
        if(operations.isEmpty()){
            return new ArrayList<>();
        }
        for(Object operation : operations){
            if(isID(operation.toString())) {
                values.add(findValueByCellId(operation.toString(), sheet));
            }
            else {
                if(Boolean.parseBoolean(operation.toString())){
                    values.add(true);
                }
                else if(operation.toString().equals("false")){
                    values.add(false);
                }
                else if(isNumber(operation.toString())){
                    values.add(Double.valueOf(operation.toString()));
                }
                else if(operation.toString().startsWith("\"") && operation.toString().endsWith("\"")
                && operation.toString().length()>=2){
                    operation =  operation.toString().substring(1, operation.toString().length()-1);
                    values.add(operation);
                }
                else {
                    values.add("#ERROR: Incompatible types");
                }
            }
        }
        return values;
    }

    public static List<Number> findAllFormulaNumberValues(Object cell, Sheet sheet) {
        List<Object> operations = formulaActions(cell.toString().replace("=", ""));
        List<Number> numberValues = new ArrayList<>();
        if(operations.isEmpty()){
            return new ArrayList<>();
        }
        for(Object operation : operations){
            if(Character.isDigit(operation.toString().charAt(0))) {
                try {
                    numberValues.add(NumberFormat.getNumberInstance().parse(operation.toString()));
                } catch (ParseException e){
                    throw new RuntimeException(e);
                }
            }
            else if(isID(operation.toString())) {
                numberValues.add((Number) findValueByCellId(operation.toString(), sheet));
            }
            else {
                return new ArrayList<>();
            }
        }
        return numberValues;
    }

}
