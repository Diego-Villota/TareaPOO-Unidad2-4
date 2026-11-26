package audiovisual.modelo;

/**
 * Clase base abstracta para todos los contenidos audiovisuales del catalogo.
 * Aplica el patron Template Method en {@link #formatearDetalle()} para eliminar duplicacion.
 */
public abstract class ContenidoAudiovisual {
    private static int siguienteId = 1;

    private final int id;
    private String titulo;
    private int duracionEnMinutos;
    private String genero;

    protected ContenidoAudiovisual(String titulo, int duracionEnMinutos, String genero) {
        this(generarSiguienteId(), titulo, duracionEnMinutos, genero);
    }

    protected ContenidoAudiovisual(int id, String titulo, int duracionEnMinutos, String genero) {
        this.id = Validador.requerirPositivo(id, "El ID del contenido");
        sincronizarSecuenciaId(this.id);
        setTitulo(titulo);
        setDuracionEnMinutos(duracionEnMinutos);
        setGenero(genero);
    }

    private static synchronized int generarSiguienteId() {
        return siguienteId++;
    }

    private static synchronized void sincronizarSecuenciaId(int idAsignado) {
        if (idAsignado >= siguienteId) {
            siguienteId = idAsignado + 1;
        }
    }

    public static synchronized void reiniciarSecuenciaId() {
        siguienteId = 1;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = Validador.requerirTextoNoVacio(titulo, "El titulo");
    }

    public int getDuracionEnMinutos() {
        return duracionEnMinutos;
    }

    public void setDuracionEnMinutos(int duracionEnMinutos) {
        this.duracionEnMinutos = Validador.requerirPositivo(duracionEnMinutos, "La duracion en minutos");
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = Validador.requerirTextoNoVacio(genero, "El genero");
    }

    public abstract String obtenerTipo();

    protected abstract String obtenerDetallesEspecificos();

    public final String formatearDetalle() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(obtenerTipo()).append("] #").append(id).append(" - ").append(titulo).append('\n');
        sb.append("  Duracion: ").append(duracionEnMinutos).append(" min | Genero: ").append(genero).append('\n');
        sb.append(obtenerDetallesEspecificos());
        return sb.toString();
    }

    @Override
    public String toString() {
        return formatearDetalle();
    }
}
