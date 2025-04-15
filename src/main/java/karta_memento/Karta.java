package karta_memento;

import java.util.ArrayList;
import java.util.List;

public class Karta {
	private List<String> oznakeVlakova = new ArrayList<>();
	private List<String> vrsteVlakova = new ArrayList<>();
	private String polaznaStanica;
	private String odredisnaStanica;
	private String datumPutovanja;
	private float popustVikend;
	private String vrijemePolaska;
	private String vrijemeDolaska;
	private float izvornaCijena;
	private String nacinKupovanja;
	private float popustWebMob;
	private float uvecanjeVlak;
	private float konacnaCijena;
	private String vrijemeIzdavanja;

	public Object napraviMemento() {
		KartaMemento memento = new KartaMemento(this.getOznakeVlakova(), this.getVrsteVlakova(),
				this.getPolaznaStanica(), this.getOdredisnaStanica(), this.getDatumPutovanja(), this.getPopustVikend(),
				this.getVrijemePolaska(), this.getVrijemeDolaska(), this.getIzvornaCijena(), this.getNacinKupovanja(),
				this.getPopustWebMob(), this.getUvecanjeVlak(), this.getKonacnaCijena(), this.getVrijemeIzdavanja());
		return memento;
	}

	public boolean postaviMemento(Object object) {
		if (!(object instanceof KartaMemento)) {
			return false;
		}

		KartaMemento memento = (KartaMemento) object;
		this.setOznakeVlakova(memento.getOznakaVlaka());
		this.setVrsteVlakova(memento.getVrstaVlaka());
		this.setPolaznaStanica(memento.getPolaznaStanica());
		this.setOdredisnaStanica(memento.getOdredisnaStanica());
		this.setDatumPutovanja(memento.getDatumPutovanja());
		this.setPopustVikend(memento.getPopustVikend());
		this.setVrijemePolaska(memento.getVrijemePolaska());
		this.setVrijemeDolaska(memento.getVrijemeDolaska());
		this.setIzvornaCijena(memento.getIzvornaCijena());
		this.setNacinKupovanja(memento.getNacinKupovanja());
		this.setPopustWebMob(memento.getPopustWebMob());
		this.setUvecanjeVlak(memento.getUvecanjeVlak());
		this.setKonacnaCijena(memento.getKonacnaCijena());
		this.setVrijemeIzdavanja(memento.getVrijemeIzdavanja());

		return true;
	}

	public void ispisiKartu() {
		System.out.println("--------------------PRIJEVOZNA KARTA--------------------");
		System.out.println("Datum izdavanja: " + this.getVrijemeIzdavanja());
		System.out.println("Datum putovanja: " + this.getDatumPutovanja());
		System.out.printf("%-" + 30 + "s %-" + 30 + "s%n", "POLAZISTE", "ODREDISTE");

		System.out.println("_".repeat(70));

		System.out.printf("%-" + 30 + "s %-" + 30 + "s%n", "Od: " + this.getPolaznaStanica(),
				"Do: " + this.getOdredisnaStanica());
		System.out.printf("%-" + 30 + "s %-" + 30 + "s%n", "Polazak: " + this.getVrijemePolaska(),
				"Dolazak: " + this.getVrijemeDolaska());
		System.out.println("_".repeat(70));

		System.out.printf("%-" + 30 + "s %-" + 30 + "s%n", "Vlak: " + this.getOznakeVlakova().get(0),
				"Vrsta vlaka: " + this.getVrsteVlakova().get(0));
		System.out.println("_".repeat(70));

		System.out.println("Izvorna cijena: " + String.format("%.2f", this.getIzvornaCijena()) + "€");
		ispisiNacinKupovanja();
		ispisiPopustZaVikend();
		System.out.println("Konačna cijena: " + String.format("%.2f", this.getKonacnaCijena()) + "€");
		System.out.println("--------------------------------------------------------");
		System.out.println();
	}

