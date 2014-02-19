package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Logger;

public class Wallet {
	private static final Logger logger = Logger.getLogger(Wallet.class.getName());
	
	static {
		logger.setParent(Logger.getLogger(Wallet.class.getPackage().getName()));
	}
	
	Map<Currency, List<Bill>> billsForCurrency;
	Conversion conversion;
	private static final int MAX_SIZE = 100; 
	int numberOfBills = 0;
	
	public Wallet() {
		billsForCurrency = new TreeMap<>();
		conversion = new DailyConversion();
		logger.fine("Created new wallet");
	}
	
	public void setConversion(Conversion conv) {
		conversion = conv;
		logger.fine("Wallet's conversion changed");
	}
	
	public void put(Bill b) {
		if (numberOfBills >= MAX_SIZE) {
			throw new IllegalStateException("Wallet is full");
		}
		if (billsForCurrency.containsKey(b.getCurrency())) {
			((ArrayList<Bill>) billsForCurrency.get(b.getCurrency())).add(b);
		} else {
			ArrayList<Bill> theBills = new ArrayList<>();
			theBills.add(b);
			billsForCurrency.put(b.getCurrency(), theBills);
		}
		numberOfBills++;
		logger.fine("Wallet contains " + numberOfBills + " bills");
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
				logger.info("Conversion rate: " + rate);
				BigDecimal converted = rate.multiply(new BigDecimal(b.getAmount()));
				total = total.add(converted);
			}
		}
		
		return price <= total.doubleValue();
	}
	
	public boolean isFull() {
		return numberOfBills == MAX_SIZE;
	}

}
