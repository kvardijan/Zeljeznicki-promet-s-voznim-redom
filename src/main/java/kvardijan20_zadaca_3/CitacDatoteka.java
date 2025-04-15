package kvardijan20_zadaca_3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tvrtkaZaPrijevoz_singleton.TvrtkaZaPrijevoz;
import vozniRed_composite.Vlak;
import vozniRed_composite.VozniRed;

public class CitacDatoteka {
	private String[] _argumenti;
	private int _globalniBrojGreski = 0;
	private int _brojGreskiStanice = 0;
	private int _brojGreskiVozila = 0;
	private int _brojGreskiKompozicije = 0;
	private int _brojGreskiDani = 0;
	private int _brojGreskiVozniRed = 0;

	public CitacDatoteka(String[] args) {
		_argumenti = args;
	}

	public boolean provjeriBrojArgumenata() {
		if (_argumenti.length != 10) {
			System.out.println("Neispravan broj argumenata!");
			return false;
		}
		return true;
	}

	public boolean obradiArgumente() {
		if (!poredajArgumente())
			return false;

		for (int i = 0; i < _argumenti.length; i += 2) {
			switch (_argumenti[i]) {
			case "--zs": {
				if (!ucitajDatotekuStanica(_argumenti[i + 1]))
					return false;
				break;
			}
			case "--zps": {
				if (!ucitajDatotekuVozila(_argumenti[i + 1]))
					return false;
				break;
			}
			case "--zk": {
				if (!ucitajDatotekuKompozicije(_argumenti[i + 1]))
					return false;
				break;
			}
			case "--zvr": {
				if (!ucitajDatotekuVozniRed(_argumenti[i + 1]))
					return false;
				break;
			}
			case "--zod": {
				if (!ucitajDatotekuDani(_argumenti[i + 1]))
					return false;
				break;
			}
			default: {
				System.out.println(_argumenti[i] + " nije poznati argument!");
				return false;
			}
			}
		}
		return true;
	}

	private boolean poredajArgumente() {
		StringBuilder poredaniArgumenti = new StringBuilder();
		boolean pronadenZPS = false;
		boolean pronadenZK = false;
		boolean pronadenZS = false;
		boolean pronadenZVR = false;
		boolean pronadenZOD = false;

		for (int i = 0; i < _argumenti.length; i += 2) {
			if (_argumenti[i].equals("--zps")) {
				poredaniArgumenti.append(_argumenti[i]).append(" ").append(_argumenti[i + 1]);
				pronadenZPS = true;
				break;
			}
		}
		poredaniArgumenti.append(" ");
		for (int i = 0; i < _argumenti.length; i += 2) {
			if (_argumenti[i].equals("--zk")) {
				poredaniArgumenti.append(_argumenti[i]).append(" ").append(_argumenti[i + 1]);
				pronadenZK = true;
				break;
			}
		}
		poredaniArgumenti.append(" ");
		for (int i = 0; i < _argumenti.length; i += 2) {
			if (_argumenti[i].equals("--zs")) {
				poredaniArgumenti.append(_argumenti[i]).append(" ").append(_argumenti[i + 1]);
				pronadenZS = true;
				break;
			}
		}
		poredaniArgumenti.append(" ");
		for (int i = 0; i < _argumenti.length; i += 2) {
			if (_argumenti[i].equals("--zod")) {
				poredaniArgumenti.append(_argumenti[i]).append(" ").append(_argumenti[i + 1]);
				pronadenZOD = true;
				break;
			}
		}
		poredaniArgumenti.append(" ");
		for (int i = 0; i < _argumenti.length; i += 2) {
			if (_argumenti[i].equals("--zvr")) {
				poredaniArgumenti.append(_argumenti[i]).append(" ").append(_argumenti[i + 1]);
				pronadenZVR = true;
				break;
			}
		}

		if (pronadenZK == false || pronadenZPS == false || pronadenZS == false || pronadenZVR == false
				|| pronadenZOD == false) {
			System.out.println("Nisu uneseni dobri argumenti kod pokretanjat!");
			return false;
		}

		_argumenti = poredaniArgumenti.toString().split(" ");
		return true;
	}

	private boolean ucitajDatotekuStanica(String nazivDatoteke) {
		List<String[]> sadrzajDatoteke = procitajDatoteku(nazivDatoteke);
		KreatorObjekata kreator = new KreatorObjekata();

		if (sadrzajDatoteke == null)
			return false;
		for (String[] redak : sadrzajDatoteke) {
			if (!provjeriBrojAtributaStanica(redak))
				continue;
			if (!provjeriIspravnostAtributaStanica(redak))
				continue;
			kreator.unesiStanicu(redak);
		}
		return true;
	}

