package rukovateljNaredbama_facade;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import kalkulatorCijene_strategy.IzdavacKarte;
import kalkulatorCijene_strategy.KalkulatorBlagajna;
import kalkulatorCijene_strategy.KalkulatorVlak;
import kalkulatorCijene_strategy.KalkulatorWebMob;
import karta_memento.Karta;
import kompozicija_builder.Kompozicija;
import korisnici_observer.Korisnik;
import kvardijan20_zadaca_3.SimulacijaVlaka;
import kvardijan20_zadaca_3.TraziteljPuta;
import podatkovneKlase.Cjenik;
import podatkovneKlase.RelacijaSmjer;
import podatkovneKlase.StanicaSUdaljenosti;
import prijevoznoSredstvo_builder.PrijevoznoSredstvo;
import pruga_state.Pruga;
import razglas_mediator.IRazglasMediator;
import razglas_mediator.RazglasMediator;
import registarKorisnika_singleton.RegistarKorisnika;
import stanica_prototype.Stanica;
import tvrtkaZaPrijevoz_singleton.TvrtkaZaPrijevoz;
import ukrcaj_command.IUkrcajCommand;
import ukrcaj_command.UkrcajPutnika;
import ukrcaj_command.Ukrcatelj;
import vozniRed_composite.Vlak;
import vozniRed_visitor.IEVDVisitor;
import vozniRed_visitor.IEVVisitor;
import vozniRed_visitor.IVRVVisitor;
import vozniRed_visitor.IVVisitor;

public class RukovateljKomandama {
	IRazglasMediator razglasMediator;

	public RukovateljKomandama() {
	}

	public void ukrcaj(String komanda) {
		String djelovi[] = komanda.split("-");
		if (djelovi.length != 3) {
			System.out.println(
					"Nepravilna sintaksa komande! (Sintaksa:UKC imeKorisnika prezimeKorisnika - nazivStanice - vrijeme");
			return;
		}

		String stanicaCekanjaNaziv = djelovi[1].trim();
		Stanica stanicaCekanja = TvrtkaZaPrijevoz.getStanica(stanicaCekanjaNaziv);
		if (stanicaCekanja == null) {
			System.out.println("Stanica s tim nazivom ne postoji.");
			return;
		}

		String vrijemeDolaskaNaStanicu = djelovi[2].trim();
		if (!vrijemeDolaskaNaStanicu.matches("^([01]?\\d|2[0-3]):[0-5]\\d$")) {
			System.out.println("Vrijednost zavrsnog vremena " + vrijemeDolaskaNaStanicu + " nije validna.");
			return;
		}

		String prviDio[] = djelovi[0].split(" ");
		if (prviDio.length != 3) {
			System.out.println("Nepravilna sintaksa komande! (Sintaksa:DPK ime prezime - oznakaVlaka [- stanica])");
			return;
		}
		Korisnik korisnik = RegistarKorisnika.dajKorisnikaPoImenuIPrezimenu(prviDio[1], prviDio[2]);
		if (korisnik == null) {
			System.out.println("Ne postoji korisnik s tim imenom i prezimenom.");
			return;
		}

		IUkrcajCommand ukrcaj = new UkrcajPutnika(new Ukrcatelj(), korisnik, stanicaCekanja, vrijemeDolaskaNaStanicu);
		TvrtkaZaPrijevoz.getUkrcajInvoker().dodajUkrcaj(ukrcaj);
	}

	public void usporediKarte(String komanda) {
		String djelovi[] = komanda.split("-");
		if (djelovi.length != 6) {
			System.out.println(
					"Nepravilna sintaksa komande! (Sintaksa:UKP2S polaznaStanica - odredišnaStanica - datum - odVr - doVr -"
							+ " načinKupovine");
			return;
		}

		String prviDio = djelovi[0].trim();
		int firstSpaceIndex = prviDio.indexOf(" ");
		String polaznaStanicaNaziv = prviDio.substring(firstSpaceIndex + 1);
		Stanica polaznaStanica = TvrtkaZaPrijevoz.getStanica(polaznaStanicaNaziv);
		if (polaznaStanica == null) {
			System.out.println("Polazna stanica s tim nazivom ne postoji.");
			return;
		}

		String odredisnaStanicaNaziv = djelovi[1].trim();
		Stanica odredisnaStanica = TvrtkaZaPrijevoz.getStanica(odredisnaStanicaNaziv);
		if (odredisnaStanica == null) {
			System.out.println("Odredisna stanica s tim nazivom ne postoji.");
			return;
		}

		if (polaznaStanica.naziv.equals(odredisnaStanica.naziv)) {
			System.out.println("Pocetna i odredisna stanica su iste.");
			return;
		}

		String nacinKupovine = djelovi[5].trim();
		if (!nacinKupovine.matches("^(WM|B|V)$")) {
			System.out.println("Odabrani nacin kupovine nije valjan.");
			return;
		}

		IzdavacKarte izdavacKarte = null;
		switch (nacinKupovine) {
		case "WM": {
			izdavacKarte = new IzdavacKarte(new KalkulatorWebMob());
			break;
		}
		case "B": {
			izdavacKarte = new IzdavacKarte(new KalkulatorBlagajna());
			break;
		}
		case "V": {
			izdavacKarte = new IzdavacKarte(new KalkulatorVlak());
			break;
		}
		}

		String vOd = djelovi[3].trim();
		if (!vOd.matches("^([01]?\\d|2[0-3]):[0-5]\\d$")) {
			System.out.println("Vrijednost pocetnog vremena " + vOd + " nije validna.");
			return;
		}

		String vDo = djelovi[4].trim();
		if (!vDo.matches("^([01]?\\d|2[0-3]):[0-5]\\d$")) {
			System.out.println("Vrijednost zavrsnog vremena " + vDo + " nije validna.");
			return;
		}

		String datum = djelovi[2].trim();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
		try {
			LocalDate.parse(datum, format);
		} catch (DateTimeParseException e) {
			System.out.println("Datum nije valjan.");
			return;
		}

		// izdavacKarte.usporediKarte(polaznaStanica, odredisnaStanica, datum, vOd,
		// vDo);
		System.out.println("Komanda nije implementirana.");
		return;
	}

