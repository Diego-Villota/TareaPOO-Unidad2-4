package audiovisual.vista;

import audiovisual.modelo.ContenidoAudiovisual;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

/**
 * Implementacion de la vista en consola para interactuar con el catalogo audiovisual.
 */
public class VistaConsola implements VistaAudiovisual {
    private final Scanner entrada;
    private final PrintStream salida;

    public VistaConsola() {
        this(System.in, System.out);
    }

    public VistaConsola(InputStream entrada, PrintStream salida) {
        this.entrada = new Scanner(entrada);
        this.salida = salida;
    }

    @Override
    public void mostrarBienvenida() {
        salida.println("==========================================");
        salida.println("  SISTEMA DE CONTENIDO AUDIOVISUAL (MVC)  ");
        salida.println("==========================================");
    }

    @Override
    public void mostrarMenu() {
        salida.println();
        salida.println("--- MENU PRINCIPAL ---");
        salida.println("1. Listar todos los contenidos");
        salida.println("2. Registrar pelicula");
        salida.println("3. Registrar serie de TV");
        salida.println("4. Registrar documental");
        salida.println("5. Registrar podcast audiovisual");
        salida.println("6. Registrar making of");
        salida.println("7. Agregar actor, temporada o investigador");
        salida.println("8. Buscar contenidos por titulo");
        salida.println("9. Eliminar contenido por ID");
        salida.println("10. Cargar catalogo desde CSV");
        salida.println("11. Guardar catalogo en CSV");
        salida.println("0. Salir");
    }

    @Override
    public int leerOpcion() {
        salida.print("Seleccione una opcion: ");
        if (!entrada.hasNextLine()) {
            return 0;
        }
        String texto = entrada.nextLine().trim();
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    @Override
    public String leerTexto(String etiqueta) {
        salida.print(etiqueta + ": ");
        return entrada.hasNextLine() ? entrada.nextLine().trim() : "";
    }

    @Override
    public int leerEntero(String etiqueta) {
        while (true) {
            salida.print(etiqueta + ": ");
            if (!entrada.hasNextLine()) {
                throw new IllegalStateException("No hay mas entrada disponible.");
            }
            String texto = entrada.nextLine().trim();
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException ex) {
                mostrarError("Debe ingresar un numero entero valido.");
            }
        }
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        salida.println("[INFO] " + mensaje);
    }

    @Override
    public void mostrarError(String mensaje) {
        salida.println("[ERROR] " + mensaje);
    }

    @Override
    public void mostrarContenidos(List<ContenidoAudiovisual> contenidos) {
        if (contenidos.isEmpty()) {
            mostrarMensaje("No hay contenidos audiovisuales registrados.");
            return;
        }
        salida.println();
        salida.println("Total de contenidos: " + contenidos.size());
        salida.println("------------------------------------------");
        for (ContenidoAudiovisual contenido : contenidos) {
            salida.println(contenido.formatearDetalle());
            salida.println("------------------------------------------");
        }
    }
}
