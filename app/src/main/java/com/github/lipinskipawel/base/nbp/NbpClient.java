package com.github.lipinskipawel.base.nbp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.lipinskipawel.base.nbp.ExchangeRatesSeries.Rate;
import com.github.lipinskipawel.base.nbp.deserializer.ExchangeRatesSeriesDeserializer;
import com.github.lipinskipawel.base.nbp.deserializer.RateDeserializer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.LocalDate;

import static java.net.URI.create;
import static java.net.http.HttpClient.newBuilder;

public final class NbpClient {

    private static final URI BASE_NBP_URI = create("https://api.nbp.pl/api/exchangerates/");
    public static final ObjectMapper NBP_MAPPER = createNbpMapper();

    private final HttpClient nbpClient;

    public NbpClient() {
        this.nbpClient = newBuilder().build();
    }

    public ExchangeRatesSeries currentUsdPln() {
        final var usdCurrentRate = HttpRequest.newBuilder()
            .uri(BASE_NBP_URI.resolve("rates/A/USD"))
            .GET()
            .header("Accept", "application/json")
            .build();
        try {
            return nbpClient.send(usdCurrentRate, new ExchangeBodyHandler()).body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public ExchangeRatesSeries usdPln30days(LocalDate from) {
        final var request = HttpRequest.newBuilder()
            .uri(BASE_NBP_URI.resolve("rates/A/USD/%s/%s".formatted(from, from.plusDays(30))))
            .GET()
            .header("Accept", "application/json")
            .build();
        try {
            return nbpClient.send(request, new ExchangeBodyHandler()).body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static ObjectMapper createNbpMapper() {
        final var mapper = new ObjectMapper();

        mapper
            .registerModule(new JavaTimeModule())
            .registerModule(new Jdk8Module())
            .registerModule(new SimpleModule()
                .addDeserializer(ExchangeRatesSeries.class, new ExchangeRatesSeriesDeserializer())
                .addDeserializer(Rate.class, new RateDeserializer())
            );

        return mapper;
    }
}
