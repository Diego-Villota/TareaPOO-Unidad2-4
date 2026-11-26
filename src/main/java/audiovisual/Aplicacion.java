package audiovisual;

import audiovisual.controlador.ControladorAudiovisual;
import audiovisual.modelo.CatalogoAudiovisual;
import audiovisual.persistencia.RepositorioContenidoCsv;
import audiovisual.vista.VistaAudiovisual;
import audiovisual.vista.VistaConsola;
import java.nio.file.Path;

/**
 * Punto de entrada de la aplicacion. Ensambla el patron MVC inyectando
 * las dependencias concretas hacia las abstracciones del controlador.
 */
public final class Aplicacion {
    private static final Path RUTA_CSV_POR_DEFECTO = Path.of("data", "contenidos.csv");

    private Aplicacion() {
    }

    public static void main(String[] args) {
        CatalogoAudiovisual catalogo = new CatalogoAudiovisual();
        RepositorioContenidoCsv repositorio = RepositorioContenidoCsv.crearPorDefecto();
        VistaAudiovisual vista = new VistaConsola();

        ControladorAudiovisual controlador = new ControladorAudiovisual(
                catalogo,
                repositorio,
                repositorio,
                vista,
                RUTA_CSV_POR_DEFECTO
        );
        controlador.iniciar();
    }
}
