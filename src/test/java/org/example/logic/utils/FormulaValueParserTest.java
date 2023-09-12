package org.example.logic.utils;

import org.example.model.Sheet;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FormulaValueParserTest {
    FormulaValueParser parser = new FormulaValueParser();
    Sheet sheet = new Sheet("sheet-1",List.of(List.of(12, 12.5, "=SUM(A1, B1)", "=B3"),
            List.of("Hello", true, 123, "=SUM(A2, B2, C2)"),
            List.of("=D1", "=B1")));
    @Test
    void evaluatesNumberValuesCorrectly() {
        List<Number> operations = parser.findAllFormulaNumberValues(sheet.getData().get(0).get(2), sheet);
        assertEquals(operations.get(0), 12);
        assertEquals(operations.get(1), 12.5);
    }

    @Test
    void evaluateAllFormulaValuesCorrectly() {
        List<Object> operations = parser.findAllFormulaValues(sheet.getData().get(1).get(3), sheet);
        assertEquals(operations.get(0), "Hello");
        assertEquals(operations.get(1), true);
        assertEquals(operations.get(2), 123);
    }

    @Test
    void getCorrectValueByCellId(){
        Object value = parser.findValueByCellId(sheet.getData().get(2).get(0).toString(), sheet);
        assertEquals(value, 12.5);
    }

}