package com.github.lipinskipawel.cli;

import java.util.Optional;

import static com.github.lipinskipawel.cli.ArgumentParser.Flag.PRICE;
import static com.github.lipinskipawel.cli.ArgumentParser.Flag.TRANSACTION_PATH;
import static com.github.lipinskipawel.cli.ArgumentParser.Flag.USD_PLN;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;

public final class ArgumentParser {
    private final String[] args;

    public enum Flag {
        TRANSACTION_PATH("transactions="),
        PRICE("assetPrice="),
        USD_PLN("usd/pln=");

        private final String flag;

        Flag(String s) {
            this.flag = s;
        }
    }

    public ArgumentParser(String[] args) {
        this.args = requireNonNull(args);
    }

    public Option option() {
        return new Valuation(
            findValue(TRANSACTION_PATH),
            findValue(PRICE),
            findValue(USD_PLN)
        );
    }

    Optional<String> findValue(Flag flag) {
        return stream(args)
            .filter(it -> it.startsWith(flag.flag))
            .map(it -> it.split("=")[1])
            .map(Optional::ofNullable)
            .flatMap(Optional::stream)
            .findFirst();
    }
}