	private boolean ucitajDatotekuVozila(String nazivDatoteke) {
		List<String[]> sadrzajDatoteke = procitajDatoteku(nazivDatoteke);
		KreatorObjekata kreator = new KreatorObjekata();

		if (sadrzajDatoteke == null)
			return false;
		for (String[] redak : sadrzajDatoteke) {
			if (!provjeriBrojAtributaVozila(redak))
				continue;
			if (!provjeriIspravnostAtributaVozila(redak))
				continue;
			kreator.kreirajVozilo(redak);
		}
		return true;
	}

	private boolean ucitajDatotekuKompozicije(String nazivDatoteke) {
		List<String[]> sadrzajDatoteke = procitajDatoteku(nazivDatoteke);
		KreatorObjekata kreator = new KreatorObjekata();
		kreator.pridruziCitac(this);

		if (sadrzajDatoteke == null)
			return false;
		for (String[] redak : sadrzajDatoteke) {
			if (!provjeriBrojAtributaKompozicije(redak))
				continue;
			if (!provjeriIspravnostAtributaKompozicije(redak))
				continue;
			kreator.gradiKompoziciju(redak);
		}

		kreator.spremiKompoziciju();
		return true;
	}

	private boolean ucitajDatotekuDani(String nazivDatoteke) {
		List<String[]> sadrzajDatoteke = procitajDatoteku(nazivDatoteke);
		KreatorObjekata kreator = new KreatorObjekata();

		if (sadrzajDatoteke == null)
			return false;
		for (String[] redak : sadrzajDatoteke) {
			if (!provjeriBrojAtributaDani(redak))
				continue;
			if (!provjeriIspravnostAtributaDani(redak))
				continue;
			kreator.spremiOznakuDana(redak);
		}
		return true;
	}

	private boolean ucitajDatotekuVozniRed(String nazivDatoteke) {
		List<String[]> sadrzajDatoteke = procitajDatoteku(nazivDatoteke);
		KreatorObjekata kreator = new KreatorObjekata();

		if (sadrzajDatoteke == null)
			return false;
		for (String[] redak : sadrzajDatoteke) {
			if (!provjeriBrojAtributaVozniRed(redak))
				continue;
			if (!provjeriIspravnostAtributaVozniRed(redak))
				continue;
			kreator.dodajVlakUComposite(redak);
		}
		HashMap<String, Vlak> vlakovi = kreator.dajPopisVlakova();
		dodajVlakoveUVozniRed(vlakovi);

		return true;
	}

	private void dodajVlakoveUVozniRed(HashMap<String, Vlak> vlakovi) {
		// int bv = 0; // TEST
		VozniRed vozniRed = new VozniRed();
		for (Vlak v : vlakovi.values()) {
			v.poredajEtape();
			v.provjeriLogicnost();
			if (v.getLogican()) {
				// bv++;
				// System.out.println(bv);
				vozniRed.add(v);
			} else {
				this._globalniBrojGreski++;
				this._brojGreskiVozniRed++;

				var ispisGreske = new StringBuilder();

				ispisGreske.append("G: ").append(_globalniBrojGreski).append(" L: ").append(_brojGreskiVozniRed)
						.append(" ").append("Vlak ").append(v.getOznaka()).append(" nije logičan.  ")
						.append(v.dajRazlog());
				System.out.println(ispisGreske.toString());
			}
		}
		TvrtkaZaPrijevoz.setVozniRed(vozniRed);
		// TEST
		// TvrtkaZaPrijevoz.getVozniRed().izvrsi();
	}

