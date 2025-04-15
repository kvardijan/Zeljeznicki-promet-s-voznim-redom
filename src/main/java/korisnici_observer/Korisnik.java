package korisnici_observer;

import podatkovneKlase.PodaciIVRV;

public class Korisnik implements IKorisnikObserver {
	private String ime;
	private String prezime;

	public Korisnik() {
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	@Override
	public void azuriraj(PodaciIVRV podaciIVRV) {
		System.out.println("Ja, " + this.ime + " " + this.prezime + " primio sam obavjest.");
	}

}
