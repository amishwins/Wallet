package model;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WalletTest {
	
	Wallet wallet;
	
	@Before
	public void setup() {
		wallet = new Wallet();		
	}
	
	@After
	public void tearDown() {
	}

	@Test
	public void addNoBillsTotalIsZero() {
		assertEquals(0, wallet.getTotalForCurrency(Currency.AUS));
		assertEquals(0, wallet.getTotalForCurrency(Currency.CAD));
		assertEquals(0, wallet.getTotalForCurrency(Currency.INR));
		assertEquals(0, wallet.getTotalForCurrency(Currency.USD));
	}
	
	@Test
	public void addOneBillTotalIsGood() {
		addTenCAD();
		
		assertEquals(0, wallet.getTotalForCurrency(Currency.AUS));
		assertEquals(10, wallet.getTotalForCurrency(Currency.CAD));
		assertEquals(0, wallet.getTotalForCurrency(Currency.INR));
		assertEquals(0, wallet.getTotalForCurrency(Currency.USD));	
	}

	@Test
	public void addMultipleBills() {
		addTenCAD();
		
		addTenUSD();
		addTenUSD();
		
		assertEquals(0, wallet.getTotalForCurrency(Currency.AUS));
		assertEquals(10, wallet.getTotalForCurrency(Currency.CAD));
		assertEquals(0, wallet.getTotalForCurrency(Currency.INR));
		assertEquals(20, wallet.getTotalForCurrency(Currency.USD));	
	}
	
	@Test
	public void addMultipleBillsOfSameType() {
		addTenCAD();
		addTenCAD();
		
		assertEquals(0, wallet.getTotalForCurrency(Currency.AUS));
		assertEquals(20, wallet.getTotalForCurrency(Currency.CAD));
		assertEquals(0, wallet.getTotalForCurrency(Currency.INR));
		assertEquals(0, wallet.getTotalForCurrency(Currency.USD));	
	}
	
	@Test
	public void emptyWalletCanPayForAnything() {
		assertEquals(false, wallet.canPayForWithCurrency(Currency.AUS, 10));
		assertEquals(false, wallet.canPayForWithCurrency(Currency.CAD, 10));
		assertEquals(false, wallet.canPayForWithCurrency(Currency.INR, 10));
		assertEquals(false, wallet.canPayForWithCurrency(Currency.USD, 10));

		assertEquals(true, wallet.canPayForWithCurrency(Currency.AUS, 0));
		assertEquals(true, wallet.canPayForWithCurrency(Currency.CAD, 0));
		assertEquals(true, wallet.canPayForWithCurrency(Currency.INR, 0));
		assertEquals(true, wallet.canPayForWithCurrency(Currency.USD, 0));
	}
	
	@Test
	public void somethingInWalletCanPayForItWithCurrency() {
		addTenCAD();
		assertEquals(true, wallet.canPayForWithCurrency(Currency.CAD, 9));
		assertEquals(true, wallet.canPayForWithCurrency(Currency.CAD, 9.999));

		assertEquals(true, wallet.canPayForWithCurrency(Currency.CAD, 10));

		assertEquals(false, wallet.canPayForWithCurrency(Currency.CAD, 10.01));
		assertEquals(false, wallet.canPayForWithCurrency(Currency.CAD, 11));
		
		addTenCAD();
		assertEquals(true, wallet.canPayForWithCurrency(Currency.CAD, 11));
		assertEquals(false, wallet.canPayForWithCurrency(Currency.AUS, 11));
		assertEquals(false, wallet.canPayForWithCurrency(Currency.INR, 11));
		assertEquals(false, wallet.canPayForWithCurrency(Currency.USD, 11));
	}
	
	@Test
	public void canPayForWithConversion() {
		addTenCAD();
	
		ConversionStub fromCADtoUSD = new ConversionStub(new BigDecimal(new BigInteger("8"), -1));
		wallet.setConversion(fromCADtoUSD);
		assertEquals(true, wallet.canPayForWithConversion(Currency.USD, 8));
	}
	
	@Test
	public void cannotPayForWithConversion() {
		addTenCAD();
		
		ConversionStub fromCADtoUSD = new ConversionStub(new BigDecimal(0.75));
		wallet.setConversion(fromCADtoUSD);
		assertEquals(false, wallet.canPayForWithConversion(Currency.USD, 8));
	}
	
	
	@Test
	public void someMoneyCanPayForSameCurrency() {
		Bill b = new Bill(10, Currency.CAD);
		wallet.put(b);
	}
	
	@Test(expected = IllegalStateException.class)
	public void fillUpWallet() {
		for(int i = 0; i < 101; i++) {
			wallet.put(new Bill(5, Currency.CAD));
		}
	}
	
	
	private void addTenCAD() {
		Bill b = new Bill(10, Currency.CAD);
		wallet.put(b);
	}
	
	private void addTenUSD() {
		Bill b = new Bill(10, Currency.USD);
		wallet.put(b);
	}
	
	class ConversionStub implements Conversion {
		
		BigDecimal rate;

		public ConversionStub(BigDecimal rate) {
			this.rate = rate;
		}
		
		void setRate(BigDecimal theRate) {
			rate = theRate;
		}

		@Override
		public BigDecimal getRate(Currency curr1, Currency curr2) {
			return rate;
		}
		
	}
	
}
