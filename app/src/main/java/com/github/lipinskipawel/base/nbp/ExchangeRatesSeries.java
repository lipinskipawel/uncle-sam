package com.github.lipinskipawel.base.nbp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static java.util.Objects.requireNonNull;

public record ExchangeRatesSeries(
    String table,
    String currency,
    String code,
    List<Rate> rates) {

    public ExchangeRatesSeries {
        requireNonNull(table);
        requireNonNull(currency);
        requireNonNull(code);
        requireAtLeastOneItem(rates);
    }

    private ExchangeRatesSeries(Builder builder) {
        this(builder.table, builder.currency, builder.code, builder.rates);
    }

    private void requireAtLeastOneItem(List<Rate> rates) {
        requireNonNull(rates);
        if (rates.isEmpty()) {
            throw new IllegalArgumentException("Rates must have at least one item");
        }
    }

    public record Rate(
        String no,
        LocalDate effectiveDate,
        BigDecimal mid
    ) {

        public Rate {
            requireNonNull(no);
            requireNonNull(effectiveDate);
            requiredFourScale(mid);
        }

        private void requiredFourScale(BigDecimal bigDecimal) {
            requireNonNull(bigDecimal);
            if (bigDecimal.scale() != 4) {
                throw new IllegalArgumentException("Mid must have 4 decimal point");
            }
        }

        public static class Builder {
            private String no;
            private LocalDate effectiveDate;
            private BigDecimal mid;

            private Builder() {
            }

            public static Builder rate() {
                return new Builder();
            }

            public Builder no(String no) {
                this.no = no;
                return this;
            }

            public Builder effectiveDate(LocalDate effectiveDate) {
                this.effectiveDate = effectiveDate;
                return this;
            }

            public Builder mid(BigDecimal mid) {
                this.mid = mid;
                return this;
            }

            public Rate build() {
                return new Rate(no, effectiveDate, mid);
            }
        }
    }

    public static class Builder {
        private String table;
        private String currency;
        private String code;
        private List<Rate> rates;

        private Builder() {
        }

        public static Builder exchangeRatesSeries() {
            return new Builder();
        }

        public Builder table(String table) {
            this.table = table;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder rates(List<Rate> rates) {
            this.rates = rates;
            return this;
        }

        public ExchangeRatesSeries build() {
            return new ExchangeRatesSeries(this);
        }
    }
}
