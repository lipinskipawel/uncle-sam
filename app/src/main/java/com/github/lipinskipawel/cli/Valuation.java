package com.github.lipinskipawel.cli;

import java.util.Optional;

public record Valuation(
    Optional<String> transactionPath,
    Optional<String> assetPrice,
    Optional<String> usdPlnRate
) implements Option {
}
