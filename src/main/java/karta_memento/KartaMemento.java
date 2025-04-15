package karta_memento;

import java.util.List;

public class KartaMemento {
	private List<String> oznakeVlakova;
	private List<String> vrsteVlakova;
	private String polaznaStanica;
	private String odredisnaStanica;
	private String datumPutovanja;
	private float popustVikend;
	private String vrijemePolaska;
	private String vrijemeDolaska;
	private float izvornaCijena;
	private String nacinKupovanja;
	private float popustWebMob;
	private float uvecanjeVlak;
	private float konacnaCijena;
	private String vrijemeIzdavanja;

	public KartaMemento(List<String> oznakeVlakova, List<String> vrsteVlakova, String polaznaStanica, String odredisnaStanica,
			String datumPutovanja, float popustVikend, String vrijemePolaska, String vrijemeDolaska,
			float izvornaCijena, String nacinKupovanja, float popustWebMob, float uvecanjeVlak, float konacnaCijena,
			String vrijemeIzdavanja) {
		super();
		this.oznakeVlakova = oznakeVlakova;
		this.vrsteVlakova = vrsteVlakova;
		this.polaznaStanica = polaznaStanica;
		this.odredisnaStanica = odredisnaStanica;
		this.datumPutovanja = datumPutovanja;
		this.popustVikend = popustVikend;
		this.vrijemePolaska = vrijemePolaska;
		this.vrijemeDolaska = vrijemeDolaska;
		this.izvornaCijena = izvornaCijena;
		this.nacinKupovanja = nacinKupovanja;
		this.popustWebMob = popustWebMob;
		this.uvecanjeVlak = uvecanjeVlak;
		this.konacnaCijena = konacnaCijena;
		this.vrijemeIzdavanja = vrijemeIzdavanja;
	}

	public List<String> getOznakaVlaka() {
		return oznakeVlakova;
	}

	public List<String> getVrstaVlaka() {
		return vrsteVlakova;
	}

	public String getPolaznaStanica() {
		return polaznaStanica;
	}

	public String getOdredisnaStanica() {
		return odredisnaStanica;
	}

	public String getDatumPutovanja() {
		return datumPutovanja;
	}

	public float getPopustVikend() {
		return popustVikend;
	}

	public String getVrijemePolaska() {
		return vrijemePolaska;
	}

	public String getVrijemeDolaska() {
		return vrijemeDolaska;
	}

	public float getIzvornaCijena() {
		return izvornaCijena;
	}

	public String getNacinKupovanja() {
		return nacinKupovanja;
	}

	public float getPopustWebMob() {
		return popustWebMob;
	}

	public float getUvecanjeVlak() {
		return uvecanjeVlak;
	}

	public float getKonacnaCijena() {
		return konacnaCijena;
	}

	public String getVrijemeIzdavanja() {
		return vrijemeIzdavanja;
	}

}
