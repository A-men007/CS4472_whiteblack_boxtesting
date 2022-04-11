/**
 * FeesCalculatorDepositTest 

 * Group 20: Ositadinma Arimah

 * The focus here is to check for the correct calculation of fees 
 * while performing a deposit transaction.
 * Weak Robust Equivalence Class Analysis is used generate test
 * cases.

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
public class FeesCalculatorDepositTest{
	public double actualValue;
	FeesCalculator depositTestObj; //initialize the calculator we will be testing
	double delta = 0.5; //delta variable used for margin of error in test case
	
	/**
	 * @parameter0 (depositAmount) (amount deposited by a client)
	 * @parameter1 (accountBalance) (account balance of a client)
	 * @parameter2 (studentCheck) (boolean variable to determine whether the client is a student)
	 * @parameter3 (expectedValue) (manual calculation of the correct/expected value of each test case)
	 */
	@Parameter(0)
	public int depositAmount;
	@Parameter(1)
	public int accountBalance;
	@Parameter(2)
	public boolean studentCheck;
	@Parameter(3)
	public double expectedValue;
	@Before
	public void calculate() {
		depositTestObj = new FeesCalculator();
	}

	@Parameters
    public static Collection<Object[]> ParameterizedTestFields() {
        return Arrays.asList(new Object[][] { 

            {55,540, true, Math.round(55.00 * 0.0)},//Test Case [0]
            {140, 3600, false, Math.round(140.00 * 0.0)},//Test Case [1]
            {950, 7500, false, Math.round(950.00 * 0.01)},//Test Case [2]
            {55, 13600, false, Math.round(95.00 * 0.005)},//Test Case [3]
            {-10, 540, false, Math.round(-10.00 * 0.0)},//Test Case [4]
            {5200, 540, false, Math.round(5200.00 * 0.005)},//Test Case [5]
	        {55, -240, false, Math.round(55.00 * 0.0)},//Test Case [6]
            {55, 45000, false, Math.round(55.00 * 0.005)},//Test Case [7]
        });
        
    }
    
	
	@Test
    public void testFeesCalculator() {
		actualValue =depositTestObj.calculateDepositInterest(depositAmount, accountBalance, studentCheck);
    	assertEquals(expectedValue, actualValue, delta);
    }}