package org.example;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import static org.example.Cash.cash;
import static org.example.Currency.USD;

final class CashTest implements WithAssertions {

    @Test
    void multiply_correctly() {
        var volume = 300;
        var pricePerShare = cash(100, USD);

        var assetValue = pricePerShare.multiply(volume);

        assertThat(assetValue).isEqualTo(cash(30_000, USD));
    }
}
