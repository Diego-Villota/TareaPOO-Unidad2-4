package audiovisual.persistencia;

import static org.junit.jupiter.api.Assertions.*;

import audiovisual.modelo.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class RepositorioContenidoCsvTest {

    @TempDir
    Path directorioTemporal;

    private RepositorioContenidoCsv repositorio;

    @BeforeEach
    void configurar() {
        ContenidoAudiovisual.reiniciarSecuenciaId();
        repositorio = RepositorioContenidoCsv.crearPorDefecto();
    }

    @Test
    void guardaYCargaTodosLosTiposPreservandoRelaciones() throws PersistenciaException {
        Actor actor = new Actor("Sam Worthington");
        Investigador investigadora = new Investigador("Ana Torres", "Astronomia");

        Pelicula pelicula = new Pelicula("Avatar, Edicion Extendida", 178, "Ciencia ficcion", "20th Century Studios");
        pelicula.agregarActor(actor);

        SerieDeTV serie = new SerieDeTV("Cosmos: una odisea", 45, "Divulgacion", 2);
        serie.agregarTemporada(1, 13);
        serie.agregarTemporada(2, 13);

        Documental documental = new Documental("El universo", 50, "Ciencia", "Astronomia");
        documental.agregarInvestigador(investigadora);

        PodcastAudiovisual podcast = new PodcastAudiovisual(
                "Conversaciones del cosmos", 30, "Divulgacion", "Ciencia Hoy", investigadora);

        MakingOf makingOf = new MakingOf("Detras de Avatar", 25, "Documental", "Luis Perez", pelicula);

        // Colocamos MakingOf antes de Pelicula para comprobar el ordenamiento por prioridad de escritura
        List<ContenidoAudiovisual> originales = Arrays.asList(makingOf, podcast, documental, serie, pelicula);

        Path archivo = directorioTemporal.resolve("catalogo.csv");
        repositorio.guardar(archivo, originales);
        List<ContenidoAudiovisual> cargados = repositorio.cargar(archivo);

        assertEquals(5, cargados.size());

        Pelicula peliculaCargada = (Pelicula) cargados.stream()
                .filter(Pelicula.class::isInstance).findFirst().orElseThrow();
        assertEquals("Avatar, Edicion Extendida", peliculaCargada.getTitulo());
        assertEquals(1, peliculaCargada.getReparto().size());

        SerieDeTV serieCargada = (SerieDeTV) cargados.stream()
                .filter(SerieDeTV.class::isInstance).findFirst().orElseThrow();
        assertEquals(2, serieCargada.getDetalleTemporadas().size());

        Documental docCargado = (Documental) cargados.stream()
                .filter(Documental.class::isInstance).findFirst().orElseThrow();
        PodcastAudiovisual podCargado = (PodcastAudiovisual) cargados.stream()
                .filter(PodcastAudiovisual.class::isInstance).findFirst().orElseThrow();
        assertSame(docCargado.getInvestigadores().get(0), podCargado.getInvitado());

        MakingOf moCargado = (MakingOf) cargados.stream()
                .filter(MakingOf.class::isInstance).findFirst().orElseThrow();
        assertSame(peliculaCargada, moCargado.getPelicula());
    }

    @Test
    void lanzaExcepcionCuandoArchivoNoExisteOFormatoEsInvalido() throws IOException {
        Path inexistente = directorioTemporal.resolve("no_existe.csv");
        assertThrows(PersistenciaException.class, () -> repositorio.cargar(inexistente));

        Path encabezadoMalo = directorioTemporal.resolve("malo.csv");
        Files.writeString(encabezadoMalo, "col1,col2\n1,2\n", StandardCharsets.UTF_8);
        assertThrows(PersistenciaException.class, () -> repositorio.cargar(encabezadoMalo));

        Path tipoDesconocido = directorioTemporal.resolve("tipo.csv");
        Files.writeString(tipoDesconocido,
                RepositorioContenidoCsv.ENCABEZADO + "\nVIDEOJUEGO,1,Halo,120,Accion,Bungie,\n",
                StandardCharsets.UTF_8);
        assertThrows(PersistenciaException.class, () -> repositorio.cargar(tipoDesconocido));
    }

    @Test
    void archivoVacioDevuelveListaVacia() throws IOException, PersistenciaException {
        Path vacio = directorioTemporal.resolve("vacio.csv");
        Files.writeString(vacio, "", StandardCharsets.UTF_8);

        assertTrue(repositorio.cargar(vacio).isEmpty());
    }
}
