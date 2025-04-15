package kalkulatorCijene_strategy;

import karta_memento.Karta;
import tvrtkaZaPrijevoz_singleton.TvrtkaZaPrijevoz;

public class KalkulatorWebMob implements IKalkulatorCijene {

	@Override
	public float izracunajCijenu(float izvornaCijena, float modifikacijaCijene, Karta karta) {
		karta.setNacinKupovanja("WM");
		karta.setPopustWebMob(TvrtkaZaPrijevoz.getCjenik().popustWebMob);
		karta.setUvecanjeVlak(0.0f);

		modifikacijaCijene += -TvrtkaZaPrijevoz.getCjenik().popustWebMob;
		izvornaCijena = izvornaCijena + (izvornaCijena * modifikacijaCijene);
		izvornaCijena = Math.round(izvornaCijena * 100.0f) / 100.0f;

		return izvornaCijena;
	}

}
