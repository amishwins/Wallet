package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Wallet {
	Map<Currency, List<Bill>> billsForCurrency;
	Conversion conversion;
	
	public Wallet() {
		billsForCurrency = new TreeMap<>();
		conversion = new DailyConversion();
	}
	
	public void setConversion(Conversion conv) {
		conversion = conv;
	}
	
	public void put(Bill b) {
		if (billsForCurrency.containsKey(b.getCurrency())) {
			((ArrayList<Bill>) billsForCurrency.get(b.getCurrency())).add(b);
		} else {
			ArrayList<Bill> theBills = new ArrayList<>();
			theBills.add(b);
			billsForCurrency.put(b.getCurrency(), theBills);
		}
	}
	
	public int getTotalForCurrency(Currency cur) {
		int total = 0;
		if (billsForCurrency.containsKey(cur)) {
			for(Bill b: billsForCurrency.get(cur)) {
				total += b.getAmount();
			}
		} 		
		return total;
	}
	
	public boolean canPayForWithCurrency(Currency cur, double price) {
		return price <= getTotalForCurrency(cur);
	}
	
	public boolean canPayForWithConversion(Currency targetCurrency, double price) {
		BigDecimal total = new BigDecimal(0);
		for(Entry<Currency, List<Bill>> currBills : billsForCurrency.entrySet()) {
			for(Bill b: currBills.getValue()) {
				BigDecimal rate = conversion.getRate(b.getCurrency(), targetCurrency);
				BigDecimal converted = rate.multiply(new BigDecimal(b.getAmount()));
				total = total.add(converted);
			}
		}
		
		return price <= total.doubleValue();
	}

}
