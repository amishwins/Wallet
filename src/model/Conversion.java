package model;

import java.math.BigDecimal;

public interface Conversion {
	BigDecimal getRate(Currency curr1, Currency curr2);
}
