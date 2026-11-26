package audiovisual.modelo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PeliculaTest {

    @BeforeEach
    void reiniciarIds() {
        ContenidoAudiovisual.reiniciarSecuenciaId();
    }

    @Test
    void creaPeliculaValidaYAgregaActoresSinDuplicados() {
        Pelicula pelicula = new Pelicula("Avatar", 162, "Ciencia ficcion", "20th Century Studios");
        pelicula.agregarActor(new Actor("Sam Worthington"));
        pelicula.agregarActor(new Actor("sam worthington"));
        pelicula.agregarActor(new Actor("Zoe Saldana"));

        assertEquals(1, pelicula.getId());
        assertEquals("PELICULA", pelicula.obtenerTipo());
        assertEquals("20th Century Studios", pelicula.getEstudio());
        assertEquals(2, pelicula.getReparto().size());
        assertTrue(pelicula.formatearDetalle().contains("Sam Worthington, Zoe Saldana"));
    }

    @Test
    void repartoDevueltoEsInmutable() {
        Pelicula pelicula = new Pelicula("Matrix", 136, "Accion", "Warner Bros");
        List<Actor> reparto = pelicula.getReparto();

        assertThrows(UnsupportedOperationException.class, () -> reparto.add(new Actor("Keanu Reeves")));
    }

    @Test
    void rechazaDatosInvalidosOActorNulo() {
        assertThrows(IllegalArgumentException.class, () -> new Pelicula("", 120, "Accion", "Estudio"));
        assertThrows(IllegalArgumentException.class, () -> new Pelicula("Titulo", 0, "Accion", "Estudio"));
        assertThrows(IllegalArgumentException.class, () -> new Pelicula("Titulo", 120, "Accion", "   "));

        Pelicula pelicula = new Pelicula("Matrix", 136, "Accion", "Warner Bros");
        assertThrows(NullPointerException.class, () -> pelicula.agregarActor(null));
    }
}
