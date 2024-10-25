package org.example;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.example.Cash.cash;
import static org.example.Currency.JPY;
import static org.example.Currency.PLN;
import static org.example.Currency.USD;
import static org.example.CurrencyPair.currencyPair;

class CurrencyPairTest implements WithAssertions {

    @Test
    void exchange_using_quoted_currency_correctly() {
        var usdPln = currencyPair(USD, BigDecimal.valueOf(4), PLN);

        var plnCash = usdPln.exchange(cash(100, USD));

        assertThat(plnCash).isEqualTo(cash(400, PLN));
    }

    @Test
    void exchange_using_base_currency_correctly() {
        var plnUsd = currencyPair(PLN, BigDecimal.valueOf(25, 2), USD);

        var plnCash = plnUsd.exchange(cash(100, USD));

        assertThat(plnCash).isEqualTo(cash(400, PLN));
    }

    @Test
    void throws_when_cash_currency_does_not_match_currency_of_currency_pair() {
        var plnUsd = currencyPair(USD, BigDecimal.valueOf(4), PLN);

        var runtimeException = catchRuntimeException(() -> plnUsd.exchange(cash(100, JPY)));

        assertThat(runtimeException)
            .hasMessage("Cash currency [JPY] does not match currency pair [CurrencyPair{baseCurrency=USD, quote=4, quotedCurrency=PLN}]");
    }
}
