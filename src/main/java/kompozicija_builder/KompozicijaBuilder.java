package kompozicija_builder;

import prijevoznoSredstvo_builder.PrijevoznoSredstvo;

public class KompozicijaBuilder {
  private Kompozicija kompozicija = null;

  public KompozicijaBuilder() {}

  public KompozicijaBuilder napraviKompoziciju(String oznaka) {
    kompozicija = new Kompozicija();
    kompozicija.setOznaka(oznaka);
    return this;
  }

  public KompozicijaBuilder dodajPogon(PrijevoznoSredstvo pogon) {
    kompozicija.dodajPrijevoznoSredstvoUKompoziciju("P", pogon);
    return this;
  }

  public KompozicijaBuilder dodajVagon(PrijevoznoSredstvo vagon) {
    kompozicija.dodajPrijevoznoSredstvoUKompoziciju("V", vagon);
    return this;
  }

  public Kompozicija dajKompoziciju() {
    return this.kompozicija;
  }
}
