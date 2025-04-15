package kvardijan20_zadaca_3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kompozicija_builder.Kompozicija;
import kompozicija_builder.KompozicijaBuilder;
import prijevoznoSredstvo_builder.PrijevoznoSredstvo;
import prijevoznoSredstvo_builder.PrijevoznoSredstvoBuilder;
import prijevoznoSredstvo_builder.PrijevoznoSredstvoDirector;
import pruga_state.Ispravna;
import pruga_state.Pruga;
import pruga_state.UKvaru;
import pruga_state.UTestiranju;
import pruga_state.Zatvorena;
import stanica_prototype.Stanica;
import tvrtkaZaPrijevoz_singleton.TvrtkaZaPrijevoz;
import vozniRed_composite.Etapa;
import vozniRed_composite.Vlak;

public class KreatorObjekata {
	KompozicijaBuilder kompozicijaBuilder;
	Stanica zadnjaProcitanaStanica = null;
	Pruga zadnjaUpisanaPruga = null;
	private String zadnjaOznakaPruge = "";
	private HashMap<String, Vlak> vlakovi = new HashMap<>();
	private CitacDatoteka citacDatoteka;
	private String statusProslePruge = "";

	public KreatorObjekata() {
		kompozicijaBuilder = null;
	}

	public void unesiStanicu(String[] stanicaCSV) {
		boolean vecPostoji = true;
		String nazivStanice = stanicaCSV[0];
		Stanica novaStanica = dajStanicuAkoPostoji(nazivStanice);

		if (novaStanica == null) {
			vecPostoji = false;
			novaStanica = kreirajObjektStanica(stanicaCSV);
			TvrtkaZaPrijevoz.setPopisStanica(novaStanica);
		}

		if (vecPostoji) {
			novaStanica = azurirajZastaviceStajalistaZaStanicu(stanicaCSV);
		}

		if (stanicaCSV[13].equals("0")) {
			this.zadnjaProcitanaStanica = novaStanica;
			this.zadnjaOznakaPruge = stanicaCSV[1];
			this.statusProslePruge = stanicaCSV[12];
			return;
		}

		if (this.zadnjaOznakaPruge.equals(stanicaCSV[1])) {
			kreirajPrugu(novaStanica, stanicaCSV);
			this.zadnjaProcitanaStanica = novaStanica;
			this.zadnjaOznakaPruge = stanicaCSV[1];
			this.statusProslePruge = stanicaCSV[12];
		}
	}

	private Stanica azurirajZastaviceStajalistaZaStanicu(String[] stanicaCSV) {
		for (Stanica s : TvrtkaZaPrijevoz.getPopisStanica()) {
			if (s.naziv.equals(stanicaCSV[0])) {
				if (stanicaCSV.length > 15) {
					if (!stanicaCSV[15].isEmpty()) {
						s.stajalisteUbrzani = true;
						azurirajZastavicuUbrzaniSvePruge(stanicaCSV[0]);
						if (Integer.parseInt(stanicaCSV[15]) > 0) {
							azurirajVrijemeUbrzaniProslePruge(stanicaCSV[15]);
						}
					}
				}
				if (stanicaCSV.length > 16) {
					if (!stanicaCSV[16].isEmpty()) {
						s.stajalisteBrzi = true;
						azurirajZastavicuBrziSvePruge(stanicaCSV[0]);
						if (Integer.parseInt(stanicaCSV[16]) > 0) {
							azurirajVrijemeBrziProslePruge(stanicaCSV[16]);
						}
					}
				}
				return s;
			}
		}
		return null;
	}

	private void azurirajZastavicuUbrzaniSvePruge(String nazivStanice) {
		for (Pruga p : TvrtkaZaPrijevoz.getPopisPruga()) {
			if (p.pocetnaStanica.naziv.equals(nazivStanice)) {
				p.pocetnaStanica.stajalisteUbrzani = true;
			}
			if (p.zavrsnaStanica.naziv.equals(nazivStanice)) {
				p.zavrsnaStanica.stajalisteUbrzani = true;
			}
		}
	}

