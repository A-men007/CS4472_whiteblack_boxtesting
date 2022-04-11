/**
 * FeesCalculatorWithdrawTest - 
 * Group 20 - Ositadinma Arimah
 * The focus here is to check for the correct calculation of fees 
 * while performing a withdrawal transaction.
 * Robust Worst Case Boundary Value Analysis is used generate test
 * cases.
 */

package bank.utils;
import static org.junit.Assert.*;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Collection;
import java.util.Arrays;
import java.util.Calendar;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;


@RunWith(Parameterized.class)
public class FeesCalculatorWithdrawTest {
	FeesCalculator withdrawalTestObj; //initialize the calculator we will be testing
	double delta = 0.3; //delta variable used for margin of error in test case
	
	
	/**
	 * @parameter0 (withdrawalAmount) (withdrawal amount by a client)
	 * @parameter1 (accountBalance) (account balance of a client)
	 * @parameter2 (studentCheck) (boolean variable to determine whether the client is a student)
	 * @parameter2 (expectedValue) (manual calculation of the correct/expected value of each test case)
	 */
	@Parameter(0)
	public double withdrawalAmount; // amount is set at default 50
	@Parameter(1)
	public double accountBalance;
	@Parameter(2)
	public boolean studentCheck;
	@Parameter(3)
	public int dayOfWeek;
	@Parameter(4)
	public double expectedValue; 
	
	public double actualValue; 
	 
	@Before
	public void calculate() {
		withdrawalTestObj = new FeesCalculator();
	}

