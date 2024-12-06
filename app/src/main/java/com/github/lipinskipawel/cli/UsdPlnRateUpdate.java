package com.github.lipinskipawel.cli;

import java.util.Optional;

public record UsdPlnRateUpdate(
    Optional<String> transactionPath
) implements Option {
}
