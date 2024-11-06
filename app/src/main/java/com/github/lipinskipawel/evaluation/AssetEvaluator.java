package com.github.lipinskipawel.evaluation;

import com.github.lipinskipawel.Cash;
import com.github.lipinskipawel.broker.Transaction;

import java.util.List;

import static java.util.Objects.requireNonNull;

public final class AssetEvaluator {
    private final List<Transaction> transactions;

    public AssetEvaluator(List<Transaction> transactions) {
        this.transactions = requireNonNull(transactions);
    }

    public Cash evaluate(Cash pricePerShare) {
        final var numberOfShares = transactions.stream()
            .mapToInt(Transaction::volume)
            .sum();
        return pricePerShare.multiply(numberOfShares);
    }
}
