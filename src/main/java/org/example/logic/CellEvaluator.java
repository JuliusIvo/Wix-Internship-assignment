package org.example.logic;

import org.example.model.Sheet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.logic.utils.FormulaValidator.*;
import static org.example.logic.utils.FormulaValueParser.*;

public class CellEvaluator {

    private static final String errorMsg = "#ERROR: Incompatible types";
    public List<Sheet> evaluateSheets(List<Sheet> sheets) {
        List<Sheet> evaluatedSheets = sheets.stream().map(this::evaluateSheet).collect(Collectors.toList());
        return evaluatedSheets;
    }

    private Sheet evaluateSheet(Sheet sheet) {
        Sheet evaluatedSheet = new Sheet(sheet.getId(), new ArrayList<>());
        sheet.getData().forEach(row -> evaluatedSheet.getData().add(this.evaluateRow(row, sheet)));
        return evaluatedSheet;
    }

    private List<Object> evaluateRow(List<Object> rows, Sheet sheet) {
        List<Object> evaluatedRows = rows.stream().map(cell -> this.evaluateCell(cell, sheet)).collect(Collectors.toList());
        return evaluatedRows;
    }

    private Object evaluateCell(Object cell, Sheet sheet) {
        if(!cell.toString().startsWith("=")) {
            return cell;
        }
        return this.evaluateExpression(cell, sheet);
    }

    private Object evaluateExpression(Object cell, Sheet sheet) {
        String formulaType = findFormulaType(cell);
        switch (formulaType) {
            case "SUM" -> {
                return this.sum(cell, sheet);
            }
            case "MULTIPLY" -> {
                return this.multiply(cell, sheet);
            }
            case "DIVIDE" -> {
                return this.divide(cell, sheet);
            }
            case "IF" -> {
                return this.formulaIf(cell, sheet);
            }
            case "GT" -> {
                return this.greaterThan(cell, sheet, "GT");
            }
            case "EQ" -> {
                return this.equal(cell, sheet);
            }
            case "AND" -> {
                return this.and(cell, sheet);
            }
            case "NOT" -> {
                return this.not(cell, sheet);
            }
            case "OR" -> {
                return this.or(cell, sheet);
            }
            case "CONCAT" -> {
                return this.concat(cell, sheet);
            }
            default -> {
                return findValueByCellId(cell.toString().replace("=", ""), sheet);
            }
        }
    }
    private Object sum(Object cell, Sheet sheet) {
        List<Number> numbersInFormula = findAllFormulaNumberValues(cell, sheet);
        long result = 0;
        if(numbersInFormula.isEmpty()){
            return errorMsg;
        }
        for(Number number: numbersInFormula){
            result = result + Long.parseLong(number.toString());
        }
        return result;
    }

    private Object multiply(Object cell, Sheet sheet) {
        List<Number> numbersInFormula = findAllFormulaNumberValues(cell, sheet);
        double result = 1;
        if(numbersInFormula.isEmpty()){
            return errorMsg;
        }
        for(Number number: numbersInFormula){
            result = result * number.doubleValue();
        }
        return result;
    }

    private Object divide(Object cell, Sheet sheet) {
        List<Number> numbersInFormula = findAllFormulaNumberValues(cell, sheet);
        if(numbersInFormula.size() != 2){
            return errorMsg;
        }
        double numerator = numbersInFormula.get(0).doubleValue();
        double denominator = numbersInFormula.get(1).doubleValue();

        return numerator/denominator;
    }

    private Object greaterThan(Object cell, Sheet sheet, String typeofComparison) {
        List<Number> numbersInFormula = findAllFormulaNumberValues(cell, sheet);
        if(numbersInFormula.stream().allMatch(e -> !(e instanceof Double)) && numbersInFormula.size()==2){
            double firstOperand = numbersInFormula.get(0).doubleValue();
            double secondOperand = numbersInFormula.get(1).doubleValue();
            return typeofComparison.equals("GT") ?
                    firstOperand > secondOperand : secondOperand > firstOperand;
        }
        return errorMsg;
    }

    private Object equal(Object cell, Sheet sheet) {
        List<Object> valuesInFormula = findAllFormulaValues(cell, sheet);
        if(valuesInFormula.size() != 2){
            return errorMsg;
        }
        Object firstOperand = valuesInFormula.get(0);
        Object secondOperand = valuesInFormula.get(1);
        if(firstOperand instanceof Boolean && secondOperand instanceof Boolean){
            return firstOperand == secondOperand;
        }
        if(firstOperand instanceof Number && secondOperand instanceof Number){
            return firstOperand.equals(secondOperand);
        }
        if(firstOperand instanceof String && secondOperand instanceof String){
            return firstOperand.equals(secondOperand);
        }
        return false;
    }

