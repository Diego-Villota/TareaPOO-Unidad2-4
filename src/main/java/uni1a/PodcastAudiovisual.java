package uni1a;

/** Episodio audiovisual que puede compartir investigadores con documentales. */
public class PodcastAudiovisual extends ContenidoAudiovisual {
    private final String canal;
    private final Investigador invitado;
    public PodcastAudiovisual(String titulo, int duracion, String genero, String canal, Investigador invitado) {
        super(titulo, duracion, genero); this.canal = canal; this.invitado = java.util.Objects.requireNonNull(invitado);
    }
    public String getCanal() { return canal; }
    public Investigador getInvitado() { return invitado; }
    @Override public void mostrarDetalles() { System.out.println("Podcast: " + getTitulo() + " | " + getDuracionEnMinutos() + " min | " + getGenero() + " | Canal: " + canal + " | Invitado: " + invitado); }
}
