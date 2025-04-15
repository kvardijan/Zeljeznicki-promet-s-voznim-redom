package stanica_prototype;

import vozniRed_composite.IVozniRedComponent;
import vozniRed_visitor.IVozniRedVisitor;

public class Stanica implements IStanicaPrototype, IVozniRedComponent {
	public String naziv;
	public String vrstaStanice;
	public String statusStanice;
	public boolean ulazIzlazPutnika;
	public boolean ulazIzlazRobe;
	public int brojPerona;
	public boolean stajalisteUbrzani;
	public boolean stajalisteBrzi;

	public Stanica() {
		this.stajalisteBrzi = false;
		this.stajalisteUbrzani = false;
	}

	public Stanica(Stanica stanica) {
		naziv = stanica.naziv;
		vrstaStanice = stanica.vrstaStanice;
		statusStanice = stanica.statusStanice;
		ulazIzlazPutnika = stanica.ulazIzlazPutnika;
		ulazIzlazRobe = stanica.ulazIzlazRobe;
		brojPerona = stanica.brojPerona;
		stajalisteBrzi = stanica.stajalisteBrzi;
		stajalisteUbrzani = stanica.stajalisteUbrzani;
	}

	public Stanica clone() {
		return new Stanica(this);
	}

	@Override
	public boolean equals(Object objekt2) {
		if (!(objekt2 instanceof Stanica))
			return false;
		Stanica stanica2 = (Stanica) objekt2;
		return stanica2.naziv.equals(naziv) && stanica2.vrstaStanice.equals(vrstaStanice)
				&& stanica2.statusStanice.equals(statusStanice) && stanica2.ulazIzlazPutnika == ulazIzlazPutnika
				&& stanica2.ulazIzlazRobe == ulazIzlazRobe && stanica2.brojPerona == brojPerona
				&& stanica2.stajalisteBrzi == stajalisteBrzi && stanica2.stajalisteUbrzani == stajalisteUbrzani;
	}

	@Override
	public void izvrsi() {
		System.out.println(naziv);
	}

	@Override
	public void accept(IVozniRedVisitor visitor) {
		visitor.visitElement(this);

	}
}