    private Object not(Object cell, Sheet sheet) {
        List<Object> allFormulaValues = findAllFormulaValues(cell, sheet);
        if(!(allFormulaValues.size()>1) && allFormulaValues.get(0) instanceof Boolean){
            return !Boolean.parseBoolean(allFormulaValues.get(0).toString());
        }
        return errorMsg;
    }

    private Object and(Object cell, Sheet sheet) {
        List<Object> allFormulaValues = findAllFormulaValues(cell, sheet);
        if(allFormulaValues.size()==1){
            return errorMsg;
        }
        for(Object formulaValue : allFormulaValues){
            if(!(formulaValue instanceof Boolean)){
                return errorMsg;
            }
        }
        for(Object formulaValue : allFormulaValues){
            if(formulaValue.equals(false)){
                return false;
            }
        }
        return true;
    }

    private Object or(Object cell, Sheet sheet) {
        List<Object> allFormulaValues = findAllFormulaValues(cell, sheet);
        if(allFormulaValues.size()<1){
            return errorMsg;
        }
        for(Object formulaValue : allFormulaValues){
            if(!(formulaValue instanceof Boolean)){
                return errorMsg;
            }
        }
        for(Object formulaValue : allFormulaValues){
            if(formulaValue.equals(true)){
                return true;
            }
        }
        return false;
    }

    private Object getObjectRealValue(Object input, Sheet sheet){
        if(isID(input.toString())) {
            return findValueByCellId(input.toString(), sheet);
        }

        if(isNumber(input.toString())){
            return parseNumber(input.toString());
        }

        if(input.equals("true") || input.equals("false")){
            return Boolean.valueOf(input.toString());
        }
        return input.toString();
    }

    private Object formulaIf(Object cell, Sheet sheet) {
        cell = cell.toString().substring(0, cell.toString().length()-1).replace("=IF(", "");
        List<String> operations = new ArrayList<>(List.of(cell.toString().split(",")));
        Object ifFalse = getObjectRealValue(operations.get(operations.size()-1).trim(), sheet);
        operations.remove(operations.size()-1);
        Object ifTrue = getObjectRealValue(operations.get(operations.size()-1).trim(), sheet);
        operations.remove(operations.size()-1);
        Object condition = String.join(", ", operations);
        if(isID(condition.toString())){
            condition = findValueByCellId(condition.toString(), sheet);
        }
        if(containsFormula(List.of(condition.toString()))){
            condition = this.evaluateExpression(condition, sheet);
        }
        if(condition.equals("true") || condition.equals(true)){
            return ifTrue;
        }
        if(condition.equals("false") || condition.equals(false)){
            return ifFalse;
        }
        return errorMsg;
    }

    private Object concat(Object cell,Sheet sheet){
        String result;
        String withoutConcat = cell.toString().substring(0, cell.toString().length()-1).replace("=CONCAT(", "");
        List<String> values = new ArrayList<>();
        while (!withoutConcat.equals("")) {
            if (withoutConcat.startsWith(" ")) {
                withoutConcat = withoutConcat.substring(1);
            }
            else {
                if (withoutConcat.startsWith("\"")) {
                    withoutConcat = withoutConcat.substring(1);
                    values.add(withoutConcat.substring(0, withoutConcat.indexOf("\"")));
                    if (withoutConcat.contains("\",")) {
                        withoutConcat = withoutConcat.substring(withoutConcat.indexOf("\",")+2);
                    } else {
                        withoutConcat = "";
                    }
                } else {
                    if (withoutConcat.contains(",")) {
                        values.add(findValueByCellId(withoutConcat.substring(0, withoutConcat.indexOf(",")), sheet).toString());
                    } else {
                        values.add(findValueByCellId(withoutConcat, sheet).toString());
                    }
                    if (withoutConcat.contains(",")) {
                        withoutConcat = withoutConcat.substring(withoutConcat.indexOf(",")+1);
                    } else {
                        withoutConcat = "";
                    }
                }
            }
        }

        result = values.stream().collect(Collectors.joining());
        return result;
    }

}
