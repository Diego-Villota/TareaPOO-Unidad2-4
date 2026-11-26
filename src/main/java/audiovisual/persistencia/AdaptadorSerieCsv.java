package audiovisual.persistencia;

import audiovisual.modelo.ContenidoAudiovisual;
import audiovisual.modelo.SerieDeTV;
import java.util.stream.Collectors;

/**
 * Adaptador CSV para instancias de {@link SerieDeTV} y la composicion de sus temporadas.
 */
public class AdaptadorSerieCsv implements AdaptadorCsvContenido {

    @Override
    public String obtenerTipo() {
        return SerieDeTV.TIPO;
    }

    @Override
    public boolean soporta(ContenidoAudiovisual contenido) {
        return contenido instanceof SerieDeTV;
    }

    @Override
    public String[] aColumnas(ContenidoAudiovisual contenido) {
        SerieDeTV serie = (SerieDeTV) contenido;
        String detalle = serie.getDetalleTemporadas().stream()
                .map(t -> t.getNumero() + ":" + t.getEpisodios())
                .collect(Collectors.joining(";"));
        return new String[] {
                obtenerTipo(),
                String.valueOf(serie.getId()),
                serie.getTitulo(),
                String.valueOf(serie.getDuracionEnMinutos()),
                serie.getGenero(),
                String.valueOf(serie.getTemporadas()),
                detalle
        };
    }

    @Override
    public ContenidoAudiovisual desdeColumnas(String[] columnas, ContextoCargaCsv contexto) {
        int id = UtilidadCsv.parsearEntero(columnas[1], "ID de serie");
        int duracion = UtilidadCsv.parsearEntero(columnas[3], "Duracion de serie");
        int temporadas = UtilidadCsv.parsearEntero(columnas[5], "Cantidad de temporadas");
        SerieDeTV serie = new SerieDeTV(id, columnas[2], duracion, columnas[4], temporadas);
        cargarTemporadas(serie, columnas[6]);
        return serie;
    }

    private void cargarTemporadas(SerieDeTV serie, String campoTemporadas) {
        if (campoTemporadas == null || campoTemporadas.isEmpty()) {
            return;
        }
        for (String entrada : campoTemporadas.split(";")) {
            if (entrada.trim().isEmpty()) {
                continue;
            }
            String[] partes = entrada.split(":");
            if (partes.length != 2) {
                throw new IllegalArgumentException("Formato de temporada invalido (esperado numero:episodios): " + entrada);
            }
            int numero = UtilidadCsv.parsearEntero(partes[0], "Numero de temporada");
            int episodios = UtilidadCsv.parsearEntero(partes[1], "Episodios de temporada");
            serie.agregarTemporada(numero, episodios);
        }
    }
}
