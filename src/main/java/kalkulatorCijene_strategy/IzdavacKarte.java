package kalkulatorCijene_strategy;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import karta_memento.Karta;
import kvardijan20_zadaca_3.TraziteljPuta;
import podatkovneKlase.Cjenik;
import podatkovneKlase.PodaciIEVD;
import podatkovneKlase.PodaciIVRV;
import podatkovneKlase.PodaciUKP2S;
import podatkovneKlase.StanicaSUdaljenosti;
import stanica_prototype.Stanica;
import tvrtkaZaPrijevoz_singleton.TvrtkaZaPrijevoz;
import vozniRed_composite.Vlak;
import vozniRed_visitor.IEVDVisitor;
import vozniRed_visitor.IVRVVisitor;

public class IzdavacKarte {
	private Cjenik cjenik;
	private Karta karta;
	private IKalkulatorCijene kalkulatorCijene;

	public IzdavacKarte(IKalkulatorCijene k) {
		this.kalkulatorCijene = k;
	}

	public void usporediKarte(Stanica polaznaStanica, Stanica odredisnaStanica, String datum, String vrijemeOd,
			String vrijemeDo) {
		IEVDVisitor visitorIEVD = new IEVDVisitor(dajDanIzDatuma(datum), false);
		List<PodaciIEVD> vlakoviZaDan = new ArrayList<>();

		TvrtkaZaPrijevoz.getVozniRed().accept(visitorIEVD);
		vlakoviZaDan = visitorIEVD.dajPodatkeOVlakovimaZaDane();

		List<PodaciIEVD> filtriraniVlakoviZaDan = new ArrayList<>();
		filtriraniVlakoviZaDan = filtrirajZaVremenskiPeriod(vlakoviZaDan, vrijemeOd, vrijemeDo);

		TraziteljPuta traziteljPuta = new TraziteljPuta();
		List<StanicaSUdaljenosti> najkraciPut = traziteljPuta.pronadiNajkraciPut(polaznaStanica.naziv,
				odredisnaStanica.naziv, TvrtkaZaPrijevoz.getPopisPruga());

		List<PodaciUKP2S> sviPodaci = napraviSvePodatke(filtriraniVlakoviZaDan);

		testispis(sviPodaci);
		// TODO: TU NAPRAVI REKURZIJU
	}

	private void testispis(List<PodaciUKP2S> sviPodaci) {
		for (PodaciUKP2S p : sviPodaci) {
			System.out.println(p.oznakaVlaka + " " + p.oznakaPruge + " " + p.polazna + " " + p.odredisna + " "
					+ p.vrijemePolaska + " " + p.vrijemeDolaska + " " + p.daniUTjednu);
			for (Stanica s : p.listaStanica) {
				System.out.println("-----" + s.naziv);
			}
		}
	}

	private List<PodaciUKP2S> napraviSvePodatke(List<PodaciIEVD> filtriraniVlakoviZaDan) {
		List<PodaciUKP2S> sviPodaci = new ArrayList<>();
		IVRVVisitor visitorIVRV;

		for (PodaciIEVD p : filtriraniVlakoviZaDan) {
			if (!etapaVlakaPostoji(p, sviPodaci)) {
				Vlak vlak = TvrtkaZaPrijevoz.getVozniRed().getVlak(p.oznakaVlaka);
				visitorIVRV = new IVRVVisitor(false);
				vlak.accept(visitorIVRV);
				List<PodaciIVRV> vozniRed = visitorIVRV.dajPodatkeVoznogRedaVlaka();
				sviPodaci.add(spojiPodatke(p, vozniRed));
			}
		}
		return sviPodaci;
	}

	private PodaciUKP2S spojiPodatke(PodaciIEVD podaciIEVD, List<PodaciIVRV> vozniRed) {
		PodaciUKP2S spoj = new PodaciUKP2S();

		spoj.daniUTjednu = podaciIEVD.daniUTjednu;
		spoj.odredisna = podaciIEVD.odredisna;
		spoj.oznakaPruge = podaciIEVD.oznakaPruge;
		spoj.oznakaVlaka = podaciIEVD.oznakaVlaka;
		spoj.polazna = podaciIEVD.polazna;
		spoj.vrijemeDolaska = podaciIEVD.vrijemeDolaska;
		spoj.vrijemePolaska = podaciIEVD.vrijemePolaska;
		for (PodaciIVRV p : vozniRed) {
			if (podaciIEVD.oznakaPruge.equals(p.oznakaPruge)) {
				spoj.listaStanica.add(TvrtkaZaPrijevoz.getStanica(p.stanica));
			}
		}
		return spoj;
	}

