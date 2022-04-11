package bank.transactions.withdrawal;

import bank.db.DBHandler;
import bank.exceptions.CardNotFoundException;
import bank.exceptions.UnsuccessfulBalanceUpdate;
import bank.exceptions.UserNotFoundException;
import bank.transactions.BankWithdrawal;
import bank.transactions.utils.AccountType;
import bank.transactions.utils.TransactionData;
import bank.transactions.utils.TransactionResult;
import bank.transactions.utils.TransactionType;
import bank.utils.FeesCalculator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Calendar;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DBMockTest {

    private FeesCalculator feesCalculator;
    private DBHandler dbHandler;
    private BankWithdrawal withdrawal;
    private final String username = "kevin";
    private final String cardNumber = "4000000000000000";
    private final char[] pin = "5555".toCharArray();
    private final AccountType[] accountTypes = { AccountType.Chequing };

    @BeforeAll
    public void setup() {
        feesCalculator = new FeesCalculator();
        dbHandler = mock(DBHandler.class);
    }

    @ParameterizedTest(name = "[{index}] - {0} | {1} | {2} | {3} | {4} | {5} ")
    @MethodSource("withdrawalData")
    public void dbMocked(double amount, double balance, boolean isStudent, int dayOfWeek, double expectedFees, double expectedBalance) throws UserNotFoundException, UnsuccessfulBalanceUpdate, CardNotFoundException {

        // Creating Stubs
        when(dbHandler.getCardOwner(cardNumber)).thenReturn(username);
        when(dbHandler.getBalance(username, accountTypes[0])).thenReturn(balance);
        when(dbHandler.isStudent(username)).thenReturn(isStudent);
        doNothing().when(dbHandler).setBalance(username,accountTypes[0],balance);

        withdrawal = spy(new BankWithdrawal(feesCalculator, dbHandler));
        doReturn(dayOfWeek).when(withdrawal).getDayOfWeek();

        // Making Calls
        TransactionData transactionData = new TransactionData(cardNumber, pin, TransactionType.Withdrawal, accountTypes, amount);
        TransactionResult actualResult = withdrawal.perform(transactionData);
        TransactionResult expectedResult = new TransactionResult(true, "", expectedFees, new double[]{expectedBalance});

        // Compare Results
        assertTrue(actualResult.isSuccessful());
        assertEquals("", actualResult.getReason() );
        assertEquals(expectedFees, actualResult.getFees() ,0);
        assertEquals(expectedResult.getAccountBalances()[0], actualResult.getAccountBalances()[0]);

    }

    @AfterAll
    public void tearDown() {
        feesCalculator = null;
        withdrawal = null;
    }

    private static Stream<Arguments> withdrawalData() {
        return Stream.of(
                Arguments.of(50, 1000, true, Calendar.SATURDAY, 0, 950),               // Test 1
                Arguments.of(50, 1000, true, Calendar.WEDNESDAY, 0.05, 949.95),        // Test 2
                Arguments.of(50, 1000, false, Calendar.SUNDAY, 0.05, 949.95),          // Test 3
                Arguments.of(50, 999, false, Calendar.FRIDAY, 0.1, 948.90),            // Test 4
                Arguments.of(50, 1001, false, Calendar.FRIDAY, 0.05, 950.95),          // Test 5
                Arguments.of(50, 10001, false, Calendar.FRIDAY, 0, 9951)               // Test 6
        );
    }
}
