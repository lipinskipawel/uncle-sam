package com.github.lipinskipawel.evaluation;

import com.github.lipinskipawel.base.cash.Cash;
import com.github.lipinskipawel.rates.UsdPlnRate;

import static com.github.lipinskipawel.base.cash.Currency.PLN;
import static com.github.lipinskipawel.base.cash.Currency.USD;
import static com.github.lipinskipawel.base.cash.CurrencyPair.currencyPair;

public final class PrettyPrint {

    public void prettyPrint(Cash investedCash, UsdPlnRate usdPlnRate, Cash currentAssetPrice, int numberOfShares, Cash inPln) {
        System.out.println("Invested cash");
        System.out.println(investedCash);
        final var investedPln = currencyPair(USD, usdPlnRate.currentUsdPln(), PLN).exchange(investedCash);
        System.out.println(investedPln);
        System.out.println("");
        System.out.println("Valuation");
        final var valuationUsd = currentAssetPrice.multiply(numberOfShares);
        System.out.println(valuationUsd);
        System.out.println(inPln);
        System.out.println("");
        System.out.println("Profit / Loss");
        final var difference = valuationUsd.minus(investedCash);
        final var percentage = difference.multiply(100).divide(investedCash);
        System.out.println(difference + " -- " + percentage + "%");
        final var plnDifference = inPln.minus(investedPln);
        final var plnPercentage = plnDifference.multiply(100).divide(investedPln);
        System.out.println(plnDifference + " -- " + plnPercentage + "%");
    }
}
