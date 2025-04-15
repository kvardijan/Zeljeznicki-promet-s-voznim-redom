package vozniRed_iterator;

import java.util.List;

import vozniRed_composite.IVozniRedComponent;

public class StanicaIterator implements IVozniRedIterator {
	protected int pozicija = 0;
	protected List<IVozniRedComponent> kolekcija;
	
	public StanicaIterator(List<IVozniRedComponent> kolekcija) {
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
	
	public void zadnji() {
		this.pozicija = kolekcija.size()-1;
	}
}
