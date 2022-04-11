/**
 * FeesCalculatorTransferTest 

 * Group 20

 * The focus here is to check for the correct calculation of fees 
 * while performing a transfer transaction.
 * Use Decision Table Analysis to generate your test cases.
 *
 */

package bank.utils;
import static org.junit.Assert.*;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Collection;
import java.util.Arrays;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;


@RunWith(Parameterized.class)
public class FeesCalculatorTransferTest{
	public double actualValue;
	FeesCalculator transferTestObj; //initialize the calculator we will be testing
	double delta = 0.5; //delta variable used for margin of error in test case
	
	/**
	 * @parameter0 (transferAmount) (amount transfered by a client)
	 * @parameter1 (fromAccountBalance) (account balance of a client :from)
	 * @parameter2 (toAccountBalance) (account balance of a client :to)
	 * @parameter3 (studentCheck) (boolean variable to determine whether the client is a student)
	 * @parameter4 (expectedValue) (manual calculation of the correct/expected value of each test case)
	 */
	@Parameter(0)
	public int transferAmount;
	@Parameter(1)
	public int fromAccountBalance;
	@Parameter(2)
	public int toAccountBalance;;
	@Parameter(3)
	public boolean studentCheck;
	@Parameter(4)
	public double expectedValue;
	@Before
	public void calculate() {
		transferTestObj = new FeesCalculator();
	}

	@Parameters
    public static Collection<Object[]> ParameterizedTestFields() {
        return Arrays.asList(new Object[][] { 
        	 {85, 850, 850, true, Math.round(0.01 * 85)},// Test Case 1
             {85, 850, 1000, true, Math.round(0.0005 * 85)},// Test Case 2
             {85, 1000, 850, true, Math.round(0.05 * 85)},// Test Case 3
             {85, 1000, 1000, true, Math.round(0.025 * 85)},// Test Case 4
             {100, 850, 850, true, Math.round(0.0005* 100)},// Test Case 5
             {100, 850, 1000, true, Math.round(0.00025* 100)},// Test Case 6
             {100, 1000, 850, true, Math.round(0.025*100)},// Test Case 7
             {100, 1000, 1000, true, Math.round(100 * 0.0125)},// Test Case 8
             
             {85, 850, 850, false, Math.round(0.02 * 85)},// Test Case 9
             {85, 850, 1000, false, Math.round(0.01 * 85)},// Test Case 10
             {85, 1000, 850, false, Math.round(0.1 * 85)},// Test Case 11
             {85, 1000, 1000, false, Math.round(0.05 * 85)},// Test Case 12
             {100, 850, 850, false, Math.round(0.01* 100)},// Test Case 13
             {100, 850, 1000, false, Math.round(0.0005* 100)},// Test Case 14
             {100, 1000, 850, false, Math.round(0.05*100)},// Test Case 15
             {100, 1000, 1000, false, Math.round(100 * 0.05)},// Test Case 16
 	    
         });
     }
        
@Test
public void testFeesCalculator() {
	actualValue  = transferTestObj.calculateTransferFee(transferAmount, fromAccountBalance, toAccountBalance, studentCheck);
	assertEquals(expectedValue, actualValue, delta);
}}