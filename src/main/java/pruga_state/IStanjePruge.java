package pruga_state;

public interface IStanjePruge {
	boolean staviUKvar(Pruga pruga, String smjer);
	boolean staviUZatvoreno(Pruga pruga, String smjer);
	boolean staviUTestiranje(Pruga pruga, String smjer);
	boolean staviUIspravno(Pruga pruga, String smjer);
	String ispisiStanje();
	String ispisiStanjeSkraceno();
}
