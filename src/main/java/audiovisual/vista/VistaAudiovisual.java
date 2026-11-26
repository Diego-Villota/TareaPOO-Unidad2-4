package audiovisual.vista;

import audiovisual.modelo.ContenidoAudiovisual;
import java.util.List;

/**
 * Contrato de vista (DIP/ISP) que desacopla el controlador de la consola concreta.
 */
public interface VistaAudiovisual {
    void mostrarBienvenida();

    void mostrarMenu();

    int leerOpcion();

    String leerTexto(String etiqueta);

    int leerEntero(String etiqueta);

    void mostrarMensaje(String mensaje);

    void mostrarError(String mensaje);

    void mostrarContenidos(List<ContenidoAudiovisual> contenidos);
}
