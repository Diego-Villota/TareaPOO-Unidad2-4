package audiovisual.modelo;

import java.util.Objects;

/**
 * Centraliza las validaciones comunes de los objetos del dominio para evitar duplicacion.
 */
public final class Validador {

    private Validador() {
    }

    public static String requerirTextoNoVacio(String valor, String nombreCampo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(nombreCampo + " no puede estar vacio.");
        }
        return valor.trim();
    }

    public static int requerirPositivo(int valor, String nombreCampo) {
        if (valor <= 0) {
            throw new IllegalArgumentException(nombreCampo + " debe ser mayor que cero.");
        }
        return valor;
    }

    public static int requerirNoNegativo(int valor, String nombreCampo) {
        if (valor < 0) {
            throw new IllegalArgumentException(nombreCampo + " no puede ser negativo.");
        }
        return valor;
    }

    public static <T> T requerirNoNulo(T objeto, String nombreCampo) {
        return Objects.requireNonNull(objeto, nombreCampo + " no puede ser nulo.");
    }
}
