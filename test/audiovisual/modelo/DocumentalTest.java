package audiovisual.modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentalTest {

    @BeforeEach
    void reiniciarIds() {
        ContenidoAudiovisual.reiniciarSecuenciaId();
    }

    @Test
    void agregaInvestigadoresSinDuplicadosYFormateaDetalle() {
        Documental documental = new Documental("El universo", 50, "Ciencia", "Astronomia");
        Investigador inv1 = new Investigador("Ana Torres", "Astronomia");
        Investigador invDuplicado = new Investigador("ana torres", "astronomia");

        documental.agregarInvestigador(inv1);
        documental.agregarInvestigador(invDuplicado);

        assertEquals("DOCUMENTAL", documental.obtenerTipo());
        assertEquals("Astronomia", documental.getTema());
        assertEquals(1, documental.getInvestigadores().size());
        assertTrue(documental.formatearDetalle().contains("Ana Torres (Astronomia)"));
    }

    @Test
    void rechazaTemaVacioEInvestigadorNulo() {
        assertThrows(IllegalArgumentException.class, () -> new Documental("Doc", 40, "Ciencia", ""));

        Documental documental = new Documental("El universo", 50, "Ciencia", "Astronomia");
        assertThrows(NullPointerException.class, () -> documental.agregarInvestigador(null));
    }
}
