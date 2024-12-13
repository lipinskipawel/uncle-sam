package com.github.lipinskipawel.valuation;

public final class PrettyPrint {

    public void prettyPrint(InvestedCash investedCash, InvestedCash valuation) {
        System.out.println("Invested cash");
        System.out.println(investedCash.inUsd());
        System.out.println(investedCash.inPln());
        System.out.println("");
        System.out.println("Valuation");
        System.out.println(valuation.inUsd());
        System.out.println(valuation.inPln());
        System.out.println("");
        System.out.println("Profit / Loss");
        final var difference = valuation.inUsd().minus(investedCash.inUsd());
        final var percentage = difference.multiply(100).divide(investedCash.inUsd());
        System.out.println(difference + " -- " + percentage + "%");
        final var plnDifference = valuation.inPln().minus(investedCash.inPln());
        final var plnPercentage = plnDifference.multiply(100).divide(investedCash.inPln());
        System.out.println(plnDifference + " -- " + plnPercentage + "%");
    }
}
