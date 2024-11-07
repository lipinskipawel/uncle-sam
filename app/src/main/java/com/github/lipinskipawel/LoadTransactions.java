package com.github.lipinskipawel;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.github.lipinskipawel.broker.Transaction;
import com.github.lipinskipawel.broker.Type;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.dataformat.csv.CsvSchema.ColumnType.NUMBER;
import static com.fasterxml.jackson.dataformat.csv.CsvSchema.builder;
import static com.github.lipinskipawel.Currency.PLN;
import static com.github.lipinskipawel.Currency.USD;
import static com.github.lipinskipawel.CurrencyPair.currencyPair;
import static com.github.lipinskipawel.broker.Transaction.Builder.transaction;
import static java.util.Objects.requireNonNull;

public final class LoadTransactions {
    private final Path transactionPath;

    private final CsvMapper mapper;
    private final CsvSchema csvSchema;

    public LoadTransactions(Path transactionPath) {
        this.transactionPath = requireNonNull(transactionPath);
        this.mapper = new CsvMapper();
        this.csvSchema = builder()
            .setUseHeader(true)
            .addColumn("type")
            .addColumn("date")
            .addColumn("price")
            .addColumn("currency")
            .addColumn("volume", NUMBER)
            .addColumn("fxRate")
            .build();
    }

    public List<Transaction> loadTransactions() {
        try (MappingIterator<CsvTransactions> parsed =
                 mapper
                     .readerFor(CsvTransactions.class)
                     .with(csvSchema)
                     .readValues(transactionPath.toFile())) {
            final var result = new ArrayList<Transaction>();

            while (parsed.hasNextValue()) {
                final var csv = parsed.nextValue();
                result.add(transaction()
                    .type(Type.valueOf(csv.type))
                    .localDate(LocalDate.parse(csv.date))
                    .price(Cash.cash(csv.price, Currency.valueOf(csv.currency)))
                    .volume(csv.volume)
                    .fxRate(currencyPair(USD, new BigDecimal(csv.fxRate), PLN))
                    .build());
            }

            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
