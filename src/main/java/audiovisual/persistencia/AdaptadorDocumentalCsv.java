package audiovisual.persistencia;

import audiovisual.modelo.ContenidoAudiovisual;
import audiovisual.modelo.Documental;
import java.util.stream.Collectors;

/**
 * Adaptador CSV para instancias de {@link Documental} y sus investigadores asociados.
 */
public class AdaptadorDocumentalCsv implements AdaptadorCsvContenido {

    @Override
    public String obtenerTipo() {
        return Documental.TIPO;
    }

    @Override
    public boolean soporta(ContenidoAudiovisual contenido) {
        return contenido instanceof Documental;
    }

    @Override
    public String[] aColumnas(ContenidoAudiovisual contenido) {
        Documental documental = (Documental) contenido;
        String investigadores = documental.getInvestigadores().stream()
                .map(i -> i.getNombre() + "|" + i.getEspecialidad())
                .collect(Collectors.joining(";"));
        return new String[] {
                obtenerTipo(),
                String.valueOf(documental.getId()),
                documental.getTitulo(),
                String.valueOf(documental.getDuracionEnMinutos()),
                documental.getGenero(),
                documental.getTema(),
                investigadores
        };
    }

    @Override
    public ContenidoAudiovisual desdeColumnas(String[] columnas, ContextoCargaCsv contexto) {
        int id = UtilidadCsv.parsearEntero(columnas[1], "ID de documental");
        int duracion = UtilidadCsv.parsearEntero(columnas[3], "Duracion de documental");
        Documental documental = new Documental(id, columnas[2], duracion, columnas[4], columnas[5]);
        cargarInvestigadores(documental, columnas[6], contexto);
        return documental;
    }

    private void cargarInvestigadores(Documental documental, String campoInvestigadores, ContextoCargaCsv contexto) {
        if (campoInvestigadores == null || campoInvestigadores.isEmpty()) {
            return;
        }
        for (String entrada : campoInvestigadores.split(";")) {
            if (entrada.trim().isEmpty()) {
                continue;
            }
            String[] partes = entrada.split("\\|");
            if (partes.length != 2) {
                throw new IllegalArgumentException(
                        "Formato de investigador invalido (esperado nombre|especialidad): " + entrada);
            }
            documental.agregarInvestigador(contexto.obtenerOCrearInvestigador(partes[0], partes[1]));
        }
    }
}
