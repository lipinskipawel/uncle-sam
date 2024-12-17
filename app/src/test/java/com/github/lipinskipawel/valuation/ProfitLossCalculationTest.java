package com.github.lipinskipawel.valuation;

import com.github.lipinskipawel.base.cash.Cash;
import com.github.lipinskipawel.valuation.ProfitLossCalculation.ProfitLoss;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.github.lipinskipawel.base.cash.Cash.cash;
import static com.github.lipinskipawel.base.cash.Currency.USD;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ProfitLossCalculationTest implements WithAssertions {

    private final ProfitLossCalculation profitLoss = new ProfitLossCalculation();

    private static Stream<Arguments> profits() {
        return Stream.of(
            arguments(cash(10_000, USD), cash(20_000, USD), new ProfitLoss(cash(10_000, USD), 100)),
            arguments(cash(10_000, USD), cash(11_000, USD), new ProfitLoss(cash(1_000, USD), 10)),
            arguments(cash(10_000, USD), cash(10_500, USD), new ProfitLoss(cash(500, USD), 5)),
            arguments(cash(10_000, USD), cash(10_130, USD), new ProfitLoss(cash(130, USD), 1.3))
        );
    }

    @ParameterizedTest
    @MethodSource(value = "profits")
    void compute_profit_correctly(Cash initialInvestment, Cash investmentValuation, ProfitLoss expected) {
        final var result = profitLoss.profitLoss(initialInvestment, investmentValuation);

        assertThat(result).isEqualTo(expected);
    }

    private static Stream<Arguments> losses() {
        return Stream.of(
            arguments(cash(10_000, USD), cash(0, USD), new ProfitLoss(cash(-10_000, USD), -100)),
            arguments(cash(10_000, USD), cash(9_000, USD), new ProfitLoss(cash(-1_000, USD), -10)),
            arguments(cash(10_000, USD), cash(9_500, USD), new ProfitLoss(cash(-500, USD), -5)),
            arguments(cash(10_000, USD), cash(9_870, USD), new ProfitLoss(cash(-130, USD), -1.3))
        );
    }

    @ParameterizedTest
    @MethodSource(value = "losses")
    void compute_losses_correctly(Cash initialInvestment, Cash investmentValuation, ProfitLoss expected) {
        final var result = profitLoss.profitLoss(initialInvestment, investmentValuation);

        assertThat(result).isEqualTo(expected);
    }
}
