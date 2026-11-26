package audiovisual.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Gestiona en memoria la coleccion de contenidos audiovisuales del sistema.
 */
public class CatalogoAudiovisual {
    private final List<ContenidoAudiovisual> contenidos = new ArrayList<>();

    public void agregar(ContenidoAudiovisual contenido) {
        ContenidoAudiovisual valido = Validador.requerirNoNulo(contenido, "El contenido audiovisual");
        if (buscarPorId(valido.getId()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un contenido con el ID " + valido.getId() + ".");
        }
        contenidos.add(valido);
    }

    public boolean eliminarPorId(int id) {
        return contenidos.removeIf(c -> c.getId() == id);
    }

    public Optional<ContenidoAudiovisual> buscarPorId(int id) {
        return contenidos.stream()
                .filter(c -> c.getId() == id)
                .findFirst();
    }

    public List<ContenidoAudiovisual> buscarPorTitulo(String consulta) {
        String filtro = Validador.requerirTextoNoVacio(consulta, "El texto de busqueda").toLowerCase();
        return contenidos.stream()
                .filter(c -> c.getTitulo().toLowerCase().contains(filtro))
                .collect(Collectors.toList());
    }

    public List<ContenidoAudiovisual> filtrarPorTipo(String tipo) {
        String tipoNormalizado = Validador.requerirTextoNoVacio(tipo, "El tipo de contenido");
        return contenidos.stream()
                .filter(c -> c.obtenerTipo().equalsIgnoreCase(tipoNormalizado))
                .collect(Collectors.toList());
    }

    public List<Pelicula> obtenerPeliculas() {
        return contenidos.stream()
                .filter(Pelicula.class::isInstance)
                .map(Pelicula.class::cast)
                .collect(Collectors.toList());
    }

    public List<ContenidoAudiovisual> obtenerTodos() {
        return Collections.unmodifiableList(contenidos);
    }

    public void reemplazarTodos(List<ContenidoAudiovisual> nuevosContenidos) {
        Validador.requerirNoNulo(nuevosContenidos, "La lista de contenidos");
        contenidos.clear();
        for (ContenidoAudiovisual contenido : nuevosContenidos) {
            agregar(contenido);
        }
    }

    public int cantidad() {
        return contenidos.size();
    }

    public boolean estaVacio() {
        return contenidos.isEmpty();
    }
}
