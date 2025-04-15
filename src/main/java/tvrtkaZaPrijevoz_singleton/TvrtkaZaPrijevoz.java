package tvrtkaZaPrijevoz_singleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import karta_memento.KartaCaretaker;
import kompozicija_builder.Kompozicija;
import korisnici_observer.PratiocVlakova;
import kvardijan20_zadaca_3.CitacDatoteka;
import podatkovneKlase.Cjenik;
import prijevoznoSredstvo_builder.PrijevoznoSredstvo;
import pruga_state.Pruga;
import stanica_prototype.Stanica;
import ukrcaj_command.UkrcajInvoker;
import vozniRed_composite.VozniRed;

public class TvrtkaZaPrijevoz {
	private static TvrtkaZaPrijevoz INSTANCA;
	private static List<Stanica> popisStanica = new ArrayList<>();
	private static List<PrijevoznoSredstvo> popisPrijevoznihSredstava = new ArrayList<>();
	private static List<Kompozicija> popisKompozicija = new ArrayList<>();
	private static List<Pruga> popisPruga = new ArrayList<>();
	private static VozniRed vozniRed = new VozniRed();
	private static HashMap<String, String> oznakeDana = new HashMap<>();
	private static PratiocVlakova pratiocVlakova = new PratiocVlakova();
	private static Cjenik cjenik = null;
	private static KartaCaretaker kartaCaretaker = new KartaCaretaker();
	private static UkrcajInvoker ukrcajInvoker = new UkrcajInvoker();

	private TvrtkaZaPrijevoz() {
	}

	public static TvrtkaZaPrijevoz dajInstancu() {
		if (INSTANCA == null)
			INSTANCA = new TvrtkaZaPrijevoz();
		return INSTANCA;
	}

	public boolean obradiUlazneDatoteke(String[] args) {
		boolean ispravniArgumenti = false;
		CitacDatoteka citac = new CitacDatoteka(args);

		if (citac.provjeriBrojArgumenata()) {
			ispravniArgumenti = citac.obradiArgumente();
			System.out.println("Obrada datoteka zavrsena...");
			return ispravniArgumenti;
		}
		return ispravniArgumenti;
	}

	public static List<Stanica> getPopisStanica() {
		return popisStanica;
	}

	public static void setPopisStanica(Stanica s) {
		popisStanica.add(s);
	}

	public static List<PrijevoznoSredstvo> getPopisPrijevoznihSredstava() {
		return popisPrijevoznihSredstava;
	}

	public static void setPopisPrijevoznihSredstava(PrijevoznoSredstvo ps) {
		popisPrijevoznihSredstava.add(ps);
	}

	public static List<Kompozicija> getPopisKompozicija() {
		return popisKompozicija;
	}

	public static void setPopisKompozicija(Kompozicija k) {
		popisKompozicija.add(k);
	}

	public static List<Pruga> getPopisPruga() {
		return popisPruga;
	}

	public static void setPopisPruga(Pruga p) {
		popisPruga.add(p);
	}

	public static VozniRed getVozniRed() {
		return vozniRed;
	}

	public static void setVozniRed(VozniRed v) {
		vozniRed = v;
	}

	public static HashMap<String, String> getOznakeDana() {
		return oznakeDana;
	}

	public static void setOznakeDana(String oznaka, String dani) {
		oznakeDana.put(oznaka, dani);
	}

	public static PratiocVlakova getPratiocVlakova() {
		return pratiocVlakova;
	}

	public static void setCjenik(Cjenik c) {
		cjenik = c;
	}

	public static Cjenik getCjenik() {
		return cjenik;
	}

	public static Stanica getStanica(String nazivStanice) {
		Stanica stanica = null;
		for (Stanica s : popisStanica) {
			if (s.naziv.equals(nazivStanice)) {
				stanica = s.clone();
				break;
			}
		}
		return stanica;
	}

	public static Pruga getPruga(String oznakaPruge) {
		Pruga pruga = null;
		for (Pruga p : popisPruga) {
			if (p.oznaka.equals(oznakaPruge)) {
				pruga = p;
				break;
			}
		}
		return pruga;
	}
	
	public static KartaCaretaker getKartaCaretaker() {
		return kartaCaretaker;
	}

	public static UkrcajInvoker getUkrcajInvoker() {
		return ukrcajInvoker;
	}
}
