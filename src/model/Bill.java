package model;

import java.util.logging.Logger;

public class Bill {
	static Logger logger = Logger.getLogger(Bill.class.getName());
	
	static {
		logger.setParent(Logger.getLogger(Bill.class.getPackage().getName()));
	}
	
	private int amount;
	private Currency currency;
	
	public Bill(int amount, Currency currency) {
		this.amount = amount;
		this.currency = currency;
		logger.fine("Created new Bill for " + amount + " " + currency);
	}
	
	public int getAmount() {
		return amount;
	}
	
	public Currency getCurrency() {
		return currency;
	}
	

}
