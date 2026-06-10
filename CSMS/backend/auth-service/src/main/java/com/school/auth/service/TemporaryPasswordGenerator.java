package com.school.auth.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class TemporaryPasswordGenerator {

    private static final String UPPERCASE = "ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijkmnopqrstuvwxyz";
    private static final String DIGITS = "23456789";
    private static final String SYMBOLS = "@#$%";
    private static final String ALL_CHARACTERS = UPPERCASE + LOWERCASE + DIGITS + SYMBOLS;

    private final SecureRandom secureRandom = new SecureRandom();

    public String generate() {
        char[] password = new char[10];
        password[0] = randomChar(UPPERCASE);
        password[1] = randomChar(LOWERCASE);
        password[2] = randomChar(DIGITS);
        password[3] = randomChar(SYMBOLS);

        for (int i = 4; i < password.length; i++) {
            password[i] = randomChar(ALL_CHARACTERS);
        }

        shuffle(password);
        return new String(password);
    }

    private char randomChar(String source) {
        return source.charAt(secureRandom.nextInt(source.length()));
    }

    private void shuffle(char[] values) {
        for (int i = values.length - 1; i > 0; i--) {
            int j = secureRandom.nextInt(i + 1);
            char temp = values[i];
            values[i] = values[j];
            values[j] = temp;
        }
    }
}
