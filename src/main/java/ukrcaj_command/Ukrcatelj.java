package ukrcaj_command;

import korisnici_observer.Korisnik;
import stanica_prototype.Stanica;

public class Ukrcatelj {
	public void ukrcaj(Korisnik korisnik, Stanica stanica) {
		if (stanica.ulazIzlazPutnika) {
			System.out.println("Korisnik " + korisnik.getIme() + " " + korisnik.getPrezime() + " se ukrcao na stanici "
					+ stanica.naziv);
		} else {
			System.out.println("Stanica " + stanica.naziv + " ne dozvoljava ulaz/izlaz putnika. " + korisnik.getIme()
					+ " " + korisnik.getPrezime() + " nije ukrcan.");
		}
	}
}
