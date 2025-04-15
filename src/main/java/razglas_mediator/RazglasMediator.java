package razglas_mediator;

import java.util.ArrayList;
import java.util.List;

import podatkovneKlase.PodaciIEVD;
import podatkovneKlase.PodaciIVRV;
import stanica_prototype.Stanica;
import tvrtkaZaPrijevoz_singleton.TvrtkaZaPrijevoz;
import vozniRed_composite.Vlak;
import vozniRed_visitor.IEVDVisitor;
import vozniRed_visitor.IVRVVisitor;

public class RazglasMediator implements IRazglasMediator {

	@Override
	public void pretplatiRazlgasnikNaPratiocVlaka(String vlak, String dan) {
		Razglasnik razglasnik = new Razglasnik(vlak, dan);
		TvrtkaZaPrijevoz.getPratiocVlakova().dodajObserver(razglasnik, vlak);
	}

	@Override
	public List<PodaciIVRV> dohvatiVozniRedVlakaZaDan(String vlak, String dan) {
		Vlak v = TvrtkaZaPrijevoz.getVozniRed().getVlak(vlak);

		IVRVVisitor visitorIVRV = new IVRVVisitor(false);
		v.accept(visitorIVRV);
		List<PodaciIVRV> kompletniVozniRedVlaka = visitorIVRV.dajPodatkeVoznogRedaVlaka();

		IEVDVisitor visitorIEVD = new IEVDVisitor(dan, false);
		TvrtkaZaPrijevoz.getVozniRed().accept(visitorIEVD);
		List<PodaciIEVD> vlakoviZaDan = visitorIEVD.dajPodatkeOVlakovimaZaDane();

		return napraviTrazeniVozniRed(kompletniVozniRedVlaka, vlakoviZaDan, vlak);
	}

	private List<PodaciIVRV> napraviTrazeniVozniRed(List<PodaciIVRV> kompletniVozniRedVlaka,
			List<PodaciIEVD> vlakoviZaDan, String oznakaVlaka) {
		String trenutnaOznakaPruge = "";
		List<PodaciIVRV> vozniRed = new ArrayList<>();
		for (PodaciIVRV podaciIVRV : kompletniVozniRedVlaka) {
			trenutnaOznakaPruge = podaciIVRV.oznakaPruge;
			for (PodaciIEVD podaciIEVD : vlakoviZaDan) {
				if (podaciIEVD.oznakaVlaka.equals(oznakaVlaka) && podaciIEVD.oznakaPruge.equals(trenutnaOznakaPruge)) {
					vozniRed.add(podaciIVRV);
				}
			}
		}
		return vozniRed;
	}

	@Override
	public Stanica dajSljedecuStanicu(List<PodaciIVRV> vozniRed, PodaciIVRV podaciIVRV) {
		PodaciIVRV sljedecaStanica = null;
		boolean nadenaTrenutna = false;
		for (PodaciIVRV p : vozniRed) {
			if (p.stanica.equals(podaciIVRV.stanica)) {
				nadenaTrenutna = true;
				continue;
			}
			if (nadenaTrenutna) {
				sljedecaStanica = p;
				break;
			}
		}
		Stanica sljedecaStanicaObjekt = null;
		if (sljedecaStanica != null) {
			for (Stanica s : TvrtkaZaPrijevoz.getPopisStanica()) {
				if (s.naziv.equals(sljedecaStanica.stanica)) {
					sljedecaStanicaObjekt = s.clone();
				}
			}
		}
		return sljedecaStanicaObjekt;
	}
}
