package com.github.lipinskipawel;

public enum Currency {
    USD,
    PLN,
    JPY;

    public static Currency currency(String currency) {
        return Currency.valueOf(currency);
    }
}
