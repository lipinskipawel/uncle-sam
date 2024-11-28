package com.github.lipinskipawel;

import java.nio.file.Path;
import java.util.Optional;

import static java.lang.System.getenv;
import static java.util.Optional.ofNullable;

public final class FileStorage {

    public Optional<Path> transactions() {
        return ofNullable(getenv("HOME"))
            .map(Path::of)
            .map(path -> path.resolve(".config"))
            .map(path -> path.resolve("exchange"))
            .map(path -> path.resolve("vuaa.uk.txt"))
            .filter(path -> path.toFile().exists() && path.toFile().isFile());
    }
}