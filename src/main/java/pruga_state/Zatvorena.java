package pruga_state;

public class Zatvorena implements IStanjePruge {
	private static String STANJE = "Zatvorena";
	private static String STANJE_SKRACENO = "Z";

	@Override
	public boolean staviUKvar(Pruga pruga, String smjer) {
		System.out.println("Pruga " + pruga.oznaka + " je zatvorena i ne moze u kvar.");
		return false;
	}

	@Override
	public boolean staviUZatvoreno(Pruga pruga, String smjer) {
		System.out.println("Pruga " + pruga.oznaka + " je vec zatvorena.");
		return false;
	}

	@Override
	public boolean staviUTestiranje(Pruga pruga, String smjer) {
		pruga.postaviStanjePruge(new UTestiranju(), smjer);
		return true;
	}

	@Override
	public boolean staviUIspravno(Pruga pruga, String smjer) {
		System.out.println("Pruga " + pruga.oznaka + " je zatvorena i ne moze postati ispravna.");
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
