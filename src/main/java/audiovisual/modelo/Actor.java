package audiovisual.modelo;

import java.util.Objects;

/**
 * Representa a un actor que puede participar en una o varias peliculas.
 */
public final class Actor {
    private final String nombre;

    public Actor(String nombre) {
        this.nombre = Validador.requerirTextoNoVacio(nombre, "El nombre del actor");
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Actor)) {
            return false;
        }
        Actor otro = (Actor) o;
        return nombre.equalsIgnoreCase(otro.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre.toLowerCase());
    }

    @Override
    public String toString() {
        return nombre;
    }
}
