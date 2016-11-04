package pl.confectionary.main;

import java.math.BigDecimal;
import java.util.Collection;

import org.javamoney.moneta.Money;

import pl.confectionary.domain.Choclate;

public class App {
	public static void main(String[] args) {
		BigDecimal number = BigDecimal.valueOf(10.15);
		String currency = "PLN";
		Choclate c = new Choclate("Truskawkowa Nadzieja", Money.of(number, currency));
		c.create();
		
		Choclate c2 = new Choclate("Truskawkowa Nadzieja", Money.of(BigDecimal.valueOf(7.45), "PLN"));
		c2.create();
		
		Collection<Choclate> choclates = Choclate.all();
		for( Choclate cc : choclates ) {
			System.out.println(String.format("Element stored in db: %s",cc));
		}			
	}
}
