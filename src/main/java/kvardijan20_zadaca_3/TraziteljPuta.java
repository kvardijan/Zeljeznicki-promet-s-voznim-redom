package kvardijan20_zadaca_3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import podatkovneKlase.StanicaSUdaljenosti;
import pruga_state.Pruga;
import stanica_prototype.Stanica;

public class TraziteljPuta {
	private float duljinaNajkracegPuta;
	// private List<Stanica> najkraciPut;
	private List<StanicaSUdaljenosti> najkraciPut;

	public TraziteljPuta() {
		this.duljinaNajkracegPuta = Float.MAX_VALUE;
		this.najkraciPut = new ArrayList<>();
	}

	public List<StanicaSUdaljenosti> pronadiNajkraciPut(String nazivPocetneStanice, String nazivZavrsneStanice,
			List<Pruga> popisPruga) {
		Set<String> posjecena = new HashSet<>();
		List<Stanica> trenutniPut = new ArrayList<>();
		List<Float> trenutneUdaljenosti = new ArrayList<>();
		duljinaNajkracegPuta = Float.MAX_VALUE;
		najkraciPut.clear();

		Stanica pocetnaStanica = findStationByName(nazivPocetneStanice, popisPruga);
		if (pocetnaStanica != null) {
			dfs(pocetnaStanica, nazivZavrsneStanice, posjecena, trenutniPut, trenutneUdaljenosti, 0, popisPruga);
		}
		return najkraciPut;
	}

	private void dfs(Stanica trenutnaStanica, String nazivZavrsneStanice, Set<String> posjecena,
			List<Stanica> trenutniPut, List<Float> trenutneUdaljenosti, float trenutnaDuljina, List<Pruga> popisPruga) {
		trenutniPut.add(trenutnaStanica);
		trenutneUdaljenosti.add(trenutnaDuljina);

		if (trenutnaStanica.naziv.equals(nazivZavrsneStanice)) {
			if (trenutnaDuljina < duljinaNajkracegPuta) {
				duljinaNajkracegPuta = trenutnaDuljina;

				najkraciPut = new ArrayList<>();
				for (int i = 0; i < trenutniPut.size(); i++) {
					najkraciPut.add(new StanicaSUdaljenosti(trenutniPut.get(i), trenutneUdaljenosti.get(i)));
				}
			}
			trenutniPut.remove(trenutniPut.size() - 1);
			trenutneUdaljenosti.remove(trenutneUdaljenosti.size() - 1);
			return;
		}

		posjecena.add(trenutnaStanica.naziv);

		for (Pruga pruga : popisPruga) {
			if (pruga.pocetnaStanica.naziv.equals(trenutnaStanica.naziv)
					&& !posjecena.contains(pruga.zavrsnaStanica.naziv)) {
				dfs(pruga.zavrsnaStanica, nazivZavrsneStanice, posjecena, trenutniPut, trenutneUdaljenosti,
						trenutnaDuljina + pruga.duljina, popisPruga);
			} else if (pruga.zavrsnaStanica.naziv.equals(trenutnaStanica.naziv)
					&& !posjecena.contains(pruga.pocetnaStanica.naziv)) {
				dfs(pruga.pocetnaStanica, nazivZavrsneStanice, posjecena, trenutniPut, trenutneUdaljenosti,
						trenutnaDuljina + pruga.duljina, popisPruga);
			}
		}

		posjecena.remove(trenutnaStanica.naziv);
		trenutniPut.remove(trenutniPut.size() - 1);
		trenutneUdaljenosti.remove(trenutneUdaljenosti.size() - 1);
	}

	private Stanica findStationByName(String naziv, List<Pruga> popisPruga) {
		for (Pruga pruga : popisPruga) {
			if (pruga.pocetnaStanica.naziv.equals(naziv)) {
				return pruga.pocetnaStanica;
			} else if (pruga.zavrsnaStanica.naziv.equals(naziv)) {
				return pruga.zavrsnaStanica;
			}
		}
		return null;
	}
}
