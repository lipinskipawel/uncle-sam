package com.github.lipinskipawel.base.nbp.deserializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.lipinskipawel.base.nbp.ExchangeRatesSeries;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.github.lipinskipawel.base.nbp.ExchangeRatesSeries.Builder.exchangeRatesSeries;
import static com.github.lipinskipawel.base.nbp.ExchangeRatesSeries.Rate.Builder.rate;
import static com.github.lipinskipawel.base.nbp.NbpClient.NBP_MAPPER;

class ExchangeRatesSeriesDeserializerTest implements WithAssertions {

    @Test
    void deserialize_exchange_rates_series() throws JsonProcessingException {
        final var json = """
            {
              "table": "A",
              "currency": "dolar amerykański",
              "code": "USD",
              "rates": [
                {
                  "no": "001/A/NBP/2024",
                  "effectiveDate": "2024-01-02",
                  "mid": 3.9432
                }
              ]
            }""";

        final var exchangeRatesSeries = NBP_MAPPER.readValue(json, ExchangeRatesSeries.class);

        assertThat(exchangeRatesSeries).isEqualTo(exchangeRatesSeries()
            .table("A")
            .currency("dolar amerykański")
            .code("USD")
            .rates(List.of(
                rate()
                    .no("001/A/NBP/2024")
                    .effectiveDate(LocalDate.of(2024, 1, 2))
                    .mid(new BigDecimal("3.9432"))
                    .build()
            ))
            .build());
    }

    @Test
    void deserialize_exchange_rates_series_with_multiple_rates() throws JsonProcessingException {
        final var json = """
            {
              "table": "A",
              "currency": "dolar amerykański",
              "code": "USD",
              "rates": [
                {
                  "no": "001/A/NBP/2024",
                  "effectiveDate": "2024-01-02",
                  "mid": 3.9432
                },
                {
                  "no": "002/A/NBP/2024",
                  "effectiveDate": "2024-01-03",
                  "mid": 3.9909
                }
              ]
            }""";

        final var exchangeRatesSeries = NBP_MAPPER.readValue(json, ExchangeRatesSeries.class);

        assertThat(exchangeRatesSeries).isEqualTo(exchangeRatesSeries()
            .table("A")
            .currency("dolar amerykański")
            .code("USD")
            .rates(List.of(
                rate()
                    .no("001/A/NBP/2024")
                    .effectiveDate(LocalDate.of(2024, 1, 2))
                    .mid(new BigDecimal("3.9432"))
                    .build(),
                rate()
                    .no("002/A/NBP/2024")
                    .effectiveDate(LocalDate.of(2024, 1, 3))
                    .mid(new BigDecimal("3.9909"))
                    .build()
            ))
            .build());
    }

    @Test
    void deserialize_exchange_rates_series_when_mid_as_4_decimal_places_even_given_3() throws JsonProcessingException {
        final var json = """
            {
              "table": "A",
              "currency": "dolar amerykański",
              "code": "USD",
              "rates": [
                {
                  "no": "001/A/NBP/2024",
                  "effectiveDate": "2024-07-03",
                  "mid": 3.999
                }
              ]
            }""";

        final var exchangeRatesSeries = NBP_MAPPER.readValue(json, ExchangeRatesSeries.class);

        assertThat(exchangeRatesSeries).isEqualTo(exchangeRatesSeries()
            .table("A")
            .currency("dolar amerykański")
            .code("USD")
            .rates(List.of(
                rate()
                    .no("001/A/NBP/2024")
                    .effectiveDate(LocalDate.of(2024, 7, 3))
                    .mid(new BigDecimal("3.9990"))
                    .build()
            ))
            .build());
    }
}
