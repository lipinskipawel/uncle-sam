package com.github.lipinskipawel.rates;

import com.github.lipinskipawel.nbp.NbpClient;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Optional;

import static java.lang.System.getenv;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

public final class UsdPlnRate {

    private final FxFileReader fxFileReader;
    private final NbpClient nbpClient;

    public UsdPlnRate(NbpClient nbpClient) {
        final var usdPlnFile = ofNullable(getenv("HOME"))
            .or(() -> Optional.of("/"))
            .map(Path::of)
            .map(path -> path.resolve(".config"))
            .map(path -> path.resolve("uncle-sam"))
            .map(path -> path.resolve("rates"))
            .map(path -> path.resolve("usdPln.txt"))
            .orElseThrow();
        this.fxFileReader = new FxFileReader(usdPlnFile);
        this.nbpClient = requireNonNull(nbpClient);
    }

    public BigDecimal currentUsdPln() {
        return fxFileReader.usdPlnRate(LocalDate.now())
            .orElseGet(() -> nbpClient.currentUsdPln().rates().get(0).mid());
    }
}
