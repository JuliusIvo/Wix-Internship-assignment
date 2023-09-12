package org.example.logic.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FormulaTest {
    @Test
    void formulaFoundCorrectly(){
        assertEquals(Formula.match("SUM"), Formula.SUM);
    }
    @Test
    void nothingWhenFormulaDoesNotExist(){
        assertEquals(Formula.match("Hello world!"), null);
    }
    @Test
    void findsFirstFormulaInInput(){
        assertEquals(Formula.match("SUM_GT"), Formula.SUM);
    }
}