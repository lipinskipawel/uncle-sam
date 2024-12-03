package com.github.lipinskipawel.rates;

import com.github.lipinskipawel.Time;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;

import static java.nio.file.Files.createTempFile;
import static java.nio.file.Files.readAllLines;
import static java.nio.file.Files.writeString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

class FxFileWriterTest implements WithAssertions {

    private final Time time = mock(Time.class);

    @Test
    void append_one_entry_to_file() {
        final var emptyUsdPlnRateFile = createFileEmptyFile();
        final var fxFileWriter = new FxFileWriter(emptyUsdPlnRateFile, time);
        final var localDate = LocalDate.of(2024, 12, 2);
        given(time.localDate()).willReturn(localDate);

        fxFileWriter.append(3.2);

        assertThat(readContent(emptyUsdPlnRateFile)).isEqualTo(localDate + ",3.2");
    }

    @Test
    void append_two_entries_to_file() {
        final var emptyUsdPlnRateFile = createFileEmptyFile();
        final var fxFileWriter = new FxFileWriter(emptyUsdPlnRateFile, time);
        final var localDate = LocalDate.of(2024, 12, 2);
        given(time.localDate()).willReturn(localDate, localDate.plusDays(1));

        fxFileWriter.append(3.2);
        fxFileWriter.append(3.3);

        then(time).should(times(2)).localDate();
        assertThat(readContent(emptyUsdPlnRateFile)).isEqualTo("""
            %s,3.2
            %s,3.3""".formatted(localDate, localDate.plusDays(1))
        );
    }

    private static String readContent(Path path) {
        try {
            return String.join("\n", readAllLines(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Path createFileEmptyFile() {
        try {
            final var usdPln = createTempFile("usdPln", ".tmp");
            writeString(usdPln, "");
            return usdPln;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