	private boolean provjeriIspravnostAtributaStanica(String[] redak) {
		boolean ispravniRedak = true;
		StringBuilder pogresneVrijednosti = new StringBuilder();

		if (!redak[0].matches("^[A-Za-zČčĆćŽžŠšĐđ\\- ]+$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[0]).append("|");
		}
		if (!redak[1].matches("^[A-Za-z0-9]+$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[1]).append("|");
		}
		if (!redak[2].matches("^(kol\\.|staj\\.)$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[2]).append("|");
		}
		if (!redak[3].matches("^(O|Z)$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[3]).append("|");
		}
		if (!redak[4].matches("^(DA|NE)$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[4]).append("|");
		}
		if (!redak[5].matches("^(DA|NE)$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[5]).append("|");
		}
		if (!redak[6].matches("^(L|R|M)$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[6]).append("|");
		}
		if (!redak[7].matches("^(0?[1-9]|[1-9][0-9])$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[7]).append("|");
		}
		if (!redak[8].matches("^(K|E)$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[8]).append("|");
		}
		if (!redak[9].matches("^(1|2)$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[9]).append("|");
		}
		if (!redak[10].matches("^(10(,[0-9]{1,2})?|[1][1-9](,[0-9]{1,2})?|[2-4][0-9](,[0-9]{1,2})?|50(,[0]{1,2})?)$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[10]).append("|");
		}
		if (!redak[11].matches("^(2(,[0-9]{1,2})?|[3-9](,[0-9]{1,2})?|10(,[0]{1,2})?)$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[11]).append("|");
		}
		if (!redak[12].matches("^(I|K|T|Z)$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[12]).append("|");
		}
		if (!redak[13].matches("^(0|[1-9][0-9]{0,2}|[1-9][0-9]{0,2}|999)$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[13]).append("|");
		}
		if (redak.length > 14) {
			if (!redak[14].matches("^(\\d+|)$")) {
				ispravniRedak = false;
				pogresneVrijednosti.append(redak[14]).append("|");
			}
		}
		if (redak.length > 15) {
			if (!redak[15].matches("^(\\d+|)$")) {
				ispravniRedak = false;
				pogresneVrijednosti.append(redak[15]).append("|");
			}
		}
		if (redak.length > 16) {
			if (!redak[16].matches("^(\\d+|)$")) {
				ispravniRedak = false;
				pogresneVrijednosti.append(redak[16]).append("|");
			}
		}

		if (!ispravniRedak) {
			_globalniBrojGreski++;
			_brojGreskiStanice++;

			String sadrzaj = pretvoriRedakUString(redak);
			var ispisGreske = new StringBuilder();
			String pv = pogresneVrijednosti.toString().trim();
			pv = pv.substring(0, pv.length() - 1);

			ispisGreske.append("G: ").append(_globalniBrojGreski).append(" L: ").append(_brojGreskiStanice).append(" ")
					.append(sadrzaj).append(" Neispravne vrijednosti: " + pv);
			System.out.println(ispisGreske.toString());
		}

		return ispravniRedak;
	}

	private boolean provjeriIspravnostAtributaVozila(String[] redak) {
		boolean ispravniRedak = true;
		StringBuilder pogresneVrijednosti = new StringBuilder();

		if (!redak[0].matches("^[A-Za-z0-9\\- ]+$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[0]).append("|");
		}
		if (!redak[1].matches("^[^;]+$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[1]).append("|");
		}
		if (!redak[2].matches("^[^;]+$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[2]).append("|");
		}
		if (!redak[3].matches("^\\d{4}$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[3]).append("|");
		}
		if (!redak[4].matches("^(PSVP|PSVPVK|PSBP)$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[4]).append("|");
		}
		if (!redak[5].matches("^[A-Z]+$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[5]).append("|");
		}
		if (!redak[6].matches("^(N|D|B|E)$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[6]).append("|");
		}
		if (!redak[7].matches("^(1?[0-9]{1,2}|200)$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[7]).append("|");
		}
		if (!redak[8].matches("^(-1|[0-9](,[0-9]{1,2})?|10(,[0]{1,2})?)$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[8]).append("|");
		}
		if (!redak[9].matches("^\\d*$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[9]).append("|");
		}
		if (!redak[10].matches("^\\d*$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[10]).append("|");
		}
		if (!redak[11].matches("^\\d*$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[11]).append("|");
		}
		if (!redak[12].matches("^\\d*$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[12]).append("|");
		}
		if (!redak[13].matches("^\\d*$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[13]).append("|");
		}
		if (!redak[14].matches("^\\d+(\\,\\d+)?$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[14]).append("|");
		}
		if (!redak[15].matches("^\\d+(\\,\\d+)?$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[15]).append("|");
		}
		if (!redak[16].matches("^\\d+(\\,\\d+)?$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[16]).append("|");
		}
		if (!redak[17].matches("^(K|I)$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[17]).append("|");
		}

		if (!ispravniRedak) {
			_globalniBrojGreski++;
			_brojGreskiVozila++;

			String sadrzaj = pretvoriRedakUString(redak);
			var ispisGreske = new StringBuilder();
			String pv = pogresneVrijednosti.toString().trim();
			pv = pv.substring(0, pv.length() - 1);

			ispisGreske.append("G: ").append(_globalniBrojGreski).append(" L: ").append(_brojGreskiVozila).append(" ")
					.append(sadrzaj).append(" Neispravne vrijednosti: " + pv);
			System.out.println(ispisGreske.toString());
		}

		return ispravniRedak;
	}

	private boolean provjeriIspravnostAtributaKompozicije(String[] redak) {
		boolean ispravniRedak = true;
		StringBuilder pogresneVrijednosti = new StringBuilder();

		if (!redak[0].matches("^[^;]+$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[0]).append("|");
		}
		if (!redak[1].matches("^[^;]+$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[1]).append("|");
		}
		if (!redak[2].matches("^(P|V)$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[2]).append("|");
		}

		if (!ispravniRedak) {
			_globalniBrojGreski++;
			_brojGreskiKompozicije++;

			String sadrzaj = pretvoriRedakUString(redak);
			var ispisGreske = new StringBuilder();
			String pv = pogresneVrijednosti.toString().trim();
			pv = pv.substring(0, pv.length() - 1);

			ispisGreske.append("G: ").append(_globalniBrojGreski).append(" L: ").append(_brojGreskiKompozicije)
					.append(" ").append(sadrzaj).append(" Neispravne vrijednosti: " + pv);
			System.out.println(ispisGreske.toString());
		}

		return ispravniRedak;
	}

	private boolean provjeriIspravnostAtributaDani(String[] redak) {
		boolean ispravniRedak = true;
		StringBuilder pogresneVrijednosti = new StringBuilder();

		if (!redak[0].matches("^\\d+$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[0]).append("|");

		}
		if (redak.length > 1) {
			if (!redak[1].matches("^(?:(Po|U|Sr|Č|Pe|Su|N)(?!.*\\1))*$")) {
				ispravniRedak = false;
				pogresneVrijednosti.append(redak[1]).append("|");
			}
		}

		if (!ispravniRedak) {
			_globalniBrojGreski++;
			_brojGreskiDani++;

			String sadrzaj = pretvoriRedakUString(redak);
			var ispisGreske = new StringBuilder();
			String pv = pogresneVrijednosti.toString().trim();
			pv = pv.substring(0, pv.length() - 1);

			ispisGreske.append("G: ").append(_globalniBrojGreski).append(" L: ").append(_brojGreskiDani).append(" ")
					.append(sadrzaj).append(" Neispravne vrijednosti: " + pv);
			System.out.println(ispisGreske.toString());
		}

		return ispravniRedak;
	}

	private boolean provjeriIspravnostAtributaVozniRed(String[] redak) {
		boolean ispravniRedak = true;
		StringBuilder pogresneVrijednosti = new StringBuilder();

		if (!redak[0].matches("^[A-Za-z0-9]+$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[0]).append("|");
		}
		if (!redak[1].matches("^(N|O)$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[1]).append("|");
		}
		if (!redak[2].matches("^[A-Za-zČčĆćŽžŠšĐđ\\- ]*$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[2]).append("|");
		}
		if (!redak[3].matches("^[A-Za-zČčĆćŽžŠšĐđ\\- ]*$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[3]).append("|");
		}
		if (!redak[4].matches("^[^;]+$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[4]).append("|");
		}
		if (!redak[5].matches("^(U|B)?$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[5]).append("|");
		}
		if (!redak[6].matches("^([01]?\\d|2[0-3]):[0-5]\\d$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[6]).append("|");
		}
		if (!redak[7].matches("^([01]?\\d|2[0-3]):[0-5]\\d$")) {
			ispravniRedak = false;
			pogresneVrijednosti.append(redak[7]).append("|");
		}
		if (redak.length > 8) {
			if (!redak[8].matches("^\\d*$")) {
				ispravniRedak = false;
				pogresneVrijednosti.append(redak[8]).append("|");
			}

		}

		if (!ispravniRedak) {
			_globalniBrojGreski++;
			_brojGreskiVozniRed++;

			String sadrzaj = pretvoriRedakUString(redak);
			var ispisGreske = new StringBuilder();
			String pv = pogresneVrijednosti.toString().trim();
			pv = pv.substring(0, pv.length() - 1);

			ispisGreske.append("G: ").append(_globalniBrojGreski).append(" L: ").append(_brojGreskiVozniRed).append(" ")
					.append(sadrzaj).append(" Neispravne vrijednosti: " + pv);
			System.out.println(ispisGreske.toString());
		}

		return ispravniRedak;
	}

	private boolean provjeriBrojAtributaStanica(String[] redak) {
		if (redak.length < 14 && redak.length > 17) {
			_globalniBrojGreski++;
			_brojGreskiStanice++;
			String sadrzaj = pretvoriRedakUString(redak);

			var ispisGreske = new StringBuilder();
			ispisGreske.append("G: ").append(_globalniBrojGreski).append(" L: ").append(_brojGreskiStanice).append(" ")
					.append(sadrzaj).append(" Neispravan broj atributa u retku.");
			System.out.println(ispisGreske.toString());
			return false;
		}
		return true;
	}

	private boolean provjeriBrojAtributaVozila(String[] redak) {
		if (redak.length != 18) {
			_globalniBrojGreski++;
			_brojGreskiVozila++;
			String sadrzaj = pretvoriRedakUString(redak);

			var ispisGreske = new StringBuilder();
			ispisGreske.append("G: ").append(_globalniBrojGreski).append(" L: ").append(_brojGreskiVozila).append(" ")
					.append(sadrzaj).append(" Neispravan broj atributa u retku.");
			System.out.println(ispisGreske.toString());
			return false;
		}
		return true;
	}

	private boolean provjeriBrojAtributaKompozicije(String[] redak) {
		if (redak.length != 3) {
			_globalniBrojGreski++;
			_brojGreskiKompozicije++;
			String sadrzaj = pretvoriRedakUString(redak);

			var ispisGreske = new StringBuilder();
			ispisGreske.append("G: ").append(_globalniBrojGreski).append(" L: ").append(_brojGreskiKompozicije)
					.append(" ").append(sadrzaj).append(" Neispravan broj atributa u retku.");
			System.out.println(ispisGreske.toString());
			return false;
		}
		return true;
	}

	private boolean provjeriBrojAtributaDani(String[] redak) {
		if (redak.length < 1 || redak.length > 2) {
			_globalniBrojGreski++;
			_brojGreskiDani++;
			String sadrzaj = pretvoriRedakUString(redak);

			var ispisGreske = new StringBuilder();
			ispisGreske.append("G: ").append(_globalniBrojGreski).append(" L: ").append(_brojGreskiDani).append(" ")
					.append(sadrzaj).append(" Neispravan broj atributa u retku.");
			System.out.println(ispisGreske.toString());
			return false;
		}
		return true;
	}

	private boolean provjeriBrojAtributaVozniRed(String[] redak) {
		if (redak.length != 8 && redak.length != 9) {
			_globalniBrojGreski++;
			_brojGreskiVozniRed++;
			String sadrzaj = pretvoriRedakUString(redak);

			var ispisGreske = new StringBuilder();
			ispisGreske.append("G: ").append(_globalniBrojGreski).append(" L: ").append(_brojGreskiVozniRed).append(" ")
					.append(sadrzaj).append(" Neispravan broj atributa u retku.");
			System.out.println(ispisGreske.toString());
			return false;
		}
		return true;
	}

	private String pretvoriRedakUString(String[] redak) {
		StringBuilder sadrzaj = new StringBuilder();
		for (String zapis : redak)
			sadrzaj.append(zapis).append(";");

		if (sadrzaj.length() > 0)
			sadrzaj.setLength(sadrzaj.length() - 1);

		return sadrzaj.toString();
	}

	private List<String[]> procitajDatoteku(String nazivDatoteke) {
		List<String[]> sadrzajDatoteke = new ArrayList<>();
		String redak;
		boolean prviRedak = true;

		try (BufferedReader br = new BufferedReader(new FileReader(nazivDatoteke))) {
			while ((redak = br.readLine()) != null) {
				if (redak.trim().replace(";", "").isEmpty())
					continue;
				if (redak.trim().startsWith("#"))
					continue;
				String[] stupci = redak.split(";");
				if (prviRedak && (stupci[0].contains("Oznaka") || stupci[0].contains("Stanica")
						|| stupci[0].contains("Oznaka dana") || stupci[0].contains("Oznaka pruge"))) {
					prviRedak = false;
					continue;
				}
				sadrzajDatoteke.add(stupci);
			}
			return sadrzajDatoteke;
		} catch (IOException e) {
			System.out.println("Nije moguće pronaći datoteku.");
		}
		return null;
	}

	public void povecajBrojGreskiKompozicije() {
		this._globalniBrojGreski++;
		this._brojGreskiKompozicije++;
	}

	public int dajBrojGreskiKompozicije() {
		return this._brojGreskiKompozicije;
	}

	public int dajGlobalniBrojGreski() {
		return this._globalniBrojGreski;
	}
}
