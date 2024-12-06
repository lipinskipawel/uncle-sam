package com.github.lipinskipawel.rates;

import com.github.lipinskipawel.Time;

import java.io.IOException;
import java.nio.file.Path;

import static java.nio.file.Files.writeString;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.util.Objects.requireNonNull;

final class FxFileWriter {

    private final Path fxPath;
    private final Time time;

    FxFileWriter(Path fxPath, Time time) {
        this.fxPath = requireNonNull(fxPath);
        this.time = requireNonNull(time);
    }

    boolean append(double rate) {
        try {
            final var fxLine = time.localDate() + "," + rate + "\n";
            writeString(fxPath, fxLine, CREATE, APPEND);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
