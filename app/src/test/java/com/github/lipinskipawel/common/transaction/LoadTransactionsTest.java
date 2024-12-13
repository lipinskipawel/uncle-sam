package com.github.lipinskipawel.common.transaction;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import static com.github.lipinskipawel.base.cash.Cash.cash;
import static com.github.lipinskipawel.base.cash.Currency.PLN;
import static com.github.lipinskipawel.base.cash.Currency.USD;
import static com.github.lipinskipawel.base.cash.CurrencyPair.currencyPair;
import static com.github.lipinskipawel.common.transaction.Transaction.Builder.transaction;
import static com.github.lipinskipawel.common.transaction.Type.BUY;
import static com.github.lipinskipawel.common.transaction.Type.SELL;
import static java.util.concurrent.ThreadLocalRandom.current;

class LoadTransactionsTest implements WithAssertions {

    @TempDir
    static Path transactionDirectory;

    @AfterAll
    static void afterAll() {
        transactionDirectory.toFile().delete();
    }

    @Test
    void read_all_transactions_from_file_when_one_transaction() {
        final var transactionPath = createFile(transactionDirectory, oneTransaction());
        final var loadTransactions = new LoadTransactions(transactionPath);

        final var transactions = loadTransactions.loadTransactions();

        assertThat(transactions).containsExactlyElementsOf(List.of(
            transaction()
                .type(BUY)
                .localDate(LocalDate.of(2024, 7, 3))
                .price(cash("104.4", USD))
                .volume(21)
                .fxRate(currencyPair(USD, new BigDecimal("4.0375"), PLN))
                .build()
        ));
        transactionPath.toFile().delete();
    }

    @Test
    void read_all_transactions_from_file_when_many_transaction_in_file() {
        final var transactionPath = createFile(transactionDirectory, manyTransaction());
        final var loadTransactions = new LoadTransactions(transactionPath);

        final var transactions = loadTransactions.loadTransactions();

        assertThat(transactions).containsExactlyElementsOf(List.of(
            transaction()
                .type(BUY)
                .localDate(LocalDate.of(2024, 7, 3))
                .price(cash("104.4", USD))
                .volume(21)
                .fxRate(currencyPair(USD, new BigDecimal("4.0375"), PLN))
                .build(),
            transaction()
                .type(BUY)
                .localDate(LocalDate.of(2024, 7, 11))
                .price(cash("110", USD))
                .volume(16)
                .fxRate(currencyPair(USD, new BigDecimal("3.9"), PLN))
                .build(),
            transaction()
                .type(SELL)
                .localDate(LocalDate.of(2024, 8, 3))
                .price(cash("90", USD))
                .volume(37)
                .fxRate(currencyPair(USD, new BigDecimal("5.12"), PLN))
                .build()
        ));
        transactionPath.toFile().delete();
    }

    private static String oneTransaction() {
        return """
            type,date,price,currency,volume,fxRate
            BUY,2024-07-03,104.4,USD,21,4.0375
            """;
    }

    private static String manyTransaction() {
        return """
            type,date,price,currency,volume,fxRate
            BUY,2024-07-03,104.4,USD,21,4.0375,
            BUY,2024-07-11,110,USD,16,3.9,
            SELL,2024-08-03,90,USD,37,5.12
            """;
    }

    private static Path createFile(Path file, String content) {
        try {
            final var createdFile = Files.createFile(file.resolve(randomString()));
            Files.writeString(createdFile, content);
            return createdFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String randomString() {
        return IntStream.range(0, 10)
            .map(it -> current().nextInt(97, 123))
            .mapToObj(String::valueOf)
            .reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append)
            .toString();
    }
}