	public void promjenaStatusaRelacije(String komanda) {
		String djelovi[] = komanda.split("-");
		if (djelovi.length != 4) {
			System.out.println(
					"Nepravilna sintaksa komande! (Sintaksa:PSP2S oznaka - polaznaStanica - odredišnaStanica - status");
			return;
		}

		List<Pruga> filtriranePruge = dohvatiPrugePoOznaci(djelovi[0].split(" ")[1].trim());
		if (filtriranePruge.isEmpty()) {
			System.out.println("Ne postoji pruga s oznakom " + djelovi[0].split(" ")[1].trim());
			return;
		}

		Stanica polaznaStanica = TvrtkaZaPrijevoz.getStanica(djelovi[1].trim());
		if (polaznaStanica == null) {
			System.out.println("Stanica " + djelovi[1].trim() + " ne postoji.");
			return;
		}

		Stanica odredisnaStanica = TvrtkaZaPrijevoz.getStanica(djelovi[2].trim());
		if (odredisnaStanica == null) {
			System.out.println("Stanica " + djelovi[2].trim() + " ne postoji.");
			return;
		}

		if (polaznaStanica.naziv.equals(odredisnaStanica.naziv)) {
			System.out.println("Uneseno je isto ime za polaznu i odredisnu stanicu.");
			return;
		}

		String status = djelovi[3].trim();
		if (!status.matches("^(I|K|T|Z)$")) {
			System.out.println("Odabrani status nije valjan.");
			return;
		}

		RelacijaSmjer relacija = provjeriValjanostRelacije(filtriranePruge, polaznaStanica, odredisnaStanica, status);
		if (relacija == null) {
			System.out.println("Nije moguce azurirati status na relaciji.");
			return;
		} else {
			for (Pruga p : relacija.relacija) {
				if (p.brojKolosjeka == 1) {
					switch (status) {
					case "K": {
						p.pokvariPrugu("N");
						p.pokvariPrugu("O");
						break;
					}
					case "Z": {
						p.zatvoriPrugu("N");
						p.zatvoriPrugu("O");
						break;
					}
					case "T": {
						p.testirajPrugu("N");
						p.testirajPrugu("O");
						break;
					}
					default: {
						p.popraviPrugu("N");
						p.popraviPrugu("O");
					}
					}
				} else {
					switch (status) {
					case "K": {
						p.pokvariPrugu(relacija.smjer);
						break;
					}
					case "Z": {
						p.zatvoriPrugu(relacija.smjer);
						break;
					}
					case "T": {
						p.testirajPrugu(relacija.smjer);
						break;
					}
					default: {
						p.popraviPrugu(relacija.smjer);
					}
					}
				}
			}
		}
	}

