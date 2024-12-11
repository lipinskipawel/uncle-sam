package com.github.lipinskipawel.evaluation;

import com.github.lipinskipawel.base.cash.Cash;
import com.github.lipinskipawel.broker.Transaction;

import java.util.List;

import static com.github.lipinskipawel.base.cash.Cash.cash;
import static com.github.lipinskipawel.base.cash.Currency.USD;
import static com.github.lipinskipawel.broker.Type.BUY;
import static java.util.Objects.requireNonNull;

public final class AssetEvaluator {
    private final List<Transaction> transactions;

    public AssetEvaluator(List<Transaction> transactions) {
        this.transactions = requireNonNull(transactions);
    }

    public Cash investedCash() {
        return transactions
            .stream()
            .map(it -> {
                final var amount = it.amount().multiply(it.volume()).amount();
                return it.type() == BUY ? cash(amount.toString(), USD) : cash(amount.negate().toString(), USD);
            })
            .reduce(cash(0, USD), Cash::add);
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
