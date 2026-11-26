package audiovisual.persistencia;

import audiovisual.modelo.ContenidoAudiovisual;
import java.nio.file.Path;
import java.util.List;

/**
 * Interfaz segregada (ISP) para la escritura de contenidos audiovisuales en almacenamiento.
 */
public interface EscritorContenidos {
    void guardar(Path ruta, List<ContenidoAudiovisual> contenidos) throws PersistenciaException;
}