	private RelacijaSmjer provjeriValjanostRelacije(List<Pruga> filtriranePruge, Stanica polaznaStanica,
			Stanica odredisnaStanica, String status) {
		boolean nadenaPolazna = false, nadenaOdredisna = false;
		String smjer = "";
		boolean postojiPresjekStatusa = false;

		for (Pruga p : filtriranePruge) {
			if (p.pocetnaStanica.naziv.equals(polaznaStanica.naziv)
					|| p.zavrsnaStanica.naziv.equals(polaznaStanica.naziv)) {
				nadenaPolazna = true;
			}
			if (p.pocetnaStanica.naziv.equals(odredisnaStanica.naziv)
					|| p.zavrsnaStanica.naziv.equals(odredisnaStanica.naziv)) {
				nadenaOdredisna = true;
			}
		}
		if (!nadenaPolazna) {
			System.out.println("Nije pronadena polazna stanica na toj pruzi.");
		}
		if (!nadenaOdredisna) {
			System.out.println("Nije pronadena odredisna stanica na toj pruzi.");
		}

		int indeksPolazna = -1;
		int indeksOdredisna = -1;
		boolean prva = true;
		int indeksStanice = 0;
		for (int i = 0; i < filtriranePruge.size(); i++) {
			if (prva) {
				if (filtriranePruge.get(i).pocetnaStanica.naziv.equals(polaznaStanica.naziv)) {
					indeksPolazna = indeksStanice;
				}
				if (filtriranePruge.get(i).pocetnaStanica.naziv.equals(odredisnaStanica.naziv)) {
					indeksOdredisna = indeksStanice;
				}
				indeksStanice++;
				prva = false;
			}
			if (filtriranePruge.get(i).zavrsnaStanica.naziv.equals(polaznaStanica.naziv)) {
				indeksPolazna = indeksStanice;
			}
			if (filtriranePruge.get(i).zavrsnaStanica.naziv.equals(odredisnaStanica.naziv)) {
				indeksOdredisna = indeksStanice;
			}
			indeksStanice++;
		}
		if (indeksPolazna < indeksOdredisna) {
			smjer = "N";
		} else {
			smjer = "O";
		}

		List<Pruga> trazenaRelacija = new ArrayList<>();
		boolean pronadenaPrva = false;
		boolean pronadenaZadnja = false;
		List<Pruga> filtriranePrugeReverse = filtriranePruge.reversed();
		if (smjer.equals("N")) {
			Pruga pTemp = new Pruga();
			for (Pruga p : filtriranePruge) {
				pTemp = p;
				if (p.pocetnaStanica.naziv.equals(polaznaStanica.naziv) && !pronadenaPrva) {
					pronadenaPrva = true;
				}
				if (p.pocetnaStanica.naziv.equals(odredisnaStanica.naziv) && pronadenaPrva) {
					pronadenaZadnja = true;
				}
				if (pronadenaPrva && !pronadenaZadnja) {
					trazenaRelacija.add(p);
				}
			}
			if (!pronadenaZadnja) {
				trazenaRelacija.add(pTemp);
			}
		} else {
			Pruga pTemp = new Pruga();
			for (Pruga p : filtriranePrugeReverse) {
				pTemp = p;
				if (p.zavrsnaStanica.naziv.equals(polaznaStanica.naziv) && !pronadenaPrva) {
					pronadenaPrva = true;
				}
				if (p.zavrsnaStanica.naziv.equals(odredisnaStanica.naziv) && pronadenaPrva) {
					pronadenaZadnja = true;
				}
				if (pronadenaPrva && !pronadenaZadnja) {
					trazenaRelacija.add(p);
				}
			}
			if (!pronadenaZadnja) {
				trazenaRelacija.add(pTemp);
			}
		}

		// TEST
		/*
		 * for (Pruga p : trazenaRelacija) { System.out.println( p.pocetnaStanica.naziv
		 * + "-" + p.zavrsnaStanica.naziv + " " + p.dajStanjePrugeSkraceno(smjer)); }
		 */
		// TEST

		String trenutniStatus = trazenaRelacija.get(0).dajStanjePrugeSkraceno(smjer);
		for (Pruga p : trazenaRelacija) {
			if (!p.dajStanjePrugeSkraceno(smjer).equals(trenutniStatus)) {
				postojiPresjekStatusa = true;
				break;
			}
		}

		if (postojiPresjekStatusa) {
			System.out.println("Sadrzana relacija ima vise razlicitih statusa unutar sebe.");
		}

		if (!postojiPresjekStatusa && nadenaPolazna && nadenaOdredisna) {
			RelacijaSmjer relacija = new RelacijaSmjer();
			relacija.relacija = trazenaRelacija;
			relacija.smjer = smjer;
			return relacija;
		} else {
			return null;
		}
	}

	private List<Pruga> dohvatiPrugePoOznaci(String oznakaPruge) {
		List<Pruga> filtriranePruge = new ArrayList<>();
		for (Pruga p : TvrtkaZaPrijevoz.getPopisPruga()) {
			if (p.oznaka.equals(oznakaPruge)) {
				filtriranePruge.add(p);
			}
		}
		return filtriranePruge;
	}

	public void ispisRelacijaSaStatusom(String komanda) {
		if (!provjeriBrojArgumenataKomande(komanda, 1) && !provjeriBrojArgumenataKomande(komanda, 2)) {
			System.out.println("Nepravilna sintaksa komande! (Sintaksa:IRPS status [oznaka]");
			return;
		}
		String djelovi[] = komanda.split(" ");

		String status = djelovi[1].trim();
		if (!status.matches("^(I|K|T|Z)$")) {
			System.out.println("Odabrani status nije valjan.");
			return;
		}

		if (djelovi.length == 3) {
			Pruga pruga = TvrtkaZaPrijevoz.getPruga(djelovi[2].trim());
			if (pruga == null) {
				System.out.println("Pruga s tim oznakom ne postoji.");
				return;
			}
		}

		if (djelovi.length == 2) {
			ispisiRelacijeSaStatusimaNormalno(status, "SVE");
			ispisiRelacijeSaStatusimaObrnuto(status, "SVE");
		} else if (djelovi.length == 3) {
			ispisiRelacijeSaStatusimaNormalno(status, djelovi[2].trim());
			ispisiRelacijeSaStatusimaObrnuto(status, djelovi[2].trim());
		}
	}

