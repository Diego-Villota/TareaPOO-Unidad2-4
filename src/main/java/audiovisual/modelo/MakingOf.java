package audiovisual.modelo;

/**
 * Material complementario detras de camaras asociado a una pelicula existente.
 */
public class MakingOf extends ContenidoAudiovisual {
    public static final String TIPO = "MAKING_OF";

    private String director;
    private Pelicula pelicula;

    public MakingOf(String titulo, int duracionEnMinutos, String genero, String director, Pelicula pelicula) {
        super(titulo, duracionEnMinutos, genero);
        setDirector(director);
        setPelicula(pelicula);
    }

    public MakingOf(int id, String titulo, int duracionEnMinutos, String genero, String director, Pelicula pelicula) {
        super(id, titulo, duracionEnMinutos, genero);
        setDirector(director);
        setPelicula(pelicula);
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = Validador.requerirTextoNoVacio(director, "El director");
    }

    public Pelicula getPelicula() {
        return pelicula;
    }

    public void setPelicula(Pelicula pelicula) {
        this.pelicula = Validador.requerirNoNulo(pelicula, "La pelicula asociada");
    }

    @Override
    public String obtenerTipo() {
        return TIPO;
    }

    @Override
    protected String obtenerDetallesEspecificos() {
        return "  Director: " + director + "\n  Pelicula asociada: " + pelicula.getTitulo() + " (#" + pelicula.getId() + ")";
    }
}
