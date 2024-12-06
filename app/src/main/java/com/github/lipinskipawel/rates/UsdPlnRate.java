package com.github.lipinskipawel.rates;

import com.github.lipinskipawel.base.nbp.NbpClient;
import com.github.lipinskipawel.base.time.Time;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;

import static java.util.Objects.requireNonNull;

public final class UsdPlnRate {

    private final FxFileReader fxFileReader;
    private final FxFileWriter fxFileWriter;
    private final NbpClient nbpClient;

    public UsdPlnRate(NbpClient nbpClient, Path usdPlnPath, Time time) {
        this.fxFileReader = new FxFileReader(usdPlnPath);
        this.fxFileWriter = new FxFileWriter(usdPlnPath, time);
        this.nbpClient = requireNonNull(nbpClient);
    }

    public BigDecimal currentUsdPln() {
        return fxFileReader.usdPlnRate(LocalDate.now())
            .orElseGet(() -> {
                final var mid = nbpClient.currentUsdPln().rates().get(0).mid();
                final var appended = fxFileWriter.append(mid.doubleValue());
                if (!appended) {
                    System.err.println("Could not add usd/pln rate to file");
                }
                return mid;
            });
    }
}
