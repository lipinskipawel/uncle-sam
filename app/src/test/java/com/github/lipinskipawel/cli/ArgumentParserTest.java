package com.github.lipinskipawel.cli;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.lipinskipawel.cli.ArgumentParser.Flag.TRANSACTION_PATH;

class ArgumentParserTest implements WithAssertions {

    @Test
    void find_transaction_in_args_when_provided() {
        final var arguments = List.of("transactions=/home/me/txn.csv", "some=error").toArray(String[]::new);
        final var parser = new ArgumentParser(arguments);

        final var path = parser.findValue(TRANSACTION_PATH);

        assertThat(path)
            .isPresent()
            .get()
            .isEqualTo("/home/me/txn.csv");
    }

    @Test
    void does_not_find_transaction_in_args_when_missing() {
        final var arguments = List.of("t=/home/me/txn.csv", "some=error").toArray(String[]::new);
        final var parser = new ArgumentParser(arguments);

        final var path = parser.findValue(TRANSACTION_PATH);

        assertThat(path).isEmpty();
    }
}
