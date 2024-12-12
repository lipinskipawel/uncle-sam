package com.github.lipinskipawel.evaluation;

import com.github.lipinskipawel.base.cash.Cash;

import static com.github.lipinskipawel.base.cash.Cash.cash;
import static com.github.lipinskipawel.base.cash.Currency.PLN;
import static com.github.lipinskipawel.base.cash.Currency.USD;
import static java.util.Objects.requireNonNull;

public record InvestedCash(
    Cash inUsd,
    Cash inPln
) {

    public InvestedCash {
        requireNonNull(inUsd);
        requireNonNull(inPln);
    }

    public static InvestedCash investedCash() {
        return new InvestedCash(cash(0, USD), cash(0, PLN));
    }

    public static InvestedCash investedCash(Cash inUsd, Cash inPln) {
        return new InvestedCash(inUsd, inPln);
    }

    public InvestedCash addInvestedCash(InvestedCash investedCash) {
        return investedCash(inUsd.add(investedCash.inUsd), inPln.add(investedCash.inPln));
    }
}
