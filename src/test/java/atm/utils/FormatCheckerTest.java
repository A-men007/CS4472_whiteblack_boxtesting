/*
* Group 20
* FormatCheckerTest - Atulpreet Johar
* The focus here is to check whether the format checker
* for the PIN as well as the Credit Card is working as per specifications.
*
* PIN - Weak Robust Equivalence Class Analysis
* Credit Card - Strong Normal Equivalence Class Analysis
*/

package atm.utils;

import atm.exceptions.InvalidCardNumberException;
import atm.exceptions.InvalidPinFormatException;
import atm.exceptions.TestCodeLogicException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runners.Parameterized.Parameter;

import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FormatCheckerTest {
    FormatChecker formatChecker;

    @Parameter(0)
    public char[] pin;

    @BeforeEach
    public void setUp() throws Exception {
        formatChecker = new FormatChecker();
    }

    @AfterEach
    public void tearDown() throws Exception {
    }

    @ParameterizedTest(name = "[{index}] - {0} | {2}")
    @MethodSource("pinData")
    public void checkPinFormatTest(String name, boolean throwsError, char[] pin) {
        if (throwsError) {
            assertThrows(InvalidPinFormatException.class, () -> {
                formatChecker.checkPinFormat(pin);
            });
        } else {
            assertDoesNotThrow(() -> {
                formatChecker.checkPinFormat(pin);
            });
        }
    }

    private static Stream<Arguments> pinData() {
        return Stream.of(
//              Argument(throwsError, pin)

//              Less than 4 digits long, robust weak BVT on one digit
                Arguments.of("Max+", true, new char[]{'3','3', ':'}),
                Arguments.of("Max", true, new char[]{'3','3', '9'}),
                Arguments.of("Max-", true, new char[]{'3','3', '8'}),
                Arguments.of("Nom", true, new char[]{'3','3', '3'}),
                Arguments.of("Min+", true, new char[]{'3','3', '1'}),
                Arguments.of("Min", true, new char[]{'3','3', '0'}),
                Arguments.of("Min-", true, new char[]{'3','3', '/'}),

//              Exactly 4 digits long, robust weak BVT on one digit
                Arguments.of("Max+",true, new char[]{':','3','3','3'}),
                Arguments.of("Max", false, new char[]{'9','3','3','3',}),
                Arguments.of("Max-",false, new char[]{'8','3','3','3',}),
                Arguments.of("Nom", false, new char[]{'3','3','3','3',}),
                Arguments.of("Min+",false, new char[]{'1','3','3','3',}),
                Arguments.of("Min", false, new char[]{'0','3','3','3',}),
                Arguments.of("Min-",true, new char[]{'/','3','3','3'}),

//              More than 4 digits long, robust weak BVT on one digit
                Arguments.of("Max+",true, new char[]{'3','3','3','3', ':'}),
                Arguments.of("Max", true, new char[]{'3','3','3','3', '9'}),
                Arguments.of("Max-",true, new char[]{'3','3','3','3', '8'}),
                Arguments.of("Nom", true, new char[]{'3','3','3','3', '3'}),
                Arguments.of("Min+",true, new char[]{'3','3','3','3', '1'}),
                Arguments.of("Min", true, new char[]{'3','3','3','3', '0'}),
                Arguments.of("Min-",true, new char[]{'3','3','3','3', '/'})
        );
    }

    @ParameterizedTest(name = "[{index}] - {0} | {2}-digits")
    @MethodSource("cardData")
    public void checkCardFormatTest(String cardType, String startingValue, int length, boolean throwsError) throws TestCodeLogicException {
        String IIN = "";
        if (startingValue != null) {
            IIN = startingValue;
        }

        String card = IIN + createRandomCardString(length - IIN.length());

        if (card.length() != length) {
            throw new TestCodeLogicException();
        }

        if (throwsError) {
            assertThrows(InvalidCardNumberException.class, () -> {
                formatChecker.checkCardFormat(card);
            });
        } else {
            assertDoesNotThrow(() -> {
                formatChecker.checkCardFormat(card);
            });
        }
    }

    private static Stream<Arguments> cardData() {
        return Stream.of(
            // AMEX
            Arguments.of("AMEX", "34", 15, false),
            Arguments.of("AMEX", "34", 15, false),

            // Diners Club - Cart Blance (CB), International (I), USA & Canada (UC)
            Arguments.of("DCCB", "300", 14, false),
            Arguments.of("DDCB", "303", 14, false),
            Arguments.of("DDCB", "305", 14, false),
            Arguments.of("DCI", "36", 14, false),
            Arguments.of("DCUC", "54", 16, false),

            // Discover
            Arguments.of("Discover", "6011", 16, false),
            Arguments.of("Discover", "622126", 16, false),
            Arguments.of("Discover", "622440", 16, false),
            Arguments.of("Discover", "622925", 16, false),
            Arguments.of("Discover", "644", 16, false),
            Arguments.of("Discover", "647", 16, false),
            Arguments.of("Discover", "649", 16, false),
            Arguments.of("Discover", "65", 16, false),
            Arguments.of("Discover", "6011", 19, false),
            Arguments.of("Discover", "622126", 19, false),
            Arguments.of("Discover", "622440", 19, false),
            Arguments.of("Discover", "622925", 19, false),
            Arguments.of("Discover", "644", 19, false),
            Arguments.of("Discover", "647", 19, false),
            Arguments.of("Discover", "649", 19, false),
            Arguments.of("Discover", "65", 19, false),

            // JCB
            Arguments.of("JCB", "3528", 16, false),
            Arguments.of("JCB", "3555", 16, false),
            Arguments.of("JCB", "3589", 16, false),
            Arguments.of("JCB", "3528", 19, false),
            Arguments.of("JCB", "3555", 19, false),
            Arguments.of("JCB", "3589", 19, false),

            //Mastercard
            Arguments.of("Mastercard", "51", 19, false),
            Arguments.of("Mastercard", "53", 19, false),
            Arguments.of("Mastercard", "5", 19, false),
            Arguments.of("Mastercard", "222100", 19, false),
            Arguments.of("Mastercard", "272099", 19, false),

            // Visa
            Arguments.of("Visa", "4", 13, false),
            Arguments.of("Visa", "4", 16, false),
            Arguments.of("Visa", "4", 19, false)
        );
    }

    private static String createRandomCardString(int length) {
        Random random = new Random();
        String res = "";

        String setOfCharacters = "0123456789";

        for (int i = 0; i < length; i++) {
            int randomInt = random.nextInt(setOfCharacters.length());
            res += setOfCharacters.charAt(randomInt);
        }

        return res;
    }
}