package org.example;

import java.math.BigDecimal;

import static java.util.Objects.requireNonNull;
import static org.example.Cash.cash;

public final class CurrencyPair {
    private final Currency baseCurrency;
    private final BigDecimal quote;
    private final Currency quotedCurrency;

    private CurrencyPair(Currency baseCurrency, BigDecimal quote, Currency quoteCurrency) {
        this.baseCurrency = requireNonNull(baseCurrency);
        this.quote = requireNonNull(quote);
        this.quotedCurrency = requireNonNull(quoteCurrency);
    }

    static CurrencyPair currencyPair(Currency baseCurrency, BigDecimal quote, Currency quoteCurrency) {
        return new CurrencyPair(baseCurrency, quote, quoteCurrency);
    }

    Cash exchange(Cash cash) {
        if (cash.currency() == baseCurrency) {
            return cash(cash.amount().multiply(quote).toString(), quotedCurrency);
        }
        if (cash.currency() == quotedCurrency) {
            return cash(cash.amount().multiply(inverted().quote).toString(), baseCurrency);
        }
        throw new RuntimeException("Cash currency [%s] does not match currency pair [%s]".formatted(cash.currency(), this));
    }

    CurrencyPair inverted() {
        return new CurrencyPair(quotedCurrency, BigDecimal.ONE.divide(quote), baseCurrency);
    }

    @Override
    public String toString() {
        return "CurrencyPair{" +
            "baseCurrency=" + baseCurrency +
            ", quote=" + quote +
            ", quotedCurrency=" + quotedCurrency +
            '}';
    }
}
