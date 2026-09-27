package wildliferescue;

import java.util.Scanner;

/**
 * Utility methods for reading and validating console input.
 * Keeps re-prompting the user until a valid value is supplied,
 * so the application never crashes or exits on bad input.
 */
public final class InputValidator {

    private InputValidator() {
        // utility class
    }

    /** Reads a non-blank line of text. */
    public static String readNonBlankString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (input != null && !input.trim().isEmpty()) {
                return input.trim();
            }
            System.out.println("Input cannot be blank. Please try again.");
        }
    }

    /** Reads a double that must be strictly greater than zero. */
    public static double readPositiveDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                double value = Double.parseDouble(input.trim());
                if (value > 0) {
                    return value;
                }
                System.out.println("Value must be greater than zero. Please try again.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /** Reads a double that may be zero but not negative (e.g. optional treatment costs). */
    public static double readNonNegativeDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                double value = Double.parseDouble(input.trim());
                if (value >= 0) {
                    return value;
                }
                System.out.println("Value cannot be negative. Please try again.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /** Reads an int that must be strictly greater than zero. */
    public static int readPositiveInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                int value = Integer.parseInt(input.trim());
                if (value > 0) {
                    return value;
                }
                System.out.println("Value must be greater than zero. Please try again.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    /** Reads a menu option, ensuring it is a whole number within [min, max]. */
    public static int readMenuOption(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                int value = Integer.parseInt(input.trim());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Please select a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid menu number.");
            }
        }
    }

    /** Reads a yes/no answer, returning true for yes. */
    public static boolean readYesNo(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) {
                return true;
            }
            if (input.equals("n") || input.equals("no")) {
                return false;
            }
            System.out.println("Please enter 'y' or 'n'.");
        }
    }
}
