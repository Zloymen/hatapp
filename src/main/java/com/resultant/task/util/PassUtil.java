package com.resultant.task.util;

import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Random;

@Slf4j
public class PassUtil {

    private static final String LCaseChars = "abcdefgijkmnopqrstwxyz";
    private static final String UCaseChars = "ABCDEFGHJKLMNPQRSTWXYZ";
    private static final String NumericChars = "0123456789";
    private static final String SpecialChars = "*$-+?_&=!%{}/";
    private static final String LCASE = "lcase";
    private static final String UCASE = "ucase";
    private static final String NUM = "num";
    private static final String SPECIAL = "special";

    /**
     * украдено с https://stackoverflow.com/questions/4090021/need-a-secure-password-generator-recommendation
     * <div>Должен состоять минимум из 6 символов</div>
     * <div>Содержать минимум одну букву в верхнем регистре</div>
     * <div>Содержать минимум один спецсимвол (*, @, !, $, %, ^, &)</div>
     *
     * @param minLength
     * @param maxLength
     * @param minLCaseCount
     * @param minUCaseCount
     * @param minNumCount
     * @param minSpecialCount
     * @return String
     */

    public static String generateRandomString(int minLength, int maxLength, int minLCaseCount, int minUCaseCount, int minNumCount, int minSpecialCount) {


        HashMap<String, Integer> charGroupsUsed = new HashMap<>();

        charGroupsUsed.put(LCASE, minLCaseCount);
        charGroupsUsed.put(UCASE, minUCaseCount);
        charGroupsUsed.put(NUM, minNumCount);
        charGroupsUsed.put(SPECIAL, minSpecialCount);

        // Because we cannot use the default randomizer, which is based on the
        // current time (it will produce the same "random" number within a
        // second), we will use a random number generator to seed the
        // randomizer.

        // Use a 4-byte array to fill it with random bytes and convert it then
        // to an integer value.
        byte[] randomBytes = new byte[4];

        // Generate 4 random bytes.
        SecureRandom rng = new SecureRandom();
        rng.nextBytes(randomBytes);

        // Convert 4 bytes into a 32-bit integer value.
        int seed = (randomBytes[0] & 0x7f) << 24 |
                randomBytes[1] << 16 |
                randomBytes[2] << 8 |
                randomBytes[3];

        // Create a randomizer from the seed.
        Random random = new Random(seed);

        int wordLenght = minLength + random.nextInt( maxLength - minLength );

        charGroupsUsed.replace(LCASE, wordLenght - (minUCaseCount + minNumCount + minSpecialCount));

        // Allocate appropriate memory for the password.
        char[] randomString = new char[wordLenght];

        // Build the password.
        for (int i = 0; i < randomString.length; i++) {
            String selectableChars = "";

            // if we still have plenty of characters left to acheive our minimum requirements.
            // choose only from a group that we need to satisfy a minimum for.
            if(charGroupsUsed.values().stream().mapToInt(Integer::intValue).sum() == 0){
                selectableChars = LCaseChars + UCaseChars + NumericChars + SpecialChars;
            }else for (String charGroup : charGroupsUsed.keySet()) {
                if(charGroupsUsed.get(charGroup) != 0){
                    switch (charGroup) {
                        case LCASE:
                            selectableChars += LCaseChars;
                            break;
                        case UCASE:
                            selectableChars += UCaseChars;
                            break;
                        case NUM:
                            selectableChars += NumericChars;
                            break;
                        case SPECIAL:
                            selectableChars += SpecialChars;
                            break;
                        default:
                    }
                }
            }
            // Now that the string is built, get the next random character.
            char nextChar = selectableChars.charAt(random.nextInt(selectableChars.length() - 1));

            // Tac it onto our password.
            randomString[i] = nextChar;

            // Now figure out where it came from, and decrement the appropriate minimum value.
            if (LCaseChars.contains(String.valueOf(nextChar))) {

                charGroupsUsed.replace(LCASE, charGroupsUsed.get(LCASE) - 1);
                if (charGroupsUsed.get(LCASE) >= 0) {
                    wordLenght--;
                }
            } else if (UCaseChars.contains(String.valueOf(nextChar))) {
                charGroupsUsed.replace(UCASE, charGroupsUsed.get(UCASE) - 1);
                if (charGroupsUsed.get(UCASE) >= 0) {
                    wordLenght--;
                }
            } else if (NumericChars.contains(String.valueOf(nextChar))) {
                charGroupsUsed.replace(NUM, charGroupsUsed.get(NUM) - 1);
                if (charGroupsUsed.get(NUM) >= 0) {
                    wordLenght--;
                }
            } else if (SpecialChars.contains(String.valueOf(nextChar))) {
                charGroupsUsed.replace(SPECIAL, charGroupsUsed.get(SPECIAL) - 1);
                if (charGroupsUsed.get(SPECIAL) >= 0) {
                    wordLenght--;
                }
            }
        }
        return new String(randomString);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            log.info(generateRandomString(6,10, 1,1,1,0));
        }

    }
}
