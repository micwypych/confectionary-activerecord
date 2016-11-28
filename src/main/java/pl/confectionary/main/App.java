package pl.confectionary.main;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.javamoney.moneta.Money;

import pl.confectionary.domain.Choclate;
import pl.confectionary.domain.ChoclateBox;

public class App {
	public static void main(String[] args) {
		BigDecimal number = BigDecimal.valueOf(10.15);
		String currency = "PLN";
		Choclate c = new Choclate("Truskawkowa Nadzieja", Money.of(number, currency));
		//c.create();
		
		Choclate c2 = new Choclate("Truskawkowa Przygoda", Money.of(BigDecimal.valueOf(7.45), "PLN"));
		//c2.create();
		List<Choclate> choclates = Arrays.asList(c,c2);
		ChoclateBox box = new ChoclateBox("Trauskawkowe Combo", choclates);
		box.create();
		
		
		ChoclateBox readBox = ChoclateBox.find(box.getId());
		for( Choclate cc : choclates ) {
			System.out.println(String.format("Element stored in db: %s",cc));
		}			
	}
}