	private void azurirajZastavicuBrziSvePruge(String nazivStanice) {
		for (Pruga p : TvrtkaZaPrijevoz.getPopisPruga()) {
			if (p.pocetnaStanica.naziv.equals(nazivStanice)) {
				p.pocetnaStanica.stajalisteBrzi = true;
			}
			if (p.zavrsnaStanica.naziv.equals(nazivStanice)) {
				p.zavrsnaStanica.stajalisteBrzi = true;
			}
		}
	}

	private void azurirajVrijemeBrziProslePruge(String vrijemeBrzi) {
		for (Pruga p : TvrtkaZaPrijevoz.getPopisPruga()) {
			if (p.pocetnaStanica.naziv.equals(this.zadnjaUpisanaPruga.pocetnaStanica.naziv)
					&& p.zavrsnaStanica.naziv.equals(this.zadnjaUpisanaPruga.zavrsnaStanica.naziv)) {
				p.vrijemeBrziVlak = vrijemeBrzi;
			}
		}
	}

	private void azurirajVrijemeUbrzaniProslePruge(String vrijemeUbrzani) {
		for (Pruga p : TvrtkaZaPrijevoz.getPopisPruga()) {
			if (p.pocetnaStanica.naziv.equals(this.zadnjaUpisanaPruga.pocetnaStanica.naziv)
					&& p.zavrsnaStanica.naziv.equals(this.zadnjaUpisanaPruga.zavrsnaStanica.naziv)) {
				p.vrijemeUbrzaniVlak = vrijemeUbrzani;
			}
		}
	}

	private void kreirajPrugu(Stanica novaStanica, String[] stanicaCSV) {
		Pruga pruga = new Pruga();
		pruga.oznaka = stanicaCSV[1];
		pruga.kategorija = stanicaCSV[6];
		pruga.vrsta = stanicaCSV[8];
		pruga.brojKolosjeka = Integer.parseInt(stanicaCSV[9]);
		pruga.dozvoljenoOpterecenjePoOsovini = formatirajStringUFloat(stanicaCSV[10]);
		pruga.dozvoljenoOpterecenjePoDuznomMetru = formatirajStringUFloat(stanicaCSV[11]);

		// pruga.status = stanicaCSV[12];

		String azurniStatus = dajPrioritetnijiStatus(stanicaCSV[12]);

		switch (azurniStatus) {
		case "K": {
			pruga.postaviStanjePruge(new UKvaru(), "N");
			pruga.postaviStanjePruge(new UKvaru(), "O");
			break;
		}
		case "T": {
			pruga.postaviStanjePruge(new UTestiranju(), "N");
			pruga.postaviStanjePruge(new UTestiranju(), "O");
			break;
		}
		case "Z": {
			pruga.postaviStanjePruge(new Zatvorena(), "N");
			pruga.postaviStanjePruge(new Zatvorena(), "O");
			break;
		}
		default: {
			pruga.postaviStanjePruge(new Ispravna(), "N");
			pruga.postaviStanjePruge(new Ispravna(), "O");
		}
		}

		pruga.duljina = formatirajStringUFloat(stanicaCSV[13]);
		pruga.pocetnaStanica = this.zadnjaProcitanaStanica;
		pruga.zavrsnaStanica = novaStanica;
		if (stanicaCSV.length > 14) {
			pruga.vrijemeNormalniVlak = stanicaCSV[14];
		} else {
			pruga.vrijemeNormalniVlak = "";
		}
		if (stanicaCSV.length > 15) {
			pruga.vrijemeUbrzaniVlak = stanicaCSV[15];
		} else {
			pruga.vrijemeUbrzaniVlak = "";
		}
		if (stanicaCSV.length > 16) {
			pruga.vrijemeBrziVlak = stanicaCSV[16];
		} else {
			pruga.vrijemeBrziVlak = "";
		}

		TvrtkaZaPrijevoz.setPopisPruga(pruga);
		this.zadnjaUpisanaPruga = pruga;
	}

