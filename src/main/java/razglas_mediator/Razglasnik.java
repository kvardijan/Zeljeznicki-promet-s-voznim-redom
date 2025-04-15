package razglas_mediator;

import java.util.List;

import korisnici_observer.IKorisnikObserver;
import podatkovneKlase.PodaciIVRV;
import stanica_prototype.Stanica;

public class Razglasnik implements IKorisnikObserver {
	IRazglasMediator razglasMediator;
	private List<PodaciIVRV> vozniRed;

	public Razglasnik(String vlak, String dan) {
		this.razglasMediator = new RazglasMediator();
		this.vozniRed = this.razglasMediator.dohvatiVozniRedVlakaZaDan(vlak, dan);
	}

	@Override
	public void azuriraj(PodaciIVRV podaciIVRV) {
		Stanica stanicaZaRazglasiti = this.razglasMediator.dajSljedecuStanicu(this.vozniRed, podaciIVRV);
		StringBuilder poruka = new StringBuilder();
		if (stanicaZaRazglasiti != null) {

			if (stanicaZaRazglasiti.vrstaStanice.equals("kol.")) {
				poruka.append("Sljedeci kolodvor");
			} else {
				poruka.append("Sljedece stajaliste");
			}

			poruka.append(" je ").append(stanicaZaRazglasiti.naziv).append(". Ulaz/izlaz putnika ");
			if (stanicaZaRazglasiti.ulazIzlazPutnika) {
				poruka.append("je moguc.");
			} else {
				poruka.append("nije moguc.");
			}
			poruka.append(" Utovar/istovar robe ");
			if (stanicaZaRazglasiti.ulazIzlazRobe) {
				poruka.append("je moguc.");
			} else {
				poruka.append("nije moguc.");
			}
		} else {
			poruka.append("Stigli smo na zadnju stanicu puta.");
		}
		System.out.println(poruka.toString());
	}

}
