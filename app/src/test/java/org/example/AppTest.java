package org.example;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

class AppTest implements WithAssertions {

    @Test
    void appHasAGreeting() {
        var classUnderTest = new App();
        assertThat(classUnderTest.getGreeting()).isEqualTo("Hello World!");
    }
}
