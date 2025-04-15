package vozniRed_composite;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import vozniRed_iterator.EtapaIterator;
import vozniRed_visitor.IVozniRedVisitor;

public class Vlak implements IVozniRedComponent {
	protected List<IVozniRedComponent> etape = new ArrayList<>();
	protected String vrsta;
	protected boolean logican = true;
	protected String oznaka;
	// nelogicanPolazakSljedeceEtape
	protected boolean slucaj1 = false;
	// zbrojVremenaPrelaziPocetakSljedeceEtape
	protected boolean slucaj2 = false;
	// nelogicneStaniceKodPrelaskaEtapa
	protected boolean slucaj3 = false;
	// vlakJeNavedenSRazlicitimVrstama
	protected boolean slucaj4 = false;

	public Vlak(String oznaka, String vrsta) {
		this.oznaka = oznaka;
		this.vrsta = vrsta;
	}

	@Override
	public void izvrsi() {
		System.out.println("\n" + oznaka + " vrsta: " + vrsta);
		for (IVozniRedComponent vrc : etape) {
			vrc.izvrsi();
		}
	}

	public boolean add(IVozniRedComponent komponenta) {
		this.etape.add(komponenta);
		return true;
	}

	@Override
	public void accept(IVozniRedVisitor visitor) {
		visitor.visitElement(this);
	}

	public EtapaIterator napraviIteratorEtapa() {
		return new EtapaIterator(etape);
	}

	public void poredajEtape() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("H:mm");
		Collections.sort(etape,
				Comparator.comparing(etapa -> LocalTime.parse(((Etapa) etapa).getVrijemePolaska(), format)));
	}

	public void provjeriLogicnost() {
		boolean jedan = provjeriPrviSlucaj();
		boolean dva = provjeriDrugislucaj();
		boolean tri = provjeriTreciSlucaj();
		if (!jedan || !dva || !tri || this.slucaj4) {
			this.logican = false;
		}
	}

	// Zbroj pocetno vrijeme + vrijeme trajanja 1. etape > pocetno vrijeme 2. etape
	private boolean provjeriPrviSlucaj() {
		EtapaIterator etapaIterator = this.napraviIteratorEtapa();
		for (etapaIterator.prvi(); !etapaIterator.gotovo();) {
			if (etapaIterator.postojiSljedeci()) {
				Etapa eSljedeca = (Etapa) etapaIterator.dajSljedeci();
				Etapa eTrenutna = (Etapa) etapaIterator.daj();
				if (usporediVremenaAfter(eTrenutna.getZavrsnoVrijemeEtape(), eSljedeca.getVrijemePolaska())) {
					this.slucaj1 = true;
					return false;
				}
			}
			etapaIterator.sljedeci();
		}
		return true;
	}

	// Vrijeme dobiveno zbrajanjem po stanicama je vece od vremena trajanja voznog
	// reda
	private boolean provjeriDrugislucaj() {
		EtapaIterator etapaIterator = this.napraviIteratorEtapa();
		for (etapaIterator.prvi(); !etapaIterator.gotovo();) {
			Etapa e = (Etapa) etapaIterator.daj();
			switch (this.vrsta) {
			case "N": {
				int m = e.getZbrojTrajanjaPoStanicamaNvlak();
				String trajanje = pretvoriIntMinuteString(m);
				if (usporediVremenaAfter(trajanje, e.getTrajanjeVoznje())) {
					this.slucaj2 = true;
					return false;
				}
				break;
			}
			case "U": {
				int m = e.getZbrojTrajanjaPoStanicamaUvlak();
				String trajanje = pretvoriIntMinuteString(m);
				if (usporediVremenaAfter(trajanje, e.getTrajanjeVoznje())) {
					this.slucaj2 = true;
					return false;
				}
				break;
			}
			case "B": {
				int m = e.getZbrojTrajanjaPoStanicamaBvlak();
				String trajanje = pretvoriIntMinuteString(m);
				if (usporediVremenaAfter(trajanje, e.getTrajanjeVoznje())) {
					this.slucaj2 = true;
					return false;
				}
				break;
			}
			}
			etapaIterator.sljedeci();
		}
		return true;
	}

	// Kod vise etapa provjerava poklapaju li se stanice kod promjene etape
	private boolean provjeriTreciSlucaj() {
		EtapaIterator etapaIterator = this.napraviIteratorEtapa();
		for (etapaIterator.prvi(); !etapaIterator.gotovo();) {
			if (etapaIterator.postojiSljedeci()) {
				Etapa eSljedeca = (Etapa) etapaIterator.dajSljedeci();
				Etapa eTrenutna = (Etapa) etapaIterator.daj();

				if (!eTrenutna.getZadnjaStanica().naziv.equals(eSljedeca.getPrvaStanica().naziv)) {
					this.slucaj3 = true;
					return false;
				}
			}
			etapaIterator.sljedeci();
		}
		return true;
	}

	private String pretvoriIntMinuteString(int m) {
		int h = m / 60;
		int min = m % 60;
		String formatiranoVrijeme = String.format("%d:%02d", h, min);
		return formatiranoVrijeme;
	}

	private boolean usporediVremenaAfter(String prvo, String drugo) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("H:mm");
		LocalTime prvovrijeme = LocalTime.parse(prvo, format);
		LocalTime drugovrijeme = LocalTime.parse(drugo, format);
		return prvovrijeme.isAfter(drugovrijeme);
	}

	public boolean getLogican() {
		return this.logican;
	}

	public String getOznaka() {
		return this.oznaka;
	}

	public String getVrsta() {
		return vrsta;
	}

	public String dajRazlog() {
		StringBuilder razlog = new StringBuilder();
		if (slucaj1) {
			razlog.append("vrijemePolaska 1.etape + trajanjeVoznje > vrijemePolaska 2.etape|");
		}
		if (slucaj2) {
			razlog.append("zbrojVremenaPoStanicama > trajanjeVoznje|");
		}
		if (slucaj3) {
			razlog.append("zavrsnaStanica 1.etape != pocetnaStanica 2.etape|");
		}
		if (slucaj4) {
			razlog.append("naveden s razlicitim vrstama|");
		}
		return razlog.toString();
	}

	public void setNelogicnost4() {
		this.slucaj4 = true;
	}
}
