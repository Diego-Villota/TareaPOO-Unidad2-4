package audiovisual.modelo;

import java.util.Objects;

/**
 * Representa a un investigador asociado a documentales o invitado en podcasts.
 */
public final class Investigador {
    private final String nombre;
    private final String especialidad;

    public Investigador(String nombre, String especialidad) {
        this.nombre = Validador.requerirTextoNoVacio(nombre, "El nombre del investigador");
        this.especialidad = Validador.requerirTextoNoVacio(especialidad, "La especialidad del investigador");
    }

    public String getNombre() {
        return nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Investigador)) {
            return false;
        }
        Investigador otro = (Investigador) o;
        return nombre.equalsIgnoreCase(otro.nombre)
                && especialidad.equalsIgnoreCase(otro.especialidad);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre.toLowerCase(), especialidad.toLowerCase());
    }

    @Override
    public String toString() {
        return nombre + " (" + especialidad + ")";
    }
}
