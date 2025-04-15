package kvardijan20_zadaca_3;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import podatkovneKlase.PodaciIEVD;
import podatkovneKlase.PodaciIVRV;
import stanica_prototype.Stanica;
import tvrtkaZaPrijevoz_singleton.TvrtkaZaPrijevoz;
import vozniRed_composite.Vlak;
import vozniRed_visitor.IEVDVisitor;
import vozniRed_visitor.IVRVVisitor;

public class SimulacijaVlaka {
	String trenutnoVrijeme = "";
	private Vlak vlak;
	String oznakaVlaka = "";
	String dan = "";
	int koeficijent = 1;
	private List<PodaciIVRV> vozniRed = new ArrayList<>();
	private List<PodaciIVRV> kompletniVozniRedVlaka = new ArrayList<>();
	private List<PodaciIEVD> vlakoviZaDan = new ArrayList<>();
	boolean simulacijaRadi;

	public SimulacijaVlaka(Vlak vlak, String dan, int koeficijent) {
		this.vlak = vlak;
		this.oznakaVlaka = vlak.getOznaka();
		this.dan = dan;
		this.koeficijent = koeficijent;

		IVRVVisitor visitorIVRV = new IVRVVisitor(false);
		this.vlak.accept(visitorIVRV);
		this.kompletniVozniRedVlaka = visitorIVRV.dajPodatkeVoznogRedaVlaka();

		IEVDVisitor visitorIEVD = new IEVDVisitor(dan, false);
		TvrtkaZaPrijevoz.getVozniRed().accept(visitorIEVD);
		this.vlakoviZaDan = visitorIEVD.dajPodatkeOVlakovimaZaDane();

		napraviTrazeniVozniRed();
	}

	public void zapocni() {
		this.simulacijaRadi = true;
		if (this.vozniRed.isEmpty()) {
			System.out.println("Ovaj vlak ne vozi ni jednu etapu na taj dan.");
			return;
		}
		this.trenutnoVrijeme = this.vozniRed.getFirst().vrijemePolaska;
		String zadnjaStanica = "";

		System.out.printf("%-" + 6 + "s %-" + 25 + "s %-" + 25 + "s%n", "Pruga", "Stanica", "Vrijeme");
		System.out.println("=".repeat(60));

		for (PodaciIVRV trenutnoStajaliste : this.vozniRed) {
			while (this.simulacijaRadi) {
				if (!provjeriVrijemeVlaka(trenutnoStajaliste.vrijemePolaska)) {
					if (!zadnjaStanica.equals(trenutnoStajaliste.stanica)) {
						System.out.printf("%-" + 6 + "s %-" + 25 + "s %-" + 25 + "s%n", trenutnoStajaliste.oznakaPruge,
								trenutnoStajaliste.stanica, this.trenutnoVrijeme);

						Stanica s = TvrtkaZaPrijevoz.getStanica(trenutnoStajaliste.stanica);
						TvrtkaZaPrijevoz.getUkrcajInvoker().izvrsiUkrcaj(s, this.trenutnoVrijeme);

						this.vlakNaNovojStanici(trenutnoStajaliste);
						this.vlakNaSpecificnojStanici(trenutnoStajaliste);
						zadnjaStanica = trenutnoStajaliste.stanica;
					}
					break;
				}
				iducaMinuta();
				provjeriKorisnickiInput();
			}
		}
		System.out.println("Simulacija gotova!");
	}

	public void vlakNaNovojStanici(PodaciIVRV podaciIVRV) {
		TvrtkaZaPrijevoz.getPratiocVlakova().obavjestiObservereDogadaja(this.oznakaVlaka, podaciIVRV);
	}

	public void vlakNaSpecificnojStanici(PodaciIVRV podaciIVRV) {
		TvrtkaZaPrijevoz.getPratiocVlakova().obavjestiObservereDogadaja(this.oznakaVlaka + "-" + podaciIVRV.stanica,
				podaciIVRV);
	}

	private void napraviTrazeniVozniRed() {
		String trenutnaOznakaPruge = "";
		for (PodaciIVRV podaciIVRV : this.kompletniVozniRedVlaka) {
			trenutnaOznakaPruge = podaciIVRV.oznakaPruge;
			for (PodaciIEVD podaciIEVD : this.vlakoviZaDan) {
				if (podaciIEVD.oznakaVlaka.equals(this.oznakaVlaka)
						&& podaciIEVD.oznakaPruge.equals(trenutnaOznakaPruge)) {
					this.vozniRed.add(podaciIVRV);
				}
			}
		}
	}

	private void provjeriKorisnickiInput() {
		try {
			if (System.in.available() > 0) {
				Scanner in = new Scanner(System.in);
				String uneseno = in.nextLine();
				if (uneseno.trim().equalsIgnoreCase("X")) {
					this.simulacijaRadi = false;
					System.out.println("Ručna zaustavljanje simulacije...");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void iducaMinuta() {
		int vrijemeSpavanja = 60000 / this.koeficijent;
		try {
			Thread.sleep(vrijemeSpavanja);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		dodajVirtualnuMinutu();
	}

	private void dodajVirtualnuMinutu() {
		String nadodano = "0:01";
		DateTimeFormatter format = DateTimeFormatter.ofPattern("H:mm");
		LocalTime pocetno = LocalTime.parse(this.trenutnoVrijeme, format);
		LocalTime trajanje = LocalTime.parse(nadodano, format);
		Duration d1 = Duration.between(LocalTime.MIN, pocetno);
		Duration d2 = Duration.between(LocalTime.MIN, trajanje);
		Duration ukd = d1.plus(d2);
		LocalTime zbrojeno = LocalTime.MIN.plus(ukd);
		this.trenutnoVrijeme = zbrojeno.format(format);
	}

	private boolean provjeriVrijemeVlaka(String polaznoVrijemeSaStanice) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("H:mm");
		LocalTime prvovrijeme = LocalTime.parse(this.trenutnoVrijeme, format);
		LocalTime drugovrijeme = LocalTime.parse(polaznoVrijemeSaStanice, format);
		return prvovrijeme.isBefore(drugovrijeme);
	}
}