	private void ispisiNacinKupovanja() {
		switch (this.getNacinKupovanja()) {
		case "B": {
			System.out.printf("%-" + 30 + "s %-" + 30 + "s%n", "Nacin kupovine: Blagajna", "Iznos popusta: -");
			break;
		}
		case "V": {
			System.out.printf("%-" + 30 + "s %-" + 30 + "s%n", "Nacin kupovine: U vlaku",
					"Iznos uvecanja: " + Math.abs(this.getUvecanjeVlak()) * 100 + "%");
			break;
		}
		case "WM": {
			System.out.printf("%-" + 30 + "s %-" + 30 + "s%n", "Nacin kupovine: Aplikacija",
					"Iznos popusta: " + Math.abs(this.getPopustWebMob()) * 100 + "%");
			break;
		}
		default: {
			System.out.println("Nepoznat nacin kupovine.");
		}
		}
	}

	private void ispisiPopustZaVikend() {
		if (this.getPopustVikend() != 0.0f) {
			System.out.printf("%-" + 30 + "s %-" + 30 + "s%n", "Popust vikend: DA",
					"Iznos: " + Math.abs(this.getPopustVikend()) * 100 + "%");
		} else {
			System.out.printf("%-" + 30 + "s %-" + 30 + "s%n", "Popust vikend: NE", "Iznos: -");
		}
	}

	public List<String> getOznakeVlakova() {
		return oznakeVlakova;
	}

	public void setOznakaVlaka(String oznakaVlaka) {
		this.oznakeVlakova.add(oznakaVlaka);
	}

	public void setOznakeVlakova(List<String> oznakeVlakova) {
		this.oznakeVlakova = oznakeVlakova;
	}

	public List<String> getVrsteVlakova() {
		return vrsteVlakova;
	}

	public void setVrstaVlaka(String vrstaVlaka) {
		this.vrsteVlakova.add(vrstaVlaka);
	}

	public void setVrsteVlakova(List<String> vrsteVlakova) {
		this.vrsteVlakova = vrsteVlakova;
	}

	public String getPolaznaStanica() {
		return polaznaStanica;
	}

	public void setPolaznaStanica(String polaznaStanica) {
		this.polaznaStanica = polaznaStanica;
	}

	public String getOdredisnaStanica() {
		return odredisnaStanica;
	}

	public void setOdredisnaStanica(String odredisnaStanica) {
		this.odredisnaStanica = odredisnaStanica;
	}

	public String getDatumPutovanja() {
		return datumPutovanja;
	}

	public void setDatumPutovanja(String datumPutovanja) {
		this.datumPutovanja = datumPutovanja;
	}

	public float getPopustVikend() {
		return popustVikend;
	}

	public void setPopustVikend(float popustVikend) {
		this.popustVikend = popustVikend;
	}

	public String getVrijemePolaska() {
		return vrijemePolaska;
	}

	public void setVrijemePolaska(String vrijemePolaska) {
		this.vrijemePolaska = vrijemePolaska;
	}

	public String getVrijemeDolaska() {
		return vrijemeDolaska;
	}

	public void setVrijemeDolaska(String vrijemeDolaska) {
		this.vrijemeDolaska = vrijemeDolaska;
	}

	public float getIzvornaCijena() {
		return izvornaCijena;
	}

	public void setIzvornaCijena(float izvornaCijena) {
		this.izvornaCijena = izvornaCijena;
	}

	public String getNacinKupovanja() {
		return nacinKupovanja;
	}

	public void setNacinKupovanja(String nacinKupovanja) {
		this.nacinKupovanja = nacinKupovanja;
	}

	public float getPopustWebMob() {
		return popustWebMob;
	}

	public void setPopustWebMob(float popustWebMob) {
		this.popustWebMob = popustWebMob;
	}

	public float getUvecanjeVlak() {
		return uvecanjeVlak;
	}

	public void setUvecanjeVlak(float uvecanjeVlak) {
		this.uvecanjeVlak = uvecanjeVlak;
	}

	public float getKonacnaCijena() {
		return konacnaCijena;
	}

	public void setKonacnaCijena(float konacnaCijena) {
		this.konacnaCijena = konacnaCijena;
	}

	public String getVrijemeIzdavanja() {
		return vrijemeIzdavanja;
	}

	public void setVrijemeIzdavanja(String vrijemeIzdavanja) {
		this.vrijemeIzdavanja = vrijemeIzdavanja;
	}

}
