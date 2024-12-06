package com.github.lipinskipawel.rates;

import com.github.lipinskipawel.base.nbp.NbpClient;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.createFile;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

public final class UsdPlnUpdater {
    private final Path usdPlnPath;
    private final FxFileWriter fxFileWriter;
    private final NbpClient nbpClient;
    private final LocalDate startDate;

    public UsdPlnUpdater(Path usdPlnPath, FxFileWriter fxFileWriter, NbpClient nbpClient, LocalDate startDate) {
        this.usdPlnPath = requireNonNull(usdPlnPath);
        this.fxFileWriter = requireNonNull(fxFileWriter);
        this.nbpClient = requireNonNull(nbpClient);
        this.startDate = requireNonNull(startDate);
    }

    public void run() {
        try {
            final var file = usdPlnPath.toFile();
            if (!file.exists()) {
                createDirectories(usdPlnPath.getParent());
                createFile(usdPlnPath);
            }

            final var nbpResponse = nbpClient.usdPln30days(startDate);

            final var ratesToSave = nbpResponse.rates()
                .stream()
                .map(rate -> rate.effectiveDate() + "," + rate.mid() + "\n")
                .collect(joining());

            fxFileWriter.write(ratesToSave);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
