package com.github.lipinskipawel.rates;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

        final var rate = fxFileReader.usdPlnRate(date);

        assertThat(rate)
            .isPresent()
            .get()
            .isEqualTo(new BigDecimal("4.3"));
    }

    @Test
    void read_fx_rate_when_reading_through_the_file() {
        final var date = LocalDate.of(2024, 1, 4);
        final var file = createFileWithContent("""
            %s,4.2
            %s,4.0""".formatted(date, date.plusDays(1)));
        final var fxFileReader = new FxFileReader(file.toAbsolutePath());

        final var rate = fxFileReader.usdPlnRate(date.plusDays(1));

        assertThat(rate)
            .isPresent()
            .get()
            .isEqualTo(new BigDecimal("4.0"));
    }

    @Test
    void jump_reading_when_requested_rate_is_far_in_the_future_thursday() {
        final var date = LocalDate.of(2024, 1, 5);
        final var file = createFileWithContent(oneMonthOfRatesHardcoded());
        final var fxFileReader = new FxFileReader(file.toAbsolutePath());

        final var rate = fxFileReader.usdPlnRate(date.plusDays(27));

        assertThat(rate)
            .isPresent()
            .get()
            .isEqualTo(new BigDecimal("4.5"));
    }

    @Test
    void jump_reading_when_requested_rate_is_far_in_the_future_friday() {
        final var date = LocalDate.of(2024, 1, 5);
        final var file = createFileWithContent(oneMonthOfRatesHardcoded());
        final var fxFileReader = new FxFileReader(file.toAbsolutePath());

        final var rate = fxFileReader.usdPlnRate(date.plusDays(28));

        assertThat(rate)
            .isPresent()
            .get()
            .isEqualTo(new BigDecimal("4.2"));
    }

    @Test
    void jump_reading_when_requested_rate_is_far_in_the_future_saturday() {
        final var date = LocalDate.of(2024, 1, 5);
        final var file = createFileWithContent(oneMonthOfRatesHardcoded());
        final var fxFileReader = new FxFileReader(file.toAbsolutePath());

        final var rate = fxFileReader.usdPlnRate(date.plusDays(29));

        assertThat(rate)
            .isPresent()
            .get()
            .isEqualTo(new BigDecimal("4.2"));
    }

    @Test
    void jump_reading_when_requested_rate_is_far_in_the_future_sunday() {
        final var date = LocalDate.of(2024, 1, 5);
        final var file = createFileWithContent(oneMonthOfRatesHardcoded());
        final var fxFileReader = new FxFileReader(file.toAbsolutePath());

        final var rate = fxFileReader.usdPlnRate(date.plusDays(30));

        assertThat(rate)
            .isPresent()
            .get()
            .isEqualTo(new BigDecimal("4.2"));
    }

    @Test
    void jump_reading_when_requested_rate_is_far_in_the_future_monday() {
        final var date = LocalDate.of(2024, 1, 5);
        final var file = createFileWithContent(oneMonthOfRatesHardcoded());
        final var fxFileReader = new FxFileReader(file.toAbsolutePath());

        final var rate = fxFileReader.usdPlnRate(date.plusDays(31));

        assertThat(rate)
            .isPresent()
            .get()
            .isEqualTo(new BigDecimal("4.24"));
    }

    @Test
    void read_fx_rate_when_requested_date_is_saturday() {
        final var date = LocalDate.of(2024, 11, 29);
        final var file = createFileWithContent(date + ",4.3");
        final var fxFileReader = new FxFileReader(file.toAbsolutePath());

        final var rate = fxFileReader.usdPlnRate(date.plusDays(1));

        assertThat(rate)
            .isPresent()
            .get()
            .isEqualTo(new BigDecimal("4.3"));
    }

    @Test
    void read_fx_rate_when_requested_date_is_sunday() {
        final var date = LocalDate.of(2024, 11, 29);
        final var file = createFileWithContent(date + ",4.3");
        final var fxFileReader = new FxFileReader(file.toAbsolutePath());

        final var rate = fxFileReader.usdPlnRate(date.plusDays(2));

        assertThat(rate)
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
            """.formatted(date, date.plusDays(1)));
        final var fxFileReader = new FxFileReader(file.toAbsolutePath());

        final var rate = fxFileReader.usdPlnRate(date.plusDays(3));

        assertThat(rate).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 4, 10})
    void not_read_entire_fx_rate_file_when_requested_date_is_more_than_2_days_before_first_entry(int daysBefore) {
        final var date = LocalDate.of(2024, 1, 1);
        final var file = createFileWithContent(date + ",4.3");
        final var fxFileReader = new FxFileReader(file.toAbsolutePath());

        final var rate = fxFileReader.usdPlnRate(date.minusDays(daysBefore));

        assertThat(rate).isEmpty();
    }

    private String oneMonthOfRatesHardcoded() {
        return LocalDate.of(2024, 1, 5) + ",4.2\n" // friday,saturday,sunday
            + LocalDate.of(2024, 1, 8) + ",4.12\n"
            + LocalDate.of(2024, 1, 9) + ",4.12\n"
            + LocalDate.of(2024, 1, 10) + ",4.12\n"
            + LocalDate.of(2024, 1, 11) + ",4.12\n"
            + LocalDate.of(2024, 1, 12) + ",4.12\n" // friday,saturday,sunday
            + LocalDate.of(2024, 1, 15) + ",4.12\n"
            + LocalDate.of(2024, 1, 16) + ",4.12\n"
            + LocalDate.of(2024, 1, 17) + ",4.12\n"
            + LocalDate.of(2024, 1, 18) + ",4.12\n"
            + LocalDate.of(2024, 1, 19) + ",4.12\n" // friday,saturday,sunday
            + LocalDate.of(2024, 1, 22) + ",4.12\n"
            + LocalDate.of(2024, 1, 23) + ",4.12\n"
            + LocalDate.of(2024, 1, 24) + ",4.12\n"
            + LocalDate.of(2024, 1, 25) + ",4.12\n"
            + LocalDate.of(2024, 1, 26) + ",4.12\n" // friday,saturday,sunday
            + LocalDate.of(2024, 1, 29) + ",4.12\n"
            + LocalDate.of(2024, 1, 30) + ",4.12\n"
            + LocalDate.of(2024, 1, 31) + ",4.12\n"
            + LocalDate.of(2024, 2, 1) + ",4.5\n" // thursday
            + LocalDate.of(2024, 2, 2) + ",4.2\n" // friday,saturday,sunday
            + LocalDate.of(2024, 2, 5) + ",4.24\n"
            + LocalDate.of(2024, 2, 6) + ",4.12\n"
            + LocalDate.of(2024, 2, 7) + ",4.12\n"
            + LocalDate.of(2024, 2, 8) + ",4.12\n"
            + LocalDate.of(2024, 2, 9) + ",4.12\n" // friday,saturday,sunday
            + LocalDate.of(2024, 2, 12) + ",4.12\n"
            + LocalDate.of(2024, 2, 13) + ",4.12\n"
            + LocalDate.of(2024, 2, 14) + ",4.12\n"
            + LocalDate.of(2024, 2, 15) + ",4.12\n"
            + LocalDate.of(2024, 2, 16) + ",4.12\n" // friday,saturday,sunday
            + LocalDate.of(2024, 2, 19) + ",4.12\n";
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
