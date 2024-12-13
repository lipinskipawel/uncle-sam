package com.github.lipinskipawel.common.files;

import java.nio.file.Path;
import java.util.Optional;

import static java.lang.System.getenv;
import static java.util.Optional.ofNullable;

public final class FileStorage {

    public Path transactions() {
        return basePath()
            .map(path -> path.resolve("exchange"))
            .map(path -> path.resolve("vuaa.uk.csv"))
            .orElseThrow();
    }

    public Path usdPln() {
        return basePath()
            .map(path -> path.resolve("rates"))
            .map(path -> path.resolve("usdPln.csv"))
            .orElseThrow();
    }

    private Optional<Path> basePath() {
        return ofNullable(getenv("HOME"))
            .or(() -> Optional.of("/"))
            .map(Path::of)
            .map(path -> path.resolve(".config"))
            .map(path -> path.resolve("uncle-sam"));
    }
}
