package audiovisual.modelo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EntidadesYPolimorfismoTest {

    @BeforeEach
    void reiniciarIds() {
        ContenidoAudiovisual.reiniciarSecuenciaId();
    }

    @Test
    void verificaSustitucionDeLiskovEnTodasLasSubclases() {
        Pelicula pelicula = new Pelicula("Avatar", 162, "Ciencia ficcion", "20th Century Studios");
        SerieDeTV serie = new SerieDeTV("Cosmos", 45, "Divulgacion", 2);
        Documental documental = new Documental("El universo", 50, "Ciencia", "Astronomia");
        Investigador investigadora = new Investigador("Ana Torres", "Astronomia");
        PodcastAudiovisual podcast = new PodcastAudiovisual("Charla", 30, "Divulgacion", "Ciencia Hoy", investigadora);
        MakingOf makingOf = new MakingOf("Detras de Avatar", 25, "Documental", "Luis Perez", pelicula);

        List<ContenidoAudiovisual> lista = Arrays.asList(pelicula, serie, documental, podcast, makingOf);

        for (ContenidoAudiovisual contenido : lista) {
            assertTrue(contenido.getId() > 0);
            assertFalse(contenido.obtenerTipo().isEmpty());
            assertTrue(contenido.formatearDetalle().contains(contenido.getTitulo()));
        }
    }

    @Test
    void sincronizaSecuenciaDeIdsAlCargarIdsExplicitos() {
        Pelicula cargada = new Pelicula(10, "Dune", 155, "Ciencia ficcion", "Legendary");
        Pelicula nueva = new Pelicula("Arrival", 116, "Ciencia ficcion", "Paramount");

        assertEquals(10, cargada.getId());
        assertEquals(11, nueva.getId());
    }

    @Test
    void validaEntidadesAuxiliaresYRelacionesObligatorias() {
        assertThrows(IllegalArgumentException.class, () -> new Actor("  "));
        assertThrows(IllegalArgumentException.class, () -> new Investigador("", "Fisica"));
        assertThrows(IllegalArgumentException.class, () -> new Investigador("Ana", null));
        assertThrows(NullPointerException.class,
                () -> new PodcastAudiovisual("Pod", 20, "Tech", "Canal", null));
        assertThrows(NullPointerException.class,
                () -> new MakingOf("MO", 15, "Doc", "Director", null));

        Actor a1 = new Actor("Ana");
        Actor a2 = new Actor("ana");
        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }
}
