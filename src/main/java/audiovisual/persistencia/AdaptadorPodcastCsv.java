package audiovisual.persistencia;

import audiovisual.modelo.ContenidoAudiovisual;
import audiovisual.modelo.Investigador;
import audiovisual.modelo.PodcastAudiovisual;

/**
 * Adaptador CSV para instancias de {@link PodcastAudiovisual}.
 */
public class AdaptadorPodcastCsv implements AdaptadorCsvContenido {

    @Override
    public String obtenerTipo() {
        return PodcastAudiovisual.TIPO;
    }

    @Override
    public boolean soporta(ContenidoAudiovisual contenido) {
        return contenido instanceof PodcastAudiovisual;
    }

    @Override
    public String[] aColumnas(ContenidoAudiovisual contenido) {
        PodcastAudiovisual podcast = (PodcastAudiovisual) contenido;
        Investigador invitado = podcast.getInvitado();
        return new String[] {
                obtenerTipo(),
                String.valueOf(podcast.getId()),
                podcast.getTitulo(),
                String.valueOf(podcast.getDuracionEnMinutos()),
                podcast.getGenero(),
                podcast.getCanal(),
                invitado.getNombre() + "|" + invitado.getEspecialidad()
        };
    }

    @Override
    public ContenidoAudiovisual desdeColumnas(String[] columnas, ContextoCargaCsv contexto) {
        int id = UtilidadCsv.parsearEntero(columnas[1], "ID de podcast");
        int duracion = UtilidadCsv.parsearEntero(columnas[3], "Duracion de podcast");
        String[] partes = columnas[6].split("\\|");
        if (partes.length != 2) {
            throw new IllegalArgumentException(
                    "El podcast requiere un invitado con formato nombre|especialidad: " + columnas[6]);
        }
        Investigador invitado = contexto.obtenerOCrearInvestigador(partes[0], partes[1]);
        return new PodcastAudiovisual(id, columnas[2], duracion, columnas[4], columnas[5], invitado);
    }
}
