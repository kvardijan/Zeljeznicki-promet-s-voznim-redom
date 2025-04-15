package kalkulatorCijene_strategy;

import karta_memento.Karta;

public class KalkulatorBlagajna implements IKalkulatorCijene {

	@Override
	public float izracunajCijenu(float izvornaCijena, float modifikacijaCijene, Karta karta) {
		karta.setNacinKupovanja("B");
		karta.setPopustWebMob(0.0f);
		karta.setUvecanjeVlak(0.0f);
		
		izvornaCijena = izvornaCijena + (izvornaCijena * modifikacijaCijene);
		izvornaCijena = Math.round(izvornaCijena * 100.0f) / 100.0f;
		
		return izvornaCijena;
	}

}
