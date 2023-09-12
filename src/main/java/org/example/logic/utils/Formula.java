package org.example.logic.utils;

import java.util.stream.Stream;
public enum Formula {
        SUM,
        MULTIPLY,
        DIVIDE,
        IF,
        GT,
        EQ,
        NOT,
        AND,
        OR,
        CONCAT;

        public static Stream<Formula> stream(){
            return Stream.of(Formula.values());
        }

        public static Formula match(String input){
            return stream()
                    .filter(formula -> input.contains(formula.name()))
                    .findFirst()
                    .orElse(null);
        }
}
