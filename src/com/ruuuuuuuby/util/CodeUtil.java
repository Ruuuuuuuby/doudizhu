package com.ruuuuuuuby.util;

import java.util.ArrayList;
import java.util.Random;

public class CodeUtil {

    // Method to generate a verification code
    public static String getCode() {
        // 1. Create a list to store characters
        ArrayList<Character> list = new ArrayList<>(); // 52 characters in total, indices range from 0 to 51

        // 2. Add letters a-z and A-Z to the list
        for (int i = 0; i < 26; i++) {
            list.add((char) ('a' + i)); // Add lowercase letters a to z
            list.add((char) ('A' + i)); // Add uppercase letters A to Z
        }

        // 3. Print the list (optional, for debugging purposes)
        System.out.println(list);

        // 4. Generate 4 random letters
        String result = "";
        Random r = new Random();
        for (int i = 0; i < 4; i++) {
            // Get a random index from the list
            int randomIndex = r.nextInt(list.size());
            char c = list.get(randomIndex); // Get the character at the random index
            result = result + c; // Append the character to the result string
        }

        // 5. Generate a random digit from 0 to 9
        int number = r.nextInt(10);

        // 6. Append the random digit to the result string
        result = result + number;

        // 7. Convert the string to a character array
        char[] chars = result.toCharArray(); // Example: ['A', 'B', 'C', 'D', '5']

        // 8. Generate a random index within the character array
        int index = r.nextInt(chars.length);

        // 9. Swap the digit (at index 4) with the character at the random index
        char temp = chars[4]; // Save the digit in a temporary variable
        chars[4] = chars[index]; // Replace the digit with the character at the random index
        chars[index] = temp; // Replace the character at the random index with the digit

        // 10. Convert the character array back to a string
        String code = new String(chars);

        // 11. Return the final generated verification code
        return code;
    }
}