package audiovisual.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Representa una serie de television que compone sus temporadas.
 */
public class SerieDeTV extends ContenidoAudiovisual {
    public static final String TIPO = "SERIE";

    private int temporadas;
    private final List<Temporada> detalleTemporadas = new ArrayList<>();

    public SerieDeTV(String titulo, int duracionEnMinutos, String genero, int temporadas) {
        super(titulo, duracionEnMinutos, genero);
        setTemporadas(temporadas);
    }

    public SerieDeTV(int id, String titulo, int duracionEnMinutos, String genero, int temporadas) {
        super(id, titulo, duracionEnMinutos, genero);
        setTemporadas(temporadas);
    }

    public int getTemporadas() {
        return temporadas;
    }

    public void setTemporadas(int temporadas) {
        Validador.requerirNoNegativo(temporadas, "La cantidad de temporadas");
        boolean excedeLimite = detalleTemporadas.stream().anyMatch(t -> t.getNumero() > temporadas);
        if (excedeLimite) {
            throw new IllegalArgumentException(
                    "La cantidad de temporadas no puede ser menor al numero de una temporada ya registrada.");
        }
        this.temporadas = temporadas;
    }

    public void agregarTemporada(int numero, int episodios) {
        boolean duplicada = detalleTemporadas.stream().anyMatch(t -> t.getNumero() == numero);
        if (duplicada) {
            throw new IllegalArgumentException("Ya existe la temporada numero " + numero + ".");
        }
        detalleTemporadas.add(new Temporada(numero, episodios));
        detalleTemporadas.sort(Comparator.comparingInt(Temporada::getNumero));
        this.temporadas = Math.max(this.temporadas, numero);
    }

    public List<Temporada> getDetalleTemporadas() {
        return Collections.unmodifiableList(detalleTemporadas);
    }

    @Override
    public String obtenerTipo() {
        return TIPO;
    }

    @Override
    protected String obtenerDetallesEspecificos() {
        String detalle = detalleTemporadas.isEmpty()
                ? "Sin detalle de temporadas"
                : detalleTemporadas.stream().map(Temporada::toString).collect(Collectors.joining(", "));
        return "  Temporadas: " + temporadas + "\n  Detalle: " + detalle;
    }
}
