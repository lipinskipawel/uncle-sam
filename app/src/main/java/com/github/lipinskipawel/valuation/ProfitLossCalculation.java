package com.github.lipinskipawel.valuation;

import com.github.lipinskipawel.base.cash.Cash;

import static java.util.Objects.requireNonNull;

final class ProfitLossCalculation {

    record ProfitLoss(Cash difference, double percentage) {

        ProfitLoss {
            requireNonNull(difference);
        }
    }

    ProfitLoss profitLoss(Cash baseline, Cash cash) {
        final var difference = cash.minus(baseline);
        final var percentage = difference.multiply(100).divideExact(baseline).amount().doubleValue();

        return new ProfitLoss(difference, percentage);
    }
}
