package com.github.lipinskipawel;

import com.github.lipinskipawel.evaluation.AssetEvaluator;

import java.nio.file.Path;

import static com.github.lipinskipawel.ArgumentParser.Flag.TRANSACTION_PATH;
import static com.github.lipinskipawel.Currency.USD;

public final class Application {

    public static void main(String[] args) {
        final var parser = new ArgumentParser(args);
        final var transactionPath = parser.findValue(TRANSACTION_PATH)
            .map(Path::of)
            .orElseThrow();
        final var loadTransactions = new LoadTransactions(transactionPath);

        final var assetEvaluator = new AssetEvaluator(loadTransactions.loadTransactions());

        final var cash = assetEvaluator.evaluate(Cash.cash(100, USD));
        System.out.println(cash);
    }
}
