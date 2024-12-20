package com.github.lipinskipawel.common.transaction;

import com.github.lipinskipawel.base.cash.Cash;
import com.github.lipinskipawel.base.cash.CurrencyPair;

import java.time.LocalDate;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public final class Transaction {
    private final Type type;
    private final LocalDate localDate;
    private final Cash price;
    private final int volume;
    private final CurrencyPair fxRate;

    private Transaction(Builder builder) {
        this.type = requireNonNull(builder.type);
        this.localDate = requireNonNull(builder.localDate);
        this.price = requireNonNull(builder.price);
        this.volume = builder.volume;
        this.fxRate = requireNonNull(builder.fxRate);
    }

    public Type type() {
        return type;
    }

    public LocalDate localDate() {
        return localDate;
    }

    public Cash amount() {
        return price;
    }

    public int volume() {
        return volume;
    }

    public CurrencyPair fxRate() {
        return fxRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return volume == that.volume
            && type == that.type
            && Objects.equals(localDate, that.localDate)
            && Objects.equals(price, that.price)
            && Objects.equals(fxRate, that.fxRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, localDate, price, volume, fxRate);
    }

    @Override
    public String toString() {
        return "Transaction{" +
            "type=" + type +
            ", localDate=" + localDate +
            ", price=" + price +
            ", volume=" + volume +
            ", fxRate=" + fxRate +
            '}';
    }

    public static class Builder {
        private Type type;
        private LocalDate localDate;
        private Cash price;
        private int volume;
        private CurrencyPair fxRate;

        private Builder() {
        }

        public static Builder transaction() {
            return new Builder();
        }

        public static Builder buyTransaction() {
            return new Builder().type(Type.BUY);
        }

        public Builder type(Type type) {
            this.type = type;
            return this;
        }

        public Builder localDate(LocalDate localDate) {
            this.localDate = localDate;
            return this;
        }

        public Builder price(Cash price) {
            this.price = price;
            return this;
        }

        public Builder volume(int volume) {
            this.volume = volume;
            return this;
        }

        public Builder fxRate(CurrencyPair fxRate) {
            this.fxRate = fxRate;
            return this;
        }

        public Transaction build() {
            return new Transaction(this);
        }
    }
}
