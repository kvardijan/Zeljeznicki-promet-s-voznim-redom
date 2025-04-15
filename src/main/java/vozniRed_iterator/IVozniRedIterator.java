package vozniRed_iterator;

import vozniRed_composite.IVozniRedComponent;

public interface IVozniRedIterator {
	public IVozniRedComponent prvi();
	public IVozniRedComponent daj();
	public void sljedeci();
	boolean gotovo();
	IVozniRedComponent trenutni();
}
