package util;

/**
 * Utility class that supplies operations that will be used in various parts of program.
 */
public class Parser {

    public static Parser Singleton = new Parser();

    private Parser() {
    }

    /**
     * Checks whether input string is a number, with optional "-" and decimal digits.
     */
    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    /**
     * Checks whether input string is a well "identifier" -- an alphanumeric value.
     */
    public static boolean isIdentifier(String str) {
        return str.matches("[A-Z][1-9]+");
    }

    /**
     * Checks whether input string is an integer.
     */
    public static boolean isInteger(String str) {
        return str.matches("-?[0-9]+");
    }
}