	private void ispisiRelacijeSaStatusimaNormalno(String status, String trazenaPruga) {
		String prvaStanicaRelacije = "";
		String zadnjaStanicaRelacije = "";
		String trenutnaEtapa = "";
		String trenutnoStanje = "";
		String trenutnoStanjeIspis = "";
		boolean prvaRelacija = true;
		System.out.println("Relacije normalni redosljed:"); // TEST

		for (Pruga p : TvrtkaZaPrijevoz.getPopisPruga()) {
			if (!p.oznaka.equals(trenutnaEtapa) || !p.dajStanjePrugeSkraceno("N").equals(trenutnoStanje)) {
				if (trenutnoStanje.equals(status) && prvaRelacija == false
						&& (trazenaPruga.equals("SVE") || trenutnaEtapa.equals(trazenaPruga))) {
					System.out.println(trenutnaEtapa + " " + prvaStanicaRelacije + " " + zadnjaStanicaRelacije + " "
							+ trenutnoStanjeIspis);
				}
				prvaRelacija = false;
				prvaStanicaRelacije = p.pocetnaStanica.naziv;
			}
			trenutnoStanje = p.dajStanjePrugeSkraceno("N");
			trenutnoStanjeIspis = p.dajStanjePruge("N");
			trenutnaEtapa = p.oznaka;
			zadnjaStanicaRelacije = p.zavrsnaStanica.naziv;
		}
		if (trenutnoStanje.equals(status) && (trazenaPruga.equals("SVE") || trenutnaEtapa.equals(trazenaPruga))) {
			System.out.println(trenutnaEtapa + " " + prvaStanicaRelacije + " " + zadnjaStanicaRelacije + " "
					+ trenutnoStanjeIspis);
		}
	}

	private void ispisiRelacijeSaStatusimaObrnuto(String status, String trazenaPruga) {
		String prvaStanicaRelacije = "";
		String zadnjaStanicaRelacije = "";
		String trenutnaEtapa = "";
		String trenutnoStanje = "";
		String trenutnoStanjeIspis = "";
		boolean prvaRelacija = true;
		System.out.println("Relacije obrnuti redosljed:"); // TEST

		List<Pruga> popisPruga = TvrtkaZaPrijevoz.getPopisPruga().reversed();
		// Collections.reverse(popisPruga);
		for (Pruga p : popisPruga) {
			if (!p.oznaka.equals(trenutnaEtapa) || !p.dajStanjePrugeSkraceno("O").equals(trenutnoStanje)) {
				if (trenutnoStanje.equals(status) && prvaRelacija == false
						&& (trazenaPruga.equals("SVE") || trenutnaEtapa.equals(trazenaPruga))) {
					System.out.println(trenutnaEtapa + " " + prvaStanicaRelacije + " " + zadnjaStanicaRelacije + " "
							+ trenutnoStanjeIspis);
				}
				prvaRelacija = false;
				prvaStanicaRelacije = p.zavrsnaStanica.naziv;
			}
			trenutnoStanje = p.dajStanjePrugeSkraceno("O");
			trenutnoStanjeIspis = p.dajStanjePruge("O");
			trenutnaEtapa = p.oznaka;
			zadnjaStanicaRelacije = p.pocetnaStanica.naziv;
		}
		if (trenutnoStanje.equals(status) && (trazenaPruga.equals("SVE") || trenutnaEtapa.equals(trazenaPruga))) {
			System.out.println(trenutnaEtapa + " " + prvaStanicaRelacije + " " + zadnjaStanicaRelacije + " "
					+ trenutnoStanjeIspis);
		}
	}

