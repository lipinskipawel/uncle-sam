package com.github.lipinskipawel;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import static com.github.lipinskipawel.Cash.cash;

final class CashTest implements WithAssertions {

    @Test
    void multiply_correctly() {
        var volume = 300;
        var pricePerShare = Cash.cash(100, Currency.USD);

        var assetValue = pricePerShare.multiply(volume);

        assertThat(assetValue).isEqualTo(Cash.cash(30_000, Currency.USD));
    }
}
