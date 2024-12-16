package com.github.lipinskipawel.valuation;

public final class PrettyPrint {

    private final ProfitLossCalculation profitLossCalculation = new ProfitLossCalculation();

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
        final var inUsd = profitLossCalculation.profitLoss(investedCash.inUsd(), valuation.inUsd());
        final var inPln = profitLossCalculation.profitLoss(investedCash.inPln(), valuation.inPln());
        System.out.println(inUsd.difference() + " -- " + inUsd.percentage() + "%");
        System.out.println(inPln.difference() + " -- " + inPln.percentage() + "%");
    }
}
