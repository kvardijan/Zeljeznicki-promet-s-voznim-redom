package prijevoznoSredstvo_builder;

public class PrijevoznoSredstvo {
  private String oznaka;
  private String opis;
  private String proizvodac;
  private int godina;
  private String namjena;
  private String vrstaPrijevoza;
  private String vrstaPogona;
  private int maxBrzina;
  private float maxSnaga;
  private int brojSjedecihMjesta;
  private int brojStajacihMjesta;
  private int brojBicikala;
  private int brojKreveta;
  private int brojAutomobila;
  private float nosivost;
  private float povrsina;
  private float zapremnina;
  private String status;

  public PrijevoznoSredstvo() {}

  public PrijevoznoSredstvo(String oznaka, String opis, int godina, String namjena,
      String vrstaPrijevoza, String vrstaPogona, int maxBrzina) {
    this.oznaka = oznaka;
    this.opis = opis;
    this.godina = godina;
    this.namjena = namjena;
    this.vrstaPrijevoza = vrstaPrijevoza;
    this.vrstaPogona = vrstaPogona;
    this.maxBrzina = maxBrzina;
  }

  public String getOznaka() {
    return oznaka;
  }

  public void setOznaka(String oznaka) {
    this.oznaka = oznaka;
  }

  public String getOpis() {
    return opis;
  }

  public void setOpis(String opis) {
    this.opis = opis;
  }

  public String getProizvodac() {
    return proizvodac;
  }

  public void setProizvodac(String proizvodac) {
    this.proizvodac = proizvodac;
  }

  public int getGodina() {
    return godina;
  }

  public void setGodina(int godina) {
    this.godina = godina;
  }

  public String getNamjena() {
    return namjena;
  }

  public void setNamjena(String namjena) {
    this.namjena = namjena;
  }

  public String getVrstaPrijevoza() {
    return vrstaPrijevoza;
  }

  public void setVrstaPrijevoza(String vrstaPrijevoza) {
    this.vrstaPrijevoza = vrstaPrijevoza;
  }

  public String getVrstaPogona() {
    return vrstaPogona;
  }

  public void setVrstaPogona(String vrstaPogona) {
    this.vrstaPogona = vrstaPogona;
  }

  public int getMaxBrzina() {
    return maxBrzina;
  }

  public void setMaxBrzina(int maxBrzina) {
    this.maxBrzina = maxBrzina;
  }

  public float getMaxSnaga() {
    return maxSnaga;
  }

  public void setMaxSnaga(float maxSnaga) {
    this.maxSnaga = maxSnaga;
  }

  public int getBrojSjedecihMjesta() {
    return brojSjedecihMjesta;
  }

  public void setBrojSjedecihMjesta(int brojSjedecihMjesta) {
    this.brojSjedecihMjesta = brojSjedecihMjesta;
  }

  public int getBrojStajacihMjesta() {
    return brojStajacihMjesta;
  }

  public void setBrojStajacihMjesta(int brojStajacihMjesta) {
    this.brojStajacihMjesta = brojStajacihMjesta;
  }

  public int getBrojBicikala() {
    return brojBicikala;
  }

  public void setBrojBicikala(int brojBicikala) {
    this.brojBicikala = brojBicikala;
  }

  public int getBrojKreveta() {
    return brojKreveta;
  }

  public void setBrojKreveta(int brojKreveta) {
    this.brojKreveta = brojKreveta;
  }

  public int getBrojAutomobila() {
    return brojAutomobila;
  }

  public void setBrojAutomobila(int brojAutomobila) {
    this.brojAutomobila = brojAutomobila;
  }

  public float getNosivost() {
    return nosivost;
  }

  public void setNosivost(float nosivost) {
    this.nosivost = nosivost;
  }

  public float getPovrsina() {
    return povrsina;
  }

  public void setPovrsina(float povrsina) {
    this.povrsina = povrsina;
  }

  public float getZapremnina() {
    return zapremnina;
  }

  public void setZapremnina(float zapremnina) {
    this.zapremnina = zapremnina;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
