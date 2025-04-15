package kvardijan20_zadaca_3;

import java.util.Scanner;

import rukovateljNaredbama_facade.RukovateljKomandama;
import tvrtkaZaPrijevoz_singleton.TvrtkaZaPrijevoz;

public class Main {

	public static void main(String[] args) {
		TvrtkaZaPrijevoz tvrtkaZaPrijevoz = TvrtkaZaPrijevoz.dajInstancu();
		if (tvrtkaZaPrijevoz.obradiUlazneDatoteke(args)) {
			RukovateljKomandama rukovateljKomandama = new RukovateljKomandama();
			Scanner in = new Scanner(System.in);
			String komanda;

			// TEST
			//System.out.println("POPIS STANICA++++++++++++++++++++++++");
			//for (Stanica s : TvrtkaZaPrijevoz.getPopisStanica()) {
			//	System.out.println(s.naziv + " U: " + s.stajalisteUbrzani + " B: " + s.stajalisteBrzi);
			//}
			//System.out.println("POPIS STANICA++++++++++++++++++++++++");

			while (true) {
				komanda = in.nextLine();
				if (komanda.equals("Q"))
					break;

				switch (komanda.split(" ")[0]) {
				case "IK": {
					rukovateljKomandama.pregledKompozicije(komanda);
					break;
				}
				case "IP": {
					rukovateljKomandama.pregledPruga(komanda);
					break;
				}
				case "ISP": {
					rukovateljKomandama.pregledStanicaPruge(komanda);
					break;
				}
				case "ISI2S": {
					rukovateljKomandama.pregledStanicaIzmeduDvijeStanice(komanda);
					break;
				}
				case "IV": {
					rukovateljKomandama.pregledVlakova(komanda);
					break;
				}
				case "IEV": {
					rukovateljKomandama.pregledEtapaVlaka(komanda);
					break;
				}
				case "IEVD": {
					rukovateljKomandama.pregledVlakovaPoDanima(komanda);
					break;
				}
				case "IVRV": {
					rukovateljKomandama.pregledVoznogRedaVlaka(komanda);
					break;
				}
				case "DK": {
					rukovateljKomandama.dodajKorisnika(komanda);
					break;
				}
				case "PK": {
					rukovateljKomandama.ispisiKorisnike(komanda);
					break;
				}
				case "DPK": {
					rukovateljKomandama.dodajKorisnikaZaPracenje(komanda);
					break;
				}
				case "SVV": {
					rukovateljKomandama.zapocniSimulaciju(komanda);
					break;
				}
				case "URV": {
					rukovateljKomandama.ukljuciRazglasVlaka(komanda);
					break;
				}
				case "CVP": {
					rukovateljKomandama.definirajCijenePrijevoza(komanda);
					break;
				}
				case "KKPV2S": {
					rukovateljKomandama.kupiKartu(komanda);
					break;
				}
				case "IKKPV": {
					rukovateljKomandama.ispisiKupljeneKarte(komanda);
					break;
				}
				case "IRPS": {
					rukovateljKomandama.ispisRelacijaSaStatusom(komanda);
					break;
				}
				case "PSP2S": {
					rukovateljKomandama.promjenaStatusaRelacije(komanda);
					break;
				}
				case "UKP2S": {
					rukovateljKomandama.usporediKarte(komanda);
					break;
				}
				case "UKC": {
					rukovateljKomandama.ukrcaj(komanda);
					break;
				}
				default:
					System.out.println("Nepoznata komanda!");
				}
			}
			in.close();
		}
	}
}
