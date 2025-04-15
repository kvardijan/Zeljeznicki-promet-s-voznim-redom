package vozniRed_visitor;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import podatkovneKlase.PodaciIEVD;
import stanica_prototype.Stanica;
import vozniRed_composite.Etapa;
import vozniRed_composite.Vlak;
import vozniRed_composite.VozniRed;
import vozniRed_iterator.EtapaIterator;
import vozniRed_iterator.StanicaIterator;
import vozniRed_iterator.VlakIterator;

public class IEVDVisitor implements IVozniRedVisitor {
	private boolean pon = false;
	private boolean uto = false;
	private boolean sri = false;
	private boolean cet = false;
	private boolean pet = false;
	private boolean sub = false;
	private boolean ned = false;
	private PodaciIEVD temp;
	private List<PodaciIEVD> sviPodaciIEVD = new ArrayList<>();
	private boolean ispis;

	public IEVDVisitor(String dani, boolean ispis) {
		obradiDane(dani);
		this.ispis = ispis;
	}

	@Override
	public void visitElement(VozniRed vozniRed) {
		if (this.ispis) {
			System.out.printf(
					"%-" + 6 + "s %-" + 6 + "s %-" + 25 + "s %-" + 25 + "s %-" + 20 + "s %-" + 20 + "s %-" + 11 + "s%n",
					"Vlak", "Pruga", "Polazna stanica", "Odredišna stanica", "Vrijeme polaska", "Vrijeme dolaska",
					"Dani");
			System.out.println("=".repeat(120));
		}

		VlakIterator vlakIterator = vozniRed.napraviIteratorVlakova();
		for (vlakIterator.prvi(); !vlakIterator.gotovo();) {
			vlakIterator.daj().accept(this);
			vlakIterator.sljedeci();
		}

		DateTimeFormatter format = DateTimeFormatter.ofPattern("H:mm");
		Collections.sort(sviPodaciIEVD, Comparator.comparing(pievd -> LocalTime.parse(pievd.vrijemePolaska, format)));
		if (this.ispis) {
			for (PodaciIEVD p : this.sviPodaciIEVD) {
				System.out.printf(
						"%-" + 6 + "s %-" + 6 + "s %-" + 25 + "s %-" + 25 + "s %-" + 20 + "s %-" + 20 + "s %-" + 11
								+ "s%n",
						p.oznakaVlaka, p.oznakaPruge, p.polazna, p.odredisna, p.vrijemePolaska, p.vrijemeDolaska,
						p.daniUTjednu);
			}
		}
	}

	@Override
	public void visitElement(Vlak vlak) {
		temp = new PodaciIEVD();
		temp.oznakaVlaka = vlak.getOznaka();

		EtapaIterator etapaIterator = vlak.napraviIteratorEtapa();
		for (etapaIterator.prvi(); !etapaIterator.gotovo();) {
			etapaIterator.daj().accept(this);
			etapaIterator.sljedeci();
		}
	}

	@Override
	public void visitElement(Etapa etapa) {
		if (!provjeriDaneEtape(etapa)) {
			return;
		}

		StanicaIterator stanicaIterator = etapa.napraviIteratorStanica();
		for (stanicaIterator.prvi(); !stanicaIterator.gotovo();) {
			stanicaIterator.daj().accept(this);
			stanicaIterator.sljedeci();
		}

		temp.oznakaPruge = etapa.getOznaka();
		temp.vrijemePolaska = etapa.getVrijemePolaska();
		temp.vrijemeDolaska = etapa.getZavrsnoVrijemeEtape();
		temp.daniUTjednu = dajDaneString(etapa);

		PodaciIEVD novo = new PodaciIEVD();
		novo.oznakaVlaka = temp.oznakaVlaka;
		novo.oznakaPruge = temp.oznakaPruge;
		novo.vrijemePolaska = temp.vrijemePolaska;
		novo.vrijemeDolaska = temp.vrijemeDolaska;
		novo.daniUTjednu = temp.daniUTjednu;
		novo.polazna = temp.polazna;
		novo.odredisna = temp.odredisna;
		this.sviPodaciIEVD.add(novo);

		temp.oznakaPruge = "";
		temp.vrijemePolaska = "";
		temp.vrijemeDolaska = "";
		temp.daniUTjednu = "";
		temp.polazna = "";
		temp.odredisna = "";
	}

	@Override
	public void visitElement(Stanica stanica) {
		if (this.temp.polazna.isEmpty()) {
			this.temp.polazna = stanica.naziv;
		}
		this.temp.odredisna = stanica.naziv;
	}

	public List<PodaciIEVD> dajPodatkeOVlakovimaZaDane() {
		return this.sviPodaciIEVD;
	}

	private String dajDaneString(Etapa etapa) {
		StringBuilder dani = new StringBuilder();
		boolean radniDani[] = etapa.dajRadneDaneEtape();

		if (radniDani[0] == true)
			dani.append("Po");
		if (radniDani[1] == true)
			dani.append("U");
		if (radniDani[2] == true)
			dani.append("Sr");
		if (radniDani[3] == true)
			dani.append("Č");
		if (radniDani[4] == true)
			dani.append("Pe");
		if (radniDani[5] == true)
			dani.append("Su");
		if (radniDani[6] == true)
			dani.append("N");
		return dani.toString();
	}

	private boolean provjeriDaneEtape(Etapa etapa) {
		boolean radniDani[] = etapa.dajRadneDaneEtape();
		boolean trazeniRadniDani[] = { this.pon, this.uto, this.sri, this.cet, this.pet, this.sub, this.ned };
		boolean prihvacenaEtapa = true;

		for (int i = 0; i < 7; i++) {
			if (trazeniRadniDani[i] == true) {
				if (radniDani[i] == false) {
					prihvacenaEtapa = false;
					break;
				}
			}
		}
		return prihvacenaEtapa;
	}

	private void obradiDane(String dani) {

		if (dani.contains("Po"))
			pon = true;
		if (dani.contains("U"))
			uto = true;
		if (dani.contains("Sr"))
			sri = true;
		if (dani.contains("Č"))
			cet = true;
		if (dani.contains("Pe"))
			pet = true;
		if (dani.contains("Su"))
			sub = true;
		if (dani.contains("N"))
			ned = true;
	}
}
