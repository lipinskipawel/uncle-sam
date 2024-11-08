package com.github.lipinskipawel;

import com.github.lipinskipawel.evaluation.AssetEvaluator;

import java.nio.file.Path;

import static com.github.lipinskipawel.ArgumentParser.Flag.PRICE;
import static com.github.lipinskipawel.ArgumentParser.Flag.TRANSACTION_PATH;
import static com.github.lipinskipawel.Cash.cash;
import static com.github.lipinskipawel.Currency.USD;

public final class Application {

    public static void main(String[] args) {
        final var parser = new ArgumentParser(args);
        final var transactionPath = parser.findValue(TRANSACTION_PATH)
            .map(Path::of)
            .orElseThrow();
        final var loadTransactions = new LoadTransactions(transactionPath);

        final var assetEvaluator = new AssetEvaluator(loadTransactions.loadTransactions());

        final var currentAssetPrice = parser.findValue(PRICE)
            .map(it -> cash(it, USD))
            .orElse(cash(100, USD));
        final var cash = assetEvaluator.evaluate(currentAssetPrice);
        System.out.println(cash);
    }
}
