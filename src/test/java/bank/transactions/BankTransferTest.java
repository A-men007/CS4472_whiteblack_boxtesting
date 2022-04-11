package bank.transactions;

import bank.db.DBHandler;

import bank.exceptions.CardNotFoundException;
import bank.exceptions.UnsuccessfulBalanceUpdate;
import bank.exceptions.UserNotFoundException;
import bank.transactions.utils.AccountType;
import bank.transactions.utils.TransactionData;
import bank.transactions.utils.TransactionResult;
import bank.transactions.utils.TransactionType;
import bank.utils.FeesCalculator;

import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.stream.Stream;

@RunWith(MockitoJUnitRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankTransferTest {

	private FeesCalculator feesCalculator;
	private DBHandler dbHandler;

	// Using same user info as in database for simplicity
	private final String username = "kevin";
	private final String cardNumber = "4000000000000000";
	private final char[] pin = { '5', '5', '5', '5' };

	private final AccountType[] accounts = { AccountType.Chequing, AccountType.Savings };

	@BeforeEach
	public void setUp() throws CardNotFoundException, UserNotFoundException {
		 feesCalculator = mock(FeesCalculator.class);
		 dbHandler = mock(DBHandler.class);

		when(dbHandler.getCardOwner(cardNumber)).thenReturn(username);
		when(dbHandler.getPIN(username)).thenReturn(pin);
	}

	//test with two stubs
	@ParameterizedTest
	@MethodSource("transferData")
	public void FeesCalculatorTransferTest_two(double amount, double fromAccountBalance, double toAccountBalance,
			boolean student, double transferFee) throws UserNotFoundException, UnsuccessfulBalanceUpdate {
	
		when(dbHandler.isStudent(username)).thenReturn(true);
		when(feesCalculator.calculateTransferFee(amount, fromAccountBalance, toAccountBalance, student))
				.thenReturn(transferFee * amount);
		when(dbHandler.getBalance(username, accounts[0])).thenReturn(fromAccountBalance);

		when(dbHandler.getBalance(username, accounts[1])).thenReturn(toAccountBalance);
		
		dbHandler.setBalance(username, accounts[0], fromAccountBalance);
		dbHandler.setBalance(username, accounts[1], toAccountBalance);

		BankTransfer btObject = new BankTransfer(feesCalculator, dbHandler);
		TransactionData tdObject = new TransactionData(cardNumber, pin, TransactionType.Transfer, accounts, amount);
		TransactionResult result = btObject.perform(tdObject);

		double expectedFee = transferFee * amount;
		double actualFee = result.getFees();

		// test
		assertEquals(true, result.isSuccessful() );
		assertEquals("", result.getReason() );
		assertEquals(expectedFee, actualFee);

	}
//test with two stubs
	@ParameterizedTest
	@MethodSource("transferData")
	public void TransferBalanceTest_two(double amount, double fromAccountBalance, double toAccountBalance,
			boolean student, double transferFee) throws UserNotFoundException, UnsuccessfulBalanceUpdate {
		
		when(dbHandler.isStudent(username)).thenReturn(true);
		when(feesCalculator.calculateTransferFee(amount, fromAccountBalance, toAccountBalance, student))
				.thenReturn(transferFee * amount);
		when(dbHandler.getBalance(username, accounts[0])).thenReturn(fromAccountBalance);

		when(dbHandler.getBalance(username, accounts[1])).thenReturn(toAccountBalance);
		dbHandler.setBalance(username, accounts[0], fromAccountBalance);
		dbHandler.setBalance(username, accounts[1], toAccountBalance);
		

		BankTransfer btObject = new BankTransfer(feesCalculator, dbHandler);
		TransactionData tdObject = new TransactionData(cardNumber, pin, TransactionType.Transfer, accounts, amount);
		TransactionResult result = btObject.perform(tdObject);

		double senderExpectedFee = fromAccountBalance - amount- (transferFee * amount);
		double recieverExpectedFee = toAccountBalance + amount ;

		double senderActualFee = result.getAccountBalances()[0];
		double recieverActualFee = result.getAccountBalances()[1];

		// test
		assertEquals(true, result.isSuccessful() );
		assertEquals("", result.getReason() );
		assertEquals(senderExpectedFee, senderActualFee);
		assertEquals(recieverExpectedFee, recieverActualFee);
	}
//test with one stub
	@ParameterizedTest
	@MethodSource("transferData")
	public void FeesCalculatorTransferTest_one(double amount, double fromAccountBalance, double toAccountBalance,
			boolean student, double transferFee) throws UserNotFoundException, UnsuccessfulBalanceUpdate {
		feesCalculator = new FeesCalculator();//use real fees calculator obj
		//mock db
		when(dbHandler.isStudent(username)).thenReturn(true);
		
		when(dbHandler.getBalance(username, accounts[0])).thenReturn(fromAccountBalance);

		when(dbHandler.getBalance(username, accounts[1])).thenReturn(toAccountBalance);
		dbHandler.setBalance(username, accounts[0], fromAccountBalance);
		dbHandler.setBalance(username, accounts[1], toAccountBalance);
		
		BankTransfer btObject = new BankTransfer(feesCalculator, dbHandler);
		TransactionData tdObject = new TransactionData(cardNumber, pin, TransactionType.Transfer, accounts, amount);
		TransactionResult result = btObject.perform(tdObject);

		double expectedFee = transferFee * amount;
		double actualFee = result.getFees();

		// test
		assertEquals(true, result.isSuccessful() );
		assertEquals("", result.getReason() );
		assertEquals(expectedFee, actualFee);

	}
	//test with one stub

	@ParameterizedTest
	@MethodSource("transferData")
	public void TransferBalanceTest_one(double amount, double fromAccountBalance, double toAccountBalance,
			boolean student, double transferFee) throws UserNotFoundException, UnsuccessfulBalanceUpdate {
		feesCalculator = new FeesCalculator();//use real fees calculator obj
		//mock db
		when(dbHandler.isStudent(username)).thenReturn(true);
		
		when(dbHandler.getBalance(username, accounts[0])).thenReturn(fromAccountBalance);

		when(dbHandler.getBalance(username, accounts[1])).thenReturn(toAccountBalance);
		dbHandler.setBalance(username, accounts[0], fromAccountBalance);
		dbHandler.setBalance(username, accounts[1], toAccountBalance);
		
		BankTransfer btObject = new BankTransfer(feesCalculator, dbHandler);
		TransactionData tdObject = new TransactionData(cardNumber, pin, TransactionType.Transfer, accounts, amount);
		TransactionResult result = btObject.perform(tdObject);

		double senderExpectedFee = fromAccountBalance - amount- (transferFee * amount);
		double recieverExpectedFee = toAccountBalance + amount ;

		double senderActualFee = result.getAccountBalances()[0];
		double recieverActualFee = result.getAccountBalances()[1];

		// test
		assertEquals(true, result.isSuccessful() );
		assertEquals("", result.getReason() );
		assertEquals(recieverExpectedFee, recieverActualFee);
		assertEquals(senderExpectedFee, senderActualFee);
	}
	 @AfterAll
	    public void tearDown () throws UnsuccessfulBalanceUpdate {
	    	
	        dbHandler = null;
	        feesCalculator = null;
	    }
	//test data
	   private static Stream<Arguments> transferData() {
	        return Stream.of(
	                Arguments.of(50, 100, 100, true, 0.01),
	                Arguments.of(50, 100, 10001, true, 0.005)
	              
	                
	          
	                      
	        );
	    }

}