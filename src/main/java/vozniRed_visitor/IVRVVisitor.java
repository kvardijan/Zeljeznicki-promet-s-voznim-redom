package vozniRed_visitor;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import podatkovneKlase.PodaciIVRV;
import pruga_state.Pruga;
import stanica_prototype.Stanica;
import tvrtkaZaPrijevoz_singleton.TvrtkaZaPrijevoz;
import vozniRed_composite.Etapa;
import vozniRed_composite.Vlak;
import vozniRed_composite.VozniRed;
import vozniRed_iterator.EtapaIterator;
import vozniRed_iterator.StanicaIterator;

public class IVRVVisitor implements IVozniRedVisitor {
	private String trenutnoVrijeme = "";
	private int trenutnaDuljina = 0;
	private List<PodaciIVRV> sviPodaciIVRV = new ArrayList<>();
	private boolean novaEtapa = true;
	private PodaciIVRV temp;
	private String vrstaVlaka = "";
	private String smjerEtape = "";
	private boolean ispis;

	public IVRVVisitor(boolean ispis) {
		this.ispis = ispis;
	}

	@Override
	public void visitElement(VozniRed vozniRed) {

	}

	@Override
	public void visitElement(Vlak vlak) {
		if (ispis) {
			System.out.printf("%-" + 6 + "s %-" + 6 + "s %-" + 25 + "s %-" + 25 + "s %-" + 20 + "s%n", "Vlak", "Pruga",
					"Stanica", "Vrijeme polaska", "km od polazne");
			System.out.println("=".repeat(88));
		}

		temp = new PodaciIVRV();
		temp.oznakaVlaka = vlak.getOznaka();
		this.vrstaVlaka = vlak.getVrsta();

		EtapaIterator etapaIterator = vlak.napraviIteratorEtapa();
		for (etapaIterator.prvi(); !etapaIterator.gotovo();) {
			etapaIterator.daj().accept(this);
			etapaIterator.sljedeci();
		}

		DateTimeFormatter format = DateTimeFormatter.ofPattern("H:mm");
		Collections.sort(sviPodaciIVRV, Comparator.comparing(pivrv -> LocalTime.parse(pivrv.vrijemePolaska, format)));
		if (ispis) {
			for (PodaciIVRV p : this.sviPodaciIVRV) {
				System.out.printf("%-" + 6 + "s %-" + 6 + "s %-" + 25 + "s %-" + 25 + "s %-" + 20 + "s%n",
						p.oznakaVlaka, p.oznakaPruge, p.stanica, p.vrijemePolaska, p.brojKmOdPolazne);
			}
		}
	}

	@Override
	public void visitElement(Etapa etapa) {
		this.smjerEtape = etapa.getSmjerString();
		temp.oznakaPruge = etapa.getOznaka();
		if (this.novaEtapa) {
			temp.vrijemePolaska = etapa.getVrijemePolaska();
			this.trenutnoVrijeme = etapa.getVrijemePolaska();
		}

		StanicaIterator stanicaIterator = etapa.napraviIteratorStanica();
		for (stanicaIterator.prvi(); !stanicaIterator.gotovo();) {
			stanicaIterator.daj().accept(this);
			stanicaIterator.sljedeci();
		}

		this.novaEtapa = true;
	}

