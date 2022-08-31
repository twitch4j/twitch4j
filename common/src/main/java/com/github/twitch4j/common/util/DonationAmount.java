package com.github.twitch4j.common.util;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Currency;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class DonationAmount {

    /**
     * The monetary amount.
     * <p>
     * The amount is specified in the currency’s minor unit.
     * <p>
     * For example, the minor units for USD is cents, so if the amount is $5.50 USD, value is set to 550.
     */
    @JsonAlias({ "amount", "donation_amount" })
    private Long value;

    /**
     * The number of decimal places used by the currency.
     * <p>
     * For example, USD uses two decimal places.
     */
    @JsonAlias("exponent")
    private Integer decimalPlaces;

    /**
     * The ISO-4217 three-letter currency code that identifies the type of currency in {@link #getValue()}.
     */
    @JsonAlias({ "campaign_currency", "donation_currency" })
    private String currency;

    /**
     * The {@link Currency} corresponding to the ISO-4217 code contained in {@link #getCurrency()}.
     */
    @JsonIgnore
    @Getter(lazy = true)
    @EqualsAndHashCode.Exclude
    private final Currency parsedCurrency = Currency.getInstance(getCurrency());

    /**
     * The donation amount, with the appropriate decimals, based on {@link #getValue()}.
     */
    @JsonIgnore
    @Getter(lazy = true)
    @EqualsAndHashCode.Exclude
    private final BigDecimal parsedValue = BigDecimal.valueOf(getValue(), getDecimalPlaces());

}
