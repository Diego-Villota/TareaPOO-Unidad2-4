package audiovisual.controlador;

import audiovisual.modelo.Actor;
import audiovisual.modelo.CatalogoAudiovisual;
import audiovisual.modelo.ContenidoAudiovisual;
import audiovisual.modelo.Documental;
import audiovisual.modelo.Investigador;
import audiovisual.modelo.MakingOf;
import audiovisual.modelo.Pelicula;
import audiovisual.modelo.PodcastAudiovisual;
import audiovisual.modelo.SerieDeTV;
import audiovisual.modelo.Validador;
import audiovisual.persistencia.EscritorContenidos;
import audiovisual.persistencia.LectorContenidos;
import audiovisual.persistencia.PersistenciaException;
import audiovisual.vista.VistaAudiovisual;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * Controlador MVC que coordina las operaciones entre el catalogo (modelo),
 * la persistencia CSV y la interfaz de usuario (vista) mediante inyeccion de dependencias (DIP).
 */
public class ControladorAudiovisual {
    private final CatalogoAudiovisual catalogo;
    private final LectorContenidos lector;
    private final EscritorContenidos escritor;
    private final VistaAudiovisual vista;
    private final Path rutaPorDefecto;

    public ControladorAudiovisual(
            CatalogoAudiovisual catalogo,
            LectorContenidos lector,
            EscritorContenidos escritor,
            VistaAudiovisual vista,
            Path rutaPorDefecto) {
        this.catalogo = Validador.requerirNoNulo(catalogo, "El catalogo");
        this.lector = Validador.requerirNoNulo(lector, "El lector de contenidos");
        this.escritor = Validador.requerirNoNulo(escritor, "El escritor de contenidos");
        this.vista = Validador.requerirNoNulo(vista, "La vista");
        this.rutaPorDefecto = Validador.requerirNoNulo(rutaPorDefecto, "La ruta por defecto");
    }

    public void iniciar() {
        vista.mostrarBienvenida();
        cargarDatosInicialesSiExisten();

        boolean ejecutando = true;
        while (ejecutando) {
            vista.mostrarMenu();
            int opcion = vista.leerOpcion();
            ejecutando = procesarOpcion(opcion);
        }
        vista.mostrarMensaje("Hasta pronto.");
    }

    public boolean procesarOpcion(int opcion) {
        try {
            switch (opcion) {
                case 1:
                    vista.mostrarContenidos(catalogo.obtenerTodos());
                    return true;
                case 2:
                    registrarPelicula();
                    return true;
                case 3:
                    registrarSerie();
                    return true;
                case 4:
                    registrarDocumental();
                    return true;
                case 5:
                    registrarPodcast();
                    return true;
                case 6:
                    registrarMakingOf();
                    return true;
                case 7:
                    agregarEntidadRelacionada();
                    return true;
                case 8:
                    buscarPorTitulo();
                    return true;
                case 9:
                    eliminarPorId();
                    return true;
                case 10:
                    cargarDesdeCsv();
                    return true;
                case 11:
                    guardarEnCsv();
                    return true;
                case 0:
                    return false;
                default:
                    vista.mostrarError("Opcion no reconocida. Intente nuevamente.");
                    return true;
            }
        } catch (IllegalArgumentException | PersistenciaException ex) {
            vista.mostrarError(ex.getMessage());
            return true;
        }
    }

    private void cargarDatosInicialesSiExisten() {
        if (!Files.exists(rutaPorDefecto)) {
            return;
        }
        try {
            List<ContenidoAudiovisual> cargados = lector.cargar(rutaPorDefecto);
            catalogo.reemplazarTodos(cargados);
            vista.mostrarMensaje("Se cargaron " + cargados.size() + " contenidos desde " + rutaPorDefecto);
        } catch (PersistenciaException ex) {
            vista.mostrarError("No se pudieron cargar los datos iniciales: " + ex.getMessage());
        }
    }

    private void registrarPelicula() {
        String titulo = vista.leerTexto("Titulo");
        int duracion = vista.leerEntero("Duracion (minutos)");
        String genero = vista.leerTexto("Genero");
        String estudio = vista.leerTexto("Estudio");

        Pelicula pelicula = new Pelicula(titulo, duracion, genero, estudio);
        String actorNombre = vista.leerTexto("Actor principal (deje vacio para omitir)");
        if (!actorNombre.isEmpty()) {
            pelicula.agregarActor(new Actor(actorNombre));
        }
        catalogo.agregar(pelicula);
        vista.mostrarMensaje("Pelicula registrada con ID #" + pelicula.getId() + ".");
    }

    private void registrarSerie() {
        String titulo = vista.leerTexto("Titulo");
        int duracion = vista.leerEntero("Duracion promedio por episodio (minutos)");
        String genero = vista.leerTexto("Genero");
        int cantidadTemporadas = vista.leerEntero("Cantidad de temporadas");

        SerieDeTV serie = new SerieDeTV(titulo, duracion, genero, cantidadTemporadas);
        if (cantidadTemporadas > 0) {
            int episodios = vista.leerEntero("Episodios de la temporada 1 (0 para omitir detalle)");
            if (episodios > 0) {
                serie.agregarTemporada(1, episodios);
            }
        }
        catalogo.agregar(serie);
        vista.mostrarMensaje("Serie registrada con ID #" + serie.getId() + ".");
    }

