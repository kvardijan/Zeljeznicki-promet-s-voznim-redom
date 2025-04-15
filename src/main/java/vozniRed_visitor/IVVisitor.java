package vozniRed_visitor;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import podatkovneKlase.PodaciIV;
import stanica_prototype.Stanica;
import vozniRed_composite.Etapa;
import vozniRed_composite.Vlak;
import vozniRed_composite.VozniRed;
import vozniRed_iterator.EtapaIterator;
import vozniRed_iterator.StanicaIterator;
import vozniRed_iterator.VlakIterator;

public class IVVisitor implements IVozniRedVisitor {
	private List<PodaciIV> sviPodaciIV = new ArrayList<>();
	private String prvaStanica = "";
	private String zadnjaStanica = "";
	private String vrijemePolaska = "";
	private String vrijemeDolaska = "";
	private int duljina = 0;

	@Override
	public void visitElement(VozniRed vozniRed) {
		System.out.printf("%-" + 15 + "s %-" + 30 + "s %-" + 30 + "s %-" + 20 + "s %-" + 20 + "s %-" + 10 + "s%n",
				"Oznaka vlaka", "Polazna stanica", "Odredišna stanica", "Vrijeme polaska", "Vrijeme dolaska",
				"Broj km");
		System.out.println("=".repeat(130));

		VlakIterator vlakIterator = vozniRed.napraviIteratorVlakova();
		for (vlakIterator.prvi(); !vlakIterator.gotovo();) {
			vlakIterator.daj().accept(this);
			vlakIterator.sljedeci();
		}

		DateTimeFormatter format = DateTimeFormatter.ofPattern("H:mm");
		Collections.sort(sviPodaciIV, Comparator.comparing(piv -> LocalTime.parse(piv.vrijemePolaska, format)));
		for (PodaciIV p : this.sviPodaciIV) {
			System.out.printf("%-" + 15 + "s %-" + 30 + "s %-" + 30 + "s %-" + 20 + "s %-" + 20 + "s %-" + 10 + "s%n",
					p.oznakaVlaka, p.polazna, p.odredisna, p.vrijemePolaska, p.vrijemeDolaska, p.brojKm);
		}
	}

	@Override
	public void visitElement(Vlak vlak) {
		PodaciIV podaciIV = dodajVlakIDaj(vlak.getOznaka());

		EtapaIterator etapaIterator = vlak.napraviIteratorEtapa();
		for (etapaIterator.prvi(); !etapaIterator.gotovo();) {
			etapaIterator.daj().accept(this);
			etapaIterator.sljedeci();
		}
		podaciIV.polazna = this.prvaStanica;
		podaciIV.odredisna = this.zadnjaStanica;
		podaciIV.vrijemePolaska = this.vrijemePolaska;
		podaciIV.vrijemeDolaska = this.vrijemeDolaska;
		podaciIV.brojKm = this.duljina;

		this.vrijemePolaska = "";
		this.vrijemeDolaska = "";
		this.prvaStanica = "";
		this.zadnjaStanica = "";
		this.duljina = 0;
	}

	@Override
	public void visitElement(Etapa etapa) {
		if (this.vrijemePolaska.isEmpty()) {
			this.vrijemePolaska = etapa.getVrijemePolaska();
		}
		this.vrijemeDolaska = etapa.getZavrsnoVrijemeEtape();
		this.duljina += etapa.getDuljina();

		StanicaIterator stanicaIterator = etapa.napraviIteratorStanica();
		for (stanicaIterator.prvi(); !stanicaIterator.gotovo();) {
			stanicaIterator.daj().accept(this);
			stanicaIterator.sljedeci();
		}
	}

	@Override
	public void visitElement(Stanica stanica) {
		if (this.prvaStanica.isEmpty()) {
			this.prvaStanica = stanica.naziv;
		}
		this.zadnjaStanica = stanica.naziv;
	}

	private PodaciIV dodajVlakIDaj(String oznaka) {
		PodaciIV podaciIV = new PodaciIV();
		podaciIV.oznakaVlaka = oznaka;
		this.sviPodaciIV.add(podaciIV);
		return podaciIV;
	}
}
