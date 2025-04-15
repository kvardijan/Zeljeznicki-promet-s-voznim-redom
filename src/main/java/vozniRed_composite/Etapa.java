package vozniRed_composite;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import stanica_prototype.Stanica;
import vozniRed_iterator.StanicaIterator;
import vozniRed_visitor.IVozniRedVisitor;

public class Etapa implements IVozniRedComponent {
	protected List<IVozniRedComponent> stanice = new ArrayList<>();
	protected String oznaka;
	protected String vrijemePolaska;
	protected String trajanjeVoznje;
	protected int zbrojTrajanjaPoStanicamaNvlak;
	protected int zbrojTrajanjaPoStanicamaUvlak;
	protected int zbrojTrajanjaPoStanicamaBvlak;
	protected int duljina;
	protected boolean pon = false;
	protected boolean uto = false;
	protected boolean sri = false;
	protected boolean cet = false;
	protected boolean pet = false;
	protected boolean sub = false;
	protected boolean ned = false;
	protected String daniString;
	protected String smjer;

	public Etapa(String oznaka, String vp, String tv, String dani, String smjer) {
		this.oznaka = oznaka;
		this.vrijemePolaska = vp;
		this.trajanjeVoznje = tv;
		this.daniString = dani;
		obradiDane(dani);
		this.smjer = smjer;
	}

	@Override
	public void izvrsi() {
		System.out.println(oznaka + " vp: " + vrijemePolaska);
		for (IVozniRedComponent vrc : stanice) {
			vrc.izvrsi();
		}
	}

	public boolean add(IVozniRedComponent komponenta) {
		this.stanice.add(komponenta);
		return true;
	}

	@Override
	public void accept(IVozniRedVisitor visitor) {
		visitor.visitElement(this);
	}

	public StanicaIterator napraviIteratorStanica() {
		return new StanicaIterator(stanice);
	}

	public String getVrijemePolaska() {
		return this.vrijemePolaska;
	}

	public int getZbrojTrajanjaPoStanicamaNvlak() {
		return zbrojTrajanjaPoStanicamaNvlak;
	}

	public void setZbrojTrajanjaPoStanicamaNvlak(int zbrojTrajanjaPoStanicamaNvlak) {
		this.zbrojTrajanjaPoStanicamaNvlak = zbrojTrajanjaPoStanicamaNvlak;
	}

	public int getZbrojTrajanjaPoStanicamaUvlak() {
		return zbrojTrajanjaPoStanicamaUvlak;
	}

	public void setZbrojTrajanjaPoStanicamaUvlak(int zbrojTrajanjaPoStanicamaUvlak) {
		this.zbrojTrajanjaPoStanicamaUvlak = zbrojTrajanjaPoStanicamaUvlak;
	}

	public int getZbrojTrajanjaPoStanicamaBvlak() {
		return zbrojTrajanjaPoStanicamaBvlak;
	}

	public void setZbrojTrajanjaPoStanicamaBvlak(int zbrojTrajanjaPoStanicamaBvlak) {
		this.zbrojTrajanjaPoStanicamaBvlak = zbrojTrajanjaPoStanicamaBvlak;
	}

	public String getZavrsnoVrijemeEtape() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("H:mm");
		LocalTime pocetno = LocalTime.parse(this.vrijemePolaska, format);
		LocalTime trajanje = LocalTime.parse(this.trajanjeVoznje, format);
		Duration d1 = Duration.between(LocalTime.MIN, pocetno);
		Duration d2 = Duration.between(LocalTime.MIN, trajanje);
		Duration ukd = d1.plus(d2);
		LocalTime zavrsno = LocalTime.MIN.plus(ukd);
		return zavrsno.format(format);
	}

	public String getTrajanjeVoznje() {
		return this.trajanjeVoznje;
	}

	public Duration getTrajanjeVoznjeDuration() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("H:mm");
		LocalTime trajanje = LocalTime.parse(this.trajanjeVoznje, format);
		Duration d = Duration.between(LocalTime.MIN, trajanje);
		return d;
	}

	public Stanica getPrvaStanica() {
		StanicaIterator iterator = this.napraviIteratorStanica();
		iterator.prvi();
		return (Stanica) iterator.daj();
	}

	public Stanica getZadnjaStanica() {
		StanicaIterator iterator = this.napraviIteratorStanica();
		iterator.zadnji();
		return (Stanica) iterator.daj();
	}

	public int getDuljina() {
		return duljina;
	}

	public void setDuljina(int duljina) {
		this.duljina = duljina;
	}

	private void obradiDane(String dani) {
		if (dani.contains("Po"))
			pon = true;
		if (dani.contains("U"))
			uto = true;
		if (dani.contains("Sr"))
			sri = true;
		if (dani.contains("Č"))
			cet = true;
		if (dani.contains("Pe"))
			pet = true;
		if (dani.contains("Su"))
			sub = true;
		if (dani.contains("N"))
			ned = true;
	}

	public String getOznaka() {
		return oznaka;
	}

	public void setOznaka(String oznaka) {
		this.oznaka = oznaka;
	}

	public String getDaniString() {
		return daniString;
	}
	
	public String getSmjerString() {
		return smjer;
	}

	public boolean[] dajRadneDaneEtape() {
		boolean radniDani[] = { this.pon, this.uto, this.sri, this.cet, this.pet, this.sub, this.ned };
		return radniDani;
	}
}