	@Override
	public void visitElement(Stanica stanica) {
		temp.stanica = stanica.naziv;

		if (this.novaEtapa) {
			temp.brojKmOdPolazne = this.trenutnaDuljina;
			temp.brojKmOdPolazne += 0;

			PodaciIVRV novi = new PodaciIVRV();
			novi.oznakaVlaka = temp.oznakaVlaka;
			novi.oznakaPruge = temp.oznakaPruge;
			novi.stanica = temp.stanica;
			novi.vrijemePolaska = temp.vrijemePolaska;
			novi.brojKmOdPolazne = temp.brojKmOdPolazne;
			if (this.vrstaVlaka.equals("N"))
				this.sviPodaciIVRV.add(novi);
			if (this.vrstaVlaka.equals("U")) {
				if (stanica.stajalisteUbrzani) {
					this.sviPodaciIVRV.add(novi);
				}
			}
			if (this.vrstaVlaka.equals("B")) {
				if (stanica.stajalisteBrzi) {
					this.sviPodaciIVRV.add(novi);
				}
			}
			this.novaEtapa = false;
		} else {
			dajInfoOPruzi(temp.oznakaPruge, stanica.naziv);
			temp.vrijemePolaska = this.trenutnoVrijeme;
			temp.brojKmOdPolazne = this.trenutnaDuljina;

			PodaciIVRV novi = new PodaciIVRV();
			novi.oznakaVlaka = temp.oznakaVlaka;
			novi.oznakaPruge = temp.oznakaPruge;
			novi.stanica = temp.stanica;
			novi.vrijemePolaska = temp.vrijemePolaska;
			novi.brojKmOdPolazne = temp.brojKmOdPolazne;
			if (this.vrstaVlaka.equals("N"))
				this.sviPodaciIVRV.add(novi);
			if (this.vrstaVlaka.equals("U")) {
				if (stanica.stajalisteUbrzani) {
					this.sviPodaciIVRV.add(novi);
				}
			}
			if (this.vrstaVlaka.equals("B")) {
				if (stanica.stajalisteBrzi) {
					this.sviPodaciIVRV.add(novi);
				}
			}
		}
	}

	public List<PodaciIVRV> dajPodatkeVoznogRedaVlaka() {
		return this.sviPodaciIVRV;
	}

	private void dajInfoOPruzi(String oznakaPruke, String nazivStanice) {
		for (Pruga p : TvrtkaZaPrijevoz.getPopisPruga()) {
			if (this.smjerEtape.equals("O")) {
				if (p.oznaka.equals(oznakaPruke) && p.pocetnaStanica.naziv.equals(nazivStanice)) {
					this.trenutnaDuljina += p.duljina;
					if (this.vrstaVlaka.equals("B") && !p.vrijemeBrziVlak.isEmpty()) {
						dodajVrijeme(this.trenutnoVrijeme, p.vrijemeBrziVlak);
					} else if (this.vrstaVlaka.equals("U") && !p.vrijemeUbrzaniVlak.isEmpty()) {
						dodajVrijeme(this.trenutnoVrijeme, p.vrijemeUbrzaniVlak);
					} else if (this.vrstaVlaka.equals("N") && !p.vrijemeNormalniVlak.isEmpty()) {
						dodajVrijeme(this.trenutnoVrijeme, p.vrijemeNormalniVlak);
					}
				}
			} else {
				if (p.oznaka.equals(oznakaPruke) && p.zavrsnaStanica.naziv.equals(nazivStanice)) {
					this.trenutnaDuljina += p.duljina;
					if (this.vrstaVlaka.equals("B") && !p.vrijemeBrziVlak.isEmpty()) {
						dodajVrijeme(this.trenutnoVrijeme, p.vrijemeBrziVlak);
					} else if (this.vrstaVlaka.equals("U") && !p.vrijemeUbrzaniVlak.isEmpty()) {
						dodajVrijeme(this.trenutnoVrijeme, p.vrijemeUbrzaniVlak);
					} else if (this.vrstaVlaka.equals("N") && !p.vrijemeNormalniVlak.isEmpty()) {
						dodajVrijeme(this.trenutnoVrijeme, p.vrijemeNormalniVlak);
					}
				}
			}
		}
	}

	private void dodajVrijeme(String sada, String minute) {
		String nadodano = pretvoriIntMinuteString(Integer.parseInt(minute));
		DateTimeFormatter format = DateTimeFormatter.ofPattern("H:mm");
		LocalTime pocetno = LocalTime.parse(sada, format);
		LocalTime trajanje = LocalTime.parse(nadodano, format);
		Duration d1 = Duration.between(LocalTime.MIN, pocetno);
		Duration d2 = Duration.between(LocalTime.MIN, trajanje);
		Duration ukd = d1.plus(d2);
		LocalTime zbrojeno = LocalTime.MIN.plus(ukd);
		this.trenutnoVrijeme = zbrojeno.format(format);
	}

	private String pretvoriIntMinuteString(int m) {
		int h = m / 60;
		int min = m % 60;
		String formatiranoVrijeme = String.format("%d:%02d", h, min);
		return formatiranoVrijeme;
	}
}
