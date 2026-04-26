package com.pao.laboratory07.exercise1;

import com.pao.test.IOTest;

public class Checker {
    public static void main(String[] args) {

//        IOTest.runParts("src/com/pao/laboratory07/exercise1/tests", Main::main);

        IOTest.runPart("src/com/pao/laboratory07/exercise1/tests", "partA", Main::main);
        IOTest.runPart("src/com/pao/laboratory07/exercise1/tests", "partB", Main::main);
        IOTest.runPart("src/com/pao/laboratory07/exercise1/tests", "partC", Main::main);
    }
}
