package kalkulatorCijene_strategy;

import karta_memento.Karta;
import tvrtkaZaPrijevoz_singleton.TvrtkaZaPrijevoz;

public class KalkulatorVlak implements IKalkulatorCijene {

	@Override
	public float izracunajCijenu(float izvornaCijena, float modifikacijaCijene, Karta karta) {
		karta.setNacinKupovanja("V");
		karta.setPopustWebMob(0.0f);
		karta.setUvecanjeVlak(TvrtkaZaPrijevoz.getCjenik().uvecanjeVlak);

		modifikacijaCijene += TvrtkaZaPrijevoz.getCjenik().uvecanjeVlak;
		izvornaCijena = izvornaCijena + (izvornaCijena * modifikacijaCijene);
		izvornaCijena = Math.round(izvornaCijena * 100.0f) / 100.0f;

		return izvornaCijena;
	}

}
