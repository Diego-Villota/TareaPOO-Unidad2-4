/**
 * Class SerieDeTV
 */
package uni1a;

// Subclase SerieDeTV que extiende de ContenidoAudiovisual
public class SerieDeTV extends ContenidoAudiovisual {
    private int temporadas;
    private final java.util.List<Temporada> detalleTemporadas = new java.util.ArrayList<>();

    public void agregarTemporada(int numero, int episodios) {
        if (detalleTemporadas.stream().anyMatch(t -> t.getNumero() == numero)) throw new IllegalArgumentException("Temporada duplicada");
        detalleTemporadas.add(new Temporada(numero, episodios));
        temporadas = detalleTemporadas.size();
    }
    public java.util.List<Temporada> getDetalleTemporadas() { return java.util.Collections.unmodifiableList(detalleTemporadas); }

    public SerieDeTV(String titulo, int duracionEnMinutos, String genero, int temporadas) {
        super(titulo, duracionEnMinutos, genero);
        this.temporadas = temporadas;
    }

    public int getTemporadas() {
        return temporadas;
    }

    public void setTemporadas(int temporadas) {
        this.temporadas = temporadas;
    }
    
    @Override
    public void mostrarDetalles() {
        System.out.println("Detalles de la serie:");
        System.out.println("ID: " + getId());
        System.out.println("Título: " + getTitulo());
        System.out.println("Duración en minutos: " + getDuracionEnMinutos());
        System.out.println("Género: " + getGenero());
        System.out.println("Temporadas: " + this.temporadas);
        System.out.println("Detalle: " + detalleTemporadas);
        System.out.println();
    }
}