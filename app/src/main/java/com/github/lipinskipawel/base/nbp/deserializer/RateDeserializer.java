package com.github.lipinskipawel.base.nbp.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.lipinskipawel.base.nbp.ExchangeRatesSeries.Rate;

import java.io.IOException;

import static com.github.lipinskipawel.base.nbp.ExchangeRatesSeries.Rate.Builder.rate;
import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_EVEN;
import static java.time.LocalDate.parse;

public final class RateDeserializer extends JsonDeserializer<Rate> {

    @Override
    public Rate deserialize(JsonParser parser, DeserializationContext dctxt) throws IOException {
        final JsonNode tree = parser.getCodec().readTree(parser);

        final var no = tree.get("no").asText();
        final var effectiveDate = parse(tree.get("effectiveDate").asText());
        final var mid = valueOf(tree.get("mid").asDouble()).setScale(4, HALF_EVEN);

        return rate()
            .no(no)
            .effectiveDate(effectiveDate)
            .mid(mid)
            .build();
    }
}