	private String dajPrioritetnijiStatus(String status) {
		// I -> K -> T -> Z najjaca
		int jacinaProsle;
		int jacinaTrenutne;
		switch (this.statusProslePruge) {
		case "K": {
			jacinaProsle = 2;
			break;
		}
		case "T": {
			jacinaProsle = 3;
			break;
		}
		case "Z": {
			jacinaProsle = 4;
			break;
		}
		default: {
			jacinaProsle = 1;
		}
		}

		switch (status) {
		case "K": {
			jacinaTrenutne = 2;
			break;
		}
		case "T": {
			jacinaTrenutne = 3;
			break;
		}
		case "Z": {
			jacinaTrenutne = 4;
			break;
		}
		default: {
			jacinaTrenutne = 1;
		}
		}

		if (jacinaProsle > jacinaTrenutne) {
			return this.statusProslePruge;
		} else {
			return status;
		}
	}

	private Stanica kreirajObjektStanica(String[] stanicaCSV) {
		Stanica novaStanica = new Stanica();
		novaStanica.naziv = stanicaCSV[0];
		novaStanica.vrstaStanice = stanicaCSV[2];
		novaStanica.statusStanice = stanicaCSV[3];
		novaStanica.ulazIzlazPutnika = formatirajStringUBoolean(stanicaCSV[4]);
		novaStanica.ulazIzlazRobe = formatirajStringUBoolean(stanicaCSV[5]);
		novaStanica.brojPerona = Integer.parseInt(stanicaCSV[7]);

		if (stanicaCSV.length > 15) {
			if (!stanicaCSV[15].isEmpty()) {
				novaStanica.stajalisteUbrzani = true;
			}
		}
		if (stanicaCSV.length > 16) {
			if (!stanicaCSV[16].isEmpty()) {
				novaStanica.stajalisteBrzi = true;
			}
		}

		return novaStanica;
	}

	public void kreirajVozilo(String[] vozilo) {
		PrijevoznoSredstvo novoPrijevoznoSredstvo = new PrijevoznoSredstvo();
		PrijevoznoSredstvoBuilder builder = new PrijevoznoSredstvoBuilder();
		PrijevoznoSredstvoDirector director = new PrijevoznoSredstvoDirector(builder);
		String oznaka = vozilo[0];
		String opis = vozilo[1];
		String proizvodac = vozilo[2];
		int godina = Integer.parseInt(vozilo[3]);
		String namjena = vozilo[4];
		String vrstaPrijevoza = vozilo[5];
		String vrstaPogona = vozilo[6];
		int maxBrzina = Integer.parseInt(vozilo[7]);
		float maxSnaga = formatirajStringUFloat(vozilo[8]);
		int brojSjedecihMjesta = Integer.parseInt(vozilo[9]);
		int brojStajacihMjesta = Integer.parseInt(vozilo[10]);
		int brojBicikala = Integer.parseInt(vozilo[11]);
		int brojKreveta = Integer.parseInt(vozilo[12]);
		int brojAutomobila = Integer.parseInt(vozilo[13]);
		float nosivost = formatirajStringUFloat(vozilo[14]);
		float povrsina = formatirajStringUFloat(vozilo[15]);
		float zapremnina = formatirajStringUFloat(vozilo[16]);
		String status = vozilo[17];

		switch (vrstaPrijevoza) {
		case "N": {
			novoPrijevoznoSredstvo = director.napraviN(oznaka, opis, proizvodac, godina, namjena, vrstaPrijevoza,
					vrstaPogona, maxBrzina, maxSnaga, status);
			break;
		}
		case "P": {
			novoPrijevoznoSredstvo = director.napraviP(oznaka, opis, proizvodac, godina, namjena, vrstaPrijevoza,
					vrstaPogona, maxBrzina, brojSjedecihMjesta, brojStajacihMjesta, brojBicikala, status);
			break;
		}
		case "TA": {
			novoPrijevoznoSredstvo = director.napraviTA(oznaka, opis, proizvodac, godina, namjena, vrstaPrijevoza,
					vrstaPogona, maxBrzina, brojAutomobila, nosivost, povrsina, status);
			break;
		}
		case "TK": {
			novoPrijevoznoSredstvo = director.napraviTK(oznaka, opis, proizvodac, godina, namjena, vrstaPrijevoza,
					vrstaPogona, maxBrzina, nosivost, povrsina, status);
			break;
		}
		case "TRS": {
			novoPrijevoznoSredstvo = director.napraviTRS(oznaka, opis, proizvodac, godina, namjena, vrstaPrijevoza,
					vrstaPogona, maxBrzina, nosivost, povrsina, zapremnina, status);
			break;
		}
		case "TTS": {
			novoPrijevoznoSredstvo = director.napraviTTS(oznaka, opis, proizvodac, godina, namjena, vrstaPrijevoza,
					vrstaPogona, maxBrzina, nosivost, povrsina, zapremnina, status);
			break;
		}
		default: {
			novoPrijevoznoSredstvo = director.napravi(oznaka, opis, proizvodac, godina, namjena, vrstaPrijevoza,
					vrstaPogona, maxBrzina, maxSnaga, brojSjedecihMjesta, brojStajacihMjesta, brojBicikala, brojKreveta,
					brojAutomobila, nosivost, povrsina, zapremnina, status);
		}
		}
		TvrtkaZaPrijevoz.setPopisPrijevoznihSredstava(novoPrijevoznoSredstvo);
	}

