package com.github.lipinskipawel;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.github.lipinskipawel.Cash.cash;

class CurrencyPairTest implements WithAssertions {

    @Test
    void exchange_using_quoted_currency_correctly() {
        var usdPln = CurrencyPair.currencyPair(Currency.USD, BigDecimal.valueOf(4), Currency.PLN);

        var plnCash = usdPln.exchange(Cash.cash(100, Currency.USD));

        assertThat(plnCash).isEqualTo(Cash.cash(400, Currency.PLN));
    }

    @Test
    void exchange_using_base_currency_correctly() {
        var plnUsd = CurrencyPair.currencyPair(Currency.PLN, BigDecimal.valueOf(25, 2), Currency.USD);

        var plnCash = plnUsd.exchange(Cash.cash(100, Currency.USD));

        assertThat(plnCash).isEqualTo(Cash.cash(400, Currency.PLN));
    }

    @Test
    void throws_when_cash_currency_does_not_match_currency_of_currency_pair() {
        var plnUsd = CurrencyPair.currencyPair(Currency.USD, BigDecimal.valueOf(4), Currency.PLN);

        var runtimeException = catchRuntimeException(() -> plnUsd.exchange(Cash.cash(100, Currency.JPY)));

        assertThat(runtimeException)
            .hasMessage("Cash currency [JPY] does not match currency pair [CurrencyPair{baseCurrency=USD, quote=4, quotedCurrency=PLN}]");
    }
}
