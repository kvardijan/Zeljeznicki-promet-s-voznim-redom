package prijevoznoSredstvo_builder;

public class PrijevoznoSredstvoBuilder {
  private PrijevoznoSredstvo prijevoznoSredstvo;

  public PrijevoznoSredstvoBuilder() {}

  public PrijevoznoSredstvoBuilder napraviPrijevoznoSredstvo(String oznaka, String opis, int godina,
      String namjena, String vrstaPrijevoza, String vrstaPogona, int maxBrzina) {
    this.prijevoznoSredstvo = new PrijevoznoSredstvo(oznaka, opis, godina, namjena, vrstaPrijevoza,
        vrstaPogona, maxBrzina);
    return this;
  }
  
  public PrijevoznoSredstvoBuilder postaviProizvodaca(String proizvodac) {
    this.prijevoznoSredstvo.setProizvodac(proizvodac);
    return this;
  }
  
  public PrijevoznoSredstvoBuilder postaviMaxSnagu(float maxSnaga) {
    this.prijevoznoSredstvo.setMaxSnaga(maxSnaga);
    return this;
  }
  
  public PrijevoznoSredstvoBuilder postaviBrojSjedecihMjesta(int brojSjedecihMjesta) {
    this.prijevoznoSredstvo.setBrojSjedecihMjesta(brojSjedecihMjesta);
    return this;
  }
  
  public PrijevoznoSredstvoBuilder postaviBrojStajacihMjesta(int brojStajacihMjesta) {
    this.prijevoznoSredstvo.setBrojStajacihMjesta(brojStajacihMjesta);
    return this;
  }
  
  public PrijevoznoSredstvoBuilder postaviBrojBicikala(int brojBicikala) {
    this.prijevoznoSredstvo.setBrojBicikala(brojBicikala);
    return this;
  }
  
  public PrijevoznoSredstvoBuilder postaviBrojKreveta(int brojKreveta) {
    this.prijevoznoSredstvo.setBrojKreveta(brojKreveta);
    return this;
  }
  
  public PrijevoznoSredstvoBuilder postaviBrojAutomobila(int brojAutomobila) {
    this.prijevoznoSredstvo.setBrojAutomobila(brojAutomobila);
    return this;
  }
  
  public PrijevoznoSredstvoBuilder postaviNosivost(float nostivost) {
    this.prijevoznoSredstvo.setNosivost(nostivost);
    return this;
  }
  
  public PrijevoznoSredstvoBuilder postaviPovrsinu(float povrsina) {
    this.prijevoznoSredstvo.setPovrsina(povrsina);
    return this;
  }
  
  public PrijevoznoSredstvoBuilder postaviZapremninu(float zapremnina) {
    this.prijevoznoSredstvo.setZapremnina(zapremnina);
    return this;
  }
  
  public PrijevoznoSredstvoBuilder postaviStatus(String status) {
    this.prijevoznoSredstvo.setStatus(status);
    return this;
  }
  
  public PrijevoznoSredstvo dajPrijevoznoSredstvo() {
    return this.prijevoznoSredstvo;
  }
}