	public void gradiKompoziciju(String[] dioKompozicija) {
		String oznakaKompozicije = dioKompozicija[0];
		String oznakaPrijevoznogSredstva = dioKompozicija[1];
		String ulogaPrijevoznogSredstva = dioKompozicija[2];
		PrijevoznoSredstvo prijevoznoSredstvo = dohvatiPrijevoznoSredstvoPoOznaci(oznakaPrijevoznogSredstva);

		if (prijevoznoSredstvo == null) {
			System.out.println(
					"Greska kod kreiranja kompozicije! Vozilo s oznakom " + oznakaPrijevoznogSredstva + " ne postoji.");
			return;
		}

		if (kompozicijaBuilder == null) {
			kompozicijaBuilder = new KompozicijaBuilder();
			kompozicijaBuilder.napraviKompoziciju(oznakaKompozicije);
		}

		if (!oznakaKompozicije.equals(kompozicijaBuilder.dajKompoziciju().getOznaka())) {
			spremiKompoziciju();

			kompozicijaBuilder = new KompozicijaBuilder();
			kompozicijaBuilder.napraviKompoziciju(oznakaKompozicije);
		}

		if (ulogaPrijevoznogSredstva.equals("P"))
			kompozicijaBuilder.dodajPogon(prijevoznoSredstvo);
		else
			kompozicijaBuilder.dodajVagon(prijevoznoSredstvo);
	}

	public Kompozicija spremiKompoziciju() {
		if (kompozicijaBuilder == null)
			kompozicijaBuilder = new KompozicijaBuilder();
		Kompozicija novaKompozicija = kompozicijaBuilder.dajKompoziciju();

		if (novaKompozicija == null)
			return null;

		if (novaKompozicija.isValidna()) {
			TvrtkaZaPrijevoz.setPopisKompozicija(novaKompozicija);
			return novaKompozicija;
		} else {
			this.citacDatoteka.povecajBrojGreskiKompozicije();

			var ispisGreske = new StringBuilder();
			ispisGreske.append("G: ").append(this.citacDatoteka.dajGlobalniBrojGreski()).append(" L: ")
					.append(this.citacDatoteka.dajBrojGreskiKompozicije()).append(" ").append("Kompozicija ")
					.append(novaKompozicija.getOznaka()).append(" nije validna.");
			System.out.println(ispisGreske.toString());
			return novaKompozicija;
		}

	}

	public void dodajVlakUComposite(String[] vozniRedCSV) {
		String oznakaPruge = vozniRedCSV[0];
		String pocetnaStanica = vozniRedCSV[2];
		String zavrsnaStanica = vozniRedCSV[3];
		List<Stanica> stanice = null;
		PodaciOStanicama podaci = null;

		if (vozniRedCSV[1].equals("N"))
			podaci = pronadiStaniceEtapeN(oznakaPruge, pocetnaStanica, zavrsnaStanica);
		else
			podaci = pronadiStaniceEtapeO(oznakaPruge, pocetnaStanica, zavrsnaStanica);

		String dani = "PoUSrČPeSuN";
		if (vozniRedCSV.length > 8) {
			if (!vozniRedCSV[8].isEmpty()) {
				dani = TvrtkaZaPrijevoz.getOznakeDana().get(vozniRedCSV[8]);
			}
		}

		stanice = podaci.stanice;
		Etapa etapa = new Etapa(oznakaPruge, vozniRedCSV[6], vozniRedCSV[7], dani, vozniRedCSV[1]);
		etapa.setZbrojTrajanjaPoStanicamaNvlak(podaci.vrijemeNvlak);
		etapa.setZbrojTrajanjaPoStanicamaUvlak(podaci.vrijemeUvlak);
		etapa.setZbrojTrajanjaPoStanicamaBvlak(podaci.vrijemeBvlak);
		etapa.setDuljina(podaci.duljina);
		for (Stanica s : stanice) {
			etapa.add(s);
		}

		String vrsta = "N";
		if (!vozniRedCSV[5].isEmpty())
			vrsta = vozniRedCSV[5];

		Vlak vlak = dajVlakAkoPostoji(vozniRedCSV[4]);

		if (vlak == null) {
			vlak = new Vlak(vozniRedCSV[4], vrsta);
			this.vlakovi.put(vozniRedCSV[4], vlak);
		} else {
			if (!vlak.getVrsta().equals(vrsta)) {
				vlak.setNelogicnost4();
			}
		}
		vlak.add(etapa);
	}