	private static double calculateWithdrawalFee(double amount, double accountBalance, boolean student, int dayOfWeek) {
		// Calculate fee percentage
		double feePercentage;
		// Check if student
		if (student) {
			// Check if it's weekend
			if ((dayOfWeek == Calendar.SATURDAY) || (dayOfWeek == Calendar.SUNDAY)) {
				feePercentage = 0.0;
			} else {
				feePercentage = 0.001;
			}
		} else {
			if (accountBalance < 1000.00) {
				feePercentage = 0.002;
			} else if (accountBalance < 10000.00) {
				feePercentage = 0.001;
			} else {
				feePercentage = 0.0;
			}
		}
		
		// Calculate fee
		double fee = amount * feePercentage;
		
		return fee;
	}


@Parameters
    public static Collection<Object[]> ParameterizedTestFields() {
        return Arrays.asList(new Object[][] { 
    
      //test true student and Sunday
	{50,-1, true, Calendar.SUNDAY, calculateWithdrawalFee(50, -1, true, Calendar.SUNDAY)}, //Test Case 1
	{50,0, true, Calendar.SUNDAY, calculateWithdrawalFee(50, 0, true, Calendar.SUNDAY)},// Test Case 2
	{50,1, true, Calendar.SUNDAY, calculateWithdrawalFee(50, 1, true, Calendar.SUNDAY)},//Test Case 3
	{50,500, true, Calendar.SUNDAY, calculateWithdrawalFee(50, 500, true, Calendar.SUNDAY)},//Test Case 4
	{50,999, true, Calendar.SUNDAY, calculateWithdrawalFee(50, 999, true, Calendar.SUNDAY)},//Test Case 5
	{50,1000, true, Calendar.SUNDAY, calculateWithdrawalFee(50, 1000, true, Calendar.SUNDAY)},//Test Case 6
	{50,1001, true, Calendar.SUNDAY, calculateWithdrawalFee(50, 1001, true, Calendar.SUNDAY)},//Test Case 7
	{50,5000, true, Calendar.SUNDAY, calculateWithdrawalFee(50, 5000, true, Calendar.SUNDAY)},//Test Case 8
	{50,9999, true, Calendar.SUNDAY, calculateWithdrawalFee(50, 9999, true, Calendar.SUNDAY)},//Test Case 9
	{50,10000, true, Calendar.SUNDAY, calculateWithdrawalFee(50, 10000, true, Calendar.SUNDAY)},//Test Case 10
	{50,10001, true, Calendar.SUNDAY, calculateWithdrawalFee(50, 10001, true, Calendar.SUNDAY)},//Test Case 11
	
	//test true student and Thursday
	{50,-1, true, Calendar.THURSDAY, calculateWithdrawalFee(50, -1, true, Calendar.THURSDAY)}, //Test Case 12
	{50,0, true, Calendar.THURSDAY, calculateWithdrawalFee(50, 0, true, Calendar.THURSDAY)},// Test Case 13
	{50,1, true, Calendar.THURSDAY, calculateWithdrawalFee(50, 1, true, Calendar.THURSDAY)},//Test Case 14
	{50,500, true, Calendar.THURSDAY, calculateWithdrawalFee(50, 500, true, Calendar.THURSDAY)},//Test Case 15
	{50,999, true, Calendar.THURSDAY, calculateWithdrawalFee(50, 999, true, Calendar.THURSDAY)},//Test Case 16
	{50,1000, true, Calendar.THURSDAY, calculateWithdrawalFee(50, 1000, true, Calendar.THURSDAY)},//Test Case 17
	{50,1001, true, Calendar.THURSDAY, calculateWithdrawalFee(50, 1001, true, Calendar.THURSDAY)},//Test Case 18
	{50,5000, true, Calendar.THURSDAY, calculateWithdrawalFee(50, 5000, true, Calendar.THURSDAY)},//Test Case 19
	{50,9999, true, Calendar.THURSDAY, calculateWithdrawalFee(50, 9999, true, Calendar.THURSDAY)},//Test Case 20
	{50,10000, true, Calendar.THURSDAY, calculateWithdrawalFee(50, 10000, true, Calendar.THURSDAY)},//Test Case 21
	{50,10001, true, Calendar.THURSDAY, calculateWithdrawalFee(50, 10001, true, Calendar.THURSDAY)},//Test Case 22
	
	// test true student and Saturday
	{50,-1, true, Calendar.SATURDAY, calculateWithdrawalFee(50, -1, true, Calendar.SATURDAY)}, //Test Case 23
	{50,0, true, Calendar.SATURDAY, calculateWithdrawalFee(50, 0, true, Calendar.SATURDAY)},// Test Case 24
	{50,1, true, Calendar.SATURDAY, calculateWithdrawalFee(50, 1, true, Calendar.SATURDAY)},//Test Case 25
	{50,500, true, Calendar.SATURDAY, calculateWithdrawalFee(50, 500, true, Calendar.SATURDAY)},//Test Case 26
	{50,999, true, Calendar.SATURDAY, calculateWithdrawalFee(50, 999, true, Calendar.SATURDAY)},//Test Case 27
	{50,1000, true, Calendar.SATURDAY, calculateWithdrawalFee(50, 1000, true, Calendar.SATURDAY)},//Test Case 28
	{50,1001, true, Calendar.SATURDAY, calculateWithdrawalFee(50, 1001, true, Calendar.SATURDAY)},//Test Case 29
	{50,5000, true, Calendar.SATURDAY, calculateWithdrawalFee(50, 5000, true, Calendar.SATURDAY)},//Test Case 30
	{50,9999, true, Calendar.SATURDAY, calculateWithdrawalFee(50, 9999, true, Calendar.SATURDAY)},//Test Case 31
	{50,10000, true, Calendar.SATURDAY, calculateWithdrawalFee(50, 10000, true, Calendar.SATURDAY)},//Test Case 32
	{50,10001, true, Calendar.SATURDAY, calculateWithdrawalFee(50, 10001, true, Calendar.SATURDAY)},//Test Case 33
	
	// test false student and Sunday
	{50,-1, false, Calendar.SUNDAY, calculateWithdrawalFee(50, -1, false, Calendar.SUNDAY)}, //Test Case 34
	{50,1, false, Calendar.SUNDAY, calculateWithdrawalFee(50, 1, false, Calendar.SUNDAY)}, //Test Case 35
	{50,0, false, Calendar.SUNDAY, calculateWithdrawalFee(50, 0, false, Calendar.SUNDAY)},// Test Case 36
	{50,500, false, Calendar.SUNDAY, calculateWithdrawalFee(50, 500, false, Calendar.SUNDAY)},//Test Case 37
	{50,999, false, Calendar.SUNDAY, calculateWithdrawalFee(50, 999, false, Calendar.SUNDAY)},//Test Case 38
	{50,1000, false, Calendar.SUNDAY, calculateWithdrawalFee(50, 1000, false, Calendar.SUNDAY)},//Test Case 39
	{50,1001, false, Calendar.SUNDAY, calculateWithdrawalFee(50, 1001, false, Calendar.SUNDAY)},//Test Case 40
	{50,5000,false, Calendar.SUNDAY, calculateWithdrawalFee(50, 5000, false, Calendar.SUNDAY)},//Test Case 41
	{50,9999, false, Calendar.SUNDAY, calculateWithdrawalFee(50, 9999, false, Calendar.SUNDAY)},//Test Case 42
	{50,10000, false, Calendar.SUNDAY, calculateWithdrawalFee(50, 10000, false, Calendar.SUNDAY)},//Test Case 43
	{50,10001, false, Calendar.SUNDAY, calculateWithdrawalFee(50, 10001, false, Calendar.SUNDAY)},//Test Case 44
	
	//test false student and Thursday
	{50,-1, false, Calendar.THURSDAY, calculateWithdrawalFee(50, -1, false, Calendar.THURSDAY)}, //Test Case 45
	{50,1, false, Calendar.THURSDAY, calculateWithdrawalFee(50, 1, false, Calendar.THURSDAY)}, //Test Case 46
	{50,0, false, Calendar.THURSDAY, calculateWithdrawalFee(50, 0, false, Calendar.THURSDAY)},// Test Case 47
	{50,500, false, Calendar.THURSDAY, calculateWithdrawalFee(50, 500, false, Calendar.THURSDAY)},//Test Case 48
	{50,999, false, Calendar.THURSDAY, calculateWithdrawalFee(50, 999, false, Calendar.THURSDAY)},//Test Case 49
	{50,1000, false, Calendar.THURSDAY, calculateWithdrawalFee(50, 1000, false, Calendar.THURSDAY)},//Test Case 50
	{50,1001, false, Calendar.THURSDAY, calculateWithdrawalFee(50, 1001, false, Calendar.THURSDAY)},//Test Case 51
	{50,5000,false, Calendar.THURSDAY, calculateWithdrawalFee(50, 5000, false, Calendar.THURSDAY)},//Test Case 52
	{50,9999, false, Calendar.THURSDAY, calculateWithdrawalFee(50, 9999, false, Calendar.THURSDAY)},//Test Case 53
	{50,10000, false, Calendar.THURSDAY, calculateWithdrawalFee(50, 10000, false, Calendar.THURSDAY)},//Test Case 54
	{50,10001, false, Calendar.THURSDAY, calculateWithdrawalFee(50, 10001, false, Calendar.THURSDAY)},//Test Case 55
   
	//Test false student and Saturday
	{50,-1, false, Calendar.SATURDAY, calculateWithdrawalFee(50, -1, false, Calendar.SATURDAY)}, //Test Case 56
	{50,1, false, Calendar.SATURDAY, calculateWithdrawalFee(50, 1, false, Calendar.SATURDAY)}, //Test Case 57
	{50,0, false, Calendar.SATURDAY, calculateWithdrawalFee(50, 0, false, Calendar.SATURDAY)},// Test Case 58
	{50,500, false, Calendar.SATURDAY, calculateWithdrawalFee(50, 500, false, Calendar.THURSDAY)},//Test Case 59
	{50,999, false, Calendar.SATURDAY, calculateWithdrawalFee(50, 999, false, Calendar.SATURDAY)},//Test Case 60
	{50,1000, false, Calendar.SATURDAY, calculateWithdrawalFee(50, 1000, false, Calendar.SATURDAY)},//Test Case 61
	{50,1001, false, Calendar.SATURDAY, calculateWithdrawalFee(50, 1001, false, Calendar.SATURDAY)},//Test Case 62
	{50,5000, false, Calendar.SATURDAY, calculateWithdrawalFee(50, 5000, false, Calendar.SATURDAY)},//Test Case 63
	{50,9999, false, Calendar.SATURDAY, calculateWithdrawalFee(50, 9999, false, Calendar.SATURDAY)},//Test Case 64
	{50,10000, false, Calendar.SATURDAY, calculateWithdrawalFee(50, 10000, false, Calendar.SATURDAY)},//Test Case 65
	{50,10001, false, Calendar.SATURDAY, calculateWithdrawalFee(50, 10001, false, Calendar.SATURDAY)},//Test Case 66   
        
   });
    }
    
	@Test
    public void testFeesCalculator() {
		actualValue =withdrawalTestObj.calculateDepositInterest(withdrawalAmount, accountBalance, studentCheck);
		assertEquals(expectedValue,actualValue, delta);
    }
}



	
	

