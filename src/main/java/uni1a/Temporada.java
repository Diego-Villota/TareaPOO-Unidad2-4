package uni1a;

/** Una temporada pertenece a una sola serie y es creada por ella. */
public final class Temporada {
    private final int numero;
    private final int episodios;
    Temporada(int numero, int episodios) {
        if (numero < 1 || episodios < 1) throw new IllegalArgumentException("Numero y episodios deben ser positivos");
        this.numero = numero; this.episodios = episodios;
    }
    public int getNumero() { return numero; }
    public int getEpisodios() { return episodios; }
    @Override public String toString() { return "Temporada " + numero + " (" + episodios + " episodios)"; }
}
