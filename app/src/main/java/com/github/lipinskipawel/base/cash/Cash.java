package com.github.lipinskipawel.base.cash;

import java.math.BigDecimal;
import java.util.Objects;

import static java.math.RoundingMode.HALF_DOWN;

public final class Cash {

    private final BigDecimal amount;
    private final Currency currency;

    private Cash(BigDecimal amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public static Cash cash(int amount, Currency currency) {
        return new Cash(BigDecimal.valueOf(amount), currency);
    }

    public static Cash cash(String amount, Currency currency) {
        return new Cash(new BigDecimal(amount), currency);
    }

    public Cash add(Cash cash) {
        if (currency != cash.currency) {
            throw new RuntimeException("Currencies must be the same");
        }
        return new Cash(amount.add(cash.amount), currency);
    }

    public Cash minus(Cash cash) {
        if (currency != cash.currency) {
            throw new RuntimeException("Currencies must be the same");
        }
        return new Cash(amount.subtract(cash.amount), currency);
    }

    public Cash divide(Cash cash) {
        if (currency != cash.currency) {
            throw new RuntimeException("Currencies must be the same");
        }
        return new Cash(amount.divide(cash.amount, HALF_DOWN), currency);
    }

    public Cash divideExact(Cash cash) {
        if (currency != cash.currency) {
            throw new RuntimeException("Currencies must be the same");
        }
        return new Cash(amount.divide(cash.amount), currency);
    }

    public Cash multiply(int multiplier) {
        return new Cash(this.amount.multiply(BigDecimal.valueOf(multiplier)), this.currency);
    }

    public Cash negate() {
        return new Cash(this.amount.negate(), this.currency);
    }

    public BigDecimal amount() {
        return amount;
    }

    public Currency currency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cash cash = (Cash) o;
        return Objects.equals(amount, cash.amount)
            && currency == cash.currency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return amount + " " + currency;
    }
}
