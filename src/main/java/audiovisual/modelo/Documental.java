package audiovisual.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Representa un documental asociado a uno o mas investigadores.
 */
public class Documental extends ContenidoAudiovisual {
    public static final String TIPO = "DOCUMENTAL";

    private String tema;
    private final List<Investigador> investigadores = new ArrayList<>();

    public Documental(String titulo, int duracionEnMinutos, String genero, String tema) {
        super(titulo, duracionEnMinutos, genero);
        setTema(tema);
    }

    public Documental(int id, String titulo, int duracionEnMinutos, String genero, String tema) {
        super(id, titulo, duracionEnMinutos, genero);
        setTema(tema);
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = Validador.requerirTextoNoVacio(tema, "El tema del documental");
    }

    public void agregarInvestigador(Investigador investigador) {
        Investigador valido = Validador.requerirNoNulo(investigador, "El investigador");
        if (!investigadores.contains(valido)) {
            investigadores.add(valido);
        }
    }

    public List<Investigador> getInvestigadores() {
        return Collections.unmodifiableList(investigadores);
    }

    @Override
    public String obtenerTipo() {
        return TIPO;
    }

    @Override
    protected String obtenerDetallesEspecificos() {
        String lista = investigadores.isEmpty()
                ? "Sin investigadores registrados"
                : investigadores.stream().map(Investigador::toString).collect(Collectors.joining(", "));
        return "  Tema: " + tema + "\n  Investigadores: " + lista;
    }
}
