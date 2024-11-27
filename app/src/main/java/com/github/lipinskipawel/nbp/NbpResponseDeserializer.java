package com.github.lipinskipawel.nbp;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.lipinskipawel.nbp.NbpResponse.Rate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.github.lipinskipawel.nbp.NbpResponse.Builder.nbpResponse;

// {"table":"A","currency":"dolar amerykański","code":"USD","rates":[{"no":"228/A/NBP/2024","effectiveDate":"2024-11-25","mid":4.1297}]}
public final class NbpResponseDeserializer extends JsonDeserializer<NbpResponse> {

    @Override
    public NbpResponse deserialize(JsonParser parser, DeserializationContext dctx) throws IOException {
        final JsonNode tree = parser.getCodec().readTree(parser);

        final var table = tree.get("table").asText();
        final var currency = tree.get("currency").asText();
        final var code = tree.get("code").asText();
        final var rates = parseList(dctx, tree);

        return nbpResponse()
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
