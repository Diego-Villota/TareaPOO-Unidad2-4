package audiovisual.vista;

import static org.junit.jupiter.api.Assertions.*;

import audiovisual.modelo.ContenidoAudiovisual;
import audiovisual.modelo.Pelicula;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VistaConsolaTest {

    @BeforeEach
    void reiniciarIds() {
        ContenidoAudiovisual.reiniciarSecuenciaId();
    }

    @Test
    void leeOpcionesEnterosYFormateaSalida() {
        String entradaSimulada = "abc\n1\nno_es_numero\n42\nAvatar\n";
        ByteArrayInputStream in = new ByteArrayInputStream(entradaSimulada.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outBytes, true, StandardCharsets.UTF_8);

        VistaConsola vista = new VistaConsola(in, out);

        assertEquals(-1, vista.leerOpcion());
        assertEquals(1, vista.leerOpcion());
        assertEquals(42, vista.leerEntero("Duracion"));
        assertEquals("Avatar", vista.leerTexto("Titulo"));

        vista.mostrarContenidos(Collections.singletonList(
                new Pelicula("Avatar", 162, "Ciencia ficcion", "Fox")));

        String salida = outBytes.toString(StandardCharsets.UTF_8);
        assertTrue(salida.contains("[ERROR] Debe ingresar un numero entero valido."));
        assertTrue(salida.contains("Avatar"));
    }
}
