package com.github.lipinskipawel.nbp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.net.http.HttpClient.newBuilder;
import static java.net.http.HttpResponse.BodySubscribers.mapping;
import static java.net.http.HttpResponse.BodySubscribers.ofString;
import static java.nio.charset.Charset.defaultCharset;

public final class NbpClient {

    private final HttpClient nbpClient;
    private final ObjectMapper mapper;

    public NbpClient() {
        this.nbpClient = newBuilder().build();
        this.mapper = createMapper();
    }

    public HttpResponse<NbpResponse> currentUsdPln() {
        final var usdCurrentRate = HttpRequest.newBuilder()
            .uri(URI.create("https://api.nbp.pl/api/exchangerates/rates/a/usd"))
            .GET()
            .header("Accept", "application/json")
            .build();
        try {
            return nbpClient.send(usdCurrentRate, new CustomHandler());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    class CustomHandler implements HttpResponse.BodyHandler<NbpResponse> {

        @Override
        public HttpResponse.BodySubscriber<NbpResponse> apply(HttpResponse.ResponseInfo responseInfo) {
            return mapping(
                ofString(defaultCharset()),
                body -> {
                    try {
                        return mapper.readValue(body, NbpResponse.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
        }
    }

    private ObjectMapper createMapper() {
        final var mapper = new ObjectMapper();

        mapper
            .registerModule(new JavaTimeModule())
            .registerModule(new Jdk8Module())
            .registerModule(new SimpleModule()
                .addDeserializer(NbpResponse.class, new NbpResponseDeserializer()));

        return mapper;
    }
}
