package vozniRed_visitor;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import podatkovneKlase.PodaciIEV;
import stanica_prototype.Stanica;
import vozniRed_composite.Etapa;
import vozniRed_composite.Vlak;
import vozniRed_composite.VozniRed;
import vozniRed_iterator.EtapaIterator;
import vozniRed_iterator.StanicaIterator;

public class IEVVisitor implements IVozniRedVisitor {
	private String oznakaVlaka = "";
	private List<PodaciIEV> sviPodaciIEV = new ArrayList<>();
	private String prvaStanicaEtape = "";
	private String zadnjaStanicaEtape = "";

	@Override
	public void visitElement(VozniRed vozniRed) {

	}

	@Override
	public void visitElement(Vlak vlak) {
		System.out.printf(
				"%-" + 6 + "s %-" + 6 + "s %-" + 25 + "s %-" + 25 + "s %-" + 20 + "s %-" + 20 + "s %-" + 10 + "s %-"
						+ 11 + "s%n",
				"Vlak", "Pruga", "Polazna stanica", "Odredišna stanica", "Vrijeme polaska", "Vrijeme dolaska",
				"Broj km", "Dani");
		System.out.println("=".repeat(130));

		this.oznakaVlaka = vlak.getOznaka();
		EtapaIterator etapaIterator = vlak.napraviIteratorEtapa();
		for (etapaIterator.prvi(); !etapaIterator.gotovo();) {
			etapaIterator.daj().accept(this);
			etapaIterator.sljedeci();
		}

		DateTimeFormatter format = DateTimeFormatter.ofPattern("H:mm");
		Collections.sort(sviPodaciIEV, Comparator.comparing(piev -> LocalTime.parse(piev.vrijemePolaska, format)));
		for (PodaciIEV p : this.sviPodaciIEV) {
			System.out.printf(
					"%-" + 6 + "s %-" + 6 + "s %-" + 25 + "s %-" + 25 + "s %-" + 20 + "s %-" + 20 + "s %-" + 10 + "s %-"
							+ 11 + "s%n",
					p.oznakaVlaka, p.oznakaPruge, p.polazna, p.odredisna, p.vrijemePolaska, p.vrijemeDolaska, p.brojKm,
					p.daniUTjednu);
		}
	}

	@Override
	public void visitElement(Etapa etapa) {
		PodaciIEV podaciIEV = dodajZapisIDaj(etapa.getOznaka());

		StanicaIterator stanicaIterator = etapa.napraviIteratorStanica();
		for (stanicaIterator.prvi(); !stanicaIterator.gotovo();) {
			stanicaIterator.daj().accept(this);
			stanicaIterator.sljedeci();
		}

		podaciIEV.oznakaVlaka = this.oznakaVlaka;
		podaciIEV.polazna = this.prvaStanicaEtape;
		podaciIEV.odredisna = this.zadnjaStanicaEtape;
		podaciIEV.vrijemePolaska = etapa.getVrijemePolaska();
		podaciIEV.vrijemeDolaska = etapa.getZavrsnoVrijemeEtape();
		podaciIEV.brojKm = etapa.getDuljina();
		podaciIEV.daniUTjednu = etapa.getDaniString();

		this.prvaStanicaEtape = "";
		this.zadnjaStanicaEtape = "";
	}

	@Override
	public void visitElement(Stanica stanica) {
		if (this.prvaStanicaEtape.isEmpty())
			this.prvaStanicaEtape = stanica.naziv;
		this.zadnjaStanicaEtape = stanica.naziv;
	}

	private PodaciIEV dodajZapisIDaj(String oznaka) {
		PodaciIEV podaciIEV = new PodaciIEV();
		podaciIEV.oznakaPruge = oznaka;
		this.sviPodaciIEV.add(podaciIEV);
		return podaciIEV;
	}
}
