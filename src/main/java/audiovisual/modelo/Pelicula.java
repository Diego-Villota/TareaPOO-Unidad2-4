package audiovisual.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Representa una pelicula con su estudio productor y el reparto de actores por agregacion.
 */
public class Pelicula extends ContenidoAudiovisual {
    public static final String TIPO = "PELICULA";

    private String estudio;
    private final List<Actor> reparto = new ArrayList<>();

    public Pelicula(String titulo, int duracionEnMinutos, String genero, String estudio) {
        super(titulo, duracionEnMinutos, genero);
        setEstudio(estudio);
    }

    public Pelicula(int id, String titulo, int duracionEnMinutos, String genero, String estudio) {
        super(id, titulo, duracionEnMinutos, genero);
        setEstudio(estudio);
    }

    public String getEstudio() {
        return estudio;
    }

    public void setEstudio(String estudio) {
        this.estudio = Validador.requerirTextoNoVacio(estudio, "El estudio");
    }

    public void agregarActor(Actor actor) {
        Actor actorValido = Validador.requerirNoNulo(actor, "El actor");
        if (!reparto.contains(actorValido)) {
            reparto.add(actorValido);
        }
    }

    public List<Actor> getReparto() {
        return Collections.unmodifiableList(reparto);
    }

    @Override
    public String obtenerTipo() {
        return TIPO;
    }

    @Override
    protected String obtenerDetallesEspecificos() {
        String listaReparto = reparto.isEmpty()
                ? "Sin actores registrados"
                : reparto.stream().map(Actor::getNombre).collect(Collectors.joining(", "));
        return "  Estudio: " + estudio + "\n  Reparto: " + listaReparto;
    }
}
