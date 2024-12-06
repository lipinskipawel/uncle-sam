package com.github.lipinskipawel.base.cash;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import static com.github.lipinskipawel.base.cash.Cash.cash;

final class CashTest implements WithAssertions {

    @Test
    void multiply_correctly() {
        var volume = 300;
        var pricePerShare = cash(100, Currency.USD);

        var assetValue = pricePerShare.multiply(volume);

        assertThat(assetValue).isEqualTo(cash(30_000, Currency.USD));
    }
}
