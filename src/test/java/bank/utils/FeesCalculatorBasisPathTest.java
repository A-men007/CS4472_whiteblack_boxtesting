package bank.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runners.Parameterized;
import static org.junit.Assert.assertEquals;

import java.util.stream.Stream;

public class FeesCalculatorBasisPathTest {
    public double actualFee, expectedFee;
    double delta = 0.0001; //delta variable used for margin of error in test case
    FeesCalculator feesCalculator;


    @Parameterized.Parameter(0)
    public boolean student;
    @Parameterized.Parameter(1)
    public double amount;
    @Parameterized.Parameter(2)
    public double fromAccountBalance;
    @Parameterized.Parameter(3)
    public double toAccountBalance;;
    @Parameterized.Parameter(4)
    public double feePercentage;

    @BeforeEach
    public void setUp() throws Exception {
        feesCalculator = new FeesCalculator();
    }

    @AfterEach
    public void tearDown() throws Exception {
        actualFee = 0;
        expectedFee = 0;
    }

    @ParameterizedTest(name = "[{index}] - {0} | {1} | {2} | {3} | {4} ")
    @MethodSource("feesData")
    public void basisPathTest(boolean student, double amount, double fromAccountBalance, double toAccountBalance, double feePercentage) {
        expectedFee = amount * feePercentage;
        actualFee = feesCalculator.calculateTransferFee(amount, fromAccountBalance, toAccountBalance, student);
        System.out.printf("Expected Value %f | Actual Value %f", expectedFee, actualFee);
        assertEquals(expectedFee, actualFee, delta);
    }

    private static Stream<Arguments> feesData() {
        return Stream.of(
                Arguments.of(false, 50, 500, 500, 0.02),        // Test 1
                Arguments.of(false, 50, 500, 1500, 0.01),       // Test 2
                Arguments.of(false, 50, 1500, 500, 0.1),        // Test 3
                Arguments.of(false, 50, 1500, 1500, 0.05),      // Test 4
                Arguments.of(false, 150, 500, 500, 0.01),       // Test 5
                Arguments.of(false, 150, 500, 1500, 0.005),     // Test 6
                Arguments.of(false, 150, 1500, 500, 0.05),      // Test 7
                Arguments.of(false, 150, 1500, 1500, 0.055),    // Test 8
                Arguments.of(true, 50, 500, 500, 0.01),         // Test 9
                Arguments.of(true, 50, 500, 1500, 0.005),       // Test 10
                Arguments.of(true, 50, 1500, 500, 0.05),        // Test 11
                Arguments.of(true, 50, 1500, 1500, 0.025),      // Test 12
                Arguments.of(true, 150, 500, 500, 0.005),       // Test 13
                Arguments.of(true, 150, 500, 1500, 0.0025),     // Test 14
                Arguments.of(true, 150, 1500, 500, 0.025),      // Test 15
                Arguments.of(true, 150, 1500, 1500, 0.0125)     // Test 16
        );
    }
}
