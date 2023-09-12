package org.example.logic.utils;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.example.logic.utils.FormulaValidator.*;
import static org.junit.jupiter.api.Assertions.*;

class FormulaValidatorTest {

    @Test
    void findFormulaTypeCorrectly(){
        assertEquals(findFormulaType("=SUM(A1, B1)"), "SUM");
    }

    @Test
    void formulaExistsInCellFoundCorrectly(){
        assertEquals(containsFormula(List.of("SUM(", "A1", "B1")), true);
    }

    @Test
    void formulaDoesNotExistInCell(){
        assertEquals(containsFormula(List.of("SHLUM", "A1", "B1")), false);
    }

    @Test
    void checkIfExpressionIsID(){
        assertEquals(isID("A1"), true);
    }

    @Test
    void checkIfExpressionIsNotID(){
        assertEquals(isID("1A"), false);
        assertEquals(isID("11"), false);
        assertEquals(isID("AAA"), false);
        assertEquals(isID("a1"), false);
    }
}