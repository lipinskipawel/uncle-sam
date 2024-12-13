package com.github.lipinskipawel;

import com.github.lipinskipawel.base.nbp.NbpClient;
import com.github.lipinskipawel.cli.ArgumentParser;
import com.github.lipinskipawel.cli.UsdPlnRateUpdate;
import com.github.lipinskipawel.cli.Valuation;
import com.github.lipinskipawel.common.files.FileStorage;
import com.github.lipinskipawel.rate.UsdPlnUpdater;
import com.github.lipinskipawel.common.transaction.LoadTransactions;
import com.github.lipinskipawel.valuation.AssetEvaluator;
import com.github.lipinskipawel.valuation.PrettyPrint;
import com.github.lipinskipawel.common.files.FxFileWriter;
import com.github.lipinskipawel.common.files.UsdPlnRate;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;

import static com.github.lipinskipawel.base.cash.Cash.cash;
import static com.github.lipinskipawel.base.cash.Currency.PLN;
import static com.github.lipinskipawel.base.cash.Currency.USD;
import static com.github.lipinskipawel.base.cash.CurrencyPair.currencyPair;
import static com.github.lipinskipawel.valuation.InvestedCash.investedCash;
import static java.util.Optional.of;

public final class Application {

    public static void main(String[] args) {
        final var parser = new ArgumentParser(args);

        final var option = parser.option();
        switch (option) {
            case Valuation valuation -> {
                final var fileStorage = new FileStorage();
                final var usdPlnRate = new UsdPlnRate(new NbpClient(), fileStorage.usdPln(), LocalDate::now);

                final var transactionPath = valuation.transactionPath()
                    .map(Path::of)
                    .or(() -> of(fileStorage.transactions()))
                    .orElseThrow(() -> new RuntimeException("Specify path to transactions or put them under $HOME/.config/uncle-sam/exchange"));
                final var loadTransactions = new LoadTransactions(transactionPath);

                final var assetEvaluator = new AssetEvaluator(loadTransactions.loadTransactions());
                final var investedCash = assetEvaluator.investedCash();
                final var numberOfShares = assetEvaluator.numberOfShares();

                final var currentAssetPrice = valuation.assetPrice()
                    .map(it -> cash(it, USD))
                    .orElse(cash(100, USD));

                final var inUsd = currentAssetPrice.multiply(numberOfShares);
                final var inPln = valuation.usdPlnRate()
                    .map(it -> currencyPair(USD, new BigDecimal(it), PLN))
                    .map(it -> it.exchange(currentAssetPrice.multiply(numberOfShares)))
                    .orElseGet(() -> currencyPair(USD, usdPlnRate.currentUsdPln(), PLN).exchange(inUsd));

                final var prettyPrint = new PrettyPrint();
                prettyPrint.prettyPrint(investedCash, investedCash(inUsd, inPln));
            }
            case UsdPlnRateUpdate updateRate -> {
                final var fileStorage = new FileStorage();

                final var transactionPath = updateRate.transactionPath()
                    .map(Path::of)
                    .or(() -> of(fileStorage.transactions()))
                    .orElseThrow(() -> new RuntimeException("Specify path to transactions or put them under $HOME/.config/uncle-sam/exchange"));
                final var loadTransactions = new LoadTransactions(transactionPath);

                final var firstTransaction = loadTransactions.loadTransactions().get(0);
                final var transactionDate = firstTransaction.localDate();

                final var usdPlnUpdater = new UsdPlnUpdater(
                    fileStorage.usdPln(),
                    new FxFileWriter(fileStorage.usdPln(), LocalDate::now),
                    new NbpClient(),
                    transactionDate
                );
                usdPlnUpdater.run();
            }
            default -> System.err.printf("Unknown option [%s]%n", option);
        }
    }
}