	private boolean etapaVlakaPostoji(PodaciIEVD p, List<PodaciUKP2S> sviPodaci) {
		boolean postoji = false;
		for (PodaciUKP2S po : sviPodaci) {
			if (po.oznakaVlaka.equals(p.oznakaVlaka) && po.oznakaPruge.equals(p.oznakaPruge)) {
				postoji = true;
				break;
			}
		}
		return postoji;
	}

	private List<PodaciIEVD> filtrirajZaVremenskiPeriod(List<PodaciIEVD> vlakoviZaDan, String vrijemeOd,
			String vrijemeDo) {
		List<PodaciIEVD> filtriraniPodaciZaDan = new ArrayList<>();

		String normaliziranoVrijemeOd = normalizirajVrijeme(vrijemeOd);
		String normaliziranoVrijemeDo = normalizirajVrijeme(vrijemeDo);
		LocalTime fromTime = LocalTime.parse(normaliziranoVrijemeOd);
		LocalTime toTime = LocalTime.parse(normaliziranoVrijemeDo);

		for (PodaciIEVD p : vlakoviZaDan) {
			String normalizedPodaciOd = normalizirajVrijeme(p.vrijemePolaska);
			String normalizedPodaciDo = normalizirajVrijeme(p.vrijemeDolaska);
			LocalTime podaciOd = LocalTime.parse(normalizedPodaciOd);
			LocalTime podaciDo = LocalTime.parse(normalizedPodaciDo);
			if (!podaciOd.isBefore(fromTime) && !podaciDo.isAfter(toTime)) {
				filtriraniPodaciZaDan.add(p);
			}
		}
		return filtriraniPodaciZaDan;
	}

	private String normalizirajVrijeme(String vrijeme) {
		String[] djelovi = vrijeme.split(":");
		String sati = djelovi[0].length() == 1 ? "0" + djelovi[0] : djelovi[0];
		String minute = djelovi[1].length() == 1 ? "0" + djelovi[1] : djelovi[1];
		return sati + ":" + minute;
	}

	public Karta izdajKartu(Vlak vlak, Stanica polaznaStanica, Stanica odredisnaStanica, String datum) {
		this.karta = new Karta();
		this.dohvatiCjenik();
		float cijenaKarte = 0;
		float modifikacijaCijene = 0;

		try {
			cijenaKarte = this.izracunajBazicnuCijenu(vlak, polaznaStanica, odredisnaStanica, datum);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}

		karta.setIzvornaCijena(Math.round(cijenaKarte * 100.0f) / 100.0f);
		modifikacijaCijene += this.primijeniPopustVikend(datum);
		karta.setPopustVikend(modifikacijaCijene);
		cijenaKarte = this.kalkulatorCijene.izracunajCijenu(cijenaKarte, modifikacijaCijene, karta);

		karta.setOznakaVlaka(vlak.getOznaka());
		karta.setVrstaVlaka(vlak.getVrsta());
		karta.setPolaznaStanica(polaznaStanica.naziv);
		karta.setOdredisnaStanica(odredisnaStanica.naziv);
		karta.setDatumPutovanja(datum);
		karta.setKonacnaCijena(cijenaKarte);
		LocalDateTime sada = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
		karta.setVrijemeIzdavanja(sada.format(format));

		Object memento = karta.napraviMemento();
		TvrtkaZaPrijevoz.getKartaCaretaker().pospremiMemento(memento);
		return karta;
	}

	private float izracunajBazicnuCijenu(Vlak vlak, Stanica polaznaStanica, Stanica odredisnaStanica, String datum)
			throws Exception {
		IVRVVisitor visitorIVRV = new IVRVVisitor(false);
		IEVDVisitor visitorIEVD = new IEVDVisitor(dajDanIzDatuma(datum), false);
		List<PodaciIVRV> kompletniVozniRedVlaka = new ArrayList<>();
		List<PodaciIEVD> vlakoviZaDan = new ArrayList<>();
		List<PodaciIVRV> vozniRed = new ArrayList<>();

		vlak.accept(visitorIVRV);
		kompletniVozniRedVlaka = visitorIVRV.dajPodatkeVoznogRedaVlaka();

		TvrtkaZaPrijevoz.getVozniRed().accept(visitorIEVD);
		vlakoviZaDan = visitorIEVD.dajPodatkeOVlakovimaZaDane();

		vozniRed = napraviTrazeniVozniRed(kompletniVozniRedVlaka, vlakoviZaDan, vlak.getOznaka());
		if (vozniRed.isEmpty()) {
			throw new Exception("Vlak ne vozi na taj dan.");
		}

		if (!provjeriLogicnostStanica(vozniRed, polaznaStanica, odredisnaStanica)) {
			throw new Exception("Redoslijed stanica nije logicno poslozen.");
		}

		int udaljenost = this.izracunajUdaljenost(vozniRed, polaznaStanica, odredisnaStanica);
		switch (vlak.getVrsta()) {
		case "N": {
			return udaljenost * this.cjenik.normalniVlak;
		}
		case "U": {
			return udaljenost * this.cjenik.ubrzaniVlak;
		}
		case "B": {
			return udaljenost * this.cjenik.brziVlak;
		}
		default: {
			throw new Exception("Nepoznata vrsta vlaka.");
		}
		}
	}

