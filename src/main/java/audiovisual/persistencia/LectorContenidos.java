package audiovisual.persistencia;

import audiovisual.modelo.ContenidoAudiovisual;
import java.nio.file.Path;
import java.util.List;

/**
 * Interfaz segregada (ISP) para la lectura de contenidos audiovisuales desde almacenamiento.
 */
public interface LectorContenidos {
    List<ContenidoAudiovisual> cargar(Path ruta) throws PersistenciaException;
}
