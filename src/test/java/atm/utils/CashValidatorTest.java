/**
 * FeesCalculatorDepositTest 
 * Group 20 Members: Bohr, Amanpreet

 * The focus here is to check if the deposited and withdrawn cash amounts are valid.
 * Weak Robust Equivalence Class Analysis is used generate test
 * cases.

 */

package atm.utils;
import static org.junit.Assert.*;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


public class CashValidatorTest {
	
	/* SETUP */
	// Test cases for deposit
	public static Stream<Arguments> deposit(){
		return Stream.of(
				// format is the value and the expected output
				// false means invalid amount, true means valid amount and can be accepted by the atm
				Arguments.of(-10, false),
				Arguments.of(55, true),
				Arguments.of(56, false),
				Arguments.of(140, true),
				Arguments.of(950, true),
				Arguments.of(710, true),
				Arguments.of(4960, true),
				Arguments.of(5200, true)
		);
	}	
	
	// Test cases for withdrawal
	public static Stream<Arguments> withdrawal(){
		return Stream.of(
				// format is the value and the expected output
				// false means invalid amount, true means valid amount and can be accepted by the atm
				Arguments.of(-250, false),
				Arguments.of(350, true),
				Arguments.of(670, true),
				Arguments.of(3600, true),
				Arguments.of(5500, true),
				Arguments.of(5505, false),
				Arguments.of(14600, true),
				Arguments.of(45000, true)
		);
	}
	
	
	/* TESTING */
	@ParameterizedTest
	@MethodSource("deposit")
	public void depositTest(int amount, boolean expected) {
		assertEquals(expected, CashValidator.validateDeposit(amount));
	}
	
	@ParameterizedTest
	@MethodSource("withdrawal")
	public void withdrawlTest(int amount, boolean expected) {
		assertEquals(expected, CashValidator.validateWithdrawal(amount));
	}

}