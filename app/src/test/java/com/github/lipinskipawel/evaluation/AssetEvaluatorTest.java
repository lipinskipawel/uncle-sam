package com.github.lipinskipawel.evaluation;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.github.lipinskipawel.Cash.cash;
import static com.github.lipinskipawel.Currency.PLN;
import static com.github.lipinskipawel.Currency.USD;
import static com.github.lipinskipawel.CurrencyPair.currencyPair;
import static com.github.lipinskipawel.broker.Transaction.Builder.buyTransaction;

class AssetEvaluatorTest implements WithAssertions {

    @Test
    void evaluate_one_transaction_when_price_did_not_move() {
        final var usdPln = currencyPair(USD, new BigDecimal("4.0375"), PLN);
        final var transactions = List.of(buyTransaction()
            .price(cash("104.4", USD))
            .volume(21)
            .transactionDate(LocalDate.of(2024, 7, 3))
            .fxRate(currencyPair(PLN, new BigDecimal("4.0375"), USD))
            .build()
        );
        final var portfolioEvaluation = new AssetEvaluator(transactions);

        final var cash = portfolioEvaluation.evaluate(cash("104.4", USD));

        assertThat(cash).isEqualTo(cash("2192.4", USD));
        assertThat(usdPln.exchange(cash)).isEqualTo(cash("8851.81500", PLN));
    }

    @Test
    void evaluate_two_transactions_when_price_declined() {
        final var usdPln = currencyPair(PLN, new BigDecimal("4.02"), USD);
        final var transactions = List.of(
            buyTransaction()
                .price(cash("104.4", USD))
                .volume(21)
                .transactionDate(LocalDate.of(2024, 7, 3))
                .fxRate(currencyPair(PLN, new BigDecimal("4.0375"), USD))
                .build(),
            buyTransaction()
                .price(cash(104, USD))
                .volume(9)
                .transactionDate(LocalDate.of(2024, 7, 24))
                .fxRate(currencyPair(PLN, new BigDecimal("3.9355"), USD))
                .build()
        );
        final var portfolioEvaluation = new AssetEvaluator(transactions);

        final var cash = portfolioEvaluation.evaluate(cash(100, USD));
        assertThat(cash).isEqualTo(cash("3000", USD));
        assertThat(usdPln.exchange(cash)).isEqualTo(cash("746.2686567164178000", PLN));
    }
}
