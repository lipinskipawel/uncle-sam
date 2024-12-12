package com.github.lipinskipawel.evaluation;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.github.lipinskipawel.base.cash.Cash.cash;
import static com.github.lipinskipawel.base.cash.Currency.PLN;
import static com.github.lipinskipawel.base.cash.Currency.USD;
import static com.github.lipinskipawel.base.cash.CurrencyPair.currencyPair;
import static com.github.lipinskipawel.broker.Transaction.Builder.buyTransaction;
import static com.github.lipinskipawel.broker.Transaction.Builder.transaction;
import static com.github.lipinskipawel.broker.Type.SELL;
import static com.github.lipinskipawel.evaluation.InvestedCash.investedCash;

class AssetEvaluatorTest implements WithAssertions {

    @Test
    void counts_number_of_shares_including_transaction_type() {
        final var transactions = List.of(
            buyTransaction()
                .price(cash("105", USD))
                .volume(21)
                .localDate(LocalDate.of(2024, 7, 3))
                .fxRate(currencyPair(PLN, new BigDecimal("4.00"), USD))
                .build(),
            buyTransaction()
                .price(cash(104, USD))
                .volume(9)
                .localDate(LocalDate.of(2024, 7, 24))
                .fxRate(currencyPair(PLN, new BigDecimal("3.9355"), USD))
                .build(),
            transaction()
                .type(SELL)
                .price(cash(104, USD))
                .volume(9)
                .localDate(LocalDate.of(2024, 7, 24))
                .fxRate(currencyPair(PLN, new BigDecimal("3.9355"), USD))
                .build()
        );
        final var portfolioEvaluation = new AssetEvaluator(transactions);

        final var numberOfShares = portfolioEvaluation.numberOfShares();

        assertThat(numberOfShares).isEqualTo(21);
    }

    @Nested
    @DisplayName("Correctly compute invested cash")
    class InvestedCash {

        @Test
        @DisplayName("when only BUY transactions are present")
        void buy_transactions() {
            final var transactions = List.of(
                buyTransaction()
                    .price(cash("105", USD))
                    .volume(21)
                    .localDate(LocalDate.of(2024, 7, 3))
                    .fxRate(currencyPair(PLN, new BigDecimal("4.00"), USD))
                    .build(),
                buyTransaction()
                    .price(cash(100, USD))
                    .volume(9)
                    .localDate(LocalDate.of(2024, 7, 24))
                    .fxRate(currencyPair(PLN, new BigDecimal("4"), USD))
                    .build()
            );
            final var evaluator = new AssetEvaluator(transactions);

            final var investedCash = evaluator.investedCash();

            assertThat(investedCash).isEqualTo(investedCash(cash(3105, USD), cash("776.25", PLN)));
        }

        @Test
        @DisplayName("when BUY and SELL transactions are present")
        void buy_and_sell_transactions() {
            final var transactions = List.of(
                buyTransaction()
                    .price(cash("105", USD))
                    .volume(21)
                    .localDate(LocalDate.of(2024, 7, 3))
                    .fxRate(currencyPair(PLN, new BigDecimal("4.00"), USD))
                    .build(),
                buyTransaction()
                    .price(cash(100, USD))
                    .volume(9)
                    .localDate(LocalDate.of(2024, 7, 24))
                    .fxRate(currencyPair(PLN, new BigDecimal("4"), USD))
                    .build(),
                transaction()
                    .type(SELL)
                    .price(cash(100, USD))
                    .volume(9)
                    .localDate(LocalDate.of(2024, 7, 24))
                    .fxRate(currencyPair(PLN, new BigDecimal("4"), USD))
                    .build()
            );
            final var evaluator = new AssetEvaluator(transactions);

            final var investedCash = evaluator.investedCash();

            assertThat(investedCash).isEqualTo(investedCash(cash(2205, USD), cash("551.25", PLN)));
        }
    }
}
