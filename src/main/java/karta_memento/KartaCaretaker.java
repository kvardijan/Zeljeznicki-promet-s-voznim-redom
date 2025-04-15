package karta_memento;

import java.util.ArrayList;
import java.util.List;

public class KartaCaretaker {
	private List<Object> povijestKupljenihKarata = new ArrayList<>();

	public boolean pospremiMemento(Object object) {
		this.povijestKupljenihKarata.add(object);
		return true;
	}

	public List<Object> dajSveKupljeneKarte() {
		return this.povijestKupljenihKarata;
	}

	public Object dajKartu(int brojKarte) {
		try {
			return povijestKupljenihKarata.get(brojKarte - 1);
		}catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
}
