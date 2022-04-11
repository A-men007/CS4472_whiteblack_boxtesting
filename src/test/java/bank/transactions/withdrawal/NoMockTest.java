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
import org.junit.After;
import org.junit.Before;
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
public class NoMockTest {

    private FeesCalculator feesCalculator;
    private DBHandler dbHandler;
    private BankWithdrawal withdrawal;
    private String user;
    private double prevBalance;
    private final String cardNumber = "4000000000000000";
    private final char[] pin = {'5', '5', '5', '5'};
    private final AccountType[] accountTypes = { AccountType.Chequing };

    @BeforeAll
    public void setup() throws CardNotFoundException, UserNotFoundException {
        feesCalculator = new FeesCalculator();
        dbHandler = new DBHandler();
        user = dbHandler.getCardOwner(cardNumber);
        prevBalance = dbHandler.getBalance(user, accountTypes[0]);
    }

    @ParameterizedTest(name = "[{index}] - {0} | {1} | {2} | {3} | {4} ")
    @MethodSource("withdrawalData")
    public void noMocks(double amount, double balance, int dayOfWeek, double expectedFees, double expectedBalance) throws  UnsuccessfulBalanceUpdate, CardNotFoundException {
        // Creating Stubs
        dbHandler.setBalance(user, accountTypes[0], balance);

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
    public void tearDown () throws UnsuccessfulBalanceUpdate {
        dbHandler.setBalance(user, accountTypes[0], prevBalance);
        dbHandler.closeConnection();
        dbHandler = null;
        feesCalculator = null;
        withdrawal = null;
    }

    private static Stream<Arguments> withdrawalData() {
        return Stream.of(
                Arguments.of(50, 1000, Calendar.SATURDAY, 0, 950),               // Test 1
                Arguments.of(50, 1000, Calendar.WEDNESDAY, 0.05, 949.95)        // Test 2
        );
    }
}
