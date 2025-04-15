package vozniRed_visitor;

import stanica_prototype.Stanica;
import vozniRed_composite.Etapa;
import vozniRed_composite.Vlak;
import vozniRed_composite.VozniRed;

public interface IVozniRedVisitor {
	public void visitElement(VozniRed vozniRed);
	public void visitElement(Vlak vlak);
	public void visitElement(Etapa etapa);
	public void visitElement(Stanica stanica);
}
