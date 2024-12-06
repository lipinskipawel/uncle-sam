package com.github.lipinskipawel.base.nbp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static java.util.Objects.requireNonNull;

public record NbpResponse(
    String table,
    String currency,
    String code,
    List<Rate> rates) {

    public NbpResponse {
        requireNonNull(table);
        requireNonNull(currency);
        requireNonNull(code);
        requireNonNull(rates);
    }

    private NbpResponse(Builder builder) {
        this(builder.table, builder.currency, builder.code, builder.rates);
    }

    public record Rate(
        String no,
        LocalDate effectiveDate,
        // todo, this is 4 decimal places. 4.077 must be understood as 4.0770
        BigDecimal mid
    ) {

        public Rate {
            requireNonNull(no);
            requireNonNull(effectiveDate);
            requireNonNull(mid);
        }
    }

    public static class Builder {
        private String table;
        private String currency;
        private String code;
        private List<Rate> rates;

        private Builder() {
        }

        public static Builder nbpResponse() {
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

        public NbpResponse build() {
            return new NbpResponse(this);
        }
    }
}
