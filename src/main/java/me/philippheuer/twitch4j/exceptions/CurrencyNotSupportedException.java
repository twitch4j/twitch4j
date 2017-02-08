package me.philippheuer.twitch4j.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.util.Currency;

@Getter
@Setter
public class CurrencyNotSupportedException extends RuntimeException {

	private Currency currency;

	public CurrencyNotSupportedException(Currency currency) {
		super(String.format("Currency %s [%s] not supported!", currency.getDisplayName(), currency.getCurrencyCode()));
		setCurrency(currency);
	}
}
