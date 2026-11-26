package audiovisual.modelo;

import java.util.Objects;

/**
 * Representa una temporada perteneciente por composicion a una serie de TV.
 */
public final class Temporada {
    private final int numero;
    private final int episodios;

    Temporada(int numero, int episodios) {
        this.numero = Validador.requerirPositivo(numero, "El numero de temporada");
        this.episodios = Validador.requerirPositivo(episodios, "La cantidad de episodios");
    }

    public int getNumero() {
        return numero;
    }

    public int getEpisodios() {
        return episodios;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Temporada)) {
            return false;
        }
        Temporada otra = (Temporada) o;
        return numero == otra.numero && episodios == otra.episodios;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero, episodios);
    }

    @Override
    public String toString() {
        return "Temporada " + numero + " (" + episodios + " episodios)";
    }
}
