package com.school.auth.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TemporaryPasswordGeneratorTest {

    private final TemporaryPasswordGenerator generator = new TemporaryPasswordGenerator();

    @Test
    void generatedPasswordContainsRequiredCharacterGroups() {
        String password = generator.generate();

        assertThat(password).hasSize(10);
        assertThat(password).containsPattern("[A-Z]");
        assertThat(password).containsPattern("[a-z]");
        assertThat(password).containsPattern("[0-9]");
        assertThat(password).containsPattern("[@#$%]");
    }
}
