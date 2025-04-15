package vozniRed_composite;

import java.util.ArrayList;
import java.util.List;

import vozniRed_iterator.VlakIterator;
import vozniRed_visitor.IVozniRedVisitor;

public class VozniRed implements IVozniRedComponent {
	protected List<IVozniRedComponent> vlakovi = new ArrayList<>();

	@Override
	public void izvrsi() {
		System.out.println("Vozni red:");
		for (IVozniRedComponent vrc : vlakovi) {
			vrc.izvrsi();
		}
	}

	public boolean add(IVozniRedComponent komponenta) {
		this.vlakovi.add(komponenta);
		return true;
	}

	@Override
	public void accept(IVozniRedVisitor visitor) {
		visitor.visitElement(this);
	}

	public VlakIterator napraviIteratorVlakova() {
		return new VlakIterator(vlakovi);
	}

	public Vlak getVlak(String oznaka) {
		VlakIterator iterator = this.napraviIteratorVlakova();
		Vlak v = null;
		v = (Vlak) iterator.dajAko(oznaka);
		return v;
	}
}
