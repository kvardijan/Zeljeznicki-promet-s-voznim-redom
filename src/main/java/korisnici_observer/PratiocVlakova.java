package korisnici_observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import podatkovneKlase.PodaciIVRV;

public class PratiocVlakova implements IKorisnikSubjekt {
	private HashMap<String, List<IKorisnikObserver>> observers = new HashMap<>();

	// dogadaj: oznakaVlaka || oznakaVlaka-stanica
	@Override
	public void dodajObserver(IKorisnikObserver o, String dogadaj) {
		if (this.observers.containsKey(dogadaj)) {
			this.observers.get(dogadaj).add(o);
		} else {
			List<IKorisnikObserver> observersDogadaja = new ArrayList<>();
			observersDogadaja.add(o);
			observers.put(dogadaj, observersDogadaja);
		}
	}

	@Override
	public void makniObserver(IKorisnikObserver o, String dogadaj) {

	}

	public void obavjestiObservereDogadaja(String dogadaj, PodaciIVRV podaciIVRV) {
		if (this.observers.containsKey(dogadaj)) {
			for (IKorisnikObserver o : this.observers.get(dogadaj)) {
				o.azuriraj(podaciIVRV);
			}
		}
	}
}
