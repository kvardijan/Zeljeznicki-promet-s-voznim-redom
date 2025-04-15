package ukrcaj_command;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import stanica_prototype.Stanica;

public class UkrcajInvoker {
	private List<IUkrcajCommand> listaUkrcavanja = new ArrayList<>();

	public void dodajUkrcaj(IUkrcajCommand ukrcaj) {
		this.listaUkrcavanja.add(ukrcaj);
	}

	public void izvrsiUkrcaj(Stanica stanica, String vrijeme) {
		for (IUkrcajCommand komanda : this.listaUkrcavanja) {
			UkrcajPutnika upk = (UkrcajPutnika) komanda;
			if (upk.getStanica().naziv.equals(stanica.naziv) && putnikDosaoNaVrijeme(upk, vrijeme)) {
				komanda.execute();
			}
			if (upk.getStanica().naziv.equals(stanica.naziv) && !putnikDosaoNaVrijeme(upk, vrijeme)) {
				System.out.println("Korisnik " + upk.getKorisnik().getIme() + " " + upk.getKorisnik().getPrezime()
						+ " je zakasnio.");
			}
		}
	}

	private boolean putnikDosaoNaVrijeme(UkrcajPutnika upk, String vrijeme) {
		String normaliziranoVrijemeTrenutno = normalizirajVrijeme(vrijeme);
		String normaliziranoVrijemeKomande = normalizirajVrijeme(upk.getVrijeme());
		LocalTime vrijemeVlaka = LocalTime.parse(normaliziranoVrijemeTrenutno);
		LocalTime vrijemeKomande = LocalTime.parse(normaliziranoVrijemeKomande);
		if (!vrijemeKomande.isAfter(vrijemeVlaka)) {
			return true;
		} else {
			return false;
		}
	}

	private String normalizirajVrijeme(String vrijeme) {
		String[] djelovi = vrijeme.split(":");
		String sati = djelovi[0].length() == 1 ? "0" + djelovi[0] : djelovi[0];
		String minute = djelovi[1].length() == 1 ? "0" + djelovi[1] : djelovi[1];
		return sati + ":" + minute;
	}
}
