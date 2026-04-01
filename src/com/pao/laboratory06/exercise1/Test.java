package com.pao.laboratory06.exercise1;
import java.util.Locale;
import com.pao.test.IOTest;

public class Test {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        IOTest.runParts("src/com/pao/laboratory06/exercise1/tests", Main::main);
    }
}