	public HashMap<String, Vlak> dajPopisVlakova() {
		return this.vlakovi;
	}

	public void spremiOznakuDana(String[] oznakeDanaCSV) {
		if (oznakeDanaCSV.length > 1)
			TvrtkaZaPrijevoz.setOznakeDana(oznakeDanaCSV[0], oznakeDanaCSV[1]);
		else
			TvrtkaZaPrijevoz.setOznakeDana(oznakeDanaCSV[0], "");
	}

	public void pridruziCitac(CitacDatoteka citac) {
		this.citacDatoteka = citac;
	}

	private Vlak dajVlakAkoPostoji(String oznaka) {
		Vlak vlak = null;
		vlak = this.vlakovi.get(oznaka);
		return vlak;
	}

	private PodaciOStanicama pronadiStaniceEtapeN(String oznakaPruge, String pocetnaStanica, String zavrsnaStanica) {
		List<Stanica> stanice = new ArrayList<>();
		boolean nadenaPrva = false;
		int vrijemeNvlak = 0;
		int vrijemeUvlak = 0;
		int vrijemeBvlak = 0;
		int duljinaEtape = 0;

		for (int i = 0; i <= TvrtkaZaPrijevoz.getPopisPruga().size() - 1; i++) {
			if (TvrtkaZaPrijevoz.getPopisPruga().get(i).oznaka.equals(oznakaPruge)) {
				if (nadenaPrva == false && pocetnaStanica.isEmpty()) {
					nadenaPrva = true;
					stanice.add(TvrtkaZaPrijevoz.getPopisPruga().get(i).pocetnaStanica);
				}

				if (nadenaPrva == false
						&& TvrtkaZaPrijevoz.getPopisPruga().get(i).pocetnaStanica.naziv.equals(pocetnaStanica)) {
					stanice.add(TvrtkaZaPrijevoz.getPopisPruga().get(i).pocetnaStanica);
					nadenaPrva = true;
				}

				if (nadenaPrva) {
					stanice.add(TvrtkaZaPrijevoz.getPopisPruga().get(i).zavrsnaStanica);
					duljinaEtape += TvrtkaZaPrijevoz.getPopisPruga().get(i).duljina;
					if (!TvrtkaZaPrijevoz.getPopisPruga().get(i).vrijemeNormalniVlak.isEmpty())
						vrijemeNvlak += Integer.parseInt(TvrtkaZaPrijevoz.getPopisPruga().get(i).vrijemeNormalniVlak);
					if (!TvrtkaZaPrijevoz.getPopisPruga().get(i).vrijemeUbrzaniVlak.isEmpty())
						vrijemeUvlak += Integer.parseInt(TvrtkaZaPrijevoz.getPopisPruga().get(i).vrijemeUbrzaniVlak);
					if (!TvrtkaZaPrijevoz.getPopisPruga().get(i).vrijemeBrziVlak.isEmpty())
						vrijemeBvlak += Integer.parseInt(TvrtkaZaPrijevoz.getPopisPruga().get(i).vrijemeBrziVlak);
				}
				if (TvrtkaZaPrijevoz.getPopisPruga().get(i).zavrsnaStanica.naziv.equals(zavrsnaStanica)) {
					break;
				}
			}
		}
		PodaciOStanicama podaci = new PodaciOStanicama(vrijemeNvlak, vrijemeUvlak, vrijemeBvlak, stanice, duljinaEtape);
		return podaci;
	}

