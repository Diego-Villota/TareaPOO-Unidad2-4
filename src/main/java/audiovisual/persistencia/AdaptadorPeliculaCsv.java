package audiovisual.persistencia;

import audiovisual.modelo.Actor;
import audiovisual.modelo.ContenidoAudiovisual;
import audiovisual.modelo.Pelicula;
import java.util.stream.Collectors;

/**
 * Adaptador CSV para instancias de {@link Pelicula} y sus actores asociados.
 */
public class AdaptadorPeliculaCsv implements AdaptadorCsvContenido {

    @Override
    public String obtenerTipo() {
        return Pelicula.TIPO;
    }

    @Override
    public boolean soporta(ContenidoAudiovisual contenido) {
        return contenido instanceof Pelicula;
    }

    @Override
    public String[] aColumnas(ContenidoAudiovisual contenido) {
        Pelicula pelicula = (Pelicula) contenido;
        String actores = pelicula.getReparto().stream()
                .map(Actor::getNombre)
                .collect(Collectors.joining(";"));
        return new String[] {
                obtenerTipo(),
                String.valueOf(pelicula.getId()),
                pelicula.getTitulo(),
                String.valueOf(pelicula.getDuracionEnMinutos()),
                pelicula.getGenero(),
                pelicula.getEstudio(),
                actores
        };
    }

    @Override
    public ContenidoAudiovisual desdeColumnas(String[] columnas, ContextoCargaCsv contexto) {
        int id = UtilidadCsv.parsearEntero(columnas[1], "ID de pelicula");
        int duracion = UtilidadCsv.parsearEntero(columnas[3], "Duracion de pelicula");
        Pelicula pelicula = new Pelicula(id, columnas[2], duracion, columnas[4], columnas[5]);
        cargarActores(pelicula, columnas[6], contexto);
        contexto.registrarPelicula(pelicula);
        return pelicula;
    }

    private void cargarActores(Pelicula pelicula, String campoActores, ContextoCargaCsv contexto) {
        if (campoActores == null || campoActores.isEmpty()) {
            return;
        }
        for (String nombre : campoActores.split(";")) {
            if (!nombre.trim().isEmpty()) {
                pelicula.agregarActor(contexto.obtenerOCrearActor(nombre));
            }
        }
    }
}
