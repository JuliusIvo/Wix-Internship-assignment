package org.example.logic.utils;

import org.junit.jupiter.api.Test;

import static org.example.logic.utils.IdInterpreter.*;
import static org.junit.jupiter.api.Assertions.*;

class IdInterpreterTest {

    @Test
    void regexPatternAccurate(){
        assertEquals(matchesRegexPattern("AA1"), false);
    }

    @Test
    void evaluateIdCorrectly(){
        assertEquals(getColumnById("A1"), 0);
        assertEquals(getRowById("A1"), 0);
    }

    @Test
    void evaluateIdCorrectlyWhenEndsWithZero(){
        assertEquals(getRowById("A0"), -1);
        assertEquals(getColumnById("A0"), -1);
    }

    @Test
    void evaluateIdCorrectlyWhenEndsMultipleOfTen(){
        assertEquals(getColumnById("A10"), 0);
        assertEquals(getRowById("A10"), 9);
    }

    @Test
    void evaluateIdCorrectlyWhenLargeValue(){
        assertEquals(getColumnById("Z123"), 25);
        assertEquals(getRowById("Z123"), 122);
    }

    @Test
    void dontEvaluateIdWhenDoesNotMatchRegex(){
        assertEquals(getRowById("Hello"), -1);
        assertEquals(getColumnById("Hello"), -1);
    }

}