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

import static org.junit.Assert.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;

//Helpful Link: https://www.tutorialspoint.com/mockito/mockito_create_mock.htm
@RunWith(MockitoJUnitRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankDepositTest {
	
	@Mock
	private FeesCalculator feesCalculator;
	
	@Mock
    private DBHandler dbHandler;
    
    //Using same user info as in database for simplicity
    private final String username = "ktsiounis";
    private final String cardNumber = "4000000000000000";
    private final char[] pin = {'5', '5', '5', '5'};
    private final AccountType[] accountTypes = { AccountType.Chequing };
    
    public static Stream<Arguments> testCases() {
        return Stream.of(
        		Arguments.of(101, 1001, true, 0.01, 1001+(101*1.01)),
                Arguments.of(101, 1000, true, 0.005, 1000+(101*1.005)),
                Arguments.of(50, 5001, true, 0.05, 5001+(50*1.005)),
                Arguments.of(50, 1000, true, 0.0, 1000+50),
                
                Arguments.of(501, 5001, false, 0.01, 5001+(501*1.01)),
                Arguments.of(501, 1000, false, 0.0, 1000+(501*1.005)),
                Arguments.of(100, 10001, false, 0.005, 10001+(100*1.005)),
                Arguments.of(100, 1000, false, 0.0, 1000+100)
        );
    }
    
    @BeforeAll
    public void setUp() {
        feesCalculator = mock(FeesCalculator.class);
        dbHandler = mock(DBHandler.class);
    }
    
    //Testing deposit with both stubs
    @ParameterizedTest
    @MethodSource("testCases")
    public void testA(double amount, double balance, boolean studentStatus, double expectedFees, double expectedBalance) throws CardNotFoundException, UserNotFoundException, UnsuccessfulBalanceUpdate {
    	/* Methods that deposit calls:
    	 * 	-dbHandler.getCardOwner()
    	 * 	-dbHandler.getBalance()
    	 * 	-dbHandler.isStudent()
    	 * 	-feesCalculator.calculateDepositInterest()
    	 * 	-dbHandler.setBalance()
    	 */

    	
    	//add the dbHandler behavior to get card owner
    	//method takes in (String cardNumber) and returns a string value 
        when(dbHandler.getCardOwner(cardNumber)).thenReturn(username);
        
        //add the dbHandler behavior to get account balance
        // method takes in (String user, AccountType type) and returns a double value
        when(dbHandler.getBalance(username, accountTypes[0])).thenReturn(balance);
        
        //add the dbHandler behavior to determine if user is a student
        //method takes in (String user) and returns a boolean value
        when(dbHandler.isStudent(username)).thenReturn(studentStatus);
        
        //add the feesCalculator behavior to determine the deposit fee
        //method takes in (double amount, double accountBalance, boolean student) and returns a double value
        when(feesCalculator.calculateDepositInterest(amount, balance, studentStatus)).thenReturn(expectedFees);
        
        //Don't want setBalance to do anything because it doesn't return anything
        doNothing().when(dbHandler).setBalance(username, accountTypes[0], expectedBalance);

        BankDeposit deposit = new BankDeposit(feesCalculator, dbHandler);
        
        TransactionResult actualResult = deposit.perform(new TransactionData(username, pin , TransactionType.Deposit, accountTypes, amount));
        
        assertEquals(true, actualResult.isSuccessful() );
        assertEquals("", actualResult.getReason() );
        assertEquals(expectedFees, actualResult.getFees() ,0);
        assertEquals(expectedBalance, actualResult.getAccountBalances()[0] ,0);
    }
    
    //Testing deposit with 1 stub
    @ParameterizedTest
    @MethodSource("testCases")
    public void testB(double amount, double balance, boolean studentStatus, double expectedFees, double expectedBalance) throws UserNotFoundException, UnsuccessfulBalanceUpdate, CardNotFoundException{
        dbHandler = mock(DBHandler.class);
        feesCalculator = new FeesCalculator();	//FeesCalculator stub is switched out for the real one
    	
    	expectedFees = feesCalculator.calculateDepositInterest(amount, balance, studentStatus); //test case value is replaced with actual value from the method
    	
    	//add the dbHandler behavior to get card owner
    	//method takes in (String cardNumber) and returns a string value 
        when(dbHandler.getCardOwner(cardNumber)).thenReturn(username);
        
        //add the dbHandler behavior to get account balance
        // method takes in (String user, AccountType type) and returns a double value
        when(dbHandler.getBalance(username, accountTypes[0])).thenReturn(balance);
        
        //add the dbHandler behavior to determine if user is a student
        //method takes in (String user) and returns a boolean value
        when(dbHandler.isStudent(username)).thenReturn(studentStatus);
        
        //Don't want setBalance to do anything because it doesn't return anything
        doNothing().when(dbHandler).setBalance(username, accountTypes[0], expectedBalance);
        
        BankDeposit deposit = new BankDeposit(feesCalculator, dbHandler);
        
        //getting values for test value verification
        TransactionResult actualResult = deposit.perform(new TransactionData(username, pin , TransactionType.Deposit, accountTypes, amount));
        
        assertEquals(true, actualResult.isSuccessful() );
        assertEquals("", actualResult.getReason() );
        assertEquals(expectedFees, actualResult.getFees() ,0);
        assertEquals(expectedBalance, actualResult.getAccountBalances()[0] ,0);
    }
    
    //Testing deposit with no stubs
    @ParameterizedTest
    @MethodSource("testCases")
    public void testC(double amount, double balance, boolean studentStatus, double expectedFees, double expectedBalance) throws UserNotFoundException {

        dbHandler = new DBHandler();	            //DBHandler stub is switched out for the real one
        //double balance is ignored here because we ditched the dbHandler stub
        feesCalculator = new FeesCalculator();		//FeesCalculator stub is switched out for the real one
    	BankDeposit deposit = new BankDeposit(feesCalculator, dbHandler);
    	
    	//getting values for test value verification
    	double initialBalance = dbHandler.getBalance(username, AccountType.Chequing);
    	expectedFees = feesCalculator.calculateDepositInterest(amount, initialBalance, studentStatus);		//test case value is replaced with actual value from the method
        
        TransactionResult actualResult = deposit.perform(new TransactionData(username, pin , TransactionType.Deposit, accountTypes, amount));
        
        assertEquals(true, actualResult.isSuccessful() );
        assertEquals("", actualResult.getReason() );
        assertEquals(expectedFees, actualResult.getFees() ,0);
        assertEquals(initialBalance+amount+expectedFees, actualResult.getAccountBalances()[0] ,0);
    }
    
}
