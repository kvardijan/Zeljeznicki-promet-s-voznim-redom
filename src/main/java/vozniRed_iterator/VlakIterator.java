package vozniRed_iterator;

import java.util.List;

import vozniRed_composite.IVozniRedComponent;
import vozniRed_composite.Vlak;

public class VlakIterator implements IVozniRedIterator{
	protected int pozicija = 0;
	protected List<IVozniRedComponent> kolekcija;
	
	public VlakIterator(List<IVozniRedComponent> kolekcija) {
		this.kolekcija = kolekcija;
	}

	@Override
	public IVozniRedComponent prvi() {
		this.pozicija = 0;
		var c = kolekcija.get(this.pozicija);
		return c;
	}

	@Override
	public IVozniRedComponent daj() {
		var c = kolekcija.get(this.pozicija);
		return c;
	}

	@Override
	public void sljedeci() {
		this.pozicija++;
	}
	
	@Override
	public boolean gotovo() {
		return this.pozicija == kolekcija.size() ? true : false;
	}

	@Override
	public IVozniRedComponent trenutni() {
		var c = kolekcija.get(this.pozicija);
		return c;
	}
	
	public IVozniRedComponent dajAko(String oznaka) {
		this.pozicija = 0;
		while(!this.gotovo()) {
			Vlak c = (Vlak) kolekcija.get(this.pozicija);
			if(c.getOznaka().equals(oznaka)) return c;
			this.sljedeci();
		}
		return null;
	}
}
