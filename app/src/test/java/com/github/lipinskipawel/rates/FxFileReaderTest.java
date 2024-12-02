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
    void jump_reading_when_requested_rate_is_far_back_in_time_monday() {
        final var date = LocalDate.of(2024, 1, 5);
        final var file = createFileWithContent(oneMonthOfRatesHardcoded());
        final var fxFileReader = new FxFileReader(file.toAbsolutePath());

        final var bigDecimal = fxFileReader.usdPlnRate(date.minusDays(25));

        assertThat(bigDecimal)
            .isPresent()
            .get()
            .isEqualTo(new BigDecimal("4.4"));
    }

    @Test
    void jump_reading_when_requested_rate_is_far_back_in_time_sunday() {
        final var date = LocalDate.of(2024, 1, 5);
        final var file = createFileWithContent(oneMonthOfRatesHardcoded());
        final var fxFileReader = new FxFileReader(file.toAbsolutePath());

        final var bigDecimal = fxFileReader.usdPlnRate(date.minusDays(26));

        assertThat(bigDecimal)
            .isPresent()
            .get()
            .isEqualTo(new BigDecimal("4.5"));
    }

    @Test
    void jump_reading_when_requested_rate_is_far_back_in_time_saturday() {
        final var date = LocalDate.of(2024, 1, 5);
        final var file = createFileWithContent(oneMonthOfRatesHardcoded());
        final var fxFileReader = new FxFileReader(file.toAbsolutePath());

        final var bigDecimal = fxFileReader.usdPlnRate(date.minusDays(27));

        assertThat(bigDecimal)
            .isPresent()
            .get()
            .isEqualTo(new BigDecimal("4.5"));
    }

    @Test
    void jump_reading_when_requested_rate_is_far_back_in_time_friday() {
        final var date = LocalDate.of(2024, 1, 5);
        final var file = createFileWithContent(oneMonthOfRatesHardcoded());
        final var fxFileReader = new FxFileReader(file.toAbsolutePath());

        final var bigDecimal = fxFileReader.usdPlnRate(date.minusDays(28));

        assertThat(bigDecimal)
            .isPresent()
            .get()
            .isEqualTo(new BigDecimal("4.5"));
    }

    @Test
    void jump_reading_when_requested_rate_is_far_back_in_time_thursday() {
        final var date = LocalDate.of(2024, 1, 5);
        final var file = createFileWithContent(oneMonthOfRatesHardcoded());
        final var fxFileReader = new FxFileReader(file.toAbsolutePath());

        final var bigDecimal = fxFileReader.usdPlnRate(date.minusDays(29));

        assertThat(bigDecimal)
            .isPresent()
            .get()
            .isEqualTo(new BigDecimal("4.1"));
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

    @ParameterizedTest
    @ValueSource(ints = {3, 4, 10})
    void not_read_entire_fx_rate_file_when_requested_date_is_more_than_2_days_in_the_future_compared_to_last_entry(int daysInTheFuture) {
        final var date = LocalDate.of(2024, 1, 1);
        final var file = createFileWithContent(date + ",4.3");
        final var fxFileReader = new FxFileReader(file.toAbsolutePath());

        final var bigDecimal = fxFileReader.usdPlnRate(date.plusDays(daysInTheFuture));

        assertThat(bigDecimal).isEmpty();
    }

    private String oneMonthOfRatesHardcoded() {
        return LocalDate.of(2024, 1, 5) + ",4.2\n" // friday,saturday,sunday
            + LocalDate.of(2024, 1, 4) + ",4.12\n"
            + LocalDate.of(2024, 1, 3) + ",4.12\n"
            + LocalDate.of(2024, 1, 2) + ",4.12\n"
            + LocalDate.of(2023, 12, 29) + ",4.12\n" // monday
            + LocalDate.of(2023, 12, 28) + ",4.12\n" // friday,saturday,sunday
            + LocalDate.of(2023, 12, 27) + ",4.12\n"
            + LocalDate.of(2023, 12, 26) + ",4.12\n"
            + LocalDate.of(2023, 12, 25) + ",4.12\n"
            + LocalDate.of(2023, 12, 22) + ",4.12\n" // monday
            + LocalDate.of(2023, 12, 21) + ",4.12\n" // friday,saturday,sunday
            + LocalDate.of(2023, 12, 20) + ",4.12\n"
            + LocalDate.of(2023, 12, 19) + ",4.12\n"
            + LocalDate.of(2023, 12, 18) + ",4.12\n"
            + LocalDate.of(2023, 12, 15) + ",4.12\n" // monday
            + LocalDate.of(2023, 12, 14) + ",4.12\n" // friday,saturday,sunday
            + LocalDate.of(2023, 12, 13) + ",4.13\n"
            + LocalDate.of(2023, 12, 12) + ",4.12\n"
            + LocalDate.of(2023, 12, 11) + ",4.4\n" // monday
            + LocalDate.of(2023, 12, 8) + ",4.5\n" // friday,saturday,sunday
            + LocalDate.of(2023, 12, 7) + ",4.1\n" // thursday
            + LocalDate.of(2023, 12, 6) + ",4.12\n"
            + LocalDate.of(2023, 12, 5) + ",4.12\n"
            + LocalDate.of(2023, 12, 4) + ",4.12\n" // monday
            + LocalDate.of(2023, 12, 1) + ",4.12\n" // friday,saturday,sunday
            + LocalDate.of(2023, 11, 30) + ",4.12\n"
            + LocalDate.of(2023, 11, 29) + ",4.12\n"
            + LocalDate.of(2023, 11, 28) + ",4.12\n"
            + LocalDate.of(2023, 11, 27) + ",4.12\n"
            + LocalDate.of(2023, 11, 24) + ",4.12\n" // monday
            + LocalDate.of(2023, 11, 23) + ",4.12\n" // friday,saturday,sunday
            + LocalDate.of(2023, 11, 22) + ",4.12\n";
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
