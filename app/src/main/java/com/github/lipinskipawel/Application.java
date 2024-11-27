package com.github.lipinskipawel;

import com.github.lipinskipawel.evaluation.AssetEvaluator;
import com.github.lipinskipawel.nbp.NbpClient;

import java.math.BigDecimal;
import java.nio.file.Path;

import static com.github.lipinskipawel.ArgumentParser.Flag.PRICE;
import static com.github.lipinskipawel.ArgumentParser.Flag.TRANSACTION_PATH;
import static com.github.lipinskipawel.ArgumentParser.Flag.USD_PLN;
import static com.github.lipinskipawel.Cash.cash;
import static com.github.lipinskipawel.Currency.PLN;
import static com.github.lipinskipawel.Currency.USD;
import static com.github.lipinskipawel.CurrencyPair.currencyPair;

public final class Application {

    private static final NbpClient nbpClient = new NbpClient();

    public static void main(String[] args) {
        final var parser = new ArgumentParser(args);
        final var transactionPath = parser.findValue(TRANSACTION_PATH)
            .map(Path::of)
            .orElseThrow();
        final var loadTransactions = new LoadTransactions(transactionPath);

        final var assetEvaluator = new AssetEvaluator(loadTransactions.loadTransactions());
        final var numberOfShares = assetEvaluator.numberOfShares();

        final var currentAssetPrice = parser.findValue(PRICE)
            .map(it -> cash(it, USD))
            .orElse(cash(100, USD));
        System.out.println(currentAssetPrice.multiply(numberOfShares));

        final var mid = nbpClient.currentUsdPln().body().rates().get(0).mid();
        final var inPln = parser.findValue(USD_PLN)
            .map(it -> currencyPair(USD, new BigDecimal(it), PLN))
            .map(it -> it.exchange(currentAssetPrice.multiply(numberOfShares)))
            .orElseGet(() -> currencyPair(USD, mid, PLN).exchange(currentAssetPrice.multiply(numberOfShares)));
        System.out.println(inPln);
    }
}
