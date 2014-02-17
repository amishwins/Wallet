package model;

public class Bill {
	private int amount;
	private Currency currency;
	
	public Bill(int amount, Currency currency) {
		this.amount = amount;
		this.currency = currency;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public Currency getCurrency() {
		return currency;
	}
	

}
