package audiovisual.persistencia;

import audiovisual.modelo.ContenidoAudiovisual;
import audiovisual.modelo.MakingOf;
import audiovisual.modelo.Pelicula;

/**
 * Adaptador CSV para instancias de {@link MakingOf}.
 * Tiene prioridad de escritura posterior a las peliculas para preservar la referencia por ID.
 */
public class AdaptadorMakingOfCsv implements AdaptadorCsvContenido {

    @Override
    public String obtenerTipo() {
        return MakingOf.TIPO;
    }

    @Override
    public boolean soporta(ContenidoAudiovisual contenido) {
        return contenido instanceof MakingOf;
    }

    @Override
    public int prioridadEscritura() {
        return 1;
    }

    @Override
    public String[] aColumnas(ContenidoAudiovisual contenido) {
        MakingOf makingOf = (MakingOf) contenido;
        return new String[] {
                obtenerTipo(),
                String.valueOf(makingOf.getId()),
                makingOf.getTitulo(),
                String.valueOf(makingOf.getDuracionEnMinutos()),
                makingOf.getGenero(),
                makingOf.getDirector(),
                String.valueOf(makingOf.getPelicula().getId())
        };
    }

    @Override
    public ContenidoAudiovisual desdeColumnas(String[] columnas, ContextoCargaCsv contexto) {
        int id = UtilidadCsv.parsearEntero(columnas[1], "ID de making of");
        int duracion = UtilidadCsv.parsearEntero(columnas[3], "Duracion de making of");
        int peliculaId = UtilidadCsv.parsearEntero(columnas[6], "ID de pelicula asociada");
        Pelicula pelicula = contexto.buscarPeliculaPorId(peliculaId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontro la pelicula asociada con ID " + peliculaId + "."));
        return new MakingOf(id, columnas[2], duracion, columnas[4], columnas[5], pelicula);
    }
}
