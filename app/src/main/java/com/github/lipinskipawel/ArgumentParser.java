package com.github.lipinskipawel;

import java.util.Arrays;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public final class ArgumentParser {
    private final String[] args;

    public enum Flag {
        TRANSACTION_PATH("transactions=");

        private final String flag;

        Flag(String s) {
            this.flag = s;
        }
    }

    public ArgumentParser(String[] args) {
        this.args = requireNonNull(args);
    }


    public Optional<String> findValue(Flag flag) {
        return Arrays.stream(args)
            .filter(it -> it.startsWith(flag.flag))
            .map(it -> it.split("=")[1])
            .map(Optional::ofNullable)
            .flatMap(Optional::stream)
            .findFirst();
    }
}
