package pruga_state;

import stanica_prototype.Stanica;

public class Pruga {
	public String oznaka;
	public String kategorija;
	public String vrsta;
	public int brojKolosjeka;
	public float dozvoljenoOpterecenjePoOsovini;
	public float dozvoljenoOpterecenjePoDuznomMetru;
	// public String status;
	public float duljina;
	public Stanica pocetnaStanica;
	public Stanica zavrsnaStanica;
	public String vrijemeNormalniVlak;
	public String vrijemeUbrzaniVlak;
	public String vrijemeBrziVlak;
	private IStanjePruge stanjeNormalno = new Ispravna();
	private IStanjePruge stanjeObrnuto = new Ispravna();

	public Pruga() {
	}

	public String dajStanjePruge(String smjer) {
		if (smjer.equals("N")) {
			return this.stanjeNormalno.ispisiStanje();
		} else {
			return this.stanjeObrnuto.ispisiStanje();
		}
	}

	public String dajStanjePrugeSkraceno(String smjer) {
		if (smjer.equals("N")) {
			return this.stanjeNormalno.ispisiStanjeSkraceno();
		} else {
			return this.stanjeObrnuto.ispisiStanjeSkraceno();
		}
	}

	public void postaviStanjePruge(IStanjePruge stanje, String smjer) {
		if (smjer.equals("N")) {
			this.stanjeNormalno = stanje;
		} else {
			this.stanjeObrnuto = stanje;
		}
	}

	public void pokvariPrugu(String smjer) {
		if (smjer.equals("N")) {
			this.stanjeNormalno.staviUKvar(this, "N");
		} else {
			this.stanjeObrnuto.staviUKvar(this, "O");
		}
	}

	public void zatvoriPrugu(String smjer) {
		if (smjer.equals("N")) {
			this.stanjeNormalno.staviUZatvoreno(this, "N");
		} else {
			this.stanjeObrnuto.staviUZatvoreno(this, "O");
		}
	}

	public void testirajPrugu(String smjer) {
		if (smjer.equals("N")) {
			this.stanjeNormalno.staviUTestiranje(this, "N");
		} else {
			this.stanjeObrnuto.staviUTestiranje(this, "O");
		}
	}

	public void popraviPrugu(String smjer) {
		if (smjer.equals("N")) {
			this.stanjeNormalno.staviUIspravno(this, "N");
		} else {
			this.stanjeObrnuto.staviUIspravno(this, "O");
		}
	}
}
