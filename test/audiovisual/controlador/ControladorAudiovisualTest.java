package audiovisual.controlador;

import static org.junit.jupiter.api.Assertions.*;

import audiovisual.modelo.CatalogoAudiovisual;
import audiovisual.modelo.ContenidoAudiovisual;
import audiovisual.modelo.Pelicula;
import audiovisual.persistencia.EscritorContenidos;
import audiovisual.persistencia.LectorContenidos;
import audiovisual.persistencia.PersistenciaException;
import audiovisual.vista.VistaAudiovisual;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ControladorAudiovisualTest {

    private CatalogoAudiovisual catalogo;
    private VistaFalsa vista;
    private List<ContenidoAudiovisual> almacen;
    private ControladorAudiovisual controlador;

    @BeforeEach
    void configurar() {
        ContenidoAudiovisual.reiniciarSecuenciaId();
        catalogo = new CatalogoAudiovisual();
        vista = new VistaFalsa();
        almacen = new ArrayList<>();

        LectorContenidos lectorFalso = ruta -> new ArrayList<>(almacen);
        EscritorContenidos escritorFalso = (ruta, contenidos) -> {
            almacen.clear();
            almacen.addAll(contenidos);
        };

        controlador = new ControladorAudiovisual(
                catalogo,
                lectorFalso,
                escritorFalso,
                vista,
                Path.of("inexistente_para_prueba.csv")
        );
    }

    @Test
    void registraPeliculaGuardaYCargaCorrectamente() {
        vista.encolarTextos("Inception", "Ciencia ficcion", "Warner Bros", "Leonardo DiCaprio", "");
        vista.encolarEnteros(148);

        assertTrue(controlador.procesarOpcion(2));
        assertEquals(1, catalogo.cantidad());

        assertTrue(controlador.procesarOpcion(11));
        assertEquals(1, almacen.size());

        catalogo.eliminarPorId(1);
        assertTrue(catalogo.estaVacio());

        vista.encolarTextos("");
        assertTrue(controlador.procesarOpcion(10));
        assertEquals(1, catalogo.cantidad());
    }

    @Test
    void manejaErroresDeValidacionYPersistenciaSinCaer() {
        LectorContenidos lectorConFallo = ruta -> {
            throw new PersistenciaException("Fallo simulado de lectura");
        };
        ControladorAudiovisual ctrlConError = new ControladorAudiovisual(
                catalogo,
                lectorConFallo,
                (r, c) -> { },
                vista,
                Path.of("no_existe.csv")
        );

        vista.encolarTextos("");
        assertTrue(ctrlConError.procesarOpcion(10));
        assertTrue(vista.errores.stream().anyMatch(e -> e.contains("Fallo simulado")));

        assertTrue(ctrlConError.procesarOpcion(99));
        assertTrue(vista.errores.stream().anyMatch(e -> e.contains("Opcion no reconocida")));
        assertFalse(ctrlConError.procesarOpcion(0));
    }

    @Test
    void agregaActorAPeliculaExistenteYBuscaPorTitulo() {
        Pelicula pelicula = new Pelicula("Interestelar", 169, "Ciencia ficcion", "Paramount");
        catalogo.agregar(pelicula);

        vista.encolarEnteros(pelicula.getId());
        vista.encolarTextos("Anne Hathaway", "Inter");

        assertTrue(controlador.procesarOpcion(7));
        assertEquals(1, pelicula.getReparto().size());

        assertTrue(controlador.procesarOpcion(8));
        assertEquals(1, vista.ultimosContenidosMostrados.size());
    }

    private static class VistaFalsa implements VistaAudiovisual {
        final Queue<String> textos = new ArrayDeque<>();
        final Queue<Integer> enteros = new ArrayDeque<>();
        final List<String> mensajes = new ArrayList<>();
        final List<String> errores = new ArrayList<>();
        List<ContenidoAudiovisual> ultimosContenidosMostrados = new ArrayList<>();

        void encolarTextos(String... valores) {
            textos.addAll(Arrays.asList(valores));
        }

        void encolarEnteros(Integer... valores) {
            enteros.addAll(Arrays.asList(valores));
        }

        @Override
        public void mostrarBienvenida() {
        }

        @Override
        public void mostrarMenu() {
        }

        @Override
        public int leerOpcion() {
            return 0;
        }

        @Override
        public String leerTexto(String etiqueta) {
            return textos.isEmpty() ? "" : textos.poll();
        }

        @Override
        public int leerEntero(String etiqueta) {
            return enteros.isEmpty() ? 0 : enteros.poll();
        }

        @Override
        public void mostrarMensaje(String mensaje) {
            mensajes.add(mensaje);
        }

        @Override
        public void mostrarError(String mensaje) {
            errores.add(mensaje);
        }

        @Override
        public void mostrarContenidos(List<ContenidoAudiovisual> contenidos) {
            ultimosContenidosMostrados = new ArrayList<>(contenidos);
        }
    }
}
