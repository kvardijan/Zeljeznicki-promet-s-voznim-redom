package pruga_state;

public class Ispravna implements IStanjePruge {
	private static String STANJE = "Ispravna";
	private static String STANJE_SKRACENO = "I";

	@Override
	public boolean staviUKvar(Pruga pruga, String smjer) {
		pruga.postaviStanjePruge(new UKvaru(), smjer);
		return true;
	}

	@Override
	public boolean staviUZatvoreno(Pruga pruga, String smjer) {
		pruga.postaviStanjePruge(new Zatvorena(), smjer);
		return true;
	}

	@Override
	public boolean staviUTestiranje(Pruga pruga, String smjer) {
		pruga.postaviStanjePruge(new UTestiranju(), smjer);
		return true;
	}

	@Override
	public boolean staviUIspravno(Pruga pruga, String smjer) {
		System.out.println("Pruga " + pruga.oznaka + " je vec ispravna.");
		return false;
	}

	@Override
	public String ispisiStanje() {
		return STANJE;
	}

	@Override
	public String ispisiStanjeSkraceno() {
		return STANJE_SKRACENO;
	}

}