    private void registrarDocumental() {
        String titulo = vista.leerTexto("Titulo");
        int duracion = vista.leerEntero("Duracion (minutos)");
        String genero = vista.leerTexto("Genero");
        String tema = vista.leerTexto("Tema");

        Documental documental = new Documental(titulo, duracion, genero, tema);
        String nombreInv = vista.leerTexto("Nombre del investigador (deje vacio para omitir)");
        if (!nombreInv.isEmpty()) {
            String especialidad = vista.leerTexto("Especialidad del investigador");
            documental.agregarInvestigador(new Investigador(nombreInv, especialidad));
        }
        catalogo.agregar(documental);
        vista.mostrarMensaje("Documental registrado con ID #" + documental.getId() + ".");
    }

    private void registrarPodcast() {
        String titulo = vista.leerTexto("Titulo");
        int duracion = vista.leerEntero("Duracion (minutos)");
        String genero = vista.leerTexto("Genero");
        String canal = vista.leerTexto("Canal");
        String nombreInvitado = vista.leerTexto("Nombre del investigador invitado");
        String especialidad = vista.leerTexto("Especialidad del invitado");

        Investigador invitado = new Investigador(nombreInvitado, especialidad);
        PodcastAudiovisual podcast = new PodcastAudiovisual(titulo, duracion, genero, canal, invitado);
        catalogo.agregar(podcast);
        vista.mostrarMensaje("Podcast registrado con ID #" + podcast.getId() + ".");
    }

    private void registrarMakingOf() {
        List<Pelicula> peliculas = catalogo.obtenerPeliculas();
        if (peliculas.isEmpty()) {
            vista.mostrarError("Debe registrar al menos una pelicula antes de crear un Making Of.");
            return;
        }

        int peliculaId = vista.leerEntero("ID de la pelicula asociada");
        Pelicula pelicula = buscarPeliculaPorId(peliculaId)
                .orElseThrow(() -> new IllegalArgumentException("No existe una pelicula con ID " + peliculaId + "."));

        String titulo = vista.leerTexto("Titulo del Making Of");
        int duracion = vista.leerEntero("Duracion (minutos)");
        String genero = vista.leerTexto("Genero");
        String director = vista.leerTexto("Director");

        MakingOf makingOf = new MakingOf(titulo, duracion, genero, director, pelicula);
        catalogo.agregar(makingOf);
        vista.mostrarMensaje("Making Of registrado con ID #" + makingOf.getId() + ".");
    }

    private void agregarEntidadRelacionada() {
        int id = vista.leerEntero("ID del contenido al que desea agregar detalle");
        ContenidoAudiovisual contenido = catalogo.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe un contenido con ID " + id + "."));

        if (contenido instanceof Pelicula) {
            String nombreActor = vista.leerTexto("Nombre del actor");
            ((Pelicula) contenido).agregarActor(new Actor(nombreActor));
            vista.mostrarMensaje("Actor agregado a la pelicula.");
        } else if (contenido instanceof SerieDeTV) {
            int numero = vista.leerEntero("Numero de temporada");
            int episodios = vista.leerEntero("Cantidad de episodios");
            ((SerieDeTV) contenido).agregarTemporada(numero, episodios);
            vista.mostrarMensaje("Temporada agregada a la serie.");
        } else if (contenido instanceof Documental) {
            String nombre = vista.leerTexto("Nombre del investigador");
            String especialidad = vista.leerTexto("Especialidad");
            ((Documental) contenido).agregarInvestigador(new Investigador(nombre, especialidad));
            vista.mostrarMensaje("Investigador agregado al documental.");
        } else {
            vista.mostrarError("Este tipo de contenido (" + contenido.obtenerTipo() + ") no admite entidades adicionales.");
        }
    }

    private void buscarPorTitulo() {
        String consulta = vista.leerTexto("Texto a buscar en el titulo");
        vista.mostrarContenidos(catalogo.buscarPorTitulo(consulta));
    }

    private void eliminarPorId() {
        int id = vista.leerEntero("ID del contenido a eliminar");
        if (catalogo.eliminarPorId(id)) {
            vista.mostrarMensaje("Contenido #" + id + " eliminado.");
        } else {
            vista.mostrarError("No se encontro un contenido con ID " + id + ".");
        }
    }

    private void cargarDesdeCsv() throws PersistenciaException {
        Path ruta = resolverRutaArchivo("Ruta del archivo CSV (vacio para usar " + rutaPorDefecto + ")");
        List<ContenidoAudiovisual> cargados = lector.cargar(ruta);
        catalogo.reemplazarTodos(cargados);
        vista.mostrarMensaje("Catalogo cargado correctamente (" + cargados.size() + " contenidos).");
    }

    private void guardarEnCsv() throws PersistenciaException {
        Path ruta = resolverRutaArchivo("Ruta de destino CSV (vacio para usar " + rutaPorDefecto + ")");
        escritor.guardar(ruta, catalogo.obtenerTodos());
        vista.mostrarMensaje("Catalogo guardado correctamente en " + ruta + ".");
    }

    private Path resolverRutaArchivo(String etiqueta) {
        String textoRuta = vista.leerTexto(etiqueta);
        return textoRuta.isEmpty() ? rutaPorDefecto : Path.of(textoRuta);
    }

    private Optional<Pelicula> buscarPeliculaPorId(int id) {
        return catalogo.buscarPorId(id)
                .filter(Pelicula.class::isInstance)
                .map(Pelicula.class::cast);
    }
}
