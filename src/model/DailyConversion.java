package model;

import java.math.BigDecimal;

public class DailyConversion implements Conversion {

	// use some web service to get the exact conversion for the day
	public DailyConversion() {
		
	}
	
	@Override
	public BigDecimal getRate(Currency curr1, Currency curr2) {
		return new BigDecimal(1);
	}

}
