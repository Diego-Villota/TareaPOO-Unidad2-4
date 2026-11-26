package audiovisual.modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SerieDeTVTest {

    @BeforeEach
    void reiniciarIds() {
        ContenidoAudiovisual.reiniciarSecuenciaId();
    }

    @Test
    void agregaTemporadasEnOrdenYActualizaContador() {
        SerieDeTV serie = new SerieDeTV("Cosmos", 45, "Divulgacion", 0);
        serie.agregarTemporada(2, 13);
        serie.agregarTemporada(1, 10);

        assertEquals("SERIE", serie.obtenerTipo());
        assertEquals(2, serie.getTemporadas());
        assertEquals(2, serie.getDetalleTemporadas().size());
        assertEquals(1, serie.getDetalleTemporadas().get(0).getNumero());
        assertEquals(2, serie.getDetalleTemporadas().get(1).getNumero());
    }

    @Test
    void rechazaTemporadaDuplicadaOValoresInvalidos() {
        SerieDeTV serie = new SerieDeTV("Dark", 53, "Suspenso", 1);
        serie.agregarTemporada(1, 10);

        assertThrows(IllegalArgumentException.class, () -> serie.agregarTemporada(1, 8));
        assertThrows(IllegalArgumentException.class, () -> serie.agregarTemporada(0, 8));
        assertThrows(IllegalArgumentException.class, () -> serie.agregarTemporada(2, 0));
    }

    @Test
    void rechazaReducirTemporadasPorDebajoDelDetalleExistente() {
        SerieDeTV serie = new SerieDeTV("Dark", 53, "Suspenso", 3);
        serie.agregarTemporada(3, 8);

        assertThrows(IllegalArgumentException.class, () -> serie.setTemporadas(2));
        assertThrows(IllegalArgumentException.class, () -> serie.setTemporadas(-1));
    }
}
