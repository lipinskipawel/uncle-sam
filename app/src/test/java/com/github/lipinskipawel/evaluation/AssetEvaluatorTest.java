package com.github.lipinskipawel.evaluation;

import org.assertj.core.api.WithAssertions;
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
}
