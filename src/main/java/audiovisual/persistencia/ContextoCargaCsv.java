package audiovisual.persistencia;

import audiovisual.modelo.Actor;
import audiovisual.modelo.Investigador;
import audiovisual.modelo.Pelicula;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Mantiene el registro temporal de entidades compartidas durante una carga CSV
 * para preservar agregaciones y asociaciones entre objetos.
 */
public class ContextoCargaCsv {
    private final Map<String, Actor> actores = new HashMap<>();
    private final Map<String, Investigador> investigadores = new HashMap<>();
    private final Map<Integer, Pelicula> peliculasPorId = new HashMap<>();

    public Actor obtenerOCrearActor(String nombre) {
        String clave = nombre.trim().toLowerCase();
        return actores.computeIfAbsent(clave, k -> new Actor(nombre));
    }

    public Investigador obtenerOCrearInvestigador(String nombre, String especialidad) {
        String clave = nombre.trim().toLowerCase() + "|" + especialidad.trim().toLowerCase();
        return investigadores.computeIfAbsent(clave, k -> new Investigador(nombre, especialidad));
    }

    public void registrarPelicula(Pelicula pelicula) {
        peliculasPorId.put(pelicula.getId(), pelicula);
    }

    public Optional<Pelicula> buscarPeliculaPorId(int id) {
        return Optional.ofNullable(peliculasPorId.get(id));
    }
}
