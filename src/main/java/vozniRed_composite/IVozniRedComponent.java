package vozniRed_composite;

import vozniRed_visitor.IVozniRedVisitor;

public interface IVozniRedComponent {
	public void izvrsi();
	public void accept (IVozniRedVisitor visitor);
}
