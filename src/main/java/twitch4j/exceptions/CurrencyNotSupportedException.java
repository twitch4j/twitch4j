package twitch4j.exceptions;

import java.util.Currency;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyNotSupportedException extends RuntimeException {

	private Currency currency;

	public CurrencyNotSupportedException(Currency currency) {
		super(String.format("Currency %s [%s] not supported!", currency.getDisplayName(), currency.getCurrencyCode()));
		setCurrency(currency);
	}
}
