package audiovisual.persistencia;

import java.util.ArrayList;
import java.util.List;

/**
 * Utilidades de formateo y parseo CSV conforme a RFC 4180.
 */
public final class UtilidadCsv {

    private UtilidadCsv() {
    }

    public static String formatearLinea(String[] columnas) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columnas.length; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(escaparCampo(columnas[i]));
        }
        return sb.toString();
    }

    public static String escaparCampo(String valor) {
        String seguro = valor == null ? "" : valor;
        boolean requiereComillas = seguro.contains(",")
                || seguro.contains("\"")
                || seguro.contains("\n")
                || seguro.contains("\r");
        if (!requiereComillas) {
            return seguro;
        }
        return "\"" + seguro.replace("\"", "\"\"") + "\"";
    }

    public static String[] parsearLinea(String linea) {
        List<String> campos = new ArrayList<>();
        StringBuilder actual = new StringBuilder();
        boolean dentroDeComillas = false;

        for (int i = 0; i < linea.length(); i++) {
            char c = linea.charAt(i);
            if (dentroDeComillas) {
                if (c == '"') {
                    if (i + 1 < linea.length() && linea.charAt(i + 1) == '"') {
                        actual.append('"');
                        i++;
                    } else {
                        dentroDeComillas = false;
                    }
                } else {
                    actual.append(c);
                }
            } else {
                if (c == '"') {
                    if (actual.length() != 0) {
                        throw new IllegalArgumentException("Comilla inesperada en medio de un campo CSV.");
                    }
                    dentroDeComillas = true;
                } else if (c == ',') {
                    campos.add(actual.toString().trim());
                    actual.setLength(0);
                } else {
                    actual.append(c);
                }
            }
        }

        if (dentroDeComillas) {
            throw new IllegalArgumentException("Comillas sin cerrar en linea CSV.");
        }
        campos.add(actual.toString().trim());
        return campos.toArray(new String[0]);
    }

    public static int parsearEntero(String texto, String nombreCampo) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(nombreCampo + " debe ser un numero entero valido: '" + texto + "'.", ex);
        }
    }
}
