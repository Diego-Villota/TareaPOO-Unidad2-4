package audiovisual.modelo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CatalogoAudiovisualTest {

    private CatalogoAudiovisual catalogo;

    @BeforeEach
    void configurar() {
        ContenidoAudiovisual.reiniciarSecuenciaId();
        catalogo = new CatalogoAudiovisual();
    }

    @Test
    void agregaBuscaFiltraYEliminaContenidos() {
        Pelicula p = new Pelicula("Avatar", 162, "Ciencia ficcion", "Fox");
        SerieDeTV s = new SerieDeTV("Avatar: La Leyenda", 24, "Animacion", 3);
        catalogo.agregar(p);
        catalogo.agregar(s);

        assertEquals(2, catalogo.cantidad());
        assertTrue(catalogo.buscarPorId(p.getId()).isPresent());
        assertEquals(2, catalogo.buscarPorTitulo("avatar").size());
        assertEquals(1, catalogo.filtrarPorTipo("PELICULA").size());
        assertEquals(1, catalogo.obtenerPeliculas().size());

        assertTrue(catalogo.eliminarPorId(p.getId()));
        assertFalse(catalogo.eliminarPorId(999));
        assertEquals(1, catalogo.cantidad());
    }

    @Test
    void rechazaIdDuplicadoONuloYPermiteReemplazarTodos() {
        Pelicula p1 = new Pelicula(5, "Uno", 100, "Drama", "Estudio");
        Pelicula p2 = new Pelicula(5, "Dos", 110, "Drama", "Estudio");
        catalogo.agregar(p1);

        assertThrows(IllegalArgumentException.class, () -> catalogo.agregar(p2));
        assertThrows(NullPointerException.class, () -> catalogo.agregar(null));

        Pelicula p3 = new Pelicula(8, "Tres", 95, "Comedia", "Estudio");
        catalogo.reemplazarTodos(Collections.singletonList(p3));
        List<ContenidoAudiovisual> todos = catalogo.obtenerTodos();
        assertEquals(1, todos.size());
        assertEquals(8, todos.get(0).getId());
    }
}
