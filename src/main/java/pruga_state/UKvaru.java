package pruga_state;

public class UKvaru implements IStanjePruge {
	private static String STANJE = "U kvaru";
	private static String STANJE_SKRACENO = "K";

	@Override
	public boolean staviUKvar(Pruga pruga, String smjer) {
		System.out.println("Pruga " + pruga.oznaka + " je vec u kvaru.");
		return false;
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
		pruga.postaviStanjePruge(new Ispravna(), smjer);
		return true;
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
