package com.github.lipinskipawel.rates;

import com.github.lipinskipawel.nbp.NbpClient;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.Optional;

import static java.lang.System.getenv;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

public final class UsdPlnRate {

    private final NbpClient nbpClient;

    public UsdPlnRate(NbpClient nbpClient) {
        this.nbpClient = requireNonNull(nbpClient);
    }

    public BigDecimal currentUsdPln() {
        return nbpClient.currentUsdPln().body().rates().get(0).mid();
    }

    private Optional<Path> usdPlnFile() {
        return ofNullable(getenv("HOME"))
            .map(Path::of)
            .map(path -> path.resolve(".config"))
            .map(path -> path.resolve("uncle-sam"))
            .map(path -> path.resolve("rates"))
            .map(path -> path.resolve("usdPln.txt"));
    }
}
