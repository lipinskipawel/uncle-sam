package com.github.lipinskipawel.rates;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;

import static java.nio.file.Files.createTempFile;
import static java.nio.file.Files.writeString;

class FxFileReaderTest implements WithAssertions {

    @Test
    void read_fx_rate_when_dates_are_matching() {
        final var date = LocalDate.of(2024, 1, 5);
        final var file = createFileWithContent(date + ",4.3");
        final var fxFileReader = new FxFileReader(file.toAbsolutePath());

        final var bigDecimal = fxFileReader.usdPlnRate(date);

        assertThat(bigDecimal)
            .isPresent()
            .get()
            .isEqualTo(new BigDecimal("4.3"));
    }

    @Test
    void read_fx_rate_when_dates_are_matching_going_backwards() {
        final var date = LocalDate.of(2024, 1, 5);
        final var file = createFileWithContent("""
            %s,4.2
            %s,4.0""".formatted(date, date.minusDays(1)));
        final var fxFileReader = new FxFileReader(file.toAbsolutePath());

        final var bigDecimal = fxFileReader.usdPlnRate(date.minusDays(1));

        assertThat(bigDecimal)
            .isPresent()
            .get()
            .isEqualTo(new BigDecimal("4.0"));
    }

    @Test
    void read_fx_rate_when_requested_date_is_saturday() {
        final var date = LocalDate.of(2024, 11, 29);
        final var file = createFileWithContent(date + ",4.3");
        final var fxFileReader = new FxFileReader(file.toAbsolutePath());

        final var bigDecimal = fxFileReader.usdPlnRate(date.plusDays(1));

        assertThat(bigDecimal)
            .isPresent()
            .get()
            .isEqualTo(new BigDecimal("4.3"));
    }

    @Test
    void read_fx_rate_when_requested_date_is_sunday() {
        final var date = LocalDate.of(2024, 11, 29);
        final var file = createFileWithContent(date + ",4.3");
        final var fxFileReader = new FxFileReader(file.toAbsolutePath());

        final var bigDecimal = fxFileReader.usdPlnRate(date.plusDays(2));

        assertThat(bigDecimal)
            .isPresent()
            .get()
            .isEqualTo(new BigDecimal("4.3"));
    }

    @Test
    void not_read_fx_rate_when_requested_date_is_before_last_recorded_rate() {
        final var date = LocalDate.of(2024, 1, 5);
        final var file = createFileWithContent("""
            %s,4.3
            %s,4.2
            """.formatted(date, date.minusDays(1)));
        final var fxFileReader = new FxFileReader(file.toAbsolutePath());

        final var bigDecimal = fxFileReader.usdPlnRate(date.minusDays(3));

        assertThat(bigDecimal).isEmpty();
    }

    private static Path createFileWithContent(String content) {
        try {
            final var usdPln = createTempFile("usdPln", ".tmp");
            writeString(usdPln, content);
            return usdPln;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
