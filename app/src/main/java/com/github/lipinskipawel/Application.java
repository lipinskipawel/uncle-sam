package com.github.lipinskipawel;

import com.github.lipinskipawel.cli.ArgumentParser;
import com.github.lipinskipawel.cli.UsdPlnRateUpdate;
import com.github.lipinskipawel.cli.Valuation;
import com.github.lipinskipawel.evaluation.AssetEvaluator;
import com.github.lipinskipawel.nbp.NbpClient;
import com.github.lipinskipawel.rates.FxFileWriter;
import com.github.lipinskipawel.rates.UsdPlnRate;
import com.github.lipinskipawel.rates.UsdPlnUpdater;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;

import static com.github.lipinskipawel.Cash.cash;
import static com.github.lipinskipawel.Currency.PLN;
import static com.github.lipinskipawel.Currency.USD;
import static com.github.lipinskipawel.CurrencyPair.currencyPair;

public final class Application {

    public static void main(String[] args) {
        final var parser = new ArgumentParser(args);

        final var option = parser.option();
        switch (option) {
            case Valuation valuation -> {
                final var fileStorage = new FileStorage();
                final var usdPlnRate = new UsdPlnRate(new NbpClient(), fileStorage.usdPlnPath(), LocalDate::now);

                final var transactionPath = fileStorage.transactions()
                    .or(() -> valuation.transactionPath()
                        .map(Path::of))
                    .orElseThrow(() -> new RuntimeException("Path to transactions have not been specified"));
                final var loadTransactions = new LoadTransactions(transactionPath);

                final var assetEvaluator = new AssetEvaluator(loadTransactions.loadTransactions());
                final var numberOfShares = assetEvaluator.numberOfShares();

                final var currentAssetPrice = valuation.assetPrice()
                    .map(it -> cash(it, USD))
                    .orElse(cash(100, USD));
                System.out.println(currentAssetPrice.multiply(numberOfShares));

                final var inPln = valuation.usdPlnRate()
                    .map(it -> currencyPair(USD, new BigDecimal(it), PLN))
                    .map(it -> it.exchange(currentAssetPrice.multiply(numberOfShares)))
                    .orElseGet(() -> currencyPair(USD, usdPlnRate.currentUsdPln(), PLN).exchange(currentAssetPrice.multiply(numberOfShares)));
                System.out.println(inPln);
            }
            case UsdPlnRateUpdate updateRate -> {
                final var fileStorage = new FileStorage();

                final var transactionPath = fileStorage.transactions()
                    .or(() -> updateRate.transactionPath()
                        .map(Path::of))
                    .orElseThrow(() -> new RuntimeException("Path to transactions have not been specified"));
                final var loadTransactions = new LoadTransactions(transactionPath);

                final var firstTransaction = loadTransactions.loadTransactions().get(0);
                final var transactionDate = firstTransaction.localDate();

                final var usdPlnUpdater = new UsdPlnUpdater(
                    fileStorage.usdPlnPath(),
                    new FxFileWriter(fileStorage.usdPlnPath(), LocalDate::now),
                    new NbpClient(),
                    transactionDate
                );
                usdPlnUpdater.run();
            }
            default -> System.err.printf("Unknown option [%s]%n", option);
        }
    }
}