	private List<PodaciIVRV> napraviTrazeniVozniRed(List<PodaciIVRV> kompletniVozniRedVlaka,
			List<PodaciIEVD> vlakoviZaDan, String oznakaVlaka) {
		List<PodaciIVRV> vozniRed = new ArrayList<>();
		String trenutnaOznakaPruge = "";
		for (PodaciIVRV podaciIVRV : kompletniVozniRedVlaka) {
			trenutnaOznakaPruge = podaciIVRV.oznakaPruge;
			for (PodaciIEVD podaciIEVD : vlakoviZaDan) {
				if (podaciIEVD.oznakaVlaka.equals(oznakaVlaka) && podaciIEVD.oznakaPruge.equals(trenutnaOznakaPruge)) {
					vozniRed.add(podaciIVRV);
				}
			}
		}
		return vozniRed;
	}

	private String dajDanIzDatuma(String datumString) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
		try {
			LocalDate datum = LocalDate.parse(datumString, format);
			DayOfWeek danUTjednu = datum.getDayOfWeek();
			switch (danUTjednu) {
			case MONDAY: {
				return "Po";
			}
			case TUESDAY: {
				return "U";
			}
			case WEDNESDAY: {
				return "Sr";
			}
			case THURSDAY: {
				return "Č";
			}
			case FRIDAY: {
				return "Pe";
			}
			case SATURDAY: {
				return "Su";
			}
			case SUNDAY: {
				return "N";
			}
			}
		} catch (DateTimeParseException e) {
		}
		return "";
	}

	private int izracunajUdaljenost(List<PodaciIVRV> vozniRed, Stanica polaznaStanica, Stanica odredisnaStanica) {
		int udaljenost = 0;
		int kmPocetna = 0;
		for (PodaciIVRV p : vozniRed) {
			if (p.stanica.equals(polaznaStanica.naziv)) {
				karta.setVrijemePolaska(p.vrijemePolaska);
				kmPocetna = p.brojKmOdPolazne;
				continue;
			}
			if (p.stanica.equals(odredisnaStanica.naziv)) {
				udaljenost = p.brojKmOdPolazne - kmPocetna;
				karta.setVrijemeDolaska(p.vrijemePolaska);
				break;
			}
		}
		return udaljenost;
	}

	private boolean provjeriLogicnostStanica(List<PodaciIVRV> vozniRed, Stanica polaznaStanica,
			Stanica odredisnaStanica) {
		int indeksPocetna = -1;
		int indeksOdredisna = -1;
		boolean logicne = false;

		for (int i = 0; i < vozniRed.size(); i++) {
			if (vozniRed.get(i).stanica.equals(polaznaStanica.naziv)) {
				indeksPocetna = i;
			}
			if (vozniRed.get(i).stanica.equals(odredisnaStanica.naziv)) {
				indeksOdredisna = i;
			}
		}
		if (indeksPocetna != -1 && indeksOdredisna != -1 && indeksPocetna < indeksOdredisna) {
			logicne = true;
		} else {
			if (indeksPocetna == -1) {
				System.out.println("Pocetnom stanicom se ne prolazi na taj dan.");
			}
			if (indeksOdredisna == -1) {
				System.out.println("Odredisnom stanicom se ne prolazi na taj dan.");
			}
			if (indeksPocetna >= indeksOdredisna) {
				System.out.println("Unesena odredisna stanica se nalazi prije unesene pocetne na pruzi.");
			}
		}
		return logicne;
	}

	private void dohvatiCjenik() {
		this.cjenik = TvrtkaZaPrijevoz.getCjenik();
	}

	private float primijeniPopustVikend(String datumString) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
		try {
			LocalDate datum = LocalDate.parse(datumString, format);
			DayOfWeek danUTjednu = datum.getDayOfWeek();
			if (danUTjednu == DayOfWeek.SATURDAY || danUTjednu == DayOfWeek.SUNDAY) {
				return -this.cjenik.popustVikend;
			} else {
				return 0;
			}
		} catch (DateTimeParseException e) {
		}
		return 0;
	}
}
