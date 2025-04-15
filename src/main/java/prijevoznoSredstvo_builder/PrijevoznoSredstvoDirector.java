package prijevoznoSredstvo_builder;

public class PrijevoznoSredstvoDirector {
  private PrijevoznoSredstvoBuilder builder;

  public PrijevoznoSredstvoDirector(final PrijevoznoSredstvoBuilder builder) {
    this.builder = builder;
  }

  public PrijevoznoSredstvo napraviN(String oznaka, String opis, String proizvodac, int godina,
      String namjena, String vrstaPrijevoza, String vrstaPogona, int maxBrzina, float maxSnaga,
      String status) {

    builder
        .napraviPrijevoznoSredstvo(oznaka, opis, godina, namjena, vrstaPrijevoza, vrstaPogona,
            maxBrzina)
        .postaviProizvodaca(proizvodac).postaviMaxSnagu(maxSnaga).postaviStatus(status);
    return builder.dajPrijevoznoSredstvo();
  }

  public PrijevoznoSredstvo napraviP(String oznaka, String opis, String proizvodac, int godina,
      String namjena, String vrstaPrijevoza, String vrstaPogona, int maxBrzina,
      int brojSjedecihMjesta, int brojStajacihMjesta, int brojBicikala, String status) {

    builder
        .napraviPrijevoznoSredstvo(oznaka, opis, godina, namjena, vrstaPrijevoza, vrstaPogona,
            maxBrzina)
        .postaviProizvodaca(proizvodac).postaviStatus(status)
        .postaviBrojSjedecihMjesta(brojSjedecihMjesta).postaviBrojStajacihMjesta(brojStajacihMjesta)
        .postaviBrojBicikala(brojBicikala);
    return builder.dajPrijevoznoSredstvo();
  }

  public PrijevoznoSredstvo napraviTA(String oznaka, String opis, String proizvodac, int godina,
      String namjena, String vrstaPrijevoza, String vrstaPogona, int maxBrzina, int brojAutomobila,
      float nosivost, float povrsina, String status) {

    builder
        .napraviPrijevoznoSredstvo(oznaka, opis, godina, namjena, vrstaPrijevoza, vrstaPogona,
            maxBrzina)
        .postaviProizvodaca(proizvodac).postaviStatus(status).postaviBrojAutomobila(brojAutomobila)
        .postaviNosivost(nosivost).postaviPovrsinu(povrsina);
    return builder.dajPrijevoznoSredstvo();
  }

  public PrijevoznoSredstvo napraviTK(String oznaka, String opis, String proizvodac, int godina,
      String namjena, String vrstaPrijevoza, String vrstaPogona, int maxBrzina, float nosivost,
      float povrsina, String status) {

    builder
        .napraviPrijevoznoSredstvo(oznaka, opis, godina, namjena, vrstaPrijevoza, vrstaPogona,
            maxBrzina)
        .postaviProizvodaca(proizvodac).postaviStatus(status).postaviNosivost(nosivost)
        .postaviPovrsinu(povrsina);
    return builder.dajPrijevoznoSredstvo();
  }

  public PrijevoznoSredstvo napraviTRS(String oznaka, String opis, String proizvodac, int godina,
      String namjena, String vrstaPrijevoza, String vrstaPogona, int maxBrzina, float nosivost,
      float povrsina, float zapremnina, String status) {

    builder
        .napraviPrijevoznoSredstvo(oznaka, opis, godina, namjena, vrstaPrijevoza, vrstaPogona,
            maxBrzina)
        .postaviProizvodaca(proizvodac).postaviStatus(status).postaviNosivost(nosivost)
        .postaviPovrsinu(povrsina).postaviZapremninu(zapremnina);
    return builder.dajPrijevoznoSredstvo();
  }

  public PrijevoznoSredstvo napraviTTS(String oznaka, String opis, String proizvodac, int godina,
      String namjena, String vrstaPrijevoza, String vrstaPogona, int maxBrzina, float nosivost,
      float povrsina, float zapremnina, String status) {

    builder
        .napraviPrijevoznoSredstvo(oznaka, opis, godina, namjena, vrstaPrijevoza, vrstaPogona,
            maxBrzina)
        .postaviProizvodaca(proizvodac).postaviStatus(status).postaviNosivost(nosivost)
        .postaviPovrsinu(povrsina).postaviZapremninu(zapremnina);
    return builder.dajPrijevoznoSredstvo();
  }

  public PrijevoznoSredstvo napravi(String oznaka, String opis, String proizvodac, int godina,
      String namjena, String vrstaPrijevoza, String vrstaPogona, int maxBrzina, float maxSnaga,
      int brojSjedecihMjesta, int brojStajacihMjesta, int brojBicikala, int brojKreveta,
      int brojAutomobila, float nosivost, float povrsina, float zapremnina, String status) {

    builder
        .napraviPrijevoznoSredstvo(oznaka, opis, godina, namjena, vrstaPrijevoza, vrstaPogona,
            maxBrzina)
        .postaviProizvodaca(proizvodac).postaviMaxSnagu(maxSnaga)
        .postaviBrojSjedecihMjesta(brojSjedecihMjesta)
        .postaviBrojStajacihMjesta(brojStajacihMjesta).postaviBrojBicikala(brojBicikala)
        .postaviBrojKreveta(brojKreveta).postaviBrojAutomobila(brojAutomobila)
        .postaviNosivost(nosivost).postaviPovrsinu(povrsina).postaviZapremninu(zapremnina)
        .postaviStatus(status);
    return builder.dajPrijevoznoSredstvo();
  }
}
