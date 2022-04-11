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
public class FeesCalculatorDUPathTest{
	public double actualValue;
	FeesCalculator depositTestObj; //initialize the calculator we will be testing
	double delta = 0.000001; //delta variable used for margin of error in test case
	
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

            {150,1200, true, 150 * 0.01f},//Test Case 1
            {150, 950, true, 150.00 * 0.005f},//Test Case 2
            {50, 5500, true, 50.00 * 0.005f},//Test Case 3
            {50, 4900, true, 50.00 * 0.0f},//Test Case 4 
            {510, 5500, false, 510.00 * 0.01f},//Test Case 5
            {510, 4900, false, 510.00 * 0.005f},//Test Case 6
	        {480, 12000, false, 480.00 * 0.005f},//Test Case 7
            {480, 9500, false, 480.00 * 0.0f},//Test Case 8
        });
        
    }
    
	
	@Test
    public void testFeesCalculator() {
		actualValue =depositTestObj.calculateDepositInterest(depositAmount, accountBalance, studentCheck);
    	assertEquals(expectedValue, actualValue, delta);
    }}