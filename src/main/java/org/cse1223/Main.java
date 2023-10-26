package org.cse1223;

import java.util.Scanner;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        boolean decide;
        int[] dice = new int[5];
        do {
            resetDice(dice);
            generateCurrentDice(dice);

            System.out.println("Your current dice: " + diceToString(dice));

            System.out.print("Select a die to re-roll (-1 to keep remaining dice): ");
            Scanner keyboard = new Scanner(System.in);
            promptForReroll(dice, keyboard);

            rollDice(dice);
            System.out.print("\n");
            System.out.println(getResult(dice));
            System.out.print("\n");

            decide = promptForPlayAgain(keyboard);
        } while (decide);

        System.out.println("Goodbye.");
    }

    // Given an array of integers as input, sets every element of the array to zero.
    private static void resetDice(int[] dice) {
        for (int i = 0; i < 5; i++) {
            dice[i] = 0;
        }
    }

    // Generate random dice
    private static void generateCurrentDice(int[] dice) {
        for (int i = 0; i < 5; i++) {
            int roll = (int) (Math.random() * 6) + 1;
            dice[i] = roll;
        }
    }

    // Given an array of integers as input, checks each element of the array.  If the value
    // of that element is zero, generate a number between 1 and 6 and replace the zero with
    // it. Otherwise, leave it as is and move to the next element.
    private static void rollDice(int[] dice) {
        System.out.println("Keeping remaining dice...");
        System.out.println("Re-rolling...");

        for (int i = 0; i < 5; i++) {
            if (dice[i] == 0) {
                int roll = (int) (Math.random() * 6) + 1;
                dice[i] = roll;
            }
        }

        System.out.println("Your final dice:" + diceToString(dice));
    }

    // Given an array of integers as input, create a formatted String that contains the
    // values in the array in the order they appear in the array.  For example, if the
    // array contains the values [0, 3, 6, 5, 2] then the String returned by this method
    // should be "0 3 6 5 2".
    private static String diceToString(int[] dice) {
        StringBuilder dicestr = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            dicestr.append(dice[i]).append(" ");
        }
        return dicestr.toString();
    }

    // Given an array of integers and a Scanner as input, prompt the user with a message
    // to indicate which dice should be re-rolled.  If the user enters a valid index (between
    // 0 and the total number of dice -1) then set the die at that index to zero.  If the
    // user enters a -1, end the loop and return to the calling program.  If the user enters
    // any other invalid index, provide an error message and ask again for a valid index.
    private static void promptForReroll(int[] dice, Scanner inScanner) {
        int input = inScanner.nextInt();
        while (input != -1) {
            if (input >= 0 && input < 5) {
                dice[input] = 0;
                System.out.println("Your current dice: " + diceToString(dice));
                System.out.print("Select a die to re-roll (-1 to keep remaining dice): ");
                input = inScanner.nextInt();
            } else {
                System.out.println("Error: Index must be between 0 and 4 (-1 to quit)! ");
                System.out.print("Select a die to re-roll (-1 to keep remaining dice): ");
                input = inScanner.nextInt();
            }
        }
    }

    // Given a Scanner as input, prompt the user to play again.  The only valid entries
    // from the user are 'Y' or 'N', in either upper or lower case.  If the user enters
    // a 'Y' the method should return a value of true to the calling program.  If the user
    // enters a 'N' the method should return a value of false.  If the user enters anything
    // other than Y or N (including an empty line), an error message should be displayed
    // and the user should be prompted again until a valid response is received.
    private static boolean promptForPlayAgain(Scanner inScanner) {
        System.out.print("Would you like to play again [Y/N]?:");
        inScanner.nextLine();
        String input = inScanner.nextLine();
        while (!input.equals("Y") && !input.equals("N")) {
            System.out.print("ERROR! Only 'Y' or 'N' allowed as input!\n");
            System.out.print("Would you like to play again [Y/N]?:");
            input = inScanner.nextLine();
        }
        return input.equals("Y");
    }

    // Given an array of integers, determines the result as a hand of Poker Dice.  The
    // result is determined as:
    //	* Five of a kind - all 5 integers in the array have the same value
    //	* Four of a kind - 4 of the five integers in the array have the same value
    //	* Full House - 3 integers in the array have the same value, and the remaining 2
    //					integers have the same value as well (Three of a kind and a pair)
    //	* Three of a kind - 3 integers in the array have the same value
    //	* Two pair - 2 integers in the array have the same value, and 2 other integers
    //					in the array have the same value
    //	* One pair - 2 integers in the array have the same value
    //	* Highest value - if none of the above hold true, the Highest Value in the array
    //						is used to determine the result
    //
    //	The method should evaluate the array and return back to the calling program a String
    //  containing the score from the array of dice.
    //
    //  EXTRA CREDIT: Include in your scoring a Straight, which is 5 numbers in sequence
    //		[1,2,3,4,5] or [2,3,4,5,6].
    private static String getResult(int[] dice) {
        int pos = 0;
        int greatestnum = dice[pos];
        int sum = dice[pos];
        while (pos < 4) {
            if (greatestnum < dice[pos + 1]) {
                greatestnum = dice[pos + 1];
            }
            sum += dice[pos + 1]; // Extra credit for Straight
            pos++;
        }

        int[] eachDiceNumberCountArray = getCounts(dice);
        int largestDiceNumberCount = Arrays.stream(eachDiceNumberCountArray).max().getAsInt();

        int numberTwoCount = 0;
        pos = 0;
        while (pos < 5) {
            if (eachDiceNumberCountArray[pos] == 2) {
                numberTwoCount++;
            }
            pos++;
        }

        String output;
        if (largestDiceNumberCount == 5) {
            output = "Five of a Kind";
        } else if (largestDiceNumberCount == 4) {
            output = "Four of a Kind";
        } else if (largestDiceNumberCount == 3) {
            if (numberTwoCount == 2) {
                output = "Full House ";
            } else {
                output = "Three of a Kind";
            }
        } else if (largestDiceNumberCount == 2) {
            if (numberTwoCount == 2) {
                output = "Two pair";
            } else {
                output = "One pair";
            }
        } else if ((sum / 5 == 4 && sum % 5 == 0) || (sum / 5 == 3 && sum % 5 == 0)) { // Extra credit for Straight
            output = "Straight";
        } else {
            output = "Highest value " + greatestnum;
        }

        return output;
    }

    // Given an array of integers as input, return back an array with the counts of the
    // individual values in it. You may assume that all elements in the array will have
    // a value between 1 and 6.
    // For example, if the array passed into the method were: [1, 2, 3, 3, 5]
    // Then the array of counts returned back by this method would be: [1, 1, 2, 0, 1, 0]
    // (Where index 0 holds the counts of the value 1, index 1 holds the counts of the value
    //  2, index 2 holds the counts of the value 3, etc.)
    // HINT: This method is very useful for determining the score of a particular hand
    //  of poker dice.  Use it as a helper method for the getResult() method above.
    private static int[] getCounts(int[] dice) {
        int check = 0;
        while (check < 5) {
            int count = 1;
            int initial = dice[check];
            int pos = check;
            while (pos < 4) {
                if (initial == dice[pos + 1]) {
                    count++;
                }
                pos++;
            }
            dice[check] = count;
            check++;
        }
        return dice;
    }
}
