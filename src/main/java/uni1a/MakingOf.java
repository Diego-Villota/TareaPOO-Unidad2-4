package uni1a;

/** Material complementario asociado a una pelicula existente. */
public class MakingOf extends ContenidoAudiovisual {
    private final Pelicula pelicula;
    private final String director;
    public MakingOf(String titulo, int duracion, String genero, String director, Pelicula pelicula) {
        super(titulo, duracion, genero); this.director = director; this.pelicula = java.util.Objects.requireNonNull(pelicula);
    }
    public Pelicula getPelicula() { return pelicula; }
    public String getDirector() { return director; }
    @Override public void mostrarDetalles() { System.out.println("Making of: " + getTitulo() + " | " + getDuracionEnMinutos() + " min | " + getGenero() + " | Director: " + director + " | Pelicula: " + pelicula.getTitulo()); }
}
