package uni1a;

public class Actor {
    private final String nombre;
    public Actor(String nombre) { if (nombre == null || nombre.trim().isEmpty()) throw new IllegalArgumentException("Nombre requerido"); this.nombre = nombre; }
    public String getNombre() { return nombre; }
    @Override public String toString() { return nombre; }
}
