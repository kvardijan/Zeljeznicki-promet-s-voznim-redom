package korisnici_observer;

public interface IKorisnikSubjekt {
	void dodajObserver(IKorisnikObserver o, String dogadaj);
	void makniObserver(IKorisnikObserver o, String dogadaj);
}
