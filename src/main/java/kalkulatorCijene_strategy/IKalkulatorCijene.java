package kalkulatorCijene_strategy;

import karta_memento.Karta;

public interface IKalkulatorCijene {
	public float izracunajCijenu(float izvornaCijena, float modifikacijaCijene, Karta karta);
}
