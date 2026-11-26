package audiovisual.persistencia;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UtilidadCsvTest {

    @Test
    void escapaYParseaCamposConComasYComillas() {
        String[] original = { "PELICULA", "1", "Titulo, \"Especial\"", "120", "Drama", "Estudio A", "Actor 1;Actor 2" };
        String linea = UtilidadCsv.formatearLinea(original);
        String[] parseado = UtilidadCsv.parsearLinea(linea);

        assertArrayEquals(original, parseado);
    }

    @Test
    void lanzaExcepcionConComillasSinCerrarOEnteroInvalido() {
        assertThrows(IllegalArgumentException.class, () -> UtilidadCsv.parsearLinea("PELICULA,1,\"Sin cierre"));
        assertThrows(IllegalArgumentException.class, () -> UtilidadCsv.parsearEntero("abc", "ID"));
    }
}
