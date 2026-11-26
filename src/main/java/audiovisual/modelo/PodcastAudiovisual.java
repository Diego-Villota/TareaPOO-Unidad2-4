package audiovisual.modelo;

/**
 * Episodio audiovisual que puede compartir investigadores con documentales.
 */
public class PodcastAudiovisual extends ContenidoAudiovisual {
    public static final String TIPO = "PODCAST";

    private String canal;
    private Investigador invitado;

    public PodcastAudiovisual(String titulo, int duracionEnMinutos, String genero, String canal, Investigador invitado) {
        super(titulo, duracionEnMinutos, genero);
        setCanal(canal);
        setInvitado(invitado);
    }

    public PodcastAudiovisual(int id, String titulo, int duracionEnMinutos, String genero, String canal, Investigador invitado) {
        super(id, titulo, duracionEnMinutos, genero);
        setCanal(canal);
        setInvitado(invitado);
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = Validador.requerirTextoNoVacio(canal, "El canal del podcast");
    }

    public Investigador getInvitado() {
        return invitado;
    }

    public void setInvitado(Investigador invitado) {
        this.invitado = Validador.requerirNoNulo(invitado, "El investigador invitado");
    }

    @Override
    public String obtenerTipo() {
        return TIPO;
    }

    @Override
    protected String obtenerDetallesEspecificos() {
        return "  Canal: " + canal + "\n  Invitado: " + invitado;
    }
}