	public void ispisiKupljeneKarte(String komanda) {
		if (!provjeriBrojArgumenataKomande(komanda, 0) && !provjeriBrojArgumenataKomande(komanda, 1)) {
			System.out.println("Nepravilna sintaksa komande! (Sintaksa:IKKPV [n]");
			return;
		}

		if (komanda.split(" ").length == 1) {
			List<Object> sveKarte = TvrtkaZaPrijevoz.getKartaCaretaker().dajSveKupljeneKarte();
			Karta k = new Karta();
			boolean nemaKarti = true;
			for (Object o : sveKarte) {
				nemaKarti = false;
				k.postaviMemento(o);
				k.ispisiKartu();
			}
			if (nemaKarti) {
				System.out.println("Nema kupljenih karti u sustavu.");
			}
		} else {
			try {
				int brojKarte = Integer.parseInt(komanda.split(" ")[1]);
				Object karta = TvrtkaZaPrijevoz.getKartaCaretaker().dajKartu(brojKarte);
				if (karta != null) {
					Karta k = new Karta();
					k.postaviMemento(karta);
					k.ispisiKartu();
				} else {
					System.out.println("Karta s tim brojem ne postoji.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Upisani broj nije vazec.");
			}
		}
	}

	public void kupiKartu(String komanda) {
		String djelovi[] = komanda.split("-");
		if (djelovi.length != 5) {
			System.out.println("Nepravilna sintaksa komande! (Sintaksa:KKPV2S oznaka - polaznaStanica - "
					+ "odredišnaStanica - datum - načinKupovine");
			return;
		}

		if (!postojiCjenik()) {
			System.out.println("Nije unesen cjenik!");
			return;
		}

		String oznakaVlaka = "";
		for (int i = 1; i <= djelovi[0].split(" ").length - 1; i++) {
			oznakaVlaka += komanda.split(" ")[i];
			oznakaVlaka += " ";
		}
		oznakaVlaka = oznakaVlaka.trim();
		Vlak vlak = TvrtkaZaPrijevoz.getVozniRed().getVlak(oznakaVlaka);
		if (vlak == null) {
			System.out.println("Vlak s tom oznakom ne postoji.");
			return;
		}

		String polaznaStanicaNaziv = djelovi[1].trim();
		Stanica polaznaStanica = TvrtkaZaPrijevoz.getStanica(polaznaStanicaNaziv);
		if (polaznaStanica == null) {
			System.out.println("Polazna stanica s tim nazivom ne postoji.");
			return;
		}

		String odredisnaStanicaNaziv = djelovi[2].trim();
		Stanica odredisnaStanica = TvrtkaZaPrijevoz.getStanica(odredisnaStanicaNaziv);
		if (odredisnaStanica == null) {
			System.out.println("Odredisna stanica s tim nazivom ne postoji.");
			return;
		}

		if (polaznaStanica.naziv.equals(odredisnaStanica.naziv)) {
			System.out.println("Pocetna i odredisna stanica su iste.");
			return;
		}

		String datum = djelovi[3].trim();
		if (!valjaniDatum(datum)) {
			System.out.println("Uneseni datum nije valjan.");
			return;
		}

		String nacinKupovine = djelovi[4].trim();
		if (!nacinKupovine.matches("^(WM|B|V)$")) {
			System.out.println("Odabrani nacin kupovine nije valjan.");
			return;
		}

		IzdavacKarte izdavacKarte = null;
		switch (nacinKupovine) {
		case "WM": {
			izdavacKarte = new IzdavacKarte(new KalkulatorWebMob());
			break;
		}
		case "B": {
			izdavacKarte = new IzdavacKarte(new KalkulatorBlagajna());
			break;
		}
		case "V": {
			izdavacKarte = new IzdavacKarte(new KalkulatorVlak());
			break;
		}
		}

		Karta novaKarta = izdavacKarte.izdajKartu(vlak, polaznaStanica, odredisnaStanica, datum);

		if (novaKarta != null) {
			// novaKarta.ispisiKartu(); TEST
		} else {
			System.out.println("Nova karta je null.");
		}
	}

	private boolean postojiCjenik() {
		return TvrtkaZaPrijevoz.getCjenik() != null;
	}

	public void definirajCijenePrijevoza(String komanda) {
		if (!provjeriBrojArgumenataKomande(komanda, 6)) {
			System.out.println("Nepravilna sintaksa komande! (Sintaksa:CVP cijenaNormalni cijenaUbrzani"
					+ " cijenaBrzi popustSuN popustWebMob uvecanjeVlak)");
			return;
		}

		Cjenik cjenik = new Cjenik();
		try {
			cjenik.normalniVlak = Float.parseFloat(komanda.split(" ")[1].trim().replace(",", "."));
			cjenik.ubrzaniVlak = Float.parseFloat(komanda.split(" ")[2].trim().replace(",", "."));
			cjenik.brziVlak = Float.parseFloat(komanda.split(" ")[3].trim().replace(",", "."));
			cjenik.popustVikend = Float.parseFloat(komanda.split(" ")[4].trim().replace(",", ".")) / 100;
			cjenik.popustWebMob = Float.parseFloat(komanda.split(" ")[5].trim().replace(",", ".")) / 100;
			cjenik.uvecanjeVlak = Float.parseFloat(komanda.split(" ")[6].trim().replace(",", ".")) / 100;
		} catch (Exception e) {
			System.out.println("Nisu unesene validne vrijednosti brojeva!");
		}
		TvrtkaZaPrijevoz.setCjenik(cjenik);
	}

	public void ukljuciRazglasVlaka(String komanda) {
		if (!provjeriBrojArgumenataKomande(komanda, 2)) {
			System.out.println("Nepravilna sintaksa komande! (Sintaksa:URV oznakaVlaka dan)");
			return;
		}
		IRazglasMediator razglasMediator = new RazglasMediator();
		String oznakaVlaka = komanda.split(" ")[1].trim();
		Vlak vlak = TvrtkaZaPrijevoz.getVozniRed().getVlak(oznakaVlaka);
		if (vlak == null) {
			System.out.println("Vlak s tom oznakom ne postoji.");
			return;
		}
		razglasMediator.pretplatiRazlgasnikNaPratiocVlaka(oznakaVlaka, komanda.split(" ")[2].trim());
	}

	public void zapocniSimulaciju(String komanda) {
		String djelovi[] = komanda.split("-");
		if (djelovi.length != 3) {
			System.out.println("Nepravilna sintaksa komande! (Sintaksa:SVV oznaka - dan - koeficijent");
			return;
		}
		String oznakaVlaka = "";
		for (int i = 1; i <= djelovi[0].split(" ").length - 1; i++) {
			oznakaVlaka += komanda.split(" ")[i];
			oznakaVlaka += " ";
		}
		oznakaVlaka = oznakaVlaka.trim();
		Vlak vlak = TvrtkaZaPrijevoz.getVozniRed().getVlak(oznakaVlaka);
		if (vlak == null) {
			System.out.println("Vlak s tom oznakom ne postoji.");
			return;
		}
		String dan = djelovi[1].trim();

		int koeficijent = 1;
		try {
			koeficijent = Integer.parseInt(djelovi[2].trim());
		} catch (NumberFormatException e) {
			System.out.println("Koeficijent nije valjan.");
			return;
		}

		SimulacijaVlaka simulacija = new SimulacijaVlaka(vlak, dan, koeficijent);
		simulacija.zapocni();
	}

	public void dodajKorisnikaZaPracenje(String komanda) {
		String djelovi[] = komanda.split("-");
		if (djelovi.length > 3 || djelovi.length < 2) {
			System.out.println("Nepravilna sintaksa komande! (Sintaksa:DPK ime prezime - oznakaVlaka [- stanica])");
			return;
		}

		String prviDio[] = djelovi[0].split(" ");
		if (prviDio.length != 3) {
			System.out.println("Nepravilna sintaksa komande! (Sintaksa:DPK ime prezime - oznakaVlaka [- stanica])");
			return;
		}

		Korisnik korisnik = RegistarKorisnika.dajKorisnikaPoImenuIPrezimenu(prviDio[1], prviDio[2]);
		if (korisnik == null) {
			System.out.println("Ne postoji korisnik s tim imenom i prezimenom.");
			return;
		}

		Vlak vlak = TvrtkaZaPrijevoz.getVozniRed().getVlak(djelovi[1].trim());
		if (vlak == null) {
			System.out.println("Vlak s oznakom " + djelovi[1].trim() + " ne postoji.");
			return;
		}

		String dogadaj = "";
		dogadaj += djelovi[1].trim();

		if (djelovi.length == 3) {
			dogadaj += "-";
			dogadaj += djelovi[2].trim();
		}

		TvrtkaZaPrijevoz.getPratiocVlakova().dodajObserver(korisnik, dogadaj);
	}

	public void ispisiKorisnike(String komanda) {
		if (!provjeriBrojArgumenataKomande(komanda, 0)) {
			System.out.println("Nepravilna sintaksa komande! (Sintaksa:PK)");
			return;
		}
		System.out.printf("%-" + 30 + "s %-" + 30 + "s%n", "Ime", "Prezime");
		System.out.println("=".repeat(70));
		for (Korisnik k : RegistarKorisnika.getPopisKorisnika()) {
			System.out.printf("%-" + 30 + "s %-" + 30 + "s%n", k.getIme(), k.getPrezime());
		}
	}

	public void dodajKorisnika(String komanda) {
		if (!provjeriBrojArgumenataKomande(komanda, 2)) {
			System.out.println("Nepravilna sintaksa komande! (Sintaksa:DK ime prezima)");
			return;
		}
		Korisnik noviKorisnik = new Korisnik();
		noviKorisnik.setIme(komanda.split(" ")[1]);
		noviKorisnik.setPrezime(komanda.split(" ")[2]);
		RegistarKorisnika.dodajKorisnika(noviKorisnik);
	}

	public void pregledVoznogRedaVlaka(String komanda) {
		if (komanda.split(" ").length <= 1) {
			System.out.println("Nepravilna sintaksa komande! (Sintaksa:IVRV oznaka)");
			return;
		}
		String oznaka = "";
		for (int i = 1; i <= komanda.split(" ").length - 1; i++) {
			oznaka += komanda.split(" ")[i];
			oznaka += " ";
		}
		oznaka = oznaka.trim();

		IVRVVisitor visitor = new IVRVVisitor(true);
		Vlak vlak = TvrtkaZaPrijevoz.getVozniRed().getVlak(oznaka);
		if (vlak != null)
			vlak.accept(visitor);
		else {
			System.out.println("Vlak s tom oznakom ne postoji.");
		}
	}

	public void pregledVlakovaPoDanima(String komanda) {
		if (!provjeriBrojArgumenataKomande(komanda, 1)) {
			System.out.println("Nepravilna sintaksa komande! (Sintaksa:IEVD dani)");
			return;
		}

		if (!komanda.split(" ")[1].matches("^(?:(Po|U|Sr|Č|Pe|Su|N)(?!.*\\1))*$")) {
			System.out.println("Nepoznati dani.");
			return;
		}

		IEVDVisitor visitor = new IEVDVisitor(komanda.split(" ")[1], true);
		TvrtkaZaPrijevoz.getVozniRed().accept(visitor);
	}

	public void pregledVlakova(String komanda) {
		if (!provjeriBrojArgumenataKomande(komanda, 0)) {
			System.out.println("Nepravilna sintaksa komande! (Sintaksa:IV)");
			return;
		}
		IVVisitor visitor = new IVVisitor();
		TvrtkaZaPrijevoz.getVozniRed().accept(visitor);
	}

	public void pregledEtapaVlaka(String komanda) {
		if (komanda.split(" ").length <= 1) {
			System.out.println("Nepravilna sintaksa komande! (Sintaksa:IEV oznaka)");
			return;
		}
		String oznaka = "";
		for (int i = 1; i <= komanda.split(" ").length - 1; i++) {
			oznaka += komanda.split(" ")[i];
			oznaka += " ";
		}
		oznaka = oznaka.trim();

		IEVVisitor visitor = new IEVVisitor();
		Vlak vlak = TvrtkaZaPrijevoz.getVozniRed().getVlak(oznaka);
		if (vlak != null)
			vlak.accept(visitor);
		else {
			System.out.println("Vlak s tom oznakom ne postoji.");
		}
	}

	public void pregledStanicaIzmeduDvijeStanice(String komanda) {
		String nazivPocetneStanice = "";
		String nazivZavrsneStanice = "";

		if (komanda.split("-").length != 2) {
			System.out.println("Nepravilna sintaksa komande! (Sintaksa:ISI2S polazna Stanica - odredisnaStanica)");
			return;
		} else {
			nazivZavrsneStanice = komanda.split("-")[1].trim();
			String prviDio = komanda.split("-")[0].trim();
			int firstSpaceIndex = prviDio.indexOf(" ");
			nazivPocetneStanice = prviDio.substring(firstSpaceIndex + 1);
		}

		Stanica prva = TvrtkaZaPrijevoz.getStanica(nazivPocetneStanice);
		Stanica druga = TvrtkaZaPrijevoz.getStanica(nazivZavrsneStanice);

		if (prva == null) {
			System.out.println("Pocetna stanica " + nazivPocetneStanice + " ne postoji.");
			return;
		}
		if (druga == null) {
			System.out.println("Odredisna stanica " + nazivZavrsneStanice + " ne postoji.");
			return;
		}
		if (prva.naziv.equals(druga.naziv)) {
			System.out.println("Pocetna i odredisna stanica su iste.");
			return;
		}

		TraziteljPuta traziteljPuta = new TraziteljPuta();
		List<StanicaSUdaljenosti> najkraciPut = traziteljPuta.pronadiNajkraciPut(nazivPocetneStanice,
				nazivZavrsneStanice, TvrtkaZaPrijevoz.getPopisPruga());
		System.out.printf("%-" + 30 + "s %-" + 5 + "s %-" + 25 + "s%n", "Naziv stanice", "Vrsta",
				"Udaljenost od pocetne");
		System.out.println("=".repeat(70));
		for (StanicaSUdaljenosti s : najkraciPut) {
			System.out.printf("%-" + 30 + "s %-" + 5 + "s %-" + 25 + "s%n", s.stanica.naziv, s.stanica.vrstaStanice,
					s.udaljenost);
		}
	}

	public void pregledStanicaPruge(String komanda) {
		if (!provjeriBrojArgumenataKomande(komanda, 2)) {
			System.out.println("Nepravilna sintaksa komande! (Sintaksa:ISP oznakaPruge redoslijed)");
			return;
		}

		Pruga p = TvrtkaZaPrijevoz.getPruga(komanda.split(" ")[1].trim());
		if (p == null) {
			System.out.println("Ne postoji pruga " + komanda.split(" ")[1].trim());
			return;
		}

		switch (komanda.split(" ")[2]) {
		case "N": {
			ispisiStanicePrugeNormalno(komanda.split(" ")[1]);
			break;
		}
		case "O": {
			ispisiStanicePrugeObrnuto(komanda.split(" ")[1]);
			break;
		}
		default: {
			System.out.println("Nepoznat zadnji argument komande! (N ili O)");
		}
		}
	}

	private void ispisiStanicePrugeNormalno(String oznakaPruge) {
		List<Pruga> pruge = TvrtkaZaPrijevoz.getPopisPruga();
		System.out.printf("%-" + 30 + "s %-" + 5 + "s %-" + 25 + "s%n", "Naziv stanice", "Vrsta",
				"Udaljenost od pocetne");
		System.out.println("=".repeat(70));

		boolean prva = true;
		float udaljenostOdPocetne = 0;
		for (int i = 0; i < pruge.size() - 1; i++) {
			if (pruge.get(i).oznaka.equals(oznakaPruge)) {
				if (prva) {
					System.out.printf("%-" + 30 + "s %-" + 5 + "s %-" + 25 + "s%n", pruge.get(i).pocetnaStanica.naziv,
							pruge.get(i).pocetnaStanica.vrstaStanice, String.valueOf(0));
					prva = false;
				}
				System.out.printf("%-" + 30 + "s %-" + 5 + "s %-" + 25 + "s%n", pruge.get(i).zavrsnaStanica.naziv,
						pruge.get(i).zavrsnaStanica.vrstaStanice,
						pretvoriDuljinuUString(udaljenostOdPocetne += pruge.get(i).duljina));
			}
		}
	}

	private void ispisiStanicePrugeObrnuto(String oznakaPruge) {
		List<Pruga> pruge = TvrtkaZaPrijevoz.getPopisPruga();
		System.out.printf("%-" + 30 + "s %-" + 5 + "s %-" + 25 + "s%n", "Naziv stanice", "Vrsta",
				"Udaljenost od pocetne");
		System.out.println("=".repeat(70));

		boolean prva = true;
		float udaljenostOdPocetne = 0;
		for (int i = pruge.size() - 1; i >= 0; i--) {
			if (pruge.get(i).oznaka.equals(oznakaPruge)) {
				if (prva) {
					System.out.printf("%-" + 30 + "s %-" + 5 + "s %-" + 25 + "s%n", pruge.get(i).zavrsnaStanica.naziv,
							pruge.get(i).zavrsnaStanica.vrstaStanice, String.valueOf(0));
					prva = false;
				}
				System.out.printf("%-" + 30 + "s %-" + 5 + "s %-" + 25 + "s%n", pruge.get(i).pocetnaStanica.naziv,
						pruge.get(i).pocetnaStanica.vrstaStanice,
						pretvoriDuljinuUString(udaljenostOdPocetne += pruge.get(i).duljina));
			}
		}
	}

	public void pregledPruga(String komanda) {
		if (!provjeriBrojArgumenataKomande(komanda, 0)) {
			System.out.println("Nepravilna sintaksa komande! (Sintaksa:IP)");
			return;
		}
		System.out.printf("%-" + 15 + "s %-" + 30 + "s %-" + 30 + "s %-" + 5 + "s%n", "Oznaka", "Pocetna stanica",
				"Zavrsna stanica", "Duljina");
		System.out.println("=".repeat(90));

		String[] prugaIspis = new String[2];
		float duljinaPruge = 0;
		Pruga zadnjaPruga = null;
		for (Pruga pruga : TvrtkaZaPrijevoz.getPopisPruga()) {
			if (zadnjaPruga == null) {
				prugaIspis[0] = pruga.oznaka;
				prugaIspis[1] = pruga.pocetnaStanica.naziv;
				duljinaPruge = pruga.duljina;
				zadnjaPruga = pruga;
				continue;
			}
			if (!pruga.oznaka.equals(zadnjaPruga.oznaka)) {
				System.out.printf("%-" + 15 + "s %-" + 30 + "s %-" + 30 + "s %-" + 5 + "s%n", prugaIspis[0],
						prugaIspis[1], zadnjaPruga.zavrsnaStanica.naziv, pretvoriDuljinuUString(duljinaPruge));
				prugaIspis[0] = pruga.oznaka;
				prugaIspis[1] = pruga.pocetnaStanica.naziv;
				duljinaPruge = pruga.duljina;
			} else {
				duljinaPruge += pruga.duljina;
			}
			zadnjaPruga = pruga;
		}
		System.out.printf("%-" + 15 + "s %-" + 30 + "s %-" + 30 + "s %-" + 5 + "s%n", prugaIspis[0], prugaIspis[1],
				zadnjaPruga.zavrsnaStanica.naziv, pretvoriDuljinuUString(duljinaPruge));
	}

	public void pregledKompozicije(String komanda) {
		if (!provjeriBrojArgumenataKomande(komanda, 1)) {
			System.out.println("Nepravilna sintaksa komande! (Sintaksa:IK oznaka)");
			return;
		}
		System.out.printf(
				"%-" + 15 + "s %-" + 5 + "s %-" + 65 + "s %-" + 10 + "s %-" + 10 + "s %-" + 15 + "s %-" + 10 + "s%n",
				"Oznaka", "Uloga", "Opis", "Godina", "Namjena", "Vrsta pogona", "Max brzina");
		System.out.println("=".repeat(140));
		boolean postoji = false;
		for (Kompozicija kompozicija : TvrtkaZaPrijevoz.getPopisKompozicija()) {
			if (kompozicija.getOznaka().equals(komanda.split(" ")[1])) {
				postoji = true;
				ispisiKompoziciju(kompozicija);
			}
		}
		if (!postoji) {
			System.out.println("Kompozicija s tom oznakom ne postoji.");
		}
	}

	private boolean valjaniDatum(String datumString) {
		boolean valjan = true;
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
		try {
			LocalDate.parse(datumString, format);
		} catch (DateTimeParseException e) {
			valjan = false;
		}
		return valjan;
	}

	private void ispisiKompoziciju(Kompozicija kompozicija) {
		for (AbstractMap.SimpleEntry<String, PrijevoznoSredstvo> vozilo : kompozicija.getListaVozila()) {
			PrijevoznoSredstvo ps = vozilo.getValue();
			System.out.printf(
					"%-" + 15 + "s %-" + 5 + "s %-" + 65 + "s %-" + 10 + "s %-" + 10 + "s %-" + 15 + "s %-" + 10
							+ "s%n",
					ps.getOznaka(), vozilo.getKey(), ps.getOpis(), ps.getGodina(), ps.getNamjena(), ps.getVrstaPogona(),
					ps.getMaxBrzina());
		}
	}

	private String pretvoriDuljinuUString(float ukupnaDuljina) {
		String duljinaString;

		if (ukupnaDuljina == (int) ukupnaDuljina) {
			duljinaString = String.valueOf((int) ukupnaDuljina);
		} else {
			duljinaString = String.valueOf(ukupnaDuljina);
		}
		return duljinaString;
	}

	private boolean provjeriBrojArgumenataKomande(String komanda, int ocekivaniBrojArgumenata) {
		return komanda.split(" ").length - 1 == ocekivaniBrojArgumenata;
	}
}
