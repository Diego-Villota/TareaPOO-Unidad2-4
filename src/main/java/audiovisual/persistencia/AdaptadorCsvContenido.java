package audiovisual.persistencia;

import audiovisual.modelo.ContenidoAudiovisual;

/**
 * Contrato extensible (OCP) para serializar y deserializar un tipo especifico de contenido en CSV.
 */
public interface AdaptadorCsvContenido {
    String obtenerTipo();

    boolean soporta(ContenidoAudiovisual contenido);

    default int prioridadEscritura() {
        return 0;
    }

    String[] aColumnas(ContenidoAudiovisual contenido);

    ContenidoAudiovisual desdeColumnas(String[] columnas, ContextoCargaCsv contexto);
}
