package com.github.lipinskipawel.base.cash;

import java.math.BigDecimal;
import java.util.Objects;

import static java.math.BigDecimal.ONE;
import static java.math.MathContext.DECIMAL64;
import static java.util.Objects.requireNonNull;

public final class CurrencyPair {
    private final Currency baseCurrency;
    private final BigDecimal quote;
    private final Currency quotedCurrency;

    private CurrencyPair(Currency baseCurrency, BigDecimal quote, Currency quoteCurrency) {
        this.baseCurrency = requireNonNull(baseCurrency);
        this.quote = requireNonNull(quote);
        this.quotedCurrency = requireNonNull(quoteCurrency);
    }

    public static CurrencyPair currencyPair(Currency baseCurrency, BigDecimal quote, Currency quoteCurrency) {
        return new CurrencyPair(baseCurrency, quote, quoteCurrency);
    }

    public Cash exchange(Cash cash) {
        if (cash.currency() == baseCurrency) {
            return Cash.cash(cash.amount().multiply(quote).toString(), quotedCurrency);
        }
        if (cash.currency() == quotedCurrency) {
            return Cash.cash(cash.amount().multiply(inverted().quote).toString(), baseCurrency);
        }
        throw new RuntimeException("Cash currency [%s] does not match currency pair [%s]".formatted(cash.currency(), this));
    }

    CurrencyPair inverted() {
        return new CurrencyPair(quotedCurrency, ONE.divide(quote, DECIMAL64), baseCurrency);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyPair that = (CurrencyPair) o;
        return baseCurrency == that.baseCurrency
            && Objects.equals(quote, that.quote)
            && quotedCurrency == that.quotedCurrency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseCurrency, quote, quotedCurrency);
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
