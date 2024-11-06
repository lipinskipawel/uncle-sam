package com.github.lipinskipawel.broker;

import com.github.lipinskipawel.Cash;
import com.github.lipinskipawel.CurrencyPair;

import java.time.LocalDate;

import static com.github.lipinskipawel.broker.Type.BUY;
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

    public CurrencyPair gxRate() {
        return fxRate;
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
            return new Builder().type(BUY);
        }

        public Builder type(Type type) {
            this.type = type;
            return this;
        }

        public Builder transactionDate(LocalDate localDate) {
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
