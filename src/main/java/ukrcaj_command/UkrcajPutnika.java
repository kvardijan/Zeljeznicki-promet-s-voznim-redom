package ukrcaj_command;

import korisnici_observer.Korisnik;
import stanica_prototype.Stanica;

public class UkrcajPutnika implements IUkrcajCommand {
	private Korisnik korisnik;
	private Stanica stanica;
	private String vrijeme;
	private Ukrcatelj ukrcatelj;

	public UkrcajPutnika(Ukrcatelj ukrcatelj, Korisnik korisnik, Stanica stanica, String vrijeme) {
		this.korisnik = korisnik;
		this.stanica = stanica;
		this.vrijeme = vrijeme;
		this.ukrcatelj = ukrcatelj;
	}

	@Override
	public void execute() {
		this.ukrcatelj.ukrcaj(korisnik, stanica);
	}

	public Korisnik getKorisnik() {
		return korisnik;
	}

	public Stanica getStanica() {
		return stanica;
	}

	public String getVrijeme() {
		return vrijeme;
	}

	public Ukrcatelj getUkrcatelj() {
		return ukrcatelj;
	}
}
