package razglas_mediator;

import java.util.List;

import podatkovneKlase.PodaciIVRV;
import stanica_prototype.Stanica;

public interface IRazglasMediator {
	void pretplatiRazlgasnikNaPratiocVlaka(String vlak, String dan);
	List<PodaciIVRV> dohvatiVozniRedVlakaZaDan(String vlak, String dan);
	Stanica dajSljedecuStanicu(List<PodaciIVRV> vozniRed, PodaciIVRV podaciIVRV);
}
