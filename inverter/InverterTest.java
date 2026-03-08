import org.junit.Test;
import static org.junit.Assert.*;

public class InverterTest {

    // -------------------------------------------------------
    // Requirement: When the argument is null, return ""
    // -------------------------------------------------------
    @Test
    public void testNullInputReturnsEmptyString() {
        assertEquals("", Inverter.invert(null));
    }

    // -------------------------------------------------------
    // Requirement: When the string is empty, return ""
    // -------------------------------------------------------
    @Test
    public void testEmptyStringReturnsEmptyString() {
        assertEquals("", Inverter.invert(""));
    }

    // -------------------------------------------------------
    // Requirement: When the string has exactly one character,
    //              return the same string
    // -------------------------------------------------------
    @Test
    public void testSingleCharacterReturnsSameString() {
        assertEquals("a", Inverter.invert("a"));
    }

    @Test
    public void testSingleCharacterIsNotNull() {
        assertNotNull(Inverter.invert("z"));
    }

    @Test
    public void testSingleDigitCharacterReturnsSameString() {
        assertEquals("1", Inverter.invert("1"));
    }

    @Test
    public void testSingleSpecialCharacterReturnsSameString() {
        assertEquals("!", Inverter.invert("!"));
    }

    // -------------------------------------------------------
    // Requirement: When string is longer than 1 character,
    //              return its inverted (reversed) version
    // -------------------------------------------------------
    @Test
    public void testFourCharStringIsInverted() {
        assertEquals("dcba", Inverter.invert("abcd"));
    }

    @Test
    public void testTwoCharStringIsInverted() {
        assertEquals("ba", Inverter.invert("ab"));
    }

    @Test
    public void testThreeCharStringIsInverted() {
        assertEquals("cba", Inverter.invert("abc"));
    }

    @Test
    public void testPalindromeStringIsReturnedAsIs() {
        // "racecar" reversed is still "racecar"
        assertEquals("racecar", Inverter.invert("racecar"));
    }

    @Test
    public void testStringWithSpacesIsInverted() {
        assertEquals("dlrow olleh", Inverter.invert("hello world"));
    }

    @Test
    public void testStringWithNumbersIsInverted() {
        assertEquals("54321", Inverter.invert("12345"));
    }

    @Test
    public void testStringWithMixedCaseIsInverted() {
        assertEquals("bA", Inverter.invert("Ab"));
    }

    @Test
    public void testStringWithSpecialCharactersIsInverted() {
        assertEquals("!dlrow ,olleH", Inverter.invert("Hello, world!"));
    }

    // -------------------------------------------------------
    // Sanity checks: return type and non-null for valid input
    // -------------------------------------------------------
    @Test
    public void testNullInputDoesNotReturnNull() {
        assertNotNull(Inverter.invert(null));
    }

    @Test
    public void testEmptyInputDoesNotReturnNull() {
        assertNotNull(Inverter.invert(""));
    }

    @Test
    public void testInvertedStringLengthEqualsOriginal() {
        String input = "abcde";
        assertEquals(input.length(), Inverter.invert(input).length());
    }

    // -------------------------------------------------------
    // Ensure invert is NOT just returning the same string for
    // multi-character non-palindromes
    // -------------------------------------------------------
    @Test
    public void testInvertedStringIsNotSameAsOriginalForNonPalindrome() {
        assertNotEquals("abcd", Inverter.invert("abcd"));
    }
}
