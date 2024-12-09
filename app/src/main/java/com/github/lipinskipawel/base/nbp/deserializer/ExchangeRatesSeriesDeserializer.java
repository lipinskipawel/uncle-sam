package com.github.lipinskipawel.base.nbp.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.lipinskipawel.base.nbp.ExchangeRatesSeries;
import com.github.lipinskipawel.base.nbp.ExchangeRatesSeries.Rate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.github.lipinskipawel.base.nbp.ExchangeRatesSeries.Builder.exchangeRatesSeries;

public final class ExchangeRatesSeriesDeserializer extends JsonDeserializer<ExchangeRatesSeries> {

    @Override
    public ExchangeRatesSeries deserialize(JsonParser parser, DeserializationContext dctx) throws IOException {
        final JsonNode tree = parser.getCodec().readTree(parser);

        final var table = tree.get("table").asText();
        final var currency = tree.get("currency").asText();
        final var code = tree.get("code").asText();
        final var rates = parseList(dctx, tree);

        return exchangeRatesSeries()
            .table(table)
            .currency(currency)
            .code(code)
            .rates(rates)
            .build();
    }

    private List<Rate> parseList(DeserializationContext dctx, TreeNode tree) throws IOException {
        final var node = tree.get("rates");
        if (node == null || !node.isArray()) {
            return List.of();
        }

        final var result = new ArrayList<Rate>();
        final var elements = ((JsonNode) node).elements();

        while (elements.hasNext()) {
            final var parser = elements.next().traverse(dctx.getParser().getCodec());
            parser.nextToken();
            result.add(dctx.readValue(parser, Rate.class));
        }

        return result;
    }
}
