package kompozicija_builder;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import prijevoznoSredstvo_builder.PrijevoznoSredstvo;

public class Kompozicija {
	private String oznaka;
	private boolean validna;
	private List<AbstractMap.SimpleEntry<String, PrijevoznoSredstvo>> listaVozila = new ArrayList<>();

	public Kompozicija() {
	}

	public String getOznaka() {
		return oznaka;
	}

	public void setOznaka(String oznaka) {
		this.oznaka = oznaka;
	}

	public boolean isValidna() {
		return validna;
	}

	public void setValidna(boolean validna) {
		this.validna = validna;
	}

	public void dodajPrijevoznoSredstvoUKompoziciju(String uloga, PrijevoznoSredstvo prijevoznoSredstvo) {
		listaVozila.add(new AbstractMap.SimpleEntry<>(uloga, prijevoznoSredstvo));
		this.validna = provjeriValjanostKompozicije();
	}

	public List<AbstractMap.SimpleEntry<String, PrijevoznoSredstvo>> getListaVozila() {
		return this.listaVozila;
	}

	private boolean provjeriValjanostKompozicije() {
		return prvoVoziloImaPogon() && postojiVagon() && postojiViseVozila() && svaPogonskaVozilaNaPocetku();
	}

	private boolean svaPogonskaVozilaNaPocetku() {
		boolean sviPogonskiNaprijed = true;
		boolean pronadenVagon = false;

		for (AbstractMap.SimpleEntry<String, PrijevoznoSredstvo> entry : listaVozila) {
			if (entry.getKey().equals("P")) {
				if (pronadenVagon) {
					sviPogonskiNaprijed = false;
					break;
				}
			} else if (entry.getKey().equals("V")) {
				pronadenVagon = true;
			}
		}
		return sviPogonskiNaprijed;
	}

	private boolean postojiViseVozila() {
		return listaVozila.size() >= 2;
	}

	private boolean prvoVoziloImaPogon() {
		return listaVozila.getFirst().getKey().equals("P");
	}

	private boolean postojiVagon() {
		boolean postojiVagon = false;
		for (AbstractMap.SimpleEntry<String, PrijevoznoSredstvo> vozilo : listaVozila) {
			if (vozilo.getKey().equals("V")) {
				postojiVagon = true;
				break;
			}
		}
		return postojiVagon;
	}
}
