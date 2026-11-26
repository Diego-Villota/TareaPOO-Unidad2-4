package audiovisual.persistencia;

import audiovisual.modelo.ContenidoAudiovisual;
import audiovisual.modelo.Validador;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Repositorio CSV extensible mediante adaptadores (OCP) que implementa lectura y escritura
 * segregadas ({@link LectorContenidos} y {@link EscritorContenidos}).
 */
public class RepositorioContenidoCsv implements LectorContenidos, EscritorContenidos {
    public static final String ENCABEZADO = "tipo,id,titulo,duracion,genero,campo_extra,relaciones";
    private static final int COLUMNAS_ESPERADAS = 7;

    private final List<AdaptadorCsvContenido> adaptadores;
    private final Map<String, AdaptadorCsvContenido> adaptadoresPorTipo = new HashMap<>();

    public RepositorioContenidoCsv(List<AdaptadorCsvContenido> adaptadores) {
        Validador.requerirNoNulo(adaptadores, "La lista de adaptadores CSV");
        this.adaptadores = new ArrayList<>(adaptadores);
        for (AdaptadorCsvContenido adaptador : this.adaptadores) {
            adaptadoresPorTipo.put(adaptador.obtenerTipo().toUpperCase(), adaptador);
        }
    }

    public static RepositorioContenidoCsv crearPorDefecto() {
        return new RepositorioContenidoCsv(Arrays.asList(
                new AdaptadorPeliculaCsv(),
                new AdaptadorSerieCsv(),
                new AdaptadorDocumentalCsv(),
                new AdaptadorPodcastCsv(),
                new AdaptadorMakingOfCsv()
        ));
    }

    @Override
    public List<ContenidoAudiovisual> cargar(Path ruta) throws PersistenciaException {
        Validador.requerirNoNulo(ruta, "La ruta del archivo CSV");
        if (!Files.exists(ruta)) {
            throw new PersistenciaException("El archivo no existe: " + ruta);
        }

        List<ContenidoAudiovisual> resultado = new ArrayList<>();
        ContextoCargaCsv contexto = new ContextoCargaCsv();

        try (BufferedReader lector = Files.newBufferedReader(ruta, StandardCharsets.UTF_8)) {
            String primeraLinea = lector.readLine();
            if (primeraLinea == null) {
                return resultado;
            }
            validarEncabezado(primeraLinea);

            String linea;
            int numeroLinea = 1;
            while ((linea = lector.readLine()) != null) {
                numeroLinea++;
                if (linea.trim().isEmpty()) {
                    continue;
                }
                resultado.add(parsearFila(linea, numeroLinea, contexto));
            }
            return resultado;
        } catch (IOException ex) {
            throw new PersistenciaException("Error de lectura en el archivo CSV: " + ruta, ex);
        }
    }

    @Override
    public void guardar(Path ruta, List<ContenidoAudiovisual> contenidos) throws PersistenciaException {
        Validador.requerirNoNulo(ruta, "La ruta del archivo CSV");
        Validador.requerirNoNulo(contenidos, "La lista de contenidos");

        List<ContenidoAudiovisual> ordenados = ordenarParaEscritura(contenidos);
        try {
            Path directorioPadre = ruta.getParent();
            if (directorioPadre != null) {
                Files.createDirectories(directorioPadre);
            }
            try (BufferedWriter escritor = Files.newBufferedWriter(ruta, StandardCharsets.UTF_8)) {
                escritor.write(ENCABEZADO);
                escritor.newLine();
                for (ContenidoAudiovisual contenido : ordenados) {
                    AdaptadorCsvContenido adaptador = buscarAdaptadorPara(contenido);
                    escritor.write(UtilidadCsv.formatearLinea(adaptador.aColumnas(contenido)));
                    escritor.newLine();
                }
            }
        } catch (IOException ex) {
            throw new PersistenciaException("Error de escritura en el archivo CSV: " + ruta, ex);
        }
    }

    private void validarEncabezado(String primeraLinea) throws PersistenciaException {
        String limpio = primeraLinea.replace("\uFEFF", "").trim();
        if (!limpio.equalsIgnoreCase(ENCABEZADO)) {
            throw new PersistenciaException(
                    "Encabezado CSV invalido. Esperado: '" + ENCABEZADO + "', recibido: '" + limpio + "'.");
        }
    }

    private ContenidoAudiovisual parsearFila(String linea, int numeroLinea, ContextoCargaCsv contexto)
            throws PersistenciaException {
        try {
            String[] columnas = UtilidadCsv.parsearLinea(linea);
            if (columnas.length != COLUMNAS_ESPERADAS) {
                throw new IllegalArgumentException(
                        "Se esperaban " + COLUMNAS_ESPERADAS + " columnas pero se encontraron " + columnas.length + ".");
            }
            String tipo = columnas[0].toUpperCase();
            AdaptadorCsvContenido adaptador = adaptadoresPorTipo.get(tipo);
            if (adaptador == null) {
                throw new IllegalArgumentException("Tipo de contenido no soportado: " + tipo);
            }
            return adaptador.desdeColumnas(columnas, contexto);
        } catch (RuntimeException ex) {
            throw new PersistenciaException("Error en la linea " + numeroLinea + " del CSV: " + ex.getMessage(), ex);
        }
    }

    private List<ContenidoAudiovisual> ordenarParaEscritura(List<ContenidoAudiovisual> contenidos)
            throws PersistenciaException {
        List<ContenidoAudiovisual> copia = new ArrayList<>(contenidos);
        for (ContenidoAudiovisual contenido : copia) {
            buscarAdaptadorPara(contenido);
        }
        copia.sort(Comparator
                .comparingInt((ContenidoAudiovisual c) -> buscarAdaptadorSeguro(c).prioridadEscritura())
                .thenComparingInt(ContenidoAudiovisual::getId));
        return copia;
    }

    private AdaptadorCsvContenido buscarAdaptadorPara(ContenidoAudiovisual contenido) throws PersistenciaException {
        for (AdaptadorCsvContenido adaptador : adaptadores) {
            if (adaptador.soporta(contenido)) {
                return adaptador;
            }
        }
        throw new PersistenciaException(
                "No existe un adaptador CSV registrado para el tipo: " + contenido.obtenerTipo());
    }

    private AdaptadorCsvContenido buscarAdaptadorSeguro(ContenidoAudiovisual contenido) {
        for (AdaptadorCsvContenido adaptador : adaptadores) {
            if (adaptador.soporta(contenido)) {
                return adaptador;
            }
        }
        throw new IllegalStateException("Adaptador no encontrado para " + contenido.obtenerTipo());
    }
}
