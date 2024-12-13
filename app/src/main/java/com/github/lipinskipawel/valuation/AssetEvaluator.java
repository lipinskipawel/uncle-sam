package com.github.lipinskipawel.valuation;

import com.github.lipinskipawel.base.cash.Cash;
import com.github.lipinskipawel.common.transaction.Transaction;

import java.util.List;

import static com.github.lipinskipawel.common.transaction.Type.BUY;
import static java.util.Objects.requireNonNull;

public final class AssetEvaluator {
    private final List<Transaction> transactions;

    public AssetEvaluator(List<Transaction> transactions) {
        this.transactions = requireNonNull(transactions);
    }

    public InvestedCash investedCash() {
        return transactions
            .stream()
            .map(it -> {
                final var inUsd = cashMovement(it);
                final var inPln = it.fxRate().exchange(inUsd);
                return InvestedCash.investedCash(inUsd, inPln);
            })
            .reduce(InvestedCash.investedCash(), InvestedCash::addInvestedCash, InvestedCash::addInvestedCash);
    }

    private Cash cashMovement(Transaction transaction) {
        final var cash = transaction.amount().multiply(transaction.volume());
        return switch (transaction.type()) {
            case BUY -> cash;
            case SELL -> cash.negate();
        };
    }

    public int numberOfShares() {
        return transactions
            .stream()
            .reduce(0, this::countShares, Integer::sum);
    }

    private int countShares(int acc, Transaction txn) {
        if (txn.type() == BUY) {
            return acc + txn.volume();
        }
        return acc - txn.volume();
    }
}
