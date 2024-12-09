package com.github.lipinskipawel.base.nbp;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodySubscriber;
import java.net.http.HttpResponse.ResponseInfo;

import static com.github.lipinskipawel.base.nbp.NbpClient.NBP_MAPPER;
import static java.net.http.HttpResponse.BodySubscribers.mapping;
import static java.net.http.HttpResponse.BodySubscribers.ofString;
import static java.nio.charset.Charset.defaultCharset;

final class ExchangeBodyHandler implements BodyHandler<ExchangeRatesSeries> {

    @Override
    public BodySubscriber<ExchangeRatesSeries> apply(ResponseInfo responseInfo) {
        return mapping(
            ofString(defaultCharset()),
            body -> {
                try {
                    return NBP_MAPPER.readValue(body, ExchangeRatesSeries.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        );
    }
}