	private PodaciOStanicama pronadiStaniceEtapeO(String oznakaPruge, String pocetnaStanica, String zavrsnaStanica) {
		List<Stanica> stanice = new ArrayList<>();
		boolean nadenaPrva = false;
		int vrijemeNvlak = 0;
		int vrijemeUvlak = 0;
		int vrijemeBvlak = 0;
		int duljinaEtape = 0;

		for (int i = TvrtkaZaPrijevoz.getPopisPruga().size() - 1; i >= 0; i--) {
			if (TvrtkaZaPrijevoz.getPopisPruga().get(i).oznaka.equals(oznakaPruge)) {
				if (nadenaPrva == false && pocetnaStanica.isEmpty()) {
					nadenaPrva = true;
					stanice.add(TvrtkaZaPrijevoz.getPopisPruga().get(i).zavrsnaStanica);
				}

				if (nadenaPrva == false
						&& TvrtkaZaPrijevoz.getPopisPruga().get(i).zavrsnaStanica.naziv.equals(pocetnaStanica)) {
					stanice.add(TvrtkaZaPrijevoz.getPopisPruga().get(i).zavrsnaStanica);
					nadenaPrva = true;
				}

				if (nadenaPrva) {
					stanice.add(TvrtkaZaPrijevoz.getPopisPruga().get(i).pocetnaStanica);
					duljinaEtape += TvrtkaZaPrijevoz.getPopisPruga().get(i).duljina;
					if (!TvrtkaZaPrijevoz.getPopisPruga().get(i).vrijemeNormalniVlak.isEmpty())
						vrijemeNvlak += Integer.parseInt(TvrtkaZaPrijevoz.getPopisPruga().get(i).vrijemeNormalniVlak);
					if (!TvrtkaZaPrijevoz.getPopisPruga().get(i).vrijemeUbrzaniVlak.isEmpty())
						vrijemeUvlak += Integer.parseInt(TvrtkaZaPrijevoz.getPopisPruga().get(i).vrijemeUbrzaniVlak);
					if (!TvrtkaZaPrijevoz.getPopisPruga().get(i).vrijemeBrziVlak.isEmpty())
						vrijemeBvlak += Integer.parseInt(TvrtkaZaPrijevoz.getPopisPruga().get(i).vrijemeBrziVlak);
				}
				if (TvrtkaZaPrijevoz.getPopisPruga().get(i).pocetnaStanica.naziv.equals(zavrsnaStanica)) {
					break;
				}
			}
		}
		PodaciOStanicama podaci = new PodaciOStanicama(vrijemeNvlak, vrijemeUvlak, vrijemeBvlak, stanice, duljinaEtape);
		return podaci;
	}

	private PrijevoznoSredstvo dohvatiPrijevoznoSredstvoPoOznaci(String oznaka) {
		PrijevoznoSredstvo prijevoznoSredstvo = null;
		for (PrijevoznoSredstvo ps : TvrtkaZaPrijevoz.getPopisPrijevoznihSredstava()) {
			if (ps.getOznaka().equals(oznaka))
				prijevoznoSredstvo = ps;
		}
		return prijevoznoSredstvo;
	}

	private float formatirajStringUFloat(String stringBroja) {
		float broj = 0;
		String normaliziraniStringBroja = stringBroja.replace(",", ".");
		broj = Float.parseFloat(normaliziraniStringBroja);
		return broj;
	}

	private boolean formatirajStringUBoolean(String istina) {
		if (istina.equals("DA"))
			return true;
		return false;
	}

	private Stanica dajStanicuAkoPostoji(String nazivStanice) {
		Stanica stanica = null;
		for (Stanica s : TvrtkaZaPrijevoz.getPopisStanica()) {
			if (s.naziv.equals(nazivStanice)) {
				stanica = s.clone();
				break;
			}
		}
		return stanica;
	}

	record PodaciOStanicama(int vrijemeNvlak, int vrijemeUvlak, int vrijemeBvlak, List<Stanica> stanice, int duljina) {
	}
}
