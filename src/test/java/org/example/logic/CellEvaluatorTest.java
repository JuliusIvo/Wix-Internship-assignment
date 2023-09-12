package org.example.logic;

import org.example.model.Sheet;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CellEvaluatorTest {
    String errorMsg = "#ERROR: Incompatible types";
    CellEvaluator evaluator = new CellEvaluator();

    @Test
    void evaluateMultiply(){
        List<Sheet> a = List.of(new Sheet("sheet-1", List.of(List.of(2.5, 2,"=MULTIPLY(A1, B1)"))));
        assertEquals(5.0, evaluator.evaluateSheets(a).get(0).getData().get(0).get(2));
    }

    @Test
    void evaluatesSumCorrectly() {
        List<Sheet> sumSheet = List.of(new Sheet("sheet-1", List.of(List.of("=SUM(12, 13)"))));
        assertEquals(evaluator.evaluateSheets(sumSheet).get(0).getData().get(0).get(0), 25L);
    }
    @Test
    void evaluatesSumAsErrorIfFormulaContainsString() {
        List<Sheet> sumSheet = List.of(new Sheet("sheet-1", List.of(List.of("=SUM(Hello, 13)"))));
        assertEquals(evaluator.evaluateSheets(sumSheet).get(0).getData().get(0).get(0), errorMsg);
    }

    @Test
    void evaluatesSumAsErrorIfFormulaContainsBoolean() {
        List<Sheet> sumSheet = List.of(new Sheet("sheet-1", List.of(List.of("=SUM(Hello, false)"))));
        assertEquals(evaluator.evaluateSheets(sumSheet).get(0).getData().get(0).get(0), errorMsg);
    }

    @Test
    void evaluatesMultiplyCorrectly() {
        List<Sheet> multiplySheet = List.of(new Sheet("sheet-1", List.of(List.of("=MULTIPLY(12, 12)"))));
        assertEquals(evaluator.evaluateSheets(multiplySheet).get(0).getData().get(0).get(0), 144L);
    }

    @Test
    void evaluatesMultiplyAsErrorIfFormulaContainsString() {
        List<Sheet> multiplySheet = List.of(new Sheet("sheet-1", List.of(List.of("=MULTIPLY(12, Hello)"))));
        assertEquals(evaluator.evaluateSheets(multiplySheet).get(0).getData().get(0).get(0), errorMsg);
    }

    @Test
    void evaluatesMultiplyAsErrorIfFormulaContainsBoolean() {
        List<Sheet> multiplySheet = List.of(new Sheet("sheet-1", List.of(List.of("=MULTIPLY(12, true)"))));
        assertEquals(evaluator.evaluateSheets(multiplySheet).get(0).getData().get(0).get(0), errorMsg);
    }
    @Test
    void evaluatesDivideCorrectly() {
        List<Sheet> divideSheet = List.of(new Sheet("sheet-1", List.of(List.of("=DIVIDE(12, 12)"))));
        assertEquals(evaluator.evaluateSheets(divideSheet).get(0).getData().get(0).get(0), 1.0);
    }

    @Test
    void evaluatesDivideAsErrorIfTooManyOperations() {
        List<Sheet> divideSheet = List.of(new Sheet("sheet-1", List.of(List.of("=DIVIDE(12, 12, 12)"))));
        assertEquals(evaluator.evaluateSheets(divideSheet).get(0).getData().get(0).get(0), errorMsg);
    }

    @Test
    void evaluatesDivideAsErrorIfContainsString() {
        List<Sheet> divideSheet = List.of(new Sheet("sheet-1", List.of(List.of("=DIVIDE(12, Hello)"))));
        assertEquals(evaluator.evaluateSheets(divideSheet).get(0).getData().get(0).get(0), errorMsg);
    }

    @Test
    void evaluatesDivideAsErrorIfContainsBoolean() {
        List<Sheet> divideSheet = List.of(new Sheet("sheet-1", List.of(List.of("=DIVIDE(12, true)"))));
        assertEquals(evaluator.evaluateSheets(divideSheet).get(0).getData().get(0).get(0), errorMsg);
    }

    @Test
    void evaluatesGreaterThanCorrectly() {
        List<Sheet> gtSheet = List.of(new Sheet("sheet-1", List.of(List.of("=GT(12, 11)"))));
        assertEquals(evaluator.evaluateSheets(gtSheet).get(0).getData().get(0).get(0), true);
    }

    @Test
    void evaluatesGreaterAsErrorIfMoreThanTwoValuesPresent() {
        List<Sheet> gtSheet = List.of(new Sheet("sheet-1", List.of(List.of("=GT(12, 11, 10)"))));
        assertEquals(evaluator.evaluateSheets(gtSheet).get(0).getData().get(0).get(0), errorMsg);
    }

    @Test
    void evaluatesGreaterAsErrorIfOneValuePresent() {
        List<Sheet> gtSheet = List.of(new Sheet("sheet-1", List.of(List.of("=GT(12)"))));
        assertEquals(evaluator.evaluateSheets(gtSheet).get(0).getData().get(0).get(0), errorMsg);
    }

    @Test
    void evaluatesGreaterAsErrorIfContainsString() {
        List<Sheet> gtSheet = List.of(new Sheet("sheet-1", List.of(List.of("=GT(12, Hello)"))));
        assertEquals(evaluator.evaluateSheets(gtSheet).get(0).getData().get(0).get(0), errorMsg);
    }

    @Test
    void evaluatesGreaterAsErrorIfContainsBoolean() {
        List<Sheet> gtSheet = List.of(new Sheet("sheet-1", List.of(List.of("=GT(12, false)"))));
        assertEquals(evaluator.evaluateSheets(gtSheet).get(0).getData().get(0).get(0), errorMsg);
    }

    @Test
    void evaluatesEqualsCorrectlyIfNumericValuesPresent() {
        List<Sheet> gtSheet = List.of(new Sheet("sheet-1", List.of(List.of("=EQ(12, 11)"))));
        assertEquals(evaluator.evaluateSheets(gtSheet).get(0).getData().get(0).get(0), false);
    }

    @Test
    void evaluatesEqualsCorrectlyIfStringValuesPresent() {
        List<Sheet> gtSheet = List.of(new Sheet("sheet-1", List.of(List.of("EQ(\"=\", \"=\")"))));
        assertEquals(false, evaluator.evaluateSheets(gtSheet).get(0).getData().get(0).get(0));
    }

    @Test
    void evaluatesEqualsAsErrorIfNotMatchingValuesPresent() {
        List<Sheet> gtSheet = List.of(new Sheet("sheet-1", List.of(List.of("=EQ(hello, 1)"))));
        assertEquals(evaluator.evaluateSheets(gtSheet).get(0).getData().get(0).get(0), errorMsg);
    }
    @Test
    void evaluatesEqualsAsErrorIfIncorrectAmountOfValuesPresent() {
        List<Sheet> gtSheet = List.of(new Sheet("sheet-1", List.of(List.of("=EQ(true)"))));
        assertEquals(evaluator.evaluateSheets(gtSheet).get(0).getData().get(0).get(0), errorMsg);
    }

    @Test
    void evaluatesNotCorrectly() {
        List<Sheet> notSheet = List.of(new Sheet("sheet-1", List.of(List.of("=NOT(true)"))));
        assertEquals(evaluator.evaluateSheets(notSheet).get(0).getData().get(0).get(0), false);
    }

    @Test
    void evaluatesNotAsErrorIfNotBoolean() {
        List<Sheet> notSheet = List.of(new Sheet("sheet-1", List.of(List.of("=NOT(123)"))));
        assertEquals(evaluator.evaluateSheets(notSheet).get(0).getData().get(0).get(0), errorMsg);
    }

    @Test
    void evaluatesNotAsErrorIfTooManyValuesPresent() {
        List<Sheet> notSheet = List.of(new Sheet("sheet-1", List.of(List.of("=NOT(true, true)"))));
        assertEquals(evaluator.evaluateSheets(notSheet).get(0).getData().get(0).get(0), errorMsg);
    }

    @Test
    void evaluatesAndCorrectly() {
        List<Sheet> andSheet = List.of(new Sheet("sheet-1", List.of(List.of("=AND(true, true)"))));
        assertEquals(evaluator.evaluateSheets(andSheet).get(0).getData().get(0).get(0), true);
    }

    @Test
    void evaluatesAndCorrectlyWhenMoreValues() {
        List<Sheet> andSheet = List.of(new Sheet("sheet-1", List.of(List.of("=AND(true, true, true)"))));
        assertEquals(evaluator.evaluateSheets(andSheet).get(0).getData().get(0).get(0), true);
    }

    @Test
    void evaluatesAndAsErrorWhenNotEnoughValuesPresent() {
        List<Sheet> andSheet = List.of(new Sheet("sheet-1", List.of(List.of("=AND(true)"))));
        assertEquals(evaluator.evaluateSheets(andSheet).get(0).getData().get(0).get(0), errorMsg);
    }

    @Test
    void evaluatesAndAsFalseWhenFalseExistsInFormula() {
        List<Sheet> andSheet = List.of(new Sheet("sheet-1", List.of(List.of("=AND(true, false)"))));
        assertEquals(evaluator.evaluateSheets(andSheet).get(0).getData().get(0).get(0), false);
    }

    @Test
    void evaluatesOrCorrectly() {
        List<Sheet> orSheet = List.of(new Sheet("sheet-1", List.of(List.of("=OR(true, false)"))));
        assertEquals(evaluator.evaluateSheets(orSheet).get(0).getData().get(0).get(0), true);
    }

    @Test
    void evaluatesOrCorrectlyWhenMoreThanTwoValuesPresent() {
        List<Sheet> orSheet = List.of(new Sheet("sheet-1", List.of(List.of("=OR(false, false, true)"))));
        assertEquals(evaluator.evaluateSheets(orSheet).get(0).getData().get(0).get(0), true);
    }

    @Test
    void evaluatesOrAsErrorIfNotBooleanValuePresent() {
        List<Sheet> orSheet = List.of(new Sheet("sheet-1", List.of(List.of("=OR(true, hello)"))));
        assertEquals(evaluator.evaluateSheets(orSheet).get(0).getData().get(0).get(0), errorMsg);
    }

    @Test
    void evaluatesIfCorrectly() {
        List<Sheet> ifSheet = List.of(new Sheet("sheet-1", List.of(List.of("=IF(true, 123, false)"))));
        assertEquals(evaluator.evaluateSheets(ifSheet).get(0).getData().get(0).get(0), 123);
    }

    @Test
    void evaluatesIfAsErrorIfIncorrectAmountOfArguments() {
        List<Sheet> ifSheet = List.of(new Sheet("sheet-1", List.of(List.of("=IF(true, 123, false, false)"))));
        assertEquals(evaluator.evaluateSheets(ifSheet).get(0).getData().get(0).get(0), errorMsg);
    }

    @Test
    void evaluatesIfAsErrorIfConditionIsNotOfBooleanValue() {
        List<Sheet> ifSheet = List.of(new Sheet("sheet-1", List.of(List.of("=IF(123, 123, false)"))));
        assertEquals(evaluator.evaluateSheets(ifSheet).get(0).getData().get(0).get(0), errorMsg);
    }

    @Test
    void evaluatesConcatCorrectly() {
        List<Sheet> ifSheet = List.of(new Sheet("sheet-1", List.of(List.of("=CONCAT(\"Hello\",\" \",\"World!\")"))));
        assertEquals(evaluator.evaluateSheets(ifSheet).get(0).getData().get(0).get(0), "Hello World!");
    }

}