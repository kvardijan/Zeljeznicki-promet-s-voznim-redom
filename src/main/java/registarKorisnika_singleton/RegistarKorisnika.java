package registarKorisnika_singleton;

import java.util.ArrayList;
import java.util.List;

import korisnici_observer.Korisnik;

public class RegistarKorisnika {
	private static RegistarKorisnika INSTANCA;
	private static List<Korisnik> popisKorisnika = new ArrayList<>();

	private RegistarKorisnika() {
	}

	public static RegistarKorisnika dajInstancu() {
		if (INSTANCA == null)
			INSTANCA = new RegistarKorisnika();
		return INSTANCA;
	}

	public static List<Korisnik> getPopisKorisnika() {
		return popisKorisnika;
	}

	public static void dodajKorisnika(Korisnik k) {
		popisKorisnika.add(k);
	}

	public static Korisnik dajKorisnikaPoImenuIPrezimenu(String ime, String prezime) {
		Korisnik korisnik = null;
		for (Korisnik k : popisKorisnika) {
			if (k.getIme().equals(ime) && k.getPrezime().equals(prezime)) {
				korisnik = k;
				break;
			}
		}
		return korisnik;
	}
}
