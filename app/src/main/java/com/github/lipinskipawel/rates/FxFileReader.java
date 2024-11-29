package com.github.lipinskipawel.rates;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Optional;

import static java.nio.file.Files.newBufferedReader;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;

final class FxFileReader {

    private record FxRate(LocalDate date, String rate) {
    }

    private final Path fxPath;

    FxFileReader(Path fxPath) {
        this.fxPath = requireNonNull(fxPath);
    }

    Optional<BigDecimal> usdPlnRate(LocalDate date) {
        try (final var reader = newBufferedReader(fxPath)) {
            var fxRate = parseToFxRate(reader);
            if (fxRate.isEmpty()) {
                return empty();
            }

            while (!fxRate.get().date.equals(date)) {
                fxRate = parseToFxRate(reader);
                if (fxRate.isEmpty()) {
                    return empty();
                }
            }

            return Optional.of(new BigDecimal(fxRate.get().rate));
        } catch (IOException e) {
            return empty();
        }
    }

    private Optional<FxRate> parseToFxRate(BufferedReader bufferedReader) throws IOException {
        final var line = bufferedReader.readLine();
        if (line == null) {
            return empty();
        }
        final var values = line.split(",");
        if (values.length != 2) {
            throw new RuntimeException("File unreadable");
        }
        return Optional.of(new FxRate(LocalDate.parse(values[0]), values[1]));
    }
}
