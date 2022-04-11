/**
 *FeesCalculatorWithdrawalCoverageTest - 
 *Group 20 - Amanpreet Gill, Bohr Deng 
 */

package bank.utils;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runners.Parameterized.Parameter;

import java.util.Calendar;
import java.util.stream.Stream;

public class FeesCalculatorWithdrawalCoverageTest {
	public double actualValue;
	FeesCalculator feesCalculator; //initialize the calculator we will be testing
	
	@Parameter(0)
	public int depositAmount;
	@Parameter(1)
	public int accountBalance;
	@Parameter(2)
	public boolean studentCheck;
	@Parameter(3)
	public int dayOfWeek;
	@Parameter(4)
	public double expectedValue;

	@BeforeEach
	void setUp() throws Exception {
		feesCalculator = new FeesCalculator();
	}
	
	@AfterEach
    public void tearDown() throws Exception {
        
    }

	@ParameterizedTest(name = "[{index}]")
	@MethodSource("withdrawalData")
	 public void withdrawalFeesCalculator(double amount, double accountBalance, boolean student, int dayOfWeek, double expectedValue) {
		actualValue = feesCalculator.calculateWithdrawalFee(amount, accountBalance, student, dayOfWeek);
		System.out.printf("Expected Value %f | Actual Value %f", expectedValue, actualValue);
	    Assertions.assertEquals(expectedValue, actualValue);
	 }

	private static Stream<Arguments> withdrawalData() {
		return Stream.of(
				Arguments.of(1,1000, true, Calendar.SATURDAY, 0.000),        	// Test 1
				Arguments.of(1,1000, true, Calendar.SATURDAY, 0.001),       	// Test 2
				Arguments.of(1,1000, true, Calendar.SATURDAY, 0.002),        	// Test 3
				Arguments.of(1,1000, true, Calendar.SATURDAY, 0.001),      		// Test 4
				Arguments.of(1,1000, true, Calendar.SATURDAY, 0.000)	       	// Test 5
		);
	}
};